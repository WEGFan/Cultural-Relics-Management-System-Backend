package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.entity.RelicStatus;
import cn.wegfan.relicsmanagement.vo.RelicVo;

import java.util.List;

public interface RelicService {

    List<RelicStatus> listAllRelicStatus();
    
    List<RelicVo> listNotDeletedRelics();
    
    RelicVo getRelicById(Integer relicId);
}
