package cn.wegfan.relicsmanagement.mapper;

import cn.wegfan.relicsmanagement.entity.Relic;
import cn.wegfan.relicsmanagement.entity.RelicCheckDetail;
import cn.wegfan.relicsmanagement.entity.User;
import cn.wegfan.relicsmanagement.vo.RelicCheckDetailVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RelicCheckDetailDao extends BaseMapper<RelicCheckDetail> {

    @Select("SELECT * FROM relic_check_detail WHERE check_id = #{checkId}")
    @Results(id = "relicCheckDetailResultMap", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "relic", column = "relic_id", javaType = Relic.class,
                    one = @One(select = "cn.wegfan.relicsmanagement.mapper.RelicDao.selectNotDeletedByRelicId",
                            fetchType = FetchType.EAGER)),
            @Result(property = "operator", column = "operator_id", javaType = User.class,
                    one = @One(select = "cn.wegfan.relicsmanagement.mapper.UserDao.selectByUserId",
                            fetchType = FetchType.EAGER))
    })
    Page<RelicCheckDetail> selectPageByCheckId(Page<?> page, Integer checkId);

    @Select("SELECT * FROM relic_check_detail WHERE check_id = #{checkId} AND relic_id = #{relicId} LIMIT 1")
    RelicCheckDetail selectByCheckIdAndRelicId(Integer checkId, Integer relicId);

}
