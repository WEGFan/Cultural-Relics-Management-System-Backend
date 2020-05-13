package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.entity.RelicStatus;
import cn.wegfan.relicsmanagement.vo.PageResultVo;
import cn.wegfan.relicsmanagement.vo.RelicIdPicturePathVo;
import cn.wegfan.relicsmanagement.vo.RelicVo;

import java.util.Date;
import java.util.List;

public interface RelicService {

    List<RelicStatus> listAllRelicStatus();

    PageResultVo<RelicVo> searchNotDeletedRelicsByPage(String name, Integer status, String dateType,
                                                       Date startTime, Date endTime,
                                                       long pageIndex, long pageSize);

    RelicVo getRelicById(Integer relicId);

    RelicIdPicturePathVo addRelicByPicturePath(String tempPath);

}
