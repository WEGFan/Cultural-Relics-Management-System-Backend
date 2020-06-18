package cn.wegfan.relicsmanagement.mapper;

import cn.wegfan.relicsmanagement.model.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserDao extends BaseMapper<User> {

    /**
     * 获取所有未删除的用户
     *
     * @return 用户对象列表
     */
    @Select("SELECT * FROM user WHERE delete_time IS NULL")
    @Results(id = "userExtraPermissionsResultMap", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "extraPermissions", column = "id", javaType = Set.class,
                    many = @Many(select = "cn.wegfan.relicsmanagement.mapper.PermissionDao.selectListByUserId",
                            fetchType = FetchType.EAGER))
    })
    List<User> selectNotDeletedList();

    /**
     * 获取所有用户
     *
     * @return 用户对象列表
     */
    @Select("SELECT * FROM user")
    @ResultMap("userExtraPermissionsResultMap")
    List<User> selectUserList();

    /**
     * 分页获取所有用户
     *
     * @param page 分页对象
     *
     * @return 用户分页对象
     */
    @Select("SELECT * FROM user WHERE delete_time IS NULL")
    @ResultMap("userExtraPermissionsResultMap")
    Page<User> selectPageNotDeleted(Page<?> page);

    /**
     * 根据用户编号获取用户
     *
     * @param userId 用户编号
     *
     * @return 用户对象
     */
    @Select("SELECT * FROM user WHERE id = #{userId} LIMIT 1")
    @ResultMap("userExtraPermissionsResultMap")
    User selectByUserId(Integer userId);

    /**
     * 根据工号获取用户
     *
     * @param workId 工号
     *
     * @return 用户对象
     */
    @Select("SELECT * FROM user WHERE work_id = #{workId} LIMIT 1")
    @ResultMap("userExtraPermissionsResultMap")
    User selectByWorkId(Integer workId);

    /**
     * 根据工号获取未删除的用户
     *
     * @param workId 工号
     *
     * @return 用户对象
     */
    @Select("SELECT * FROM user WHERE work_id = #{workId} AND delete_time IS NULL LIMIT 1")
    @ResultMap("userExtraPermissionsResultMap")
    User selectNotDeletedByWorkId(Integer workId);

    /**
     * 根据用户编号获取未删除的用户
     *
     * @param userId 用户编号
     *
     * @return 用户对象
     */
    @Select("SELECT * FROM user WHERE id = #{userId} AND delete_time IS NULL LIMIT 1")
    @ResultMap("userExtraPermissionsResultMap")
    User selectNotDeletedById(Integer userId);

    /**
     * 根据用户编号删除用户
     *
     * @param userId 用户编号
     *
     * @return 数据库修改的行数
     */
    @Update("UPDATE user SET delete_time = now() WHERE id = #{userId}")
    int deleteUserById(Integer userId);

}
