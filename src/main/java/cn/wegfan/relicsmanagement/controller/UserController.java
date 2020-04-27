package cn.wegfan.relicsmanagement.controller;

import cn.wegfan.relicsmanagement.mapper.UserDao;
import cn.wegfan.relicsmanagement.service.PermissionService;
import cn.wegfan.relicsmanagement.service.UserService;
import cn.wegfan.relicsmanagement.util.DataReturn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取所有用户信息【管理员】
     */
    @GetMapping("")
    public DataReturn listAllUsers() {
        return DataReturn.success(userService.listAllUsers());
    }

    /**
     * 增加新用户【管理员】
     */
    @PostMapping("")
    public DataReturn addUser() {
        throw new NotImplementedException();
    }

    /**
     * 修改用户信息【管理员】
     *
     * @param userId 用户编号
     */
    @PutMapping("{userId}")
    public DataReturn updateUserInfo(@PathVariable String userId) {
        throw new NotImplementedException();
    }

    /**
     * 修改用户密码
     */
    @PutMapping("password")
    public DataReturn updateUserPassword() {
        throw new NotImplementedException();
    }

    /**
     * 删除用户【管理员】
     *
     * @param userId 用户编号
     */
    @PutMapping("{userId}")
    public DataReturn deleteUser(@PathVariable String userId) {
        throw new NotImplementedException();
    }

    /**
     * 导出用户信息 Excel 表【管理员】
     */
    @GetMapping(value = "", params = "excel=true")
    public DataReturn exportAllUsersToExcel() {
        return DataReturn.success(userService.listAllUsers());
    }

}
