package cn.wegfan.relicsmanagement.controller;

import cn.wegfan.relicsmanagement.constant.PermissionCodeEnum;
import cn.wegfan.relicsmanagement.model.dto.RelicMoveDto;
import cn.wegfan.relicsmanagement.model.dto.WarehouseIdDto;
import cn.wegfan.relicsmanagement.model.stringdto.RelicMoveStringDto;
import cn.wegfan.relicsmanagement.model.stringdto.WarehouseIdStringDto;
import cn.wegfan.relicsmanagement.model.vo.DataReturnVo;
import cn.wegfan.relicsmanagement.service.RelicCheckDetailService;
import cn.wegfan.relicsmanagement.service.RelicCheckService;
import cn.wegfan.relicsmanagement.util.PaginationUtil;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/checks")
public class RelicCheckController {

    @Autowired
    private RelicCheckService relicCheckService;

    @Autowired
    private RelicCheckDetailService relicCheckDetailService;

    @Autowired
    private MapperFacade mapperFacade;

    /**
     * 获取盘点列表
     *
     * @param warehouseId 按仓库编号筛选
     * @param page        页码
     * @param count       每页个数
     */
    @GetMapping("")
    @RequiresPermissions(PermissionCodeEnum.CHECK_RELIC)
    public DataReturnVo listChecks(@RequestParam(required = false) Integer warehouseId,
                                   @RequestParam Integer page,
                                   @RequestParam Integer count) {
        return DataReturnVo.success(relicCheckService.listByWarehouseIdAndPage(warehouseId,
                page, PaginationUtil.clampPageCount(count)));
    }

    /**
     * 获取某次盘点的文物列表
     *
     * @param checkId 盘点编号
     * @param checked 按盘点状态筛选 true已盘点 false未盘点 null不筛选
     * @param page    页码
     * @param count   每页个数
     * @param excel   是否导出成 Excel
     */
    @GetMapping("{checkId}/relics")
    @RequiresPermissions(PermissionCodeEnum.CHECK_RELIC)
    public DataReturnVo listRelicsByCheckId(@PathVariable Integer checkId,
                                            @RequestParam(required = false) Boolean checked,
                                            @RequestParam(required = false) Integer page,
                                            @RequestParam(required = false) Integer count,
                                            @RequestParam(required = false) Boolean excel) {
        if (Boolean.TRUE.equals(excel)) {
            return DataReturnVo.success(relicCheckDetailService.exportRelicCheckDetailByCheckIdToExcel(checkId));
        }
        // 防止没传分页参数造成错误
        if (page == null) {
            page = 1;
        }
        if (count == null) {
            count = 1;
        }
        return DataReturnVo.success(relicCheckDetailService.listRelicCheckDetailByCheckIdAndStatusAndPage(checkId, checked,
                page, PaginationUtil.clampPageCount(count)));
    }

    /**
     * 开始一次盘点
     *
     * @param stringDto 仓库编号对象
     */
    @PostMapping("sessions")
    @RequiresPermissions(PermissionCodeEnum.CHECK_RELIC)
    public DataReturnVo startCheck(@RequestBody @Valid WarehouseIdStringDto stringDto) {
        WarehouseIdDto dto = mapperFacade.map(stringDto, WarehouseIdDto.class);
        return DataReturnVo.success(relicCheckService.startRelicCheck(dto.getWarehouseId()));
    }

    /**
     * 结束一次盘点
     *
     * @param checkId 盘点编号
     */
    @DeleteMapping("sessions/{checkId}")
    @RequiresPermissions(PermissionCodeEnum.CHECK_RELIC)
    public DataReturnVo endCheck(@PathVariable Integer checkId) {
        return DataReturnVo.success(relicCheckService.endRelicCheck(checkId));
    }

    /**
     * 提交当前文物位置和状态信息
     *
     * @param checkId   盘点编号
     * @param relicId   文物编号
     * @param stringDto 文物移动对象
     */
    @PutMapping("{checkId}/relics/{relicId}")
    @RequiresPermissions(PermissionCodeEnum.CHECK_RELIC)
    public DataReturnVo updateRelicPlaceInfo(@PathVariable Integer checkId,
                                             @PathVariable Integer relicId,
                                             @RequestBody @Valid RelicMoveStringDto stringDto) {
        RelicMoveDto dto = mapperFacade.map(stringDto, RelicMoveDto.class);
        return DataReturnVo.success(relicCheckDetailService.addRelicCheckDetail(checkId, relicId, dto));
    }

}
