package cn.wegfan.relicsmanagement.mapper;

import cn.wegfan.relicsmanagement.entity.Relic;
import cn.wegfan.relicsmanagement.entity.RelicCheck;
import cn.wegfan.relicsmanagement.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface RelicCheckDao extends BaseMapper<RelicCheck> {

    // language=xml
    @Select("<script>" +
            "SELECT * FROM relic_check" +
            "<where>" +
            "  <if test='warehouseId != null'>" +
            "    AND warehouse_id = #{warehouseId}" +
            "  </if>" +
            "</where>" +
            "</script>")
    // language=none
    @Results(id = "relicCheckOperatorResultMap", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "operator", column = "operator_id", javaType = User.class,
                    one = @One(select = "cn.wegfan.relicsmanagement.mapper.UserDao.selectByUserId",
                            fetchType = FetchType.EAGER))
    })
    Page<RelicCheck> selectPageByWarehouseId(Page<?> page, Integer warehouseId);

}
