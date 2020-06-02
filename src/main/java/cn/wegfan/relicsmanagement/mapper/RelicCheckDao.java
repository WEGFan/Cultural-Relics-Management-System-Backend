package cn.wegfan.relicsmanagement.mapper;

import cn.wegfan.relicsmanagement.entity.RelicCheck;
import cn.wegfan.relicsmanagement.entity.Warehouse;
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
            "ORDER BY start_time DESC" +
            "</script>")
    // language=none
    @Results(id = "relicCheckResultMap", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "checkCount", column = "id", javaType = Integer.class,
                    one = @One(select = "cn.wegfan.relicsmanagement.mapper.RelicCheckDetailDao.countCheckedByCheckId",
                            fetchType = FetchType.EAGER)),
            @Result(property = "abnormalCount", column = "id", javaType = Integer.class,
                    one = @One(select = "cn.wegfan.relicsmanagement.mapper.RelicCheckDetailDao.countAbnormalByCheckId",
                            fetchType = FetchType.EAGER)),
            @Result(property = "warehouseId", column = "warehouse_id"),
            @Result(property = "warehouse", column = "warehouse_id", javaType = Warehouse.class,
                    one = @One(select = "cn.wegfan.relicsmanagement.mapper.WarehouseDao.selectByWarehouseId",
                            fetchType = FetchType.EAGER))
    })
    Page<RelicCheck> selectPageByWarehouseId(Page<?> page, Integer warehouseId);

    @Select("SELECT * FROM relic_check WHERE id = #{checkId}")
    RelicCheck selectByCheckId(Integer checkId);

    @Select("SELECT * FROM relic_check WHERE id = #{checkId} AND end_time IS NULL LIMIT 1")
    @ResultMap("relicCheckResultMap")
    RelicCheck selectNotEndByCheckId(Integer checkId);

    @Select("SELECT * FROM relic_check WHERE warehouse_id = #{warehouseId} AND end_time IS NULL LIMIT 1")
    RelicCheck selectNotEndByWarehouseId(Integer warehouseId);

    @Update("UPDATE relic_check SET end_time = now() WHERE id = #{checkId}")
    int updateEndTimeByCheckId(Integer checkId);

}
