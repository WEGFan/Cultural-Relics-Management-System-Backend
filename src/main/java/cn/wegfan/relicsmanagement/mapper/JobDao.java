package cn.wegfan.relicsmanagement.mapper;

import cn.wegfan.relicsmanagement.model.entity.Job;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface JobDao extends BaseMapper<Job> {

    /**
     * 获取所有职务
     *
     * @return 职务对象列表
     */
    @Select("SELECT * FROM job")
    @Results(id = "jobPermissionsResultMap", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "permissions", column = "id", javaType = Set.class,
                    many = @Many(select = "cn.wegfan.relicsmanagement.mapper.PermissionDao.selectListByJobId",
                            fetchType = FetchType.EAGER))
    })
    List<Job> selectJobList();

}
