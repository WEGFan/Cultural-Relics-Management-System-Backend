package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.model.dto.UserInfoDto;
import cn.wegfan.relicsmanagement.model.vo.*;

import java.util.List;

public interface UserService {

    List<UserVo> listAllInWorkUsers();

    PageResultVo<UserVo> listAllInWorkUsersByPage(long pageIndex, long pageSize);

    UserVo getUserById(Integer userId);

    UserIdVo addUser(UserInfoDto userInfo);

    SuccessVo updateUserInfo(Integer userId, UserInfoDto userInfo);

    SuccessVo deleteUserById(Integer userId);

    UserVo userLogin(Integer workId, String password);

    SuccessVo userLogout();

    SuccessVo changeUserPassword(String oldPassword, String newPassword);

    FilePathVo exportAllUsersToExcel();

}

