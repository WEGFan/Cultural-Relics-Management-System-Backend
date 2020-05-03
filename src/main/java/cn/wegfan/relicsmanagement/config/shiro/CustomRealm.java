package cn.wegfan.relicsmanagement.config.shiro;

import cn.wegfan.relicsmanagement.entity.User;
import cn.wegfan.relicsmanagement.mapper.UserDao;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;

public class CustomRealm extends AuthorizingRealm {

    @Resource
    private UserDao userDao;

    @Override
    public String getName() {
        return "CustomRealm";
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // String a = "21312312";

        // if (true) {
        //     return null;
        // }
        return null;
        // 获取登录用户名
        // String name = (String)principalCollection.getPrimaryPrincipal();
        //根据用户名去数据库查询用户信息
        // User user = loginService.getUserByName(name);
        //添加角色和权限
        // SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        // for (Role role : user.getRoles()) {
        //     //添加角色
        //     simpleAuthorizationInfo.addRole(role.getRoleName());
        //     //添加权限
        //     for (Permissions permissions : role.getPermissions()) {
        //         simpleAuthorizationInfo.addStringPermission(permissions.getPermissionsName());
        //     }
        // }
        // return simpleAuthorizationInfo;
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
