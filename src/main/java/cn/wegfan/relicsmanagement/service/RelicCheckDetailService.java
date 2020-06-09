package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.model.dto.RelicMoveDto;
import cn.wegfan.relicsmanagement.model.entity.RelicCheckDetail;
import cn.wegfan.relicsmanagement.model.vo.FilePathVo;
import cn.wegfan.relicsmanagement.model.vo.PageResultVo;
import cn.wegfan.relicsmanagement.model.vo.RelicCheckDetailVo;
import cn.wegfan.relicsmanagement.model.vo.SuccessVo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface RelicCheckDetailService extends IService<RelicCheckDetail> {

    PageResultVo<RelicCheckDetailVo> listRelicCheckDetailByCheckIdAndStatusAndPage(Integer checkId, Boolean checked, long pageIndex, long pageSize);

    FilePathVo exportRelicCheckDetailByCheckIdToExcel(Integer checkId);

    SuccessVo addRelicCheckDetail(Integer checkId, Integer relicId, RelicMoveDto dto);

    void updateRelicCheckDetailAfterRelicMove(Integer relicId, RelicMoveDto oldPlace, RelicMoveDto newPlace);

    void updateRelicCheckDetailAfterShelfMove(Integer shelfId, Integer oldWarehouseId, Integer newWarehouseId);

}
