package cn.wegfan.relicsmanagement.mapper;

import cn.wegfan.relicsmanagement.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionDao extends BaseMapper<Permission> {

    @Select("SELECT * FROM permission, user_permission " +
            "WHERE user_permission.permission_id = permission.id AND user_id = #{userId}")
    List<Permission> selectListByUserId(Integer userId);
    
   

}
