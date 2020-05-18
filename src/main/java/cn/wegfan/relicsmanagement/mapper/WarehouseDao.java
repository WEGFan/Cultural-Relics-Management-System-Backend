package cn.wegfan.relicsmanagement.mapper;

import cn.wegfan.relicsmanagement.entity.Warehouse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseDao extends BaseMapper<Warehouse> {

    // language=xml
    @Select("<script>" +
            "SELECT * FROM warehouse" +
            "  <where>" +
            "    <if test='name != null'>" +
            "      AND name LIKE concat('%', #{name}, '%')" +
            "    </if>" +
            "    AND delete_time IS NULL" +
            "  </where>" +
            "</script>")
    Page<Warehouse> selectPageNotDeletedByName(Page<?> page, String name);

    @Select("SELECT * FROM warehouse WHERE delete_time IS NULL")
    List<Warehouse> selectNotDeletedList();

    @Select("SELECT * FROM warehouse WHERE name = #{name} AND delete_time IS NULL")
    Warehouse selectNotDeletedByExactName(String name);

    @Select("SELECT * FROM warehouse WHERE id = #{warehouseId} AND delete_time IS NULL")
    Warehouse selectNotDeletedById(Integer warehouseId);

    @Update("UPDATE warehouse SET delete_time = now() WHERE id = #{warehouseId}")
    int deleteWarehouseById(Integer warehouseId);

}
