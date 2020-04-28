package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.entity.Job;
import cn.wegfan.relicsmanagement.mapper.JobDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class JobServiceImpl implements JobService {

    private final JobDao jobDao;

    public JobServiceImpl(JobDao jobDao) {
        this.jobDao = jobDao;
    }

    @Override
    public List<Job> listAllJobs() {
        return jobDao.selectList(null);
    }

}
