package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.entity.User;
import cn.wegfan.relicsmanagement.vo.UserVo;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.mapping.FetchType;

import javax.xml.ws.ServiceMode;
import java.util.List;


public interface UserService {

    List<UserVo> listAllUsers();
    
    UserVo getUserById(Integer userId);

}

