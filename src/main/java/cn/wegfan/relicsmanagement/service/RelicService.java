package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.model.dto.RelicInfoDto;
import cn.wegfan.relicsmanagement.model.entity.Relic;
import cn.wegfan.relicsmanagement.model.entity.RelicStatus;
import cn.wegfan.relicsmanagement.model.vo.*;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

public interface RelicService extends IService<Relic> {

    List<RelicStatus> listAllRelicStatus();

    PageResultVo<RelicVo> searchNotDeletedRelicsByPage(String name, Integer status,
                                                       Integer warehouseId, Integer shelfId,
                                                       String dateType, Date startTime, Date endTime,
                                                       long pageIndex, long pageSize);

    RelicVo getRelicById(Integer relicId);

    RelicIdPicturePathVo addRelicByPicturePath(String filePath) throws IllegalAccessException;

    SuccessVo deleteRelicById(Integer relicId);

    RelicVo updateRelicInfo(Integer relicId, RelicInfoDto relicInfo);

    FilePathVo exportRelicByConditionToExcel(String name, Integer status,
                                             Integer warehouseId, Integer shelfId,
                                             String dateType, Date startTime, Date endTime);

}
