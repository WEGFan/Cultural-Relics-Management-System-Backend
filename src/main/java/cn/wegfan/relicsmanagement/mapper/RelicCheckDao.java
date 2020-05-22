package cn.wegfan.relicsmanagement.mapper;

import cn.wegfan.relicsmanagement.entity.RelicCheck;
import cn.wegfan.relicsmanagement.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

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
    Page<RelicCheck> selectPageByWarehouseId(Page<?> page, Integer warehouseId);

    @Select("SELECT * FROM relic_check WHERE id = #{checkId} AND end_time IS NULL LIMIT 1")
    RelicCheck selectNotEndByCheckId(Integer checkId);

    @Select("SELECT * FROM relic_check WHERE warehouse_id = #{warehouseId} AND end_time IS NULL LIMIT 1")
    RelicCheck selectNotEndByWarehouseId(Integer warehouseId);

    @Update("UPDATE relic_check SET end_time = now() WHERE id = #{checkId}")
    int updateEndTimeByCheckId(Integer checkId);

}
