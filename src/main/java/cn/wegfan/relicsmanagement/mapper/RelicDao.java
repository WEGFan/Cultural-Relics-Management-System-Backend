package cn.wegfan.relicsmanagement.mapper;

import cn.wegfan.relicsmanagement.model.entity.Relic;
import cn.wegfan.relicsmanagement.model.entity.Shelf;
import cn.wegfan.relicsmanagement.model.entity.Warehouse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RelicDao extends BaseMapper<Relic> {

    /**
     * 根据仓库编号获取文物列表
     *
     * @param warehouseId 仓库编号
     *
     * @return 文物对象列表
     */
    @Select("SELECT * FROM relic WHERE warehouse_id = #{warehouseId} AND delete_time IS NULL")
    @Results(id = "relicResultMap", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "warehouseId", column = "warehouse_id"),
            @Result(property = "warehouse", column = "warehouse_id", javaType = Warehouse.class,
                    one = @One(select = "cn.wegfan.relicsmanagement.mapper.WarehouseDao.selectByWarehouseId",
                            fetchType = FetchType.EAGER)),
            @Result(property = "shelfId", column = "shelf_id"),
            @Result(property = "shelf", column = "shelf_id", javaType = Shelf.class,
                    one = @One(select = "cn.wegfan.relicsmanagement.mapper.ShelfDao.selectByShelfId",
                            fetchType = FetchType.EAGER))
    })
    List<Relic> selectNotDeletedByWarehouseId(Integer warehouseId);

    /**
     * 根据货架编号获取文物列表
     *
     * @param shelfId 货架编号
     *
     * @return 文物对象列表
     */
    @Select("SELECT * FROM relic WHERE shelf_id = #{shelfId} AND delete_time IS NULL")
    List<Relic> selectNotDeletedByShelfId(Integer shelfId);

    /**
     * 根据文物编号获取未删除的文物
     *
     * @param relicId 文物编号
     *
     * @return 文物对象
     */
    @Select("SELECT * FROM relic WHERE id = #{relicId} AND delete_time IS NULL LIMIT 1")
    @ResultMap("relicResultMap")
    Relic selectNotDeletedByRelicId(Integer relicId);

    /**
     * 分页筛选或获取所有文物
     *
     * @param page        分页对象
     * @param name        按名称筛选
     * @param status      按状态筛选
     * @param warehouseId 按仓库编号筛选
     * @param shelfId     按货架编号筛选
     * @param dateType    时间类型
     * @param startTime   开始时间
     * @param endTime     结束时间
     *
     * @return 文物分页对象
     */
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
            "  <if test='warehouseId != null'>" +
            "    AND warehouse_id = #{warehouseId}" +
            "  </if>" +
            "  <if test='shelfId != null'>" +
            "    AND shelf_id = #{shelfId}" +
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
            "ORDER BY" +
            "<if test='dateType != null'>" +
            "  ${dateType} DESC," +
            "</if>" +
            "update_time DESC" +
            "</script>")
    // language=none
    @ResultMap("relicResultMap")
    Page<Relic> selectPageNotDeletedByCondition(Page<?> page, String name, Integer status,
                                                Integer warehouseId, Integer shelfId,
                                                String dateType, Date startTime, Date endTime);

    /**
     * 根据文物编号删除文物
     *
     * @param relicId 文物编号
     *
     * @return 数据库修改的行数
     */
    @Update("UPDATE relic SET warehouse_id = NULL, shelf_id = NULL, delete_time = now() WHERE id = #{relicId}")
    int deleteRelicById(Integer relicId);

    /**
     * 更新文物所在仓库
     *
     * @param oldWarehouseId 旧仓库编号
     * @param newWarehouseId 新仓库编号
     *
     * @return 数据库修改的行数
     */
    @Update("UPDATE relic SET warehouse_id = #{newWarehouseId} WHERE warehouse_id = #{oldWarehouseId}")
    int updateRelicWarehouse(Integer oldWarehouseId, Integer newWarehouseId);

}
