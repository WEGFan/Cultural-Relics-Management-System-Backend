package cn.wegfan.relicsmanagement.config;

import cn.wegfan.relicsmanagement.dto.UserInfoDto;
import cn.wegfan.relicsmanagement.service.UserService;
import cn.wegfan.relicsmanagement.util.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Slf4j
@Component
public class InitApp implements ApplicationRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 新增管理员帐号，方便调试
        UserInfoDto userInfo = new UserInfoDto();
        userInfo.setName("admin");
        userInfo.setPassword("admin");
        userInfo.setWorkId(10000);
        userInfo.setJobId(5);
        userInfo.setTelephone("0");
        userInfo.setPermissionId(new ArrayList<>());

        try {
            userService.addUser(userInfo);
        } catch (BusinessException e) {
            log.error("", e);
        }
    }

}
