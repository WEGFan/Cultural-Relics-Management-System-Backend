package cn.wegfan.relicsmanagement.controller;

import cn.wegfan.relicsmanagement.dto.UserLoginDto;
import cn.wegfan.relicsmanagement.service.UserService;
import cn.wegfan.relicsmanagement.vo.DataReturnVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/sessions")
public class SessionController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     */
    @PostMapping("")
    public DataReturnVo userLogin(@RequestBody @Valid UserLoginDto loginInfo) {
        return DataReturnVo.success(userService.userLogin(Integer.parseInt(loginInfo.getWorkId()), loginInfo.getPassword()));
    }

    /**
     * 用户退出登录
     */
    @DeleteMapping("")
    public DataReturnVo userLogout() {
        return DataReturnVo.success(userService.userLogout());
    }

}
