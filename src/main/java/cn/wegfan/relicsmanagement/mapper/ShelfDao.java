package cn.wegfan.relicsmanagement.mapper;

import cn.wegfan.relicsmanagement.entity.Shelf;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShelfDao extends BaseMapper<Shelf> {

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
    Page<Shelf> selectPageNotDeletedByWarehouseIdAndName(Page<?> page, Integer warehouseId, String name);

    @Select("SELECT * FROM shelf WHERE delete_time IS NULL")
    List<Shelf> selectNotDeletedList();

    @Select("SELECT * FROM shelf WHERE warehouse_id = #{warehouseId} AND name = #{name} AND delete_time IS NULL LIMIT 1")
    Shelf selectNotDeletedByWarehouseIdAndExactName(Integer warehouseId, String name);

    @Select("SELECT * FROM shelf WHERE warehouse_id = #{warehouseId} AND id = #{shelfId} AND delete_time IS NULL LIMIT 1")
    Shelf selectNotDeletedByWarehouseIdAndShelfId(Integer warehouseId, Integer shelfId);

    @Select("SELECT * FROM shelf WHERE id = #{shelfId} AND delete_time IS NULL LIMIT 1")
    Shelf selectNotDeletedById(Integer shelfId);

    @Update("UPDATE shelf SET delete_time = now() WHERE id = #{shelfId}")
    int deleteShelfById(Integer shelfId);

}
