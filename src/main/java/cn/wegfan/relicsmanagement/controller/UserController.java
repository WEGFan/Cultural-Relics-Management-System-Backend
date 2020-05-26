package cn.wegfan.relicsmanagement.controller;

import cn.wegfan.relicsmanagement.dto.UserChangePasswordDto;
import cn.wegfan.relicsmanagement.dto.UserInfoDto;
import cn.wegfan.relicsmanagement.dto.stringdto.UserInfoStringDto;
import cn.wegfan.relicsmanagement.service.UserService;
import cn.wegfan.relicsmanagement.util.PermissionCodeEnum;
import cn.wegfan.relicsmanagement.vo.DataReturnVo;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MapperFacade mapperFacade;

    /**
     * 获取所有用户信息【管理员】
     */
    @GetMapping("")
    @RequiresPermissions(PermissionCodeEnum.ADMIN)
    public DataReturnVo listAllUsers(@RequestParam(required = false) Integer page,
                                     @RequestParam(required = false) Integer count) {
        if (page != null && count != null) {
            return DataReturnVo.success(userService.listAllInWorkUsersByPage(page, count));
        }
        return DataReturnVo.success(userService.listAllInWorkUsers());
    }

    /**
     * 增加新用户【管理员】
     */
    @PostMapping("")
    @RequiresPermissions(PermissionCodeEnum.ADMIN)
    public DataReturnVo addUser(@RequestBody @Valid UserInfoStringDto stringDto) {
        UserInfoDto dto = mapperFacade.map(stringDto, UserInfoDto.class);
        return DataReturnVo.success(userService.addUser(dto));
    }

    /**
     * 修改用户信息【管理员】
     *
     * @param userId 用户编号
     */
    @PutMapping("{userId}")
    @RequiresPermissions(PermissionCodeEnum.ADMIN)
    public DataReturnVo updateUserInfo(@PathVariable Integer userId,
                                       @RequestBody @Valid UserInfoStringDto stringDto) {
        UserInfoDto dto = mapperFacade.map(stringDto, UserInfoDto.class);
        return DataReturnVo.success(userService.updateUserInfo(userId, dto));
    }

    /**
     * 修改用户密码
     */
    @PutMapping("password")
    @RequiresUser
    public DataReturnVo updateUserPassword(@RequestBody @Valid UserChangePasswordDto dto) {
        return DataReturnVo.success(userService.changeUserPassword(dto.getOldPassword(), dto.getNewPassword()));
    }

    /**
     * 删除用户【管理员】
     *
     * @param userId 用户编号
     */
    @DeleteMapping("{userId}")
    @RequiresPermissions(PermissionCodeEnum.ADMIN)
    public DataReturnVo deleteUser(@PathVariable Integer userId) {
        return DataReturnVo.success(userService.deleteUserById(userId));
    }

    /**
     * 导出用户信息 Excel 表【管理员】
     *
     * @param excel 是否导出成 Excel
     */
    @GetMapping(value = "", params = "excel=true")
    @RequiresPermissions(PermissionCodeEnum.ADMIN)
    public DataReturnVo exportAllUsersToExcel(@RequestParam Boolean excel) {
        if (excel.equals(Boolean.FALSE)) {
            throw new NotImplementedException();
        }
        return DataReturnVo.success(userService.listAllInWorkUsers());
    }

}
