package cn.wegfan.relicsmanagement.config.shiro;

import cn.wegfan.relicsmanagement.mapper.UserDao;
import cn.wegfan.relicsmanagement.model.entity.User;
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

        Set<String> permissionCodeSet = permissionService.listAllPermissionCodeByUserId(user.getId());
        info.setStringPermissions(permissionCodeSet);

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

}
