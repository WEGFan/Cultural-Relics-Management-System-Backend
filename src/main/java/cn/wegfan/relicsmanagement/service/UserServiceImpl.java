package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.dto.UserInfoDto;
import cn.wegfan.relicsmanagement.entity.Permission;
import cn.wegfan.relicsmanagement.entity.User;
import cn.wegfan.relicsmanagement.mapper.PermissionDao;
import cn.wegfan.relicsmanagement.mapper.UserDao;
import cn.wegfan.relicsmanagement.util.BusinessErrorEnum;
import cn.wegfan.relicsmanagement.util.BusinessException;
import cn.wegfan.relicsmanagement.util.PasswordUtil;
import cn.wegfan.relicsmanagement.vo.SuccessVo;
import cn.wegfan.relicsmanagement.vo.UserIdVo;
import cn.wegfan.relicsmanagement.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private UserPermissionService userPermissionService;

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    private MapperFacade mapperFacade;

    public UserServiceImpl() {
        mapperFactory
                .getConverterFactory()
                .registerConverter("permissionConvert", new CustomConverter<List<Permission>, List<Integer>>() {
                    @Override
                    public List<Integer> convert(List<Permission> permissions, Type<? extends List<Integer>> type, MappingContext mappingContext) {
                        // 把权限的id提取成列表
                        return permissions.stream()
                                .map(Permission::getId)
                                .collect(Collectors.toList());
                    }
                });
        mapperFactory.classMap(User.class, UserVo.class)
                .fieldMap("permissions", "permissionId").converter("permissionConvert").add()
                .byDefault()
                .register();
        mapperFactory.classMap(UserInfoDto.class, User.class)
                .byDefault()
                .register();
        mapperFacade = mapperFactory.getMapperFacade();
    }

    @Override
    public List<UserVo> listAllInWorkUsers() {
        List<User> userList = userDao.selectNotDeletedList();
        for (User user : userList) {
            user.setPermissions(permissionDao.selectListByUserId(user.getId()));
        }

        log.debug(userList.toString());
        List<UserVo> userVoList = mapperFacade.mapAsList(userList, UserVo.class);
        return userVoList;
    }

    @Override
    public UserVo getUserById(Integer userId) {
        User user = userDao.selectById(userId);
        user.setPermissions(permissionDao.selectListByUserId(user.getId()));
        log.debug(user.toString());
        UserVo userVo = mapperFacade.map(user, UserVo.class);
        return userVo;
    }

    @Override
    public UserIdVo addUser(UserInfoDto userInfo) {
        // 从所有员工中检查工号是否重复
        if (userDao.selectByWorkId(userInfo.getWorkId()) != null) {
            throw new BusinessException(BusinessErrorEnum.DuplicateWorkId);
        }

        User user = mapperFacade.map(userInfo, User.class);
        user.setCreateTime(new Date());
        // 密码加盐
        user.setSalt(PasswordUtil.generateSalt(user.getName() + user.getTelephone()));
        user.setPassword(PasswordUtil.encryptPassword(user.getPassword(), user.getSalt()));
        log.debug(user.toString());

        userDao.insert(user);
        userPermissionService.updateUserPermissions(user.getId(), userInfo.getPermissionId());
        return new UserIdVo(user.getId());
    }

    @Override
    public SuccessVo updateUserInfo(Integer userId, UserInfoDto userInfo) {
        // 从所有员工中检查工号是否重复
        if (userDao.selectByWorkId(userInfo.getWorkId()) != null) {
            throw new BusinessException(BusinessErrorEnum.DuplicateWorkId);
        }
        // TODO: 清除用户的session缓存
        User user = mapperFacade.map(userInfo, User.class);
        user.setId(userId);
        user.setPassword(PasswordUtil.encryptPassword(user.getPassword(), user.getSalt()));
        user.setUpdateTime(new Date());
        log.debug(user.toString());

        userDao.updateById(user);
        userPermissionService.updateUserPermissions(user.getId(), userInfo.getPermissionId());
        return new SuccessVo(true);
    }

    @Override
    public SuccessVo deleteUserById(Integer userId) {
        // 获取当前登录的用户编号
        Integer currentLoginUserId = Integer.parseInt((String)SecurityUtils.getSubject().getPrincipal());
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

        // 将用户编号作为用户名给shiro
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

        UserVo userVo = mapperFacade.map(user, UserVo.class);
        return userVo;
    }

    @Override
    public SuccessVo userLogout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return new SuccessVo(true);
    }

}