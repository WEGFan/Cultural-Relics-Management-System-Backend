package cn.wegfan.relicsmanagement.config.orika;

import cn.wegfan.relicsmanagement.mapper.JobDao;
import cn.wegfan.relicsmanagement.model.entity.Job;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * 职务编号到职务名称转换器
 */
@Component
public class JobNameConverter extends CustomConverter<Integer, String> {

    private Map<Integer, String> jobNameMap;

    public JobNameConverter(JobDao jobDao) {
        super();
        jobNameMap = jobDao.selectJobList()
                .stream()
                .collect(Collectors.toMap(Job::getId, Job::getName));
    }

    @Override
    public String convert(Integer source, Type<? extends String> destinationType, MappingContext mappingContext) {
        return jobNameMap.get(source);
    }

}
