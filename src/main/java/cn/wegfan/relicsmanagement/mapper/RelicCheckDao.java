package cn.wegfan.relicsmanagement.mapper;

import cn.wegfan.relicsmanagement.model.entity.RelicCheck;
import cn.wegfan.relicsmanagement.model.entity.Warehouse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

@Repository
public interface RelicCheckDao extends BaseMapper<RelicCheck> {

    /**
     * 分页筛选或获取所有盘点记录
     *
     * @param page        分页对象
     * @param warehouseId 根据仓库编号筛选
     *
     * @return 盘点记录分页对象
     */
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

    /**
     * 根据盘点编号获取盘点记录
     *
     * @param checkId 盘点编号
     *
     * @return 盘点记录对象
     */
    @Select("SELECT * FROM relic_check WHERE id = #{checkId}")
    RelicCheck selectByCheckId(Integer checkId);

    /**
     * 根据盘点编号获取没有结束的盘点记录
     *
     * @param checkId 盘点编号
     *
     * @return 盘点记录对象
     */
    @Select("SELECT * FROM relic_check WHERE id = #{checkId} AND end_time IS NULL LIMIT 1")
    @ResultMap("relicCheckResultMap")
    RelicCheck selectNotEndByCheckId(Integer checkId);

    /**
     * 根据仓库编号获取没有结束的盘点记录
     *
     * @param warehouseId 仓库编号
     *
     * @return 盘点记录对象
     */
    @Select("SELECT * FROM relic_check WHERE warehouse_id = #{warehouseId} AND end_time IS NULL LIMIT 1")
    RelicCheck selectNotEndByWarehouseId(Integer warehouseId);

    /**
     * 根据盘点编号更新结束时间
     *
     * @param checkId 盘点编号
     *
     * @return 数据库修改的行数
     */
    @Update("UPDATE relic_check SET end_time = now() WHERE id = #{checkId}")
    int updateEndTimeByCheckId(Integer checkId);

}
