package cn.wegfan.relicsmanagement.mapper;

import cn.wegfan.relicsmanagement.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component
public interface UserDao extends BaseMapper<User> {

    @Select("SELECT * FROM user")
    @Results(id = "userResultMap", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "permissions", column = "id", javaType = List.class,
                    many = @Many(select = "cn.wegfan.relicsmanagement.mapper.PermissionDao.listPermissionsByUserId",
                            fetchType = FetchType.EAGER))
    })
    List<User> selectList();

    @Select("SELECT * FROM user WHERE id = #{userId}")
    @ResultMap("userResultMap")
    User selectById(Integer userId);

}
