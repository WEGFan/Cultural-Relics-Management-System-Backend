package cn.wegfan.relicsmanagement.mapper;

import cn.wegfan.relicsmanagement.entity.Relic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RelicDao extends BaseMapper<Relic> {

    @Select("SELECT * FROM relic WHERE warehouse_id = #{warehouseId} AND delete_time IS NULL")
    List<Relic> selectNotDeletedByWarehouseId(Integer warehouseId);

    @Select("SELECT * FROM relic WHERE shelf_id = #{shelfId} AND delete_time IS NULL")
    List<Relic> selectNotDeletedByShelfId(Integer shelfId);

    @Select("SELECT * FROM relic WHERE id = #{relicId} AND delete_time IS NULL LIMIT 1")
    Relic selectNotDeletedByRelicId(Integer relicId);

    // language=xml
    @Select("<script>" +
            "SELECT * FROM relic" +
            "<where>" +
            "  <if test='name != null'>" +
            "    AND name LIKE concat('%', #{name}, '%')" +
            "  </if>" +
            "  <if test='status != null'>" +
            "    AND status_id = #{status}" +
            "  </if>" +
            "  <if test='dateType != null'>" +
            "    <if test='startTime != null'>" +
            "      AND ${dateType} &gt;= #{startTime}" +
            "    </if>" +
            "    <if test='endTime != null'>" +
            "      AND ${dateType} &lt;= #{endTime}" +
            "    </if>" +
            "  </if>" +
            "  AND delete_time IS NULL" +
            "</where>" +
            "<if test='dateType != null'>" +
            "  ORDER BY ${dateType} DESC" +
            "</if>" +
            "</script>")
    Page<Relic> selectPageNotDeletedByCondition(Page<?> page, String name, Integer status,
                                                String dateType, Date startTime, Date endTime);

    @Update("UPDATE relic SET warehouse_id = NULL, shelf_id = NULL, delete_time = now() WHERE id = #{relicId}")
    int deleteRelicById(Integer relicId);

    @Update("UPDATE relic SET warehouse_id = #{newWarehouseId} WHERE warehouse_id = #{oldWarehouseId}")
    int changeRelicWarehouse(Integer oldWarehouseId, Integer newWarehouseId);

}
