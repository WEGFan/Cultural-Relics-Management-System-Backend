package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.vo.UserVo;

import java.util.List;


public interface UserService {

    List<UserVo> listAllUsers();
    
    UserVo getUserById(Integer userId);

}

