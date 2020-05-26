package cn.wegfan.relicsmanagement.config.orika;

import cn.wegfan.relicsmanagement.entity.RelicCheck;
import cn.wegfan.relicsmanagement.vo.RelicCheckVo;
import ma.glasnost.orika.MapperFactory;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

@Component
public class RelicCheckMapping implements OrikaMapperFactoryConfigurer {

    @Override
    public void configure(MapperFactory mapperFactory) {
        mapperFactory.classMap(RelicCheck.class, RelicCheckVo.class)
                .fieldMap("warehouse.name", "warehouseName").add()
                .byDefault()
                .register();
    }

}
