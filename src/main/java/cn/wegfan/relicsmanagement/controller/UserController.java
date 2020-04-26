package cn.wegfan.relicsmanagement.controller;

import cn.wegfan.relicsmanagement.mapper.UserDao;
import cn.wegfan.relicsmanagement.util.DataReturn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserDao userDao;

    @GetMapping("")
    public DataReturn listUsers() {
        return DataReturn.success(userDao.selectList(null));
    }

}
