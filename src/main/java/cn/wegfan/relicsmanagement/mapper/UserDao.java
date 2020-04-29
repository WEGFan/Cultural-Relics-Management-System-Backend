package cn.wegfan.relicsmanagement.mapper;

import cn.wegfan.relicsmanagement.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends BaseMapper<User> {

    // @Select("SELECT * FROM user")
    // @Results(id = "userResultMap", value = {
    //         @Result(property = "id", column = "id", id = true),
    //         @Result(property = "permissions", column = "id", javaType = List.class,
    //                 many = @Many(select = "cn.wegfan.relicsmanagement.mapper.PermissionDao.listPermissionsByUserId",
    //                         fetchType = FetchType.EAGER))
    // })
    // @Override
    // List<User> selectList();

    // @Select("SELECT * FROM user WHERE id = #{userId}")
    // @ResultMap("userResultMap")
    // @Override
    // User selectById(Serializable userId);

    @Select("SELECT * FROM user WHERE work_id = #{workId} LIMIT 1")
    User selectByWorkId(Integer workId);

}
