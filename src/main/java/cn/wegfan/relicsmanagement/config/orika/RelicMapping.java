package cn.wegfan.relicsmanagement.config.orika;

import cn.wegfan.relicsmanagement.dto.RelicInfoDto;
import cn.wegfan.relicsmanagement.entity.Relic;
import cn.wegfan.relicsmanagement.vo.RelicExcelVo;
import cn.wegfan.relicsmanagement.vo.RelicVo;
import ma.glasnost.orika.MapperFactory;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

@Component
public class RelicMapping implements OrikaMapperFactoryConfigurer {

    @Override
    public void configure(MapperFactory mapperFactory) {
        mapperFactory.classMap(Relic.class, RelicVo.class)
                .fieldMap("warehouse.name", "warehouseName").add()
                .fieldMap("shelf.name", "shelfName").add()
                .byDefault()
                .register();
        mapperFactory.classMap(RelicInfoDto.class, Relic.class)
                .mapNulls(false)
                .byDefault()
                .register();
        mapperFactory.classMap(RelicVo.class, RelicExcelVo.class)
                .fieldMap("statusId", "status").converter("relicStatusNameConverter").add()
                .byDefault()
                .register();
    }

}
