package cn.wegfan.relicsmanagement.config.shiro;

import cn.wegfan.relicsmanagement.entity.Permission;
import cn.wegfan.relicsmanagement.entity.User;
import cn.wegfan.relicsmanagement.mapper.JobDao;
import cn.wegfan.relicsmanagement.mapper.PermissionDao;
import cn.wegfan.relicsmanagement.mapper.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class CustomRealm extends AuthorizingRealm {

    @Resource
    private UserDao userDao;

    @Resource
    private JobDao jobDao;

    @Resource
    private PermissionDao permissionDao;

    @Override
    public String getName() {
        return "CustomRealm";
    }

    // private final Integer ADMIN_JOB_ID = 5;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Subject subject = SecurityUtils.getSubject();
        Integer userId = (Integer)subject.getPrincipal();

        User user = userDao.selectNotDeletedById(userId);
        if (user == null) {
            return null;
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        log.debug(user.toString());

        // 获取职位基础权限
        Set<String> permissionCode = permissionDao.selectListByJobId(user.getJobId())
                .stream()
                .map(Permission::getCode)
                .collect(Collectors.toSet());
        // 获取用户额外权限
        Set<String> extraPermissionCode = user.getExtraPermissions()
                .stream()
                .map(Permission::getCode)
                .collect(Collectors.toSet());

        permissionCode.addAll(extraPermissionCode);
        // if (user.getJobId().equals(ADMIN_JOB_ID)) {
        //     permissionCode.add(PermissionCodeEnum.ADMIN);
        // }
        log.debug("{}={}", user, permissionCode);
        info.setStringPermissions(permissionCode);

        // List<Permission> permissionsByUserName = permissionDao.getPermissionsByUserName(userName);
        // for (Permission permission : permissionsByUserName) {
        //     info.addStringPermission(permission.getPermissionName());
        // }
        // info.setRoles(roles);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String principal = (String)token.getPrincipal();
        if (principal == null) {
            return null;
        }
        Integer userId = Integer.parseInt(principal);

        // 从未离职的员工中查找
        User user = userDao.selectNotDeletedById(userId);
        if (user == null) {
            return null;
        }
        String correctPassword = user.getPassword();
        String salt = user.getSalt();
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userId, correctPassword,
                ByteSource.Util.bytes(salt), getName());
        return authenticationInfo;
    }

    // @Override
    // protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    //     // 获得需认证账号
    //     String account = (String)token.getPrincipal();
    //     // 在数据库中查找用户信息
    //     User confirmInfo = userService.findUserByAccount(account);
    //     // 获得正确的密码
    //     if (confirmInfo == null) {
    //         return null;
    //     }
    //     String password = confirmInfo.getPassword();
    //     if (password == null || password.equals("")) {
    //         return null;
    //     }
    //     // 打包认证信息
    //     SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(confirmInfo, password, getName());
    //     // 设置加密盐
    //     String salt = confirmInfo.getSalt();
    //     authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(salt));
    //
    //     return authenticationInfo;
    // }

}
