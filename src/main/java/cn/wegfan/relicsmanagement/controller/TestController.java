package cn.wegfan.relicsmanagement.controller;

import cn.wegfan.relicsmanagement.vo.DataReturnVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/api/v1/tests")
public class TestController {

    @GetMapping("")
    public DataReturnVo test(@RequestParam Integer workId, @RequestParam String password) {
        log.debug("{} {}", workId, password);
        String hashAlgorithmName = "MD5";//加密方式  
    
        Object crdentials = "abcdef";//密码原值
        ByteSource salt = ByteSource.Util.bytes("123");//以账号作为盐值
        int hashIterations = 1;//加密1024次
        SimpleHash hash = new SimpleHash(hashAlgorithmName, crdentials, salt, hashIterations);
        log.debug(hash.toHex());
        
        // 获得shiro的实体
        Subject subject = SecurityUtils.getSubject();
        // 封装账号密码
        UsernamePasswordToken token = new UsernamePasswordToken(String.valueOf(workId), password);
        // 登陆，验证
        subject.login(token);
        log.debug("{}", subject.isAuthenticated());

        // if (!) {
        //     return Boolean.FALSE;
        // }
        // // 更新上次登入时间
        // try {
        //     UserUpdateLastLoginTime(account, new Date(System.currentTimeMillis()));
        // } catch (Exception e) {
        //     TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        //     e.printStackTrace();
        // }
        // return Boolean.TRUE;
        return DataReturnVo.success(null);
    }

}
