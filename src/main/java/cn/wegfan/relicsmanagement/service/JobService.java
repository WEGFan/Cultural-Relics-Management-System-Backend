package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.model.vo.JobVo;

import java.util.List;

public interface JobService {

    /**
     * 获取所有职务
     *
     * @return 职务对象列表
     */
    List<JobVo> listAllJobs();

}
