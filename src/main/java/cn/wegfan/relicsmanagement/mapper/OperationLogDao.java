package cn.wegfan.relicsmanagement.mapper;

import cn.wegfan.relicsmanagement.entity.OperationLog;
import cn.wegfan.relicsmanagement.entity.User;
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
