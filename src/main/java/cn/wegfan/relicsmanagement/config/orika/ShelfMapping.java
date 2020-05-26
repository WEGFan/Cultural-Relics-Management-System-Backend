package cn.wegfan.relicsmanagement.config.orika;

import cn.wegfan.relicsmanagement.entity.Shelf;
import cn.wegfan.relicsmanagement.vo.ShelfVo;
import ma.glasnost.orika.MapperFactory;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

@Component
public class ShelfMapping implements OrikaMapperFactoryConfigurer {

    @Override
    public void configure(MapperFactory mapperFactory) {
        mapperFactory.classMap(Shelf.class, ShelfVo.class)
                .fieldMap("warehouse.name", "warehouseName").add()
                .byDefault()
                .register();
    }

}
