package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.dto.RelicInfoDto;
import cn.wegfan.relicsmanagement.entity.Relic;
import cn.wegfan.relicsmanagement.entity.RelicStatus;
import cn.wegfan.relicsmanagement.vo.*;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

public interface RelicCheckService {
    
    PageResultVo<RelicCheckVo> listByWarehouseIdAndPage(Integer warehouseId, long pageIndex, long pageSize);

}
