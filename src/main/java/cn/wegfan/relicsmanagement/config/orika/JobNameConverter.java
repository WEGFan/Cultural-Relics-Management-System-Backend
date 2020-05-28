package cn.wegfan.relicsmanagement.config.orika;

import cn.wegfan.relicsmanagement.entity.Job;
import cn.wegfan.relicsmanagement.mapper.JobDao;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.print.attribute.standard.JobName;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
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
