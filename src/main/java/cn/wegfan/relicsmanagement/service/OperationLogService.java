package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.constant.OperationItemTypeEnum;
import cn.wegfan.relicsmanagement.model.vo.FilePathVo;
import cn.wegfan.relicsmanagement.model.vo.OperationLogVo;
import cn.wegfan.relicsmanagement.model.vo.PageResultVo;

import java.util.Date;

public interface OperationLogService {

    /**
     * 分页获取所有操作记录
     *
     * @param operatorId 按操作人筛选
     * @param itemType   按操作对象类型筛选
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param pageIndex  当前页码
     * @param pageSize   每页个数
     *
     * @return 操作记录分页对象
     */
    PageResultVo<OperationLogVo> listOperationLogByOperatorAndTypeAndDateAndPage(Integer operatorId, String itemType,
                                                                                 Date startTime, Date endTime,
                                                                                 long pageIndex, long pageSize);

    /**
     * 导出操作记录到 Excel 表
     *
     * @param operatorId 按操作人筛选
     * @param itemType   按操作对象类型筛选
     * @param startTime  开始时间
     * @param endTime    结束时间
     *
     * @return 文件路径对象
     */
    FilePathVo exportOperationLogToExcel(Integer operatorId, String itemType, Date startTime, Date endTime);

    /**
     * 增加操作记录
     *
     * @param itemType      操作对象类型
     * @param itemId        操作对象编号
     * @param operationType 操作类型
     * @param detail        详细日志
     */
    void addOperationLog(OperationItemTypeEnum itemType, Integer itemId, String operationType, String detail);

}
