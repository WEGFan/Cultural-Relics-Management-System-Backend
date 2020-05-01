package cn.wegfan.relicsmanagement;

import cn.wegfan.relicsmanagement.entity.Job;
import cn.wegfan.relicsmanagement.entity.User;
import cn.wegfan.relicsmanagement.mapper.JobDao;
import cn.wegfan.relicsmanagement.mapper.UserDao;
import cn.wegfan.relicsmanagement.service.UserPermissionService;
import cn.wegfan.relicsmanagement.service.UserService;
import cn.wegfan.relicsmanagement.service.WarehouseService;
import cn.wegfan.relicsmanagement.vo.PageResultVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
@Slf4j
public class Test1 {

    @Autowired
    private UserDao userDao;

    @Autowired
    private JobDao jobDao;

    @Autowired
    private UserService userService;

    @Autowired
    private UserPermissionService userPermissionService;

    @Test
    void test1() {
        List<User> userList = userDao.selectList(null);
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
        List<User> userList = userDao.selectList(null);
        log.debug(userList.toString());
    }

    @Test
    void test4() {
        log.debug(userService.getUserById(1).toString());
        log.debug(userService.listAllInWorkUsers().toString());
        log.debug(String.valueOf(userDao.selectByWorkId(11111)));
    }

    @Test
    void test5() {
        try {
            userPermissionService.updateUserPermissions(1,
                    Arrays.asList(3, 5));
            userPermissionService.updateUserPermissions(2,
                    Arrays.asList(1));
        } catch (Exception e) {
            log.error("", e);
            // e.printStackTrace();
        }

    }

    @Test
    void test6() {
        User user = userDao.selectById(1);
        user.setDeleteTime(null);
        userDao.updateById(user);

    }

    @Autowired
    private WarehouseService warehouseService;

    @Test
    void test7() {
        PageResultVo result = warehouseService.listWarehousesByNameAndPage(null, 1, 8);
        log.debug(result.toString());
    }

    @Test
    void test8() {
        long a = (long)Integer.MAX_VALUE + 1;
        int b = (int)a;
        System.out.println(b);
    }

}
