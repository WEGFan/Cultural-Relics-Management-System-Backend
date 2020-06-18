package cn.wegfan.relicsmanagement.mapper;

import cn.wegfan.relicsmanagement.model.entity.Shelf;
import cn.wegfan.relicsmanagement.model.entity.Warehouse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShelfDao extends BaseMapper<Shelf> {

    /**
     * 根据货架编号获取货架
     *
     * @param shelfId 货架编号
     *
     * @return 货架对象
     */
    @SuppressWarnings("unused")
    @Select("SELECT * FROM shelf WHERE id = #{shelfId} LIMIT 1")
    @Results(id = "shelfResultMap", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "warehouseId", column = "warehouse_id"),
            @Result(property = "warehouse", column = "warehouse_id", javaType = Warehouse.class,
                    one = @One(select = "cn.wegfan.relicsmanagement.mapper.WarehouseDao.selectByWarehouseId",
                            fetchType = FetchType.EAGER))
    })
    Shelf selectByShelfId(Integer shelfId);

    /**
     * 分页筛选或获取所有未删除的货架
     *
     * @param page        分页对象
     * @param warehouseId 根据仓库编号筛选
     * @param name        根据名称筛选
     *
     * @return 货架分页对象
     */
    // language=xml
    @Select("<script>" +
            "SELECT * FROM shelf" +
            "  <where>" +
            "    <if test='warehouseId != null'>" +
            "      AND warehouse_id = #{warehouseId}" +
            "    </if>" +
            "    <if test='name != null'>" +
            "      AND name LIKE concat('%', #{name}, '%')" +
            "    </if>" +
            "    AND delete_time IS NULL" +
            "  </where>" +
            "</script>")
    // language=none
    @ResultMap("shelfResultMap")
    Page<Shelf> selectPageNotDeletedByWarehouseIdAndName(Page<?> page, Integer warehouseId, String name);

    /**
     * 获取所有未删除的货架
     *
     * @return 货架对象列表
     */
    @Select("SELECT * FROM shelf WHERE delete_time IS NULL")
    @ResultMap("shelfResultMap")
    List<Shelf> selectNotDeletedList();

    /**
     * 根据仓库编号获取未删除的货架列表
     *
     * @param warehouseId 仓库编号
     *
     * @return 货架对象列表
     */
    @Select("SELECT * FROM shelf WHERE warehouse_id = #{warehouseId} AND delete_time IS NULL")
    @ResultMap("shelfResultMap")
    List<Shelf> selectNotDeletedListByWarehouseId(Integer warehouseId);

    /**
     * 根据仓库编号和名称获取未删除的货架
     *
     * @param warehouseId 仓库编号
     * @param name        名称
     *
     * @return 货架对象
     */
    @Select("SELECT * FROM shelf WHERE warehouse_id = #{warehouseId} AND name = #{name} AND delete_time IS NULL LIMIT 1")
    Shelf selectNotDeletedByWarehouseIdAndExactName(Integer warehouseId, String name);

    /**
     * 根据仓库编号和货架编号获取未删除的货架
     *
     * @param warehouseId 仓库编号
     * @param shelfId     货架编号
     *
     * @return 货架对象
     */
    @Select("SELECT * FROM shelf WHERE warehouse_id = #{warehouseId} AND id = #{shelfId} AND delete_time IS NULL LIMIT 1")
    Shelf selectNotDeletedByWarehouseIdAndShelfId(Integer warehouseId, Integer shelfId);

    /**
     * 根据货架编号获取未删除的货架
     *
     * @param shelfId 货架编号
     *
     * @return 货架对象
     */
    @Select("SELECT * FROM shelf WHERE id = #{shelfId} AND delete_time IS NULL LIMIT 1")
    Shelf selectNotDeletedById(Integer shelfId);

    /**
     * 根据货架编号删除货架
     *
     * @param shelfId 货架编号
     *
     * @return 数据库修改的行数
     */
    @Update("UPDATE shelf SET delete_time = now() WHERE id = #{shelfId}")
    int deleteShelfById(Integer shelfId);

}
