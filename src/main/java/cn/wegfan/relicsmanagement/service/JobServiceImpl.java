package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.mapper.JobDao;
import cn.wegfan.relicsmanagement.model.entity.Job;
import cn.wegfan.relicsmanagement.model.vo.JobVo;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class JobServiceImpl implements JobService {

    @Autowired
    private JobDao jobDao;

    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public List<JobVo> listAllJobs() {
        List<Job> jobList = jobDao.selectJobList();
        List<JobVo> jobVoList = mapperFacade.mapAsList(jobList, JobVo.class);
        return jobVoList;
    }

}
