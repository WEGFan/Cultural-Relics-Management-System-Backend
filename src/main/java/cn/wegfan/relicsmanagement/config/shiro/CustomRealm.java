package cn.wegfan.relicsmanagement.config.shiro;

import cn.wegfan.relicsmanagement.entity.User;
import cn.wegfan.relicsmanagement.mapper.JobDao;
import cn.wegfan.relicsmanagement.mapper.PermissionDao;
import cn.wegfan.relicsmanagement.mapper.UserDao;
import cn.wegfan.relicsmanagement.service.PermissionService;
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

@Slf4j
public class CustomRealm extends AuthorizingRealm {

    @Resource
    private UserDao userDao;

    @Resource
    private PermissionService permissionService;

    @Override
    public String getName() {
        return "CustomRealm";
    }

    @Override
    public AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Subject subject = SecurityUtils.getSubject();
        Integer userId = (Integer)subject.getPrincipal();

        User user = userDao.selectNotDeletedById(userId);
        if (user == null) {
            return null;
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        log.debug(user.toString());

        Set<String> permissionCodeSet = permissionService.listAllPermissionCodeByUserId(user.getId());

        log.debug("{}={}", user, permissionCodeSet);
        info.setStringPermissions(permissionCodeSet);

        // List<Permission> permissionsByUserName = permissionDao.getPermissionsByUserName(userName);
        // for (Permission permission : permissionsByUserName) {
        //     info.addStringPermission(permission.getPermissionName());
        // }
        // info.setRoles(roles);
        return info;
    }

    @Override
    public AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
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
