package cn.wegfan.relicsmanagement.controller;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.wegfan.relicsmanagement.util.BusinessErrorEnum;
import cn.wegfan.relicsmanagement.util.BusinessException;
import cn.wegfan.relicsmanagement.util.PermissionCodeEnum;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.file.Paths;

@Slf4j
@Controller
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private MapperFacade mapperFacade;

    /**
     * 获取文物图片
     */
    @RequestMapping(value = "relics/images/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    @RequiresPermissions(value = {PermissionCodeEnum.ADD_RELIC, PermissionCodeEnum.VIEW_RELIC_INFO}, logical = Logical.OR)
    public byte[] getRelicImage(@PathVariable String fileName) {
        String dir = Paths.get("data", "images")
                .toAbsolutePath()
                .toString();
        String filePath = dir + "/" + fileName;
        log.debug(filePath);
        if (!FileUtil.exist(filePath)) {
            throw new BusinessException(BusinessErrorEnum.FileNotFound);
        }
        return ImgUtil.toBytes(ImgUtil.read(filePath), "jpg");
    }

}
