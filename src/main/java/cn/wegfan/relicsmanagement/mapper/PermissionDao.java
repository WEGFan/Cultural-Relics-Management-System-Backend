package cn.wegfan.relicsmanagement.mapper;

import cn.wegfan.relicsmanagement.entity.Job;
import cn.wegfan.relicsmanagement.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PermissionDao extends BaseMapper<Permission> {

    @Select("SELECT * FROM permission, user_permission " +
            "WHERE user_permission.permission_id = permission.id AND user_id = #{userId}")
    List<Permission> listPermissionsByUserId(Integer userId);

}
