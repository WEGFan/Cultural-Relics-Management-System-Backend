package cn.wegfan.relicsmanagement.mapper;

import cn.wegfan.relicsmanagement.entity.Relic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RelicDao extends BaseMapper<Relic> {

    @Select("SELECT * FROM relic WHERE warehouse_id = #{warehouseId} AND delete_time IS NULL")
    List<Relic> selectNotDeletedByWarehouseId(Integer warehouseId);

    @Select("SELECT * FROM relic WHERE id = #{relicId} AND delete_time IS NULL")
    Relic selectNotDeletedByRelicId(Integer relicId);

    // language=xml
    @Select("<script>" +
            "SELECT * FROM relic" +
            "<where>" +
            "<if test='name != null'>" +
            "AND NAME LIKE concat(\"%\", #{name}, \"%\")" +
            "</if>" +
            "AND delete_time IS NULL" +
            "</where>" +
            "</script>")
    Page<Relic> selectPageNotDeletedByCondition(Page<?> page, String name, Integer status,
                                                String dateType, Date startTime, Date endTime);

}
