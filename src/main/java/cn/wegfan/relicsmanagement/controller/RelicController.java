package cn.wegfan.relicsmanagement.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.wegfan.relicsmanagement.constant.PermissionCodeEnum;
import cn.wegfan.relicsmanagement.model.dto.RelicInfoDto;
import cn.wegfan.relicsmanagement.model.stringdto.RelicInfoStringDto;
import cn.wegfan.relicsmanagement.model.vo.DataReturnVo;
import cn.wegfan.relicsmanagement.service.RelicService;
import cn.wegfan.relicsmanagement.util.PaginationUtil;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/api/v1/relics")
public class RelicController {

    @Autowired
    private RelicService relicService;

    @Autowired
    private MapperFacade mapperFacade;

    /**
     * 获取所有文物状态
     */
    @GetMapping("status")
    @RequiresUser
    public DataReturnVo listAllRelicStatuses() {
        return DataReturnVo.success(relicService.listAllRelicStatus());
    }

    /**
     * 获取所有文物信息
     *
     * @param name      按名称筛选
     * @param status    按状态筛选
     * @param warehouse 按仓库筛选
     * @param shelf     按货架筛选
     * @param from      开始时间
     * @param to        结束时间
     * @param dateType  时间类型 enter入馆时间 leave离馆时间 lend外借时间 fix送修时间
     * @param page      页码
     * @param count     每页个数
     * @param excel     是否导出成 Excel
     */
    @GetMapping("")
    @RequiresPermissions(PermissionCodeEnum.VIEW_RELIC_INFO)
    public DataReturnVo listRelics(@RequestParam(required = false) String name,
                                   @RequestParam(required = false) Integer status,
                                   @RequestParam(required = false) Integer warehouse,
                                   @RequestParam(required = false) Integer shelf,
                                   @RequestParam(required = false) Date from,
                                   @RequestParam(required = false) Date to,
                                   @RequestParam(required = false) String dateType,
                                   @RequestParam Integer page,
                                   @RequestParam Integer count,
                                   @RequestParam(required = false) Boolean excel) {
        if (Boolean.TRUE.equals(excel)) {
            return DataReturnVo.success(relicService.exportRelicByConditionToExcel(name, status,
                    warehouse, shelf, dateType, from, to));
        }
        return DataReturnVo.success(relicService.searchNotDeletedRelicsByPage(name, status,
                warehouse, shelf, dateType, from, to,
                page, PaginationUtil.clampPageCount(count)));
    }

    /**
     * 获取文物详细信息
     *
     * @param relicId 文物编号
     */
    @GetMapping("{relicId}")
    @RequiresPermissions(PermissionCodeEnum.VIEW_RELIC_INFO)
    public DataReturnVo getRelicInfo(@PathVariable Integer relicId) {
        return DataReturnVo.success(relicService.getRelicById(relicId));
    }

    /**
     * 拍照上传文物
     *
     * @param multipartFile 上传的文件
     */
    @PostMapping("")
    @RequiresPermissions(PermissionCodeEnum.ADD_RELIC)
    public DataReturnVo addRelic(@RequestParam("file") MultipartFile multipartFile) throws IOException, IllegalAccessException {
        String tempFileName = UUID.fastUUID().toString(true);
        Path dir = Paths.get("data", "images", "tmp")
                .toAbsolutePath();
        FileUtil.mkdir(dir.toFile());
        File file = dir.resolve(tempFileName)
                .toAbsolutePath()
                .toFile();
        log.debug("{}", file);
        multipartFile.transferTo(file);
        return DataReturnVo.success(relicService.addRelicByPicturePath(file.toString()));
    }

    /**
     * 修改文物信息
     *
     * @param relicId   文物编号
     * @param stringDto 文物信息对象
     */
    @PutMapping("{relicId}")
    @RequiresPermissions(
            value = {PermissionCodeEnum.EDIT_RELIC_INFO, PermissionCodeEnum.EDIT_RELIC_STATUS,
                    PermissionCodeEnum.VIEW_EDIT_RELIC_PRICE, PermissionCodeEnum.WAREHOUSE,
                    PermissionCodeEnum.MOVE_RELIC, PermissionCodeEnum.RELIC_ENTER_MUSEUM},
            logical = Logical.OR
    )
    public DataReturnVo updateRelicInfo(@PathVariable Integer relicId,
                                        @RequestBody @Valid RelicInfoStringDto stringDto) {
        RelicInfoDto dto = mapperFacade.map(stringDto, RelicInfoDto.class);
        return DataReturnVo.success(relicService.updateRelicInfo(relicId, dto));
    }

    /**
     * 删除文物
     *
     * @param relicId 文物编号
     */
    @DeleteMapping("{relicId}")
    @RequiresPermissions(PermissionCodeEnum.ADMIN)
    public DataReturnVo deleteRelic(@PathVariable Integer relicId) {
        return DataReturnVo.success(relicService.deleteRelicById(relicId));
    }

}
