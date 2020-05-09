package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.entity.Job;
import cn.wegfan.relicsmanagement.vo.JobVo;

import java.util.List;

public interface JobService {

    List<JobVo> listAllJobs();

}
