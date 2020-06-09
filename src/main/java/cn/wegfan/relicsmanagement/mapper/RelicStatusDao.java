package cn.wegfan.relicsmanagement.mapper;

import cn.wegfan.relicsmanagement.model.entity.RelicStatus;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelicStatusDao extends BaseMapper<RelicStatus> {

    @Select("SELECT * FROM relic_status")
    List<RelicStatus> selectRelicStatusList();

}
