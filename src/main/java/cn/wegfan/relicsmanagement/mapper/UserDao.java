package cn.wegfan.relicsmanagement.mapper;

import cn.wegfan.relicsmanagement.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    @Select("SELECT * FROM user WHERE delete_time IS NULL")
    List<User> selectNotDeletedList();

    @Select("SELECT * FROM user WHERE work_id = #{workId}")
    User selectByWorkId(Integer workId);

    @Select("SELECT * FROM user WHERE work_id = #{workId} AND delete_time IS NULL")
    User selectNotDeletedByWorkId(Integer workId);

    @Select("SELECT * FROM user WHERE id = #{userId} AND delete_time IS NULL")
    User selectNotDeletedById(Integer userId);

    @Update("UPDATE user SET delete_time = now() WHERE id = #{userId}")
    int deleteUserById(Integer userId);

    @Select("SELECT * FROM user WHERE work_id = #{workId} AND password = #{password} AND delete_time IS NULL LIMIT 1")
    User selectNotDeletedByWorkIdAndPassword(Integer workId, String password);

}