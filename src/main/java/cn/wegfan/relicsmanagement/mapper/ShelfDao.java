package cn.wegfan.relicsmanagement.mapper;

import cn.wegfan.relicsmanagement.entity.Shelf;
import cn.wegfan.relicsmanagement.entity.Warehouse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShelfDao extends BaseMapper<Shelf> {

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

    @Select("SELECT * FROM shelf WHERE delete_time IS NULL")
    @ResultMap("shelfResultMap")
    List<Shelf> selectNotDeletedList();

    @Select("SELECT * FROM shelf WHERE warehouse_id = #{warehouseId} AND delete_time IS NULL")
    @ResultMap("shelfResultMap")
    List<Shelf> selectNotDeletedListByWarehouseId(Integer warehouseId);

    @Select("SELECT * FROM shelf WHERE warehouse_id = #{warehouseId} AND name = #{name} AND delete_time IS NULL LIMIT 1")
    Shelf selectNotDeletedByWarehouseIdAndExactName(Integer warehouseId, String name);

    @Select("SELECT * FROM shelf WHERE warehouse_id = #{warehouseId} AND id = #{shelfId} AND delete_time IS NULL LIMIT 1")
    Shelf selectNotDeletedByWarehouseIdAndShelfId(Integer warehouseId, Integer shelfId);

    @Select("SELECT * FROM shelf WHERE id = #{shelfId} AND delete_time IS NULL LIMIT 1")
    Shelf selectNotDeletedById(Integer shelfId);

    @Update("UPDATE shelf SET delete_time = now() WHERE id = #{shelfId}")
    int deleteShelfById(Integer shelfId);

}
