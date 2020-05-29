package cn.wegfan.relicsmanagement;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.io.FileUtil;
import cn.wegfan.relicsmanagement.entity.Job;
import cn.wegfan.relicsmanagement.entity.Permission;
import cn.wegfan.relicsmanagement.entity.User;
import cn.wegfan.relicsmanagement.mapper.JobDao;
import cn.wegfan.relicsmanagement.mapper.PermissionDao;
import cn.wegfan.relicsmanagement.mapper.RelicDao;
import cn.wegfan.relicsmanagement.mapper.UserDao;
import cn.wegfan.relicsmanagement.vo.UserExcelVo;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.converters.string.StringImageConverter;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
@Slf4j
public class TestExcel {

    @Data
    public static class UserExcelVoaaa {

        /**
         * 用户编号
         */
        @ExcelProperty("用户编号")
        private Integer id;

        /**
         * 工号
         */
        @ExcelProperty("工号")
        private Integer workId;

        /**
         * 姓名
         */
        @ExcelProperty("姓名")
        private String name;

        /**
         * 职务
         */
        @ExcelProperty("职务")
        private String job;

        /**
         * 手机号
         */
        @ExcelProperty("手机号")
        private String telephone;

        /**
         * 额外权限
         */
        @ExcelProperty("额外权限")
        private String extraPermissions;

        /**
         * 入职时间
         */
        @ExcelProperty("入职时间")
        @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
        private Date createTime;

        /**
         * 离职时间
         */
        @ExcelProperty("离职时间")
        @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
        private Date deleteTime;

    }

    @Autowired
    private UserDao userDao;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private JobDao jobDao;

    @Autowired
    private RelicDao relicDao;

    @Data
    private static class Relic {

        @ExcelProperty("编号")
        @Excel(name = "编号", width = 30)
        private Integer id;

        @ExcelProperty(value = "图片", converter = StringImageConverter.class)
        @Excel(name = "图片", width = 30, type = 2, imageType = 1)
        private String picturePath;

    }

    @Test
    void testImageExport() throws IOException {
        String dir = Paths.get("data", "test")
                .toAbsolutePath()
                .toString();
        List<Relic> relicList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Relic relic = new Relic();
            relic.setId(i);
            relic.setPicturePath(dir + "/" + i + ".jpg");
            relicList.add(relic);
        }
        ExportParams exportParams = new ExportParams("计算机一班学生", "学生");
        exportParams.setHeight((short)40);
        exportParams.setType(ExcelType.XSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams,
                Relic.class, relicList);
        workbook.write(FileUtil.getOutputStream(new File(dir + "/simpleWrite" + System.currentTimeMillis() + ".xlsx")));
        // String fileName = dir + "/simpleWrite" + System.currentTimeMillis() + ".xlsx";
        // // // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // EasyExcel.write(fileName, Relic.class)
        //         // .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
        //         .sheet("模板")
        //         .doWrite(relicList);

    }

    @Autowired
    private MapperFacade mapperFacade;

    @Test
    void testExportUserExcel() {
        Map<Integer, String> jobNameMap = jobDao.selectJobList()
                .stream()
                .collect(Collectors.toMap(Job::getId, Job::getName));
        Map<Integer, String> permissionNameMap = permissionDao.selectPermissionList()
                .stream()
                .collect(Collectors.toMap(Permission::getId, Permission::getName));

        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.getConverterFactory()
                .registerConverter("jobNameConverter", new CustomConverter<Integer, String>() {
                    @Override
                    public String convert(Integer source, Type<? extends String> destinationType, MappingContext mappingContext) {
                        return jobNameMap.get(source);
                    }
                });
        mapperFactory.getConverterFactory()
                .registerConverter("extraPermissionNameConverter", new CustomConverter<Set<Permission>, String>() {
                    @Override
                    public String convert(Set<Permission> source, Type<? extends String> destinationType, MappingContext mappingContext) {
                        StringJoiner stringJoiner = new StringJoiner("；");
                        source.forEach(permission -> stringJoiner.add(permission.getName()));
                        return stringJoiner.toString();
                    }
                });
        mapperFactory.classMap(User.class, UserExcelVo.class)
                .fieldMap("jobId", "job").converter("jobNameConverter").add()
                .fieldMap("extraPermissions").converter("extraPermissionNameConverter").add()
                .byDefault()
                .register();
        // MapperFacade mapperFacade = mapperFactory.getMapperFacade();

        List<User> userList = userDao.selectUserList();

        List<UserExcelVo> userExcelVoList = mapperFacade.mapAsList(userList, UserExcelVo.class);
        log.debug(userExcelVoList.toString());
        String dir = Paths.get("data")
                .toAbsolutePath()
                .toString();
        String fileName = dir + "/simpleWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, UserExcelVo.class)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet("模板")
                .doWrite(userExcelVoList);
    }

}
