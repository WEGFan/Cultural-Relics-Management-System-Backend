package cn.wegfan.relicsmanagement.config.shiro;

import cn.wegfan.relicsmanagement.util.PasswordUtil;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }

    @Bean(name = "shiroSessionManager")
    public SessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        // 设置自定义sessionDao操作类
        sessionManager.setSessionDAO(new EnterpriseCacheSessionDAO());
        // 设置session一天后过期
        sessionManager.setGlobalSessionTimeout(24 * 60 * 60 * 1000);
        // 删除过期缓存
        sessionManager.setDeleteInvalidSessions(true);
        // 设置定期扫描缓存
        sessionManager.setSessionValidationSchedulerEnabled(true);
        // 设置url不暴露sessionID
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;
    }

    // 自定义 realm
    @Bean
    public Realm realm() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        // 设置密码加密方式
        matcher.setHashAlgorithmName(PasswordUtil.HASH_ALGORITHM);
        // 设置密码加密次数
        matcher.setHashIterations(PasswordUtil.HASH_ITERATIONS);
        // realm 实例的创建加密设置
        CustomRealm customRealm = new CustomRealm();
        customRealm.setCredentialsMatcher(matcher);
        customRealm.setCachingEnabled(false);
        return customRealm;
    }

    // @Bean
    // public CacheManager cacheManager() {
    //     EhCacheManager cacheManager = new EhCacheManager();
    //     cacheManager.setCacheManagerConfigFile("classpath:ehcache-config.xml");
    //     return cacheManager;
    // }
    //权限管理，配置主要是Realm的管理认证
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm());
        // securityManager.setCacheManager(cacheManager());
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("authc", new CustomLoginFilter());
        shiroFilterFactoryBean.setFilters(filterMap);

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        // TODO
        filterChainDefinitionMap.put("/api/v1/sessions", "anon");
        filterChainDefinitionMap.put("/api/v1/tests", "anon");
        // filterChainDefinitionMap.put("/api/v1/backups", "perms[aaa]");
        filterChainDefinitionMap.put("/api/v1/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;

    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
