package cn.wegfan.relicsmanagement.controller;

import cn.wegfan.relicsmanagement.util.BusinessErrorEnum;
import cn.wegfan.relicsmanagement.util.BusinessException;
import cn.wegfan.relicsmanagement.util.generator.TestDataGenerator;
import cn.wegfan.relicsmanagement.vo.DataReturnVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.batik.transcoder.TranscoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private TestDataGenerator generator;

    @PostMapping("generate")
    public DataReturnVo test(@RequestParam("password") String password,
                             @RequestParam("relic") Boolean relic,
                             @RequestParam("warehouse") Boolean warehouse,
                             @RequestParam("shelf") Boolean shelf) throws IOException, TranscoderException {

        if (!"wegfan.wegfan".equals(password)) {
            throw new BusinessException(BusinessErrorEnum.Unauthorized);
        }
        if (warehouse) {
            log.debug("generating warehouse");
            generator.generateWarehouse();
        }
        if (shelf) {
            log.debug("generating shelf");
            generator.generateShelf();
        }
        if (relic) {
            log.debug("generating relic");
            generator.generateRelic();
        }
        return DataReturnVo.success(null);
    }

}
