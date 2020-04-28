package cn.wegfan.relicsmanagement;

import cn.wegfan.relicsmanagement.entity.Job;
import cn.wegfan.relicsmanagement.entity.User;
import cn.wegfan.relicsmanagement.mapper.JobDao;
import cn.wegfan.relicsmanagement.mapper.UserDao;
import cn.wegfan.relicsmanagement.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class MybatisPlusTest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private JobDao jobDao;

    @Autowired
    private UserService userService;
    
    @Test
    void test1() {
        List<User> userList = userDao.selectList();
        log.debug(userList.toString());
    }

    @Test
    void test2() {

        Job job = new Job();
        job.setName("1312312313");
        jobDao.insert(job);
        log.debug(jobDao.selectList(null).toString());
    }

    @Test
    void test3() {
        log.debug(userDao.selectById(1).toString());
        List<User> userList = userDao.selectList();
        log.debug(userList.toString());
    }
    
    @Test
    void test4() {
        log.debug(userService.getUserById(1).toString());
        log.debug(userService.listAllUsers().toString());
    }

}
