package cn.wegfan.relicsmanagement.config.orika;

import cn.wegfan.relicsmanagement.model.dto.WarehouseNameDto;
import cn.wegfan.relicsmanagement.model.entity.Warehouse;
import cn.wegfan.relicsmanagement.model.vo.WarehouseVo;
import ma.glasnost.orika.MapperFactory;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

@Component
public class WarehouseMapping implements OrikaMapperFactoryConfigurer {

    @Override
    public void configure(MapperFactory mapperFactory) {
        mapperFactory.classMap(Warehouse.class, WarehouseVo.class)
                .byDefault()
                .register();
        mapperFactory.classMap(WarehouseNameDto.class, Warehouse.class)
                .byDefault()
                .register();
    }

}
