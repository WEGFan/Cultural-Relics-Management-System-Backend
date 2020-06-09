package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.constant.OperationItemTypeEnum;
import cn.wegfan.relicsmanagement.model.vo.FilePathVo;
import cn.wegfan.relicsmanagement.model.vo.OperationLogVo;
import cn.wegfan.relicsmanagement.model.vo.PageResultVo;

import java.util.Date;

public interface OperationLogService {

    PageResultVo<OperationLogVo> listOperationLogByOperatorAndTypeAndDateAndPage(Integer operatorId, String itemType,
                                                                                 Date startTime, Date endTime,
                                                                                 long pageIndex, long pageSize);

    FilePathVo exportOperationLogToExcel(Integer operatorId, String itemType, Date startTime, Date endTime);

    void addOperationLog(OperationItemTypeEnum itemType, Integer itemId, String operationType, String detail);

}
