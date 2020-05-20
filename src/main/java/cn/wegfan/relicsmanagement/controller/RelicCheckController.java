package cn.wegfan.relicsmanagement.controller;

import cn.wegfan.relicsmanagement.dto.RelicMoveDto;
import cn.wegfan.relicsmanagement.dto.WarehouseIdDto;
import cn.wegfan.relicsmanagement.dto.WarehouseNameDto;
import cn.wegfan.relicsmanagement.entity.Relic;
import cn.wegfan.relicsmanagement.service.RelicCheckService;
import cn.wegfan.relicsmanagement.service.WarehouseService;
import cn.wegfan.relicsmanagement.util.PermissionCodeEnum;
import cn.wegfan.relicsmanagement.vo.DataReturnVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/checks")
public class RelicCheckController {

    @Autowired
    private RelicCheckService relicCheckService;

    /**
     * 获取盘点列表
     *
     * @param warehouseId 按仓库编号筛选
     * @param page        页码
     * @param count       获取个数
     */
    @GetMapping("")
    @RequiresPermissions(PermissionCodeEnum.CHECK_RELIC)
    public DataReturnVo listChecks(@RequestParam(required = false) Integer warehouseId,
                                   @RequestParam Integer page,
                                   @RequestParam Integer count) {
        return DataReturnVo.success(relicCheckService.listByWarehouseIdAndPage(warehouseId, page, count));
    }

    /**
     * 获取某次盘点的文物列表
     */
    @GetMapping("checks/{checkId}/relics")
    @RequiresPermissions(PermissionCodeEnum.CHECK_RELIC)
    public DataReturnVo listRelicsByCheckId(@PathVariable Integer checkId,
                                            @RequestParam Integer page,
                                            @RequestParam Integer count) {
        throw new NotImplementedException();
    }

    /**
     * 开始一次盘点
     */
    @PostMapping("sessions")
    @RequiresPermissions(PermissionCodeEnum.CHECK_RELIC)
    public DataReturnVo startCheck(@RequestBody @Valid WarehouseIdDto dto) {
        throw new NotImplementedException();
    }

    /**
     * 结束一次盘点
     */
    @DeleteMapping("sessions")
    @RequiresPermissions(PermissionCodeEnum.CHECK_RELIC)
    public DataReturnVo endCheck() {
        throw new NotImplementedException();
    }

    /**
     * 提交当前文物位置和状态信息
     *
     * @param relicId 文物编号
     */
    @PutMapping("relics/{relicId}")
    @RequiresPermissions(PermissionCodeEnum.CHECK_RELIC)
    public DataReturnVo updateRelicPlaceInfo(@PathVariable Integer relicId,
                                             @RequestBody @Valid RelicMoveDto dto) {
        throw new NotImplementedException();
    }

    /**
     * 导出某次盘点的文物 Excel 表
     *
     * @param excel 是否导出成 Excel
     */
    @GetMapping(value = "{checkId}/relics", params = "excel=true")
    @RequiresPermissions(PermissionCodeEnum.CHECK_RELIC)
    public DataReturnVo exportRelicCheckToExcel(@PathVariable Integer checkId,
                                                @RequestParam Boolean excel) {
        if (excel.equals(Boolean.FALSE)) {
            throw new NotImplementedException();
        }
        throw new NotImplementedException();
    }

}
