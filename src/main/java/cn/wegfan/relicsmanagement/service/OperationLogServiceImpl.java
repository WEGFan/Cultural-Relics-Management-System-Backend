package cn.wegfan.relicsmanagement.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.wegfan.relicsmanagement.constant.OperationItemTypeEnum;
import cn.wegfan.relicsmanagement.entity.OperationLog;
import cn.wegfan.relicsmanagement.mapper.OperationLogDao;
import cn.wegfan.relicsmanagement.vo.FilePathVo;
import cn.wegfan.relicsmanagement.vo.OperationLogExcelVo;
import cn.wegfan.relicsmanagement.vo.OperationLogVo;
import cn.wegfan.relicsmanagement.vo.PageResultVo;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class OperationLogServiceImpl implements OperationLogService {

    @Autowired
    private OperationLogDao operationLogDao;

    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public PageResultVo<OperationLogVo> listOperationLogByOperatorAndTypeAndDateAndPage(Integer operatorId, String itemType,
                                                                                        Date startTime, Date endTime,
                                                                                        long pageIndex, long pageSize) {
        Page<OperationLog> page = new Page<>(pageIndex, pageSize);

        if (StrUtil.isEmpty(itemType)) {
            itemType = null;
        }

        Page<OperationLog> pageResult = operationLogDao.selectPageByOperatorAndTypeAndDate(page, operatorId, itemType,
                startTime, endTime);
        List<OperationLog> operationLogList = pageResult.getRecords();
        List<OperationLogVo> operationLogVoList = mapperFacade.mapAsList(operationLogList, OperationLogVo.class);

        return new PageResultVo<OperationLogVo>(operationLogVoList, pageResult);
    }

    @Override
    public FilePathVo exportOperationLogToExcel(Integer operatorId, String itemType, Date startTime, Date endTime) {
        long pageSize = 500;

        Path dir = Paths.get("data", "exports", "operations")
                .toAbsolutePath();
        FileUtil.mkdir(dir.toFile());
        String fileName = "操作记录_" + DateUtil.format(new Date(), "yyyy-MM-dd_HH-mm-ss") + ".xlsx";
        File file = dir.resolve(fileName)
                .toFile();

        ExcelWriter excelWriter = EasyExcel.write(file, OperationLogExcelVo.class)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .build();

        WriteSheet writeSheet = EasyExcel.writerSheet("操作记录").build();

        int pageIndex = 1;
        PageResultVo<OperationLogVo> pageResult;
        do {
            pageResult = listOperationLogByOperatorAndTypeAndDateAndPage(operatorId, itemType,
                    startTime, endTime, pageIndex, pageSize);
            List<OperationLogVo> operationLogVoList = pageResult.getContent();
            List<OperationLogExcelVo> data = mapperFacade.mapAsList(operationLogVoList, OperationLogExcelVo.class);

            excelWriter.write(data, writeSheet);
            pageIndex++;
        } while (pageResult.getHasNext());
        excelWriter.finish();

        return new FilePathVo("/api/files/exports/operations/" + fileName);
    }

    @Override
    public void addOperationLog(OperationItemTypeEnum itemType, Integer itemId, String operationType, String detail) {
        OperationLog operationLog = new OperationLog();
        operationLog.setItemType(itemType.getCode());
        operationLog.setItemId(itemId);
        operationLog.setType(operationType);
        operationLog.setDetail(detail);
        operationLog.setCreateTime(new Date());
        operationLog.setOperatorId((Integer)SecurityUtils.getSubject().getPrincipal());
        operationLogDao.insert(operationLog);
    }

}
