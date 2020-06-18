package cn.wegfan.relicsmanagement.mapper;

import cn.wegfan.relicsmanagement.model.entity.OperationLog;
import cn.wegfan.relicsmanagement.model.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface OperationLogDao extends BaseMapper<OperationLog> {

    /**
     * 分页筛选或获取所有操作记录
     *
     * @param page       分页对象
     * @param operatorId 根据操作人编号筛选
     * @param itemType   根据操作对象类型筛选
     * @param startTime  根据开始时间筛选
     * @param endTime    根据结束时间筛选
     *
     * @return 操作记录分页对象
     */
    // language=xml
    @Select("<script>" +
            "SELECT * FROM operation_log" +
            "<where>" +
            "  <if test='operatorId != null'>" +
            "    AND operator_id = #{operatorId}" +
            "  </if>" +
            "  <if test='itemType != null'>" +
            "    AND item_type = #{itemType}" +
            "  </if>" +
            "  <if test='startTime != null'>" +
            "    AND create_time &gt;= #{startTime}" +
            "  </if>" +
            "  <if test='endTime != null'>" +
            "    AND create_time &lt;= #{endTime}" +
            "  </if>" +
            "</where>" +
            "ORDER BY create_time DESC" +
            "</script>")
    // language=none
    @Results(id = "operationLogResultMap", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "operatorId", column = "operator_id"),
            @Result(property = "operator", column = "operator_id", javaType = User.class,
                    one = @One(select = "cn.wegfan.relicsmanagement.mapper.UserDao.selectByUserId",
                            fetchType = FetchType.EAGER))
    })
    Page<OperationLog> selectPageByOperatorAndTypeAndDate(Page<?> page, Integer operatorId, String itemType,
                                                          Date startTime, Date endTime);

}
