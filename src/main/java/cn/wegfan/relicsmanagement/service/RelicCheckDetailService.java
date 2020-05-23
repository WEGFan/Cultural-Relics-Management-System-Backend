package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.dto.RelicMoveDto;
import cn.wegfan.relicsmanagement.entity.RelicCheckDetail;
import cn.wegfan.relicsmanagement.vo.PageResultVo;
import cn.wegfan.relicsmanagement.vo.RelicCheckDetailVo;
import cn.wegfan.relicsmanagement.vo.SuccessVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface RelicCheckDetailService extends IService<RelicCheckDetail> {

    PageResultVo<RelicCheckDetailVo> listRelicCheckDetailByCheckIdAndPage(Integer checkId, long pageIndex, long pageSize);

    SuccessVo addRelicCheckDetail(Integer checkId, Integer relicId, RelicMoveDto dto);

    void updateRelicCheckDetailAfterRelicMove(Integer relicId, RelicMoveDto oldPlace, RelicMoveDto newPlace);

}
