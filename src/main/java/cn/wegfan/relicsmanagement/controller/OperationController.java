package cn.wegfan.relicsmanagement.controller;

import cn.wegfan.relicsmanagement.constant.PermissionCodeEnum;
import cn.wegfan.relicsmanagement.model.vo.DataReturnVo;
import cn.wegfan.relicsmanagement.service.OperationLogService;
import cn.wegfan.relicsmanagement.util.PaginationUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/api/v1/operations")
public class OperationController {

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 获取所有操作记录
     *
     * @param operator 按操作人筛选
     * @param itemType 按操作对象类型筛选
     * @param from     开始时间
     * @param to       结束时间
     * @param page     页码
     * @param count    每页个数
     * @param excel    是否导出成 Excel
     */
    @GetMapping("")
    @RequiresPermissions(PermissionCodeEnum.ADMIN)
    public DataReturnVo listOperations(@RequestParam(required = false, name = "operatorName") Integer operator,
                                       @RequestParam(required = false) String itemType,
                                       @RequestParam(required = false) Date from,
                                       @RequestParam(required = false) Date to,
                                       @RequestParam Integer page,
                                       @RequestParam Integer count,
                                       @RequestParam(required = false) Boolean excel) {
        if (Boolean.TRUE.equals(excel)) {
            return DataReturnVo.success(operationLogService.exportOperationLogToExcel(operator, itemType,
                    from, to));
        }
        return DataReturnVo.success(operationLogService.listOperationLogByOperatorAndTypeAndDateAndPage(operator, itemType,
                from, to, page, PaginationUtil.clampPageCount(count)));
    }

}
