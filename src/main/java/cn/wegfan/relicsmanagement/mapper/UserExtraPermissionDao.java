package cn.wegfan.relicsmanagement.mapper;

import cn.wegfan.relicsmanagement.entity.UserExtraPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Repository;

@Repository
public interface UserExtraPermissionDao extends BaseMapper<UserExtraPermission> {

    @Delete("DELETE FROM user_extra_permission WHERE user_id = #{userId}")
    int deleteByUserId(Integer userId);

}
