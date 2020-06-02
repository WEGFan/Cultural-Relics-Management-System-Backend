package cn.wegfan.relicsmanagement.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.wegfan.relicsmanagement.dto.UserInfoDto;
import cn.wegfan.relicsmanagement.entity.Permission;
import cn.wegfan.relicsmanagement.entity.User;
import cn.wegfan.relicsmanagement.entity.Warehouse;
import cn.wegfan.relicsmanagement.mapper.UserDao;
import cn.wegfan.relicsmanagement.util.*;
import cn.wegfan.relicsmanagement.util.OperationLogUtil.FieldDifference;
import cn.wegfan.relicsmanagement.vo.*;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.metadata.TypeBuilder;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserExtraPermissionService userExtraPermissionService;

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public List<UserVo> listAllInWorkUsers() {
        List<User> userList = userDao.selectNotDeletedList();
        // for (User user : userList) {
        //     user.setPermissions(permissionDao.selectListByUserId(user.getId()));
        // }
        log.debug(userList.toString());
        List<UserVo> userVoList = mapperFacade.mapAsList(userList, UserVo.class);
        return userVoList;
    }

    @Override
    public PageResultVo<UserVo> listAllInWorkUsersByPage(long pageIndex, long pageSize) {
        Page<Warehouse> page = new Page<>(pageIndex, pageSize);
        Page<User> pageResult = userDao.selectPageNotDeleted(page);

        List<User> userList = pageResult.getRecords();
        List<UserVo> userVoList = mapperFacade.mapAsList(userList, UserVo.class);

        return new PageResultVo<UserVo>(userVoList, pageResult);
    }

    @Override
    public UserVo getUserById(Integer userId) {
        User user = userDao.selectById(userId);
        log.debug(user.toString());
        UserVo userVo = mapperFacade.map(user, UserVo.class);
        return userVo;
    }

    @Override
    public UserIdVo addUser(UserInfoDto userInfo) {
        // 检查密码是否为空
        if (StrUtil.isEmpty(userInfo.getPassword())) {
            throw new BusinessException(400, "密码不能为空");
        }
        // 从所有员工中检查工号是否重复
        if (userDao.selectByWorkId(Integer.parseInt(userInfo.getWorkId())) != null) {
            throw new BusinessException(BusinessErrorEnum.DuplicateWorkId);
        }

        User user = mapperFacade.map(userInfo, User.class);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        // 密码加盐
        user.setSalt(PasswordUtil.generateSalt(user.getName() + user.getTelephone()));
        user.setPassword(PasswordUtil.encryptPassword(user.getPassword(), user.getSalt()));
        log.debug(user.toString());

        userDao.insert(user);
        Set<Integer> realPermissionIdSet = userExtraPermissionService.updateUserExtraPermissions(user.getId(), user.getJobId(), userInfo.getExtraPermissionsId());

        Set<Permission> realPermission = mapperFacade.map(realPermissionIdSet,
                new TypeBuilder<Set<Integer>>() {
                }.build(),
                new TypeBuilder<Set<Permission>>() {
                }.build());
        user.setExtraPermissions(realPermission);

        try {
            Map<String, FieldDifference> fieldDifferenceMap = OperationLogUtil.getDifferenceFieldMap(null, user, User.class);
            // 把额外权限变成字符串
            String extraPermissionString = mapperFacade.convert(realPermission, String.class, "extraPermissionNameConverter", null);
            fieldDifferenceMap.get("extraPermissions").setNewValue(extraPermissionString);
            // 把职务编号变成字符串
            String jobString = mapperFacade.convert(user.getJobId(), String.class, "jobNameConverter", null);
            fieldDifferenceMap.get("jobId").setNewValue(jobString);

            log.debug("{}", fieldDifferenceMap);
            // 添加操作记录
            OperationItemTypeEnum itemType = OperationItemTypeEnum.User;
            Integer itemId = user.getId();
            String detail = OperationLogUtil.getCreateItemDetailLog(itemType, itemId, fieldDifferenceMap);
            operationLogService.addOperationLog(itemType, itemId,
                    "新建用户", detail);
        } catch (IllegalAccessException | InstantiationException e) {
            log.error("获取不同成员变量错误", e);
            throw new BusinessException(BusinessErrorEnum.InternalServerError);
        }

        return new UserIdVo(user.getId());
    }

    @Override
    public SuccessVo updateUserInfo(Integer userId, UserInfoDto userInfo) {
        User user = userDao.selectNotDeletedById(userId);
        // 检测在职员工中是否存在该用户编号对应的用户
        if (user == null) {
            throw new BusinessException(BusinessErrorEnum.UserNotExists);
        }
        // 从所有员工中查找工号是否被其他人占用
        User sameWorkIdUser = userDao.selectByWorkId(Integer.parseInt(userInfo.getWorkId()));
        // 如果存在且用户编号不是被修改用户的
        if (sameWorkIdUser != null && !sameWorkIdUser.getId().equals(userId)) {
            throw new BusinessException(BusinessErrorEnum.DuplicateWorkId);
        }

        User oldUser = SerializationUtils.clone(user);

        // 判断是否要修改密码
        if (!StrUtil.isEmpty(userInfo.getPassword())) {
            userInfo.setPassword(PasswordUtil.encryptPassword(userInfo.getPassword(), user.getSalt()));
        } else {
            userInfo.setPassword(null);
        }
        // TODO: 清除用户的session缓存
        // TODO: 判断职务和权限
        mapperFacade.map(userInfo, user);

        user.setUpdateTime(new Date());
        log.debug(user.toString());

        userDao.updateById(user);
        Set<Integer> realPermissionIdSet = userExtraPermissionService.updateUserExtraPermissions(user.getId(), user.getJobId(), userInfo.getExtraPermissionsId());

        Set<Permission> realPermission = mapperFacade.map(realPermissionIdSet,
                new TypeBuilder<Set<Integer>>() {
                }.build(),
                new TypeBuilder<Set<Permission>>() {
                }.build());
        user.setExtraPermissions(realPermission);

        try {
            Map<String, FieldDifference> fieldDifferenceMap = OperationLogUtil.getDifferenceFieldMap(oldUser, user, User.class);
            // 把额外权限变成字符串
            if (fieldDifferenceMap.containsKey("extraPermissions")) {
                String oldPermissionString = mapperFacade.convert(user.getExtraPermissions(), String.class, "extraPermissionNameConverter", null);
                fieldDifferenceMap.get("extraPermissions").setOldValue(oldPermissionString);
                String extraPermissionString = mapperFacade.convert(realPermission, String.class, "extraPermissionNameConverter", null);
                fieldDifferenceMap.get("extraPermissions").setNewValue(extraPermissionString);
            }
            // 把职务编号变成字符串  
            if (fieldDifferenceMap.containsKey("jobId")) {
                String oldJobString = mapperFacade.convert(oldUser.getJobId(), String.class, "jobNameConverter", null);
                fieldDifferenceMap.get("jobId").setOldValue(oldJobString);
                String jobString = mapperFacade.convert(user.getJobId(), String.class, "jobNameConverter", null);
                fieldDifferenceMap.get("jobId").setNewValue(jobString);
            }

            log.debug("{}", fieldDifferenceMap);
            // 添加操作记录
            OperationItemTypeEnum itemType = OperationItemTypeEnum.User;
            Integer itemId = user.getId();
            String detail = OperationLogUtil.getUpdateItemDetailLog(itemType, itemId, user.getName(), fieldDifferenceMap);
            operationLogService.addOperationLog(itemType, itemId,
                    "修改用户", detail);
        } catch (IllegalAccessException | InstantiationException e) {
            log.error("获取不同成员变量错误", e);
            throw new BusinessException(BusinessErrorEnum.InternalServerError);
        }

        return new SuccessVo(true);
    }

    @Override
    public SuccessVo deleteUserById(Integer userId) {
        User user = userDao.selectNotDeletedById(userId);
        // 检测在职员工中是否存在该用户编号对应的用户
        if (user == null) {
            throw new BusinessException(BusinessErrorEnum.UserNotExists);
        }
        // 获取当前登录的用户编号
        Integer currentLoginUserId = (Integer)SecurityUtils.getSubject().getPrincipal();
        // 检测删除的是否为自己的帐号
        if (currentLoginUserId.equals(userId)) {
            throw new BusinessException(BusinessErrorEnum.CanNotDeleteOwnAccount);
        }
        // TODO: 清除用户的session缓存
        int result = userDao.deleteUserById(userId);

        // 添加操作记录
        String detail = OperationLogUtil.getDeleteItemDetailLog(OperationItemTypeEnum.User, userId, user.getName());
        operationLogService.addOperationLog(OperationItemTypeEnum.User, userId,
                "删除用户", detail);

        return new SuccessVo(result > 0);
    }

    @Override
    public UserVo userLogin(Integer workId, String password) {
        // 从在职员工中根据工号查找
        User user = userDao.selectNotDeletedByWorkId(workId);
        if (user == null) {
            throw new BusinessException(BusinessErrorEnum.WrongAccountOrPassword);
        }

        // 将用户编号作为用户名生成token
        String username = String.valueOf(user.getId());

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            throw new BusinessException(BusinessErrorEnum.WrongAccountOrPassword);
        }

        // if (!subject.isAuthenticated()) {
        //     throw new BusinessException(BusinessErrorEnum.WrongAccountOrPassword);
        // }
        log.debug(user.toString());
        UserVo userVo = mapperFacade.map(user, UserVo.class);
        return userVo;
    }

    @Override
    public SuccessVo userLogout() {
        Subject subject = SecurityUtils.getSubject();
        // 如果用户本身未登录
        if (subject.getPrincipal() == null) {
            throw new BusinessException(BusinessErrorEnum.UserNotLogin);
        }
        // TODO: 清除缓存
        subject.logout();
        return new SuccessVo(true);
    }

    @Override
    public SuccessVo changeUserPassword(String oldPassword, String newPassword) {
        Subject subject = SecurityUtils.getSubject();

        // 获取当前登录的用户编号
        Integer currentLoginUserId = (Integer)subject.getPrincipal();

        User user = userDao.selectNotDeletedById(currentLoginUserId);
        log.debug(user.toString());
        String encryptedOldPassword = PasswordUtil.encryptPassword(oldPassword, user.getSalt());
        String encryptedNewPassword = PasswordUtil.encryptPassword(newPassword, user.getSalt());

        log.debug("{} {}", user.getPassword(), encryptedOldPassword);

        // 如果旧密码不正确
        if (!encryptedOldPassword.equals(user.getPassword())) {
            throw new BusinessException(BusinessErrorEnum.WrongAccountOrPassword);
        }

        user.setPassword(encryptedNewPassword);
        user.setUpdateTime(new Date());

        userDao.updateById(user);

        // 用新的密码重新登录
        UsernamePasswordToken token = new UsernamePasswordToken(String.valueOf(user.getId()), newPassword);
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            // 按理来说不应该会发生
            log.error("", e);
        }

        return new SuccessVo(true);
    }

    @Override
    public FilePathVo exportAllUsersToExcel() {
        List<User> userList = userDao.selectUserList();

        List<UserExcelVo> userExcelVoList = mapperFacade.mapAsList(userList, UserExcelVo.class);

        Path dir = Paths.get("data", "exports", "users")
                .toAbsolutePath();
        FileUtil.mkdir(dir.toFile());

        String fileName = "员工列表_" + DateUtil.format(new Date(), "yyyy-MM-dd_HH-mm-ss") + ".xlsx";
        File file = dir.resolve(fileName)
                .toFile();

        EasyExcel.write(file, UserExcelVo.class)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet("员工列表")
                .doWrite(userExcelVoList);
        return new FilePathVo("/api/files/exports/users/" + fileName);
    }

}
