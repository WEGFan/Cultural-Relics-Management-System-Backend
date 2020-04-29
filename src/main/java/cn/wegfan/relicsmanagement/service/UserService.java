package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.dto.UserInfoDto;
import cn.wegfan.relicsmanagement.vo.SuccessVo;
import cn.wegfan.relicsmanagement.vo.UserIdVo;
import cn.wegfan.relicsmanagement.vo.UserVo;

import java.util.List;

public interface UserService {

    List<UserVo> listAllUsers();

    UserVo getUserById(Integer userId);

    UserIdVo addUser(UserInfoDto userInfo);

    SuccessVo updateUserInfo(UserInfoDto userInfo);

}

