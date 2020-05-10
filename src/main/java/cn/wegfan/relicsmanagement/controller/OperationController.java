package cn.wegfan.relicsmanagement.controller;

import cn.wegfan.relicsmanagement.service.OperationService;
import cn.wegfan.relicsmanagement.util.PermissionCodeEnum;
import cn.wegfan.relicsmanagement.vo.DataReturnVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/api/v1/operations")
public class OperationController {

    @Autowired
    private OperationService operationService;

    /**
     * 导出操作动态 Excel 表【管理员】
     *
     * @param excel 是否导出成 Excel
     * @param from  起始时间
     * @param to    结束时间
     */
    @GetMapping(value = "", params = "excel=true")
    @RequiresPermissions(PermissionCodeEnum.ADMIN)
    public DataReturnVo exportOperationsToExcel(@RequestParam Boolean excel,
                                                @RequestParam(required = false) Date from,
                                                @RequestParam(required = false) Date to) {
        if (excel.equals(Boolean.FALSE)) {
            throw new NotImplementedException();
        }
        throw new NotImplementedException();
    }

}
