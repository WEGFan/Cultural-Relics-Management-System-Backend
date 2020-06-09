package cn.wegfan.relicsmanagement.config.orika;

import cn.wegfan.relicsmanagement.model.entity.RelicCheckDetail;
import cn.wegfan.relicsmanagement.model.vo.RelicCheckDetailExcelVo;
import cn.wegfan.relicsmanagement.model.vo.RelicCheckDetailVo;
import ma.glasnost.orika.MapperFactory;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

@Component
public class RelicCheckDetailMapping implements OrikaMapperFactoryConfigurer {

    @Override
    public void configure(MapperFactory mapperFactory) {
        mapperFactory.classMap(RelicCheckDetail.class, RelicCheckDetailVo.class)
                .fieldMap("relic.name", "name").add()
                .fieldMap("relic.picturePath", "picturePath").add()
                .fieldMap("operator.name", "operatorName").add()
                .fieldMap("oldWarehouse.name", "oldWarehouseName").add()
                .fieldMap("oldShelf.name", "oldShelfName").add()
                .fieldMap("newWarehouse.name", "newWarehouseName").add()
                .fieldMap("newShelf.name", "newShelfName").add()
                .byDefault()
                .register();
        mapperFactory.classMap(RelicCheckDetailVo.class, RelicCheckDetailExcelVo.class)
                .byDefault()
                .register();
    }

}
