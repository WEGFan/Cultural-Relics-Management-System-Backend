package cn.wegfan.relicsmanagement;

import cn.wegfan.relicsmanagement.entity.User;
import cn.wegfan.relicsmanagement.mapper.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class CulturalRelicsManagementSystemBackendApplicationTests {

    @Autowired
    private UserDao userDao;

    @Test
    void test1() {
        User user = userDao.selectById(1);
        log.debug(user.toString());
    }

    @Test
    void contextLoads() {
    }

}
