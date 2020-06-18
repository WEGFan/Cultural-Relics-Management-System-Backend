package cn.wegfan.relicsmanagement.mapper;

import cn.wegfan.relicsmanagement.model.entity.Warehouse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseDao extends BaseMapper<Warehouse> {

    /**
     * 分页筛选或获取所有未删除的仓库
     *
     * @param page 分页对象
     * @param name 按名称筛选
     *
     * @return 仓库分页对象
     */
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

    /**
     * 获取所有未删除的仓库
     *
     * @return 仓库对象列表
     */
    @Select("SELECT * FROM warehouse WHERE delete_time IS NULL")
    List<Warehouse> selectNotDeletedList();

    /**
     * 根据名称获取未删除的仓库
     *
     * @param name 名称
     *
     * @return 仓库对象
     */
    @Select("SELECT * FROM warehouse WHERE name = #{name} AND delete_time IS NULL LIMIT 1")
    Warehouse selectNotDeletedByExactName(String name);

    /**
     * 根据仓库编号获取未删除的仓库
     *
     * @param warehouseId 仓库编号
     *
     * @return 仓库对象
     */
    @Select("SELECT * FROM warehouse WHERE id = #{warehouseId} AND delete_time IS NULL LIMIT 1")
    Warehouse selectNotDeletedById(Integer warehouseId);

    /**
     * 根据仓库编号删除仓库
     *
     * @param warehouseId 仓库编号
     *
     * @return 数据库修改的行数
     */
    @Update("UPDATE warehouse SET delete_time = now() WHERE id = #{warehouseId}")
    int deleteWarehouseById(Integer warehouseId);

    /**
     * 根据仓库编号获取仓库
     *
     * @param warehouseId 仓库编号
     *
     * @return 仓库对象
     */
    @Select("SELECT * FROM warehouse WHERE id = #{warehouseId} LIMIT 1")
    @SuppressWarnings("unused")
    Warehouse selectByWarehouseId(Integer warehouseId);

}
