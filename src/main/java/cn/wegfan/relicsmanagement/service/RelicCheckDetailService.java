package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.dto.RelicMoveDto;
import cn.wegfan.relicsmanagement.entity.RelicCheckDetail;
import cn.wegfan.relicsmanagement.vo.FilePathVo;
import cn.wegfan.relicsmanagement.vo.PageResultVo;
import cn.wegfan.relicsmanagement.vo.RelicCheckDetailVo;
import cn.wegfan.relicsmanagement.vo.SuccessVo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface RelicCheckDetailService extends IService<RelicCheckDetail> {

    PageResultVo<RelicCheckDetailVo> listRelicCheckDetailByCheckIdAndStatusAndPage(Integer checkId, Boolean checked, long pageIndex, long pageSize);

    FilePathVo exportRelicCheckDetailByCheckIdToExcel(Integer checkId);

    SuccessVo addRelicCheckDetail(Integer checkId, Integer relicId, RelicMoveDto dto);

    void updateRelicCheckDetailAfterRelicMove(Integer relicId, RelicMoveDto oldPlace, RelicMoveDto newPlace);

    void updateRelicCheckDetailAfterShelfMove(Integer shelfId, Integer oldWarehouseId, Integer newWarehouseId);

}
