package cn.wegfan.relicsmanagement.config;

import cn.hutool.core.lang.UUID;
import cn.wegfan.relicsmanagement.mapper.UserDao;
import cn.wegfan.relicsmanagement.model.entity.User;
import cn.wegfan.relicsmanagement.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class InitApp implements ApplicationRunner {

    @Autowired
    private UserDao userDao;

    @Override
    public void run(ApplicationArguments args) {
        log.info("app start running");
        // 如果数据库里没有用户，则创建管理员账户
        if (userDao.selectUserList().isEmpty()) {
            User user = new User();
            user.setWorkId(1);
            user.setName("管理员");
            user.setTelephone("13000000000");
            user.setJobId(5);
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            user.setSalt(PasswordUtil.generateSalt(UUID.fastUUID().toString()));
            String password = "admin123";
            user.setPassword(PasswordUtil.encryptPassword(password, user.getSalt()));
            userDao.insert(user);
            log.info("已创建管理员帐号，工号：{}，密码：{}", user.getWorkId(), password);
        }
    }

}
