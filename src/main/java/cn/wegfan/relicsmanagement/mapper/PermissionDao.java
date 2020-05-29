package cn.wegfan.relicsmanagement.mapper;

import cn.wegfan.relicsmanagement.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionDao extends BaseMapper<Permission> {

    @Select("SELECT * FROM permission")
    List<Permission> selectPermissionList();

    @SuppressWarnings("unused")
    @Select("SELECT permission.* FROM permission, user_extra_permission " +
            "WHERE user_extra_permission.permission_id = permission.id AND user_id = #{userId}")
    List<Permission> selectListByUserId(Integer userId);

    @Select("SELECT permission.* FROM permission, job_permission " +
            "WHERE job_permission.permission_id = permission.id AND job_id = #{jobId}")
    List<Permission> selectListByJobId(Integer jobId);

}
