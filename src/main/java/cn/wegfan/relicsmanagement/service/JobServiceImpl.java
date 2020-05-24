package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.entity.Job;
import cn.wegfan.relicsmanagement.entity.Permission;
import cn.wegfan.relicsmanagement.mapper.JobDao;
import cn.wegfan.relicsmanagement.vo.JobVo;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class JobServiceImpl implements JobService {

    @Autowired
    private JobDao jobDao;

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    private MapperFacade mapperFacade;

    public JobServiceImpl() {
        mapperFactory
                .getConverterFactory()
                .registerConverter("permissionIdConvert", new CustomConverter<Set<Permission>, Set<Integer>>() {
                    @Override
                    public Set<Integer> convert(Set<Permission> permissions, Type<? extends Set<Integer>> type, MappingContext mappingContext) {
                        // 把权限的id提取成列表
                        return permissions.stream()
                                .map(Permission::getId)
                                .collect(Collectors.toSet());
                    }
                });
        mapperFactory.classMap(Job.class, JobVo.class)
                .fieldMap("permissions", "permissionsId").converter("permissionIdConvert").add()
                .byDefault()
                .register();
        mapperFacade = mapperFactory.getMapperFacade();
    }

    @Override
    public List<JobVo> listAllJobs() {
        List<Job> jobList = jobDao.selectJobList();
        List<JobVo> jobVoList = mapperFacade.mapAsList(jobList, JobVo.class);
        return jobVoList;
    }

}
