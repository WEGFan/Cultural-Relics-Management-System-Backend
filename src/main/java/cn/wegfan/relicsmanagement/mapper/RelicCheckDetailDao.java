package cn.wegfan.relicsmanagement.mapper;

import cn.wegfan.relicsmanagement.model.entity.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelicCheckDetailDao extends BaseMapper<RelicCheckDetail> {

    /**
     * 根据盘点编号分页筛选或获取所有盘点详情记录
     *
     * @param page    分页对象
     * @param checkId 盘点编号
     * @param checked 根据盘点状态筛选 true已盘点 false未盘点 null不筛选
     *
     * @return 盘点详情记录分页对象
     */
    // language=xml
    @Select("<script>" +
            "SELECT * FROM relic_check_detail" +
            "<where>" +
            "  check_id = #{checkId}" +
            "  <choose>" +
            "    <when test='checked == true'>" +
            "      AND check_time IS NOT NULL" +
            "    </when>" +
            "    <when test='checked == false'>" +
            "      AND check_time IS NULL" +
            "    </when>" +
            "  </choose>" +
            "</where>" +
            "ORDER BY" +
            "<if test='checked != false'>" +
            "  check_time DESC," +
            "</if>" +
            "relic_id ASC" +
            "</script>")
    // language=none
    @Results(id = "relicCheckDetailResultMap", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "relicId", column = "relic_id"),
            @Result(property = "relic", column = "relic_id", javaType = Relic.class,
                    one = @One(select = "cn.wegfan.relicsmanagement.mapper.RelicDao.selectNotDeletedByRelicId",
                            fetchType = FetchType.EAGER)),
            @Result(property = "operatorId", column = "operator_id"),
            @Result(property = "operator", column = "operator_id", javaType = User.class,
                    one = @One(select = "cn.wegfan.relicsmanagement.mapper.UserDao.selectByUserId",
                            fetchType = FetchType.EAGER)),
            @Result(property = "oldWarehouseId", column = "old_warehouse_id"),
            @Result(property = "oldWarehouse", column = "old_warehouse_id", javaType = Warehouse.class,
                    one = @One(select = "cn.wegfan.relicsmanagement.mapper.WarehouseDao.selectByWarehouseId",
                            fetchType = FetchType.EAGER)),
            @Result(property = "oldShelfId", column = "old_shelf_id"),
            @Result(property = "oldShelf", column = "old_shelf_id", javaType = Shelf.class,
                    one = @One(select = "cn.wegfan.relicsmanagement.mapper.ShelfDao.selectByShelfId",
                            fetchType = FetchType.EAGER)),
            @Result(property = "newWarehouseId", column = "new_warehouse_id"),
            @Result(property = "newWarehouse", column = "new_warehouse_id", javaType = Warehouse.class,
                    one = @One(select = "cn.wegfan.relicsmanagement.mapper.WarehouseDao.selectByWarehouseId",
                            fetchType = FetchType.EAGER)),
            @Result(property = "newShelfId", column = "new_shelf_id"),
            @Result(property = "newShelf", column = "new_shelf_id", javaType = Shelf.class,
                    one = @One(select = "cn.wegfan.relicsmanagement.mapper.ShelfDao.selectByShelfId",
                            fetchType = FetchType.EAGER))
    })
    Page<RelicCheckDetail> selectPageByCheckId(Page<?> page, Integer checkId, Boolean checked);

    /**
     * 按盘点编号和文物编号获取盘点详情记录
     *
     * @param checkId 盘点编号
     * @param relicId 文物编号
     *
     * @return 盘点详情记录对象
     */
    @Select("SELECT * FROM relic_check_detail WHERE check_id = #{checkId} AND relic_id = #{relicId} LIMIT 1")
    RelicCheckDetail selectByCheckIdAndRelicId(Integer checkId, Integer relicId);

    /**
     * 按盘点编号和文物编号获取没有被盘点的盘点详情记录
     *
     * @param checkId 盘点编号
     * @param relicId 文物编号
     *
     * @return 盘点详情记录对象
     */
    @Select("SELECT * FROM relic_check_detail WHERE check_id = #{checkId} AND relic_id = #{relicId} AND check_time IS NULL LIMIT 1")
    RelicCheckDetail selectNotCheckedByCheckIdAndRelicId(Integer checkId, Integer relicId);

    /**
     * 按盘点编号和货架编号获取没有被盘点的盘点详情记录列表
     *
     * @param checkId 盘点编号
     * @param shelfId 文物编号
     *
     * @return 盘点详情记录分页对象列表
     */
    @Select("SELECT * FROM relic_check_detail WHERE check_id = #{checkId} AND old_shelf_id = #{shelfId} AND check_time IS NULL")
    List<RelicCheckDetail> selectNotCheckedListByCheckIdAndOldShelfId(Integer checkId, Integer shelfId);

    /**
     * 根据盘点编号统计已盘点文物数量
     *
     * @param checkId 盘点编号
     *
     * @return 已盘点文物数量
     */
    @SuppressWarnings("unused")
    @Select("SELECT count(*) FROM relic_check_detail WHERE check_id = #{checkId} AND check_time IS NOT NULL")
    int countCheckedByCheckId(Integer checkId);

    /**
     * 根据盘点编号统计盘点异常的文物数量
     *
     * @param checkId 盘点编号
     *
     * @return 盘点异常的文物数量
     */
    @SuppressWarnings("unused")
    @Select("SELECT count(*) FROM relic_check_detail " +
            "WHERE check_id = #{checkId} " +
            "AND (check_time IS NULL OR (old_warehouse_id != new_warehouse_id OR old_shelf_id != new_shelf_id))")
    int countAbnormalByCheckId(Integer checkId);

}
