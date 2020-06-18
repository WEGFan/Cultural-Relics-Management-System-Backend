package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.model.dto.RelicMoveDto;
import cn.wegfan.relicsmanagement.model.entity.RelicCheckDetail;
import cn.wegfan.relicsmanagement.model.vo.FilePathVo;
import cn.wegfan.relicsmanagement.model.vo.PageResultVo;
import cn.wegfan.relicsmanagement.model.vo.RelicCheckDetailVo;
import cn.wegfan.relicsmanagement.model.vo.SuccessVo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface RelicCheckDetailService extends IService<RelicCheckDetail> {

    /**
     * 根据盘点编号筛选或获取所有盘点详情记录
     *
     * @param checkId   盘点编号
     * @param checked   根据盘点状态筛选 true已盘点 false未盘点 null不筛选
     * @param pageIndex 当前页码
     * @param pageSize  每页个数
     *
     * @return 盘点详情记录分页对象
     */
    PageResultVo<RelicCheckDetailVo> listRelicCheckDetailByCheckIdAndStatusAndPage(Integer checkId, Boolean checked, long pageIndex, long pageSize);

    /**
     * 根据盘点编号导出盘点详情记录到 Excel 表
     *
     * @param checkId 盘点编号
     *
     * @return 文件路径对象
     */
    FilePathVo exportRelicCheckDetailByCheckIdToExcel(Integer checkId);

    /**
     * 增加盘点详情记录
     *
     * @param checkId 盘点编号
     * @param relicId 文物编号
     * @param dto     文物移动对象
     *
     * @return 成功对象
     */
    SuccessVo addRelicCheckDetail(Integer checkId, Integer relicId, RelicMoveDto dto);

    /**
     * 文物移动后更新盘点详情记录
     *
     * @param relicId  文物编号
     * @param oldPlace 移动前的位置信息
     * @param newPlace 移动后的位置信息
     */
    void updateRelicCheckDetailAfterRelicMove(Integer relicId, RelicMoveDto oldPlace, RelicMoveDto newPlace);

    /**
     * 货架移动后更新盘点详情记录
     *
     * @param shelfId        货架编号
     * @param oldWarehouseId 移动前的仓库编号
     * @param newWarehouseId 移动后的仓库编号
     */
    void updateRelicCheckDetailAfterShelfMove(Integer shelfId, Integer oldWarehouseId, Integer newWarehouseId);

}
