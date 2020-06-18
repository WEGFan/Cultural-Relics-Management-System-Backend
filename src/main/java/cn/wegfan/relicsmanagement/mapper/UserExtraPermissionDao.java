package cn.wegfan.relicsmanagement.mapper;

import cn.wegfan.relicsmanagement.model.entity.UserExtraPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Repository;

@Repository
public interface UserExtraPermissionDao extends BaseMapper<UserExtraPermission> {

    /**
     * 根据用户编号删除用户的所有额外权限
     *
     * @param userId 用户编号
     *
     * @return 数据库修改的行数
     */
    @Delete("DELETE FROM user_extra_permission WHERE user_id = #{userId}")
    int deleteByUserId(Integer userId);

}
