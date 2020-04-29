package cn.wegfan.relicsmanagement.mapper;

import cn.wegfan.relicsmanagement.entity.UserPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPermissionDao extends BaseMapper<UserPermission> {

    @Delete("DELETE FROM user_permission WHERE user_id = #{userId}")
    int deleteByUserId(Integer userId);

}
