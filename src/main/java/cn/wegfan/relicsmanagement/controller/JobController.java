package cn.wegfan.relicsmanagement.controller;

import cn.wegfan.relicsmanagement.service.JobService;
import cn.wegfan.relicsmanagement.vo.DataReturnVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    /**
     * 获取所有职务
     */
    @GetMapping("")
    public DataReturnVo listAllJobs() {
        return DataReturnVo.success(jobService.listAllJobs());
    }

}
