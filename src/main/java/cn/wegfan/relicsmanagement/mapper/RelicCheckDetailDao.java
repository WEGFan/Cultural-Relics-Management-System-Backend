package cn.wegfan.relicsmanagement.mapper;

import cn.wegfan.relicsmanagement.entity.Relic;
import cn.wegfan.relicsmanagement.entity.RelicCheckDetail;
import cn.wegfan.relicsmanagement.entity.User;
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
            "<if test='checked != false'>" +
            "  ORDER BY check_time DESC" +
            "</if>" +
            "</script>")
    // language=none
    @Results(id = "relicCheckDetailResultMap", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "relic", column = "relic_id", javaType = Relic.class,
                    one = @One(select = "cn.wegfan.relicsmanagement.mapper.RelicDao.selectNotDeletedByRelicId",
                            fetchType = FetchType.EAGER)),
            @Result(property = "operator", column = "operator_id", javaType = User.class,
                    one = @One(select = "cn.wegfan.relicsmanagement.mapper.UserDao.selectByUserId",
                            fetchType = FetchType.EAGER))
    })
    Page<RelicCheckDetail> selectPageByCheckId(Page<?> page, Integer checkId, Boolean checked);

    @Select("SELECT * FROM relic_check_detail WHERE check_id = #{checkId} AND relic_id = #{relicId} LIMIT 1")
    RelicCheckDetail selectByCheckIdAndRelicId(Integer checkId, Integer relicId);

    @Select("SELECT * FROM relic_check_detail WHERE check_id = #{checkId} AND relic_id = #{relicId} AND check_time IS NULL LIMIT 1")
    RelicCheckDetail selectNotCheckedByCheckIdAndRelicId(Integer checkId, Integer relicId);

    @Select("SELECT * FROM relic_check_detail WHERE check_id = #{checkId} AND old_shelf_id = #{shelfId} AND check_time IS NULL")
    List<RelicCheckDetail> selectNotCheckedListByCheckIdAndOldShelfId(Integer checkId, Integer shelfId);

    @SuppressWarnings("unused")
    @Select("SELECT count(*) FROM relic_check_detail WHERE check_id = #{checkId} AND check_time IS NOT NULL")
    int countCheckedByCheckId(Integer checkId);

    @SuppressWarnings("unused")
    @Select("SELECT count(*) FROM relic_check_detail " +
            "WHERE check_id = #{checkId} " +
            "AND (check_time IS NULL OR (old_warehouse_id != new_warehouse_id OR old_shelf_id != new_shelf_id))")
    int countAbnormalByCheckId(Integer checkId);

}
