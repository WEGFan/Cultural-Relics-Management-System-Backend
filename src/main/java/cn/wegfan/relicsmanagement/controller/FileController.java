package cn.wegfan.relicsmanagement.controller;

import cn.hutool.core.img.ImgUtil;
import cn.wegfan.relicsmanagement.util.PermissionCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.file.Paths;

@Slf4j
@Controller
@RequestMapping("/files")
public class FileController {

    /**
     * 获取文物图片
     */
    @RequestMapping(value = "relics/images/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    @RequiresPermissions(PermissionCodeEnum.ADMIN)
    public byte[] getRelicImage(@PathVariable String fileName) {
        String dir = Paths.get("data", "images")
                .toAbsolutePath()
                .toString();
        String filePath = dir + "/" + fileName;
        log.debug(filePath);
        return ImgUtil.toBytes(ImgUtil.read(filePath), "jpg");
    }

}
