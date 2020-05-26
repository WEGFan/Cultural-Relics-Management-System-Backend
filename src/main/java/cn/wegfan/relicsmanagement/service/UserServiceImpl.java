package cn.wegfan.relicsmanagement.service;

import cn.hutool.core.util.StrUtil;
import cn.wegfan.relicsmanagement.dto.UserInfoDto;
import cn.wegfan.relicsmanagement.entity.User;
import cn.wegfan.relicsmanagement.entity.Warehouse;
import cn.wegfan.relicsmanagement.mapper.UserDao;
import cn.wegfan.relicsmanagement.util.BusinessErrorEnum;
import cn.wegfan.relicsmanagement.util.BusinessException;
import cn.wegfan.relicsmanagement.util.PasswordUtil;
import cn.wegfan.relicsmanagement.vo.PageResultVo;
import cn.wegfan.relicsmanagement.vo.SuccessVo;
import cn.wegfan.relicsmanagement.vo.UserIdVo;
import cn.wegfan.relicsmanagement.vo.UserVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserExtraPermissionService userExtraPermissionService;

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
        userExtraPermissionService.updateUserExtraPermissions(user.getId(), user.getJobId(), userInfo.getExtraPermissionsId());
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
        userExtraPermissionService.updateUserExtraPermissions(user.getId(), user.getJobId(), userInfo.getExtraPermissionsId());
        return new SuccessVo(true);
    }

    @Override
    public SuccessVo deleteUserById(Integer userId) {
        // 检测在职员工中是否存在该用户编号对应的用户
        if (userDao.selectNotDeletedById(userId) == null) {
            throw new BusinessException(BusinessErrorEnum.UserNotExists);
        }
        // 获取当前登录的用户编号
        Integer currentLoginUserId = (Integer)SecurityUtils.getSubject().getPrincipal();
        // 检测删除的是否为自己的帐号
        if (currentLoginUserId.equals(userId)) {
            return new SuccessVo(false);
        }
        // TODO: 清除用户的session缓存
        int result = userDao.deleteUserById(userId);
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

}
