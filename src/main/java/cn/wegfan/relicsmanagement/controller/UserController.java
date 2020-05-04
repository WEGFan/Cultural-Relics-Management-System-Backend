package cn.wegfan.relicsmanagement.controller;

import cn.wegfan.relicsmanagement.dto.UserChangePasswordDto;
import cn.wegfan.relicsmanagement.dto.UserInfoDto;
import cn.wegfan.relicsmanagement.service.UserService;
import cn.wegfan.relicsmanagement.vo.DataReturnVo;
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
    public DataReturnVo listAllUsers() {
        return DataReturnVo.success(userService.listAllInWorkUsers());
    }

    /**
     * 增加新用户【管理员】
     */
    @PostMapping("")
    public DataReturnVo addUser(@RequestBody UserInfoDto userInfo) {
        return DataReturnVo.success(userService.addUser(userInfo));
    }

    /**
     * 修改用户信息【管理员】
     *
     * @param userId 用户编号
     */
    @PutMapping("{userId}")
    public DataReturnVo updateUserInfo(@PathVariable Integer userId,
                                       @RequestBody UserInfoDto dto) {
        return DataReturnVo.success(userService.updateUserInfo(userId, dto));
    }

    /**
     * 修改用户密码
     */
    @PutMapping("password")
    public DataReturnVo updateUserPassword(@RequestBody UserChangePasswordDto dto) {
        return DataReturnVo.success(userService.changeUserPassword(dto.getOldPassword(), dto.getNewPassword()));
    }

    /**
     * 删除用户【管理员】
     *
     * @param userId 用户编号
     */
    @DeleteMapping("{userId}")
    public DataReturnVo deleteUser(@PathVariable Integer userId) {
        return DataReturnVo.success(userService.deleteUserById(userId));
    }

    /**
     * 导出用户信息 Excel 表【管理员】
     *
     * @param excel 是否导出成 Excel
     */
    @GetMapping(value = "", params = "excel=true")
    public DataReturnVo exportAllUsersToExcel(@RequestParam Boolean excel) {
        if (excel.equals(Boolean.FALSE)) {
            throw new NotImplementedException();
        }
        return DataReturnVo.success(userService.listAllInWorkUsers());
    }

}
