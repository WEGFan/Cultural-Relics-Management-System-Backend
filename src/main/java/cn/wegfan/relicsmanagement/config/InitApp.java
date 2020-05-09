package cn.wegfan.relicsmanagement.config;

import cn.wegfan.relicsmanagement.dto.UserInfoDto;
import cn.wegfan.relicsmanagement.mapper.UserDao;
import cn.wegfan.relicsmanagement.service.UserService;
import cn.wegfan.relicsmanagement.util.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;

@Slf4j
@Component
public class InitApp implements ApplicationRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 新增管理员帐号，方便调试
        Integer workId = 10000;
        if (userDao.selectByWorkId(workId) == null) {
            UserInfoDto userInfo = new UserInfoDto();

            userInfo.setName("admin");
            userInfo.setPassword("admin");
            userInfo.setWorkId(workId);
            userInfo.setJobId(5);
            userInfo.setTelephone("0");
            userInfo.setExtraPermissionsId(new HashSet<>());

            try {
                userService.addUser(userInfo);
            } catch (BusinessException e) {
                log.error("", e);
            }
        }
    }

}
