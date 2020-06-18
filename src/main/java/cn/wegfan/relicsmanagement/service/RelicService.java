package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.model.dto.RelicInfoDto;
import cn.wegfan.relicsmanagement.model.entity.Relic;
import cn.wegfan.relicsmanagement.model.entity.RelicStatus;
import cn.wegfan.relicsmanagement.model.vo.*;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

public interface RelicService extends IService<Relic> {

    /**
     * 获取所有文物状态
     *
     * @return 文物状态对象列表
     */
    List<RelicStatus> listAllRelicStatus();

    /**
     * 分页筛选或获取所有文物
     *
     * @param name        按名称筛选
     * @param status      按状态筛选
     * @param warehouseId 按仓库编号筛选
     * @param shelfId     按货架编号筛选
     * @param dateType    时间类型
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @param pageIndex   当前页码
     * @param pageSize    每页个数
     *
     * @return 文物分页对象
     */
    PageResultVo<RelicVo> searchNotDeletedRelicsByPage(String name, Integer status,
                                                       Integer warehouseId, Integer shelfId,
                                                       String dateType, Date startTime, Date endTime,
                                                       long pageIndex, long pageSize);

    /**
     * 根据文物编号获取未删除的文物
     *
     * @param relicId 文物编号
     *
     * @return 文物对象
     */
    RelicVo getRelicById(Integer relicId);

    /**
     * 用图片路径创建文物
     *
     * @param filePath 图片文件路径
     *
     * @return 文物编号和图片路径对象
     */
    RelicIdPicturePathVo addRelicByPicturePath(String filePath) throws IllegalAccessException;

    /**
     * 根据文物编号删除文物
     *
     * @param relicId 文物编号
     *
     * @return 成功对象
     */
    SuccessVo deleteRelicById(Integer relicId);

    /**
     * 根据文物编号更新文物
     *
     * @param relicId   文物编号
     * @param relicInfo 文物信息对象
     *
     * @return 文物对象
     */
    RelicVo updateRelicInfo(Integer relicId, RelicInfoDto relicInfo);

    /**
     * 根据筛选条件导出 Excel 表
     *
     * @param name        按名称筛选
     * @param status      按状态筛选
     * @param warehouseId 按仓库编号筛选
     * @param shelfId     按货架编号筛选
     * @param dateType    时间类型
     * @param startTime   开始时间
     * @param endTime     结束时间
     *
     * @return 文件路径对象
     */
    FilePathVo exportRelicByConditionToExcel(String name, Integer status,
                                             Integer warehouseId, Integer shelfId,
                                             String dateType, Date startTime, Date endTime);

}
