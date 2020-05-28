package cn.wegfan.relicsmanagement.mapper;

import cn.wegfan.relicsmanagement.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserDao extends BaseMapper<User> {

    @Select("SELECT * FROM user WHERE delete_time IS NULL")
    @Results(id = "userExtraPermissionsResultMap", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "extraPermissions", column = "id", javaType = Set.class,
                    many = @Many(select = "cn.wegfan.relicsmanagement.mapper.PermissionDao.selectListByUserId",
                            fetchType = FetchType.EAGER))
    })
    List<User> selectNotDeletedList();

    @Select("SELECT * FROM user")
    @ResultMap("userExtraPermissionsResultMap")
    List<User> selectUserList();

    @Select("SELECT * FROM user WHERE delete_time IS NULL")
    @ResultMap("userExtraPermissionsResultMap")
    Page<User> selectPageNotDeleted(Page<?> page);

    @Select("SELECT * FROM user WHERE id = #{userId} LIMIT 1")
    @ResultMap("userExtraPermissionsResultMap")
    User selectByUserId(Integer userId);

    @Select("SELECT * FROM user WHERE work_id = #{workId} LIMIT 1")
    @ResultMap("userExtraPermissionsResultMap")
    User selectByWorkId(Integer workId);

    @Select("SELECT * FROM user WHERE work_id = #{workId} AND delete_time IS NULL LIMIT 1")
    @ResultMap("userExtraPermissionsResultMap")
    User selectNotDeletedByWorkId(Integer workId);

    @Select("SELECT * FROM user WHERE id = #{userId} AND delete_time IS NULL LIMIT 1")
    @ResultMap("userExtraPermissionsResultMap")
    User selectNotDeletedById(Integer userId);

    @Update("UPDATE user SET delete_time = now() WHERE id = #{userId}")
    int deleteUserById(Integer userId);

    @Select("SELECT * FROM user WHERE work_id = #{workId} AND password = #{password} AND delete_time IS NULL LIMIT 1")
    @ResultMap("userExtraPermissionsResultMap")
    User selectNotDeletedByWorkIdAndPassword(Integer workId, String password);

}
