package cn.wegfan.relicsmanagement;

import cn.hutool.core.util.RandomUtil;
import cn.wegfan.relicsmanagement.entity.Job;
import cn.wegfan.relicsmanagement.entity.Relic;
import cn.wegfan.relicsmanagement.entity.User;
import cn.wegfan.relicsmanagement.mapper.JobDao;
import cn.wegfan.relicsmanagement.mapper.RelicDao;
import cn.wegfan.relicsmanagement.mapper.UserDao;
import cn.wegfan.relicsmanagement.service.UserExtraPermissionService;
import cn.wegfan.relicsmanagement.service.UserService;
import cn.wegfan.relicsmanagement.service.WarehouseService;
import cn.wegfan.relicsmanagement.util.PasswordUtil;
import cn.wegfan.relicsmanagement.vo.PageResultVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
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
    private UserExtraPermissionService userExtraPermissionService;

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

        class aaa {

            public aaa() {

            }

            @SneakyThrows
            @Override
            public String toString() {
                throw new Exception();
            }

        }
        aaa d = new aaa();
        log.debug("{}", d);
        // try {
        //     userPermissionService.updateUserPermissions(1,
        //             Arrays.asList(3, 5));
        //     userPermissionService.updateUserPermissions(2,
        //             Arrays.asList(1));
        // } catch (Exception e) {
        //     log.error("", e);
        //     // e.printStackTrace();
        // }

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
        PageResultVo result = warehouseService.listNotDeletedWarehousesByNameAndPage(null, 1, 8);
        log.debug(result.toString());
    }

    @Test
    void test8() {
        long a = (long)Integer.MAX_VALUE + 1;
        int b = (int)a;
        System.out.println(b);
    }

    @Test
    void testPasswordUtil() {
        String salt = PasswordUtil.generateSalt("aaaa");
        log.debug(salt);
        String password = PasswordUtil.encryptPassword("123456", salt);
        log.debug(password);
    }

    @Test
    void test9() throws JsonProcessingException {
        Set<Integer> set = new HashSet<>(Arrays.asList(1, 2, 2));
        set.addAll(Lists.newArrayList(1, 3, 4, 5));
        set.forEach(System.out::println);
        set.removeAll(Arrays.asList(3));
        log.debug(new ObjectMapper().writeValueAsString(set));
    }

    @Autowired
    private RelicDao relicDao;

    @Test
    void generateRandomRelics() {
        for (int i = 100; i <= 150; i++) {
            Relic relic = new Relic();
            relic.setId(i);
            relic.setName("relic name " + i);
            final long millisecondsPerHour = 60 * 60 * 1000;
            long baseTime = new Date().getTime() - 10 * millisecondsPerHour;
            relic.setCreateTime(new Date(baseTime - RandomUtil.randomLong(2 * millisecondsPerHour)));
            relic.setUpdateTime(new Date(baseTime + RandomUtil.randomLong(7 * millisecondsPerHour, 9 * millisecondsPerHour)));
            relic.setComment1("relic comment1 " + i);
            relic.setQuantity(i);
            relic.setStatusId(RandomUtil.randomInt(2, 5 + 1));
            switch (relic.getStatusId()) {
                case 2:
                    relic.setEnterTime(new Date(baseTime + RandomUtil.randomLong(2 * millisecondsPerHour, 3 * millisecondsPerHour)));
                    break;
                case 3:
                    relic.setLendTime(new Date(baseTime + RandomUtil.randomLong(3 * millisecondsPerHour, 4 * millisecondsPerHour)));
                    break;
                case 4:
                    relic.setFixTime(new Date(baseTime + RandomUtil.randomLong(5 * millisecondsPerHour, 6 * millisecondsPerHour)));
                    break;
                case 5:
                    relic.setLeaveTime(new Date(baseTime + RandomUtil.randomLong(6 * millisecondsPerHour, 8 * millisecondsPerHour)));
                    relic.setLeavePrice(new BigDecimal(i * 500));
                    break;
                default:
                    break;
            }
            relic.setPicturePath("/api/files/relics/images/3.jpg");
            relic.setSize(String.format("%d x %d x %d", i, i, i));
            relic.setPlace("relic place " + i);
            relic.setYear(String.valueOf(1500 + i));
            relic.setReign("relic reign " + i);
            relic.setType("relic type " + i);
            relic.setSource("relic source " + i);
            relic.setWeight(RandomUtil.randomDouble(100000));

            relic.setEnterPrice(new BigDecimal(i * 1000));
            relic.setWarehouseId(RandomUtil.randomInt(1, 3));
            if (RandomUtil.randomBoolean()) {
                relic.setLastCheckTime(new Date(baseTime + RandomUtil.randomLong(2 * millisecondsPerHour, 6 * millisecondsPerHour)));
            }

            relic.setMoveTime(new Date(baseTime + RandomUtil.randomLong(4 * millisecondsPerHour, 5 * millisecondsPerHour)));
            relicDao.insert(relic);
        }

    }

}
