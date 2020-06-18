package cn.wegfan.relicsmanagement.mapper;

import cn.wegfan.relicsmanagement.model.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionDao extends BaseMapper<Permission> {

    /**
     * 获取所有权限
     *
     * @return 权限对象列表
     */
    @Select("SELECT * FROM permission")
    List<Permission> selectPermissionList();

    /**
     * 获取单个用户的额外权限
     *
     * @param userId 用户编号
     *
     * @return 权限对象列表
     */
    @SuppressWarnings("unused")
    @Select("SELECT permission.* FROM permission, user_extra_permission " +
            "WHERE user_extra_permission.permission_id = permission.id AND user_id = #{userId}")
    List<Permission> selectListByUserId(Integer userId);

    /**
     * 获取单个职务的基础权限
     *
     * @param jobId 职务编号
     *
     * @return 权限对象列表
     */
    @Select("SELECT permission.* FROM permission, job_permission " +
            "WHERE job_permission.permission_id = permission.id AND job_id = #{jobId}")
    List<Permission> selectListByJobId(Integer jobId);

}
