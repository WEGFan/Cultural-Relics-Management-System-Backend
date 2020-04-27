package cn.wegfan.relicsmanagement.controller;

import cn.wegfan.relicsmanagement.service.SessionService;
import cn.wegfan.relicsmanagement.util.DataReturn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Slf4j
@RestController
@RequestMapping("/api/v1/sessions")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    /**
     * 用户登录
     */
    @PostMapping("")
    public DataReturn userLogin() {
        throw new NotImplementedException();
    }

    /**
     * 用户退出登录
     */
    @DeleteMapping("")
    public DataReturn userLogout() {
        throw new NotImplementedException();
    }

}
