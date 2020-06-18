package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.model.dto.UserInfoDto;
import cn.wegfan.relicsmanagement.model.vo.*;

import java.util.List;

public interface UserService {

    /**
     * 获取所有未删除的用户
     *
     * @return 用户对象列表
     */
    List<UserVo> listAllInWorkUsers();

    /**
     * 分页获取所有用户
     *
     * @param pageIndex 当前页码
     * @param pageSize  每页个数
     *
     * @return 用户分页对象
     */
    PageResultVo<UserVo> listAllInWorkUsersByPage(long pageIndex, long pageSize);

    /**
     * 根据用户编号获取用户
     *
     * @param userId 用户编号
     *
     * @return 用户对象
     */
    UserVo getUserById(Integer userId);

    /**
     * 创建用户
     *
     * @param userInfo 用户对象
     *
     * @return 用户编号对象
     */
    UserIdVo addUser(UserInfoDto userInfo);

    /**
     * 根据用户编号更新用户
     *
     * @param userId   用户编号
     * @param userInfo 用户对象
     *
     * @return 成功对象
     */
    SuccessVo updateUserInfo(Integer userId, UserInfoDto userInfo);

    /**
     * 根据用户编号删除用户
     *
     * @param userId 用户编号
     *
     * @return 成功对象
     */
    SuccessVo deleteUserById(Integer userId);

    /**
     * 用户登录
     *
     * @param workId   工号
     * @param password 密码
     *
     * @return 用户对象
     */
    UserVo userLogin(Integer workId, String password);

    /**
     * 用户退出登录
     *
     * @return 成功对象
     */
    SuccessVo userLogout();

    /**
     * 用户修改密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     *
     * @return 成功对象
     */
    SuccessVo changeUserPassword(String oldPassword, String newPassword);

    /**
     * 导出所有用户到 Excel 表
     *
     * @return 文件路径对象
     */
    FilePathVo exportAllUsersToExcel();

}

