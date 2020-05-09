package cn.wegfan.relicsmanagement.config.shiro;

import cn.wegfan.relicsmanagement.util.PasswordUtil;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
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

    // 自定义权限资源过滤器
    // @Bean
    // public ShiroFilterChainDefinition shiroFilterChainDefinition() {
    //     DefaultShiroFilterChainDefinition chain = new DefaultShiroFilterChainDefinition();
    //
    //     // 无需验证的路径
    //     chain.addPathDefinition("/test/**", "anon");
    //     chain.addPathDefinition("/api/v1/**", "anon");
    //     // swagger资源
    //     chain.addPathDefinition("/swagger-ui.html/**", "anon");
    //     chain.addPathDefinition("/webjars/**", "anon");
    //     chain.addPathDefinition("/swagger-resources/**", "anon");
    //     chain.addPathDefinition("/v2/api-docs/**", "anon");
    //
    //     // 需要登入的请求
    //     chain.addPathDefinition("/**", "user");
    //     return chain;
    // }
    //将自己的验证方式加入容器
    // @Bean
    // public CustomRealm myShiroRealm() {
    //     CustomRealm customRealm = new CustomRealm();
    //     return customRealm;
    // }

    //权限管理，配置主要是Realm的管理认证
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm());
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
