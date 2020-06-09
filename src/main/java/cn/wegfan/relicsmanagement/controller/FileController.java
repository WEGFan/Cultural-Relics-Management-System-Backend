package cn.wegfan.relicsmanagement.controller;

import cn.hutool.core.io.FileUtil;
import cn.wegfan.relicsmanagement.constant.BusinessErrorEnum;
import cn.wegfan.relicsmanagement.constant.PermissionCodeEnum;
import cn.wegfan.relicsmanagement.util.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Paths;

@Slf4j
@Controller
@RequestMapping("/api/files")
public class FileController {

    /**
     * 获取文物图片
     */
    @GetMapping(value = "relics/images/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    @RequiresPermissions(value = {PermissionCodeEnum.ADD_RELIC, PermissionCodeEnum.VIEW_RELIC_INFO}, logical = Logical.OR)
    public byte[] getRelicImage(@PathVariable String fileName) {
        File file = Paths.get("data", "images")
                .resolve(fileName)
                .toAbsolutePath()
                .toFile();
        log.debug(file.toString());
        if (!FileUtil.exist(file)) {
            throw new BusinessException(BusinessErrorEnum.FileNotFound);
        }
        return FileUtil.readBytes(file);
    }

    /**
     * 用户信息 Excel 表
     */
    @GetMapping(value = "exports/users/{fileName}")
    @ResponseBody
    @RequiresPermissions(PermissionCodeEnum.ADMIN)
    public ResponseEntity<Resource> getUserExcel(@PathVariable String fileName) throws UnsupportedEncodingException {
        File file = Paths.get("data", "exports", "users")
                .resolve(fileName)
                .toAbsolutePath()
                .toFile();
        log.debug(file.toString());
        if (!FileUtil.exist(file)) {
            throw new BusinessException(BusinessErrorEnum.FileNotFound);
        }

        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        byte[] data = FileUtil.readBytes(file);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=" + URLEncoder.encode(FileUtil.getName(file), "UTF-8"))
                .contentType(mediaType)
                .contentLength(data.length)
                .body(resource);
    }

    /**
     * 文物 Excel 表
     */
    @GetMapping(value = "exports/relics/{fileName}")
    @ResponseBody
    @RequiresPermissions(PermissionCodeEnum.VIEW_RELIC_INFO)
    public ResponseEntity<Resource> getRelicExcel(@PathVariable String fileName) throws UnsupportedEncodingException {
        File file = Paths.get("data", "exports", "relics")
                .resolve(fileName)
                .toAbsolutePath()
                .toFile();
        log.debug(file.toString());
        if (!FileUtil.exist(file)) {
            throw new BusinessException(BusinessErrorEnum.FileNotFound);
        }

        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        byte[] data = FileUtil.readBytes(file);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=" + URLEncoder.encode(FileUtil.getName(file), "UTF-8"))
                .contentType(mediaType)
                .contentLength(data.length)
                .body(resource);
    }

    /**
     * 文物盘点 Excel 表
     */
    @GetMapping(value = "exports/checks/{fileName}")
    @ResponseBody
    @RequiresPermissions(PermissionCodeEnum.CHECK_RELIC)
    public ResponseEntity<Resource> getRelicCheckExcel(@PathVariable String fileName) throws UnsupportedEncodingException {
        File file = Paths.get("data", "exports", "checks")
                .resolve(fileName)
                .toAbsolutePath()
                .toFile();
        log.debug(file.toString());
        if (!FileUtil.exist(file)) {
            throw new BusinessException(BusinessErrorEnum.FileNotFound);
        }

        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        byte[] data = FileUtil.readBytes(file);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=" + URLEncoder.encode(FileUtil.getName(file), "UTF-8"))
                .contentType(mediaType)
                .contentLength(data.length)
                .body(resource);
    }

    /**
     * 操作记录 Excel 表
     */
    @GetMapping(value = "exports/operations/{fileName}")
    @ResponseBody
    @RequiresPermissions(PermissionCodeEnum.CHECK_RELIC)
    public ResponseEntity<Resource> getOperationLogExcel(@PathVariable String fileName) throws UnsupportedEncodingException {
        File file = Paths.get("data", "exports", "operations")
                .resolve(fileName)
                .toAbsolutePath()
                .toFile();
        log.debug(file.toString());
        if (!FileUtil.exist(file)) {
            throw new BusinessException(BusinessErrorEnum.FileNotFound);
        }

        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        byte[] data = FileUtil.readBytes(file);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=" + URLEncoder.encode(FileUtil.getName(file), "UTF-8"))
                .contentType(mediaType)
                .contentLength(data.length)
                .body(resource);
    }

}
