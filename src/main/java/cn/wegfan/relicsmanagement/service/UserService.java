package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.dto.UserInfoDto;
import cn.wegfan.relicsmanagement.vo.SuccessVo;
import cn.wegfan.relicsmanagement.vo.UserIdVo;
import cn.wegfan.relicsmanagement.vo.UserVo;

import java.util.List;

public interface UserService {

    List<UserVo> listAllInWorkUsers();

    UserVo getUserById(Integer userId);

    UserIdVo addUser(UserInfoDto userInfo);

    SuccessVo updateUserInfo(Integer userId, UserInfoDto userInfo);

    SuccessVo deleteUserById(Integer userId);

    UserVo userLogin(Integer workId, String password);

    SuccessVo userLogout();

}

