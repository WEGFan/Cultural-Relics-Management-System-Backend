package cn.wegfan.relicsmanagement.config.orika;

import cn.wegfan.relicsmanagement.dto.RelicInfoDto;
import cn.wegfan.relicsmanagement.entity.Relic;
import cn.wegfan.relicsmanagement.entity.RelicCheckDetail;
import cn.wegfan.relicsmanagement.entity.User;
import cn.wegfan.relicsmanagement.vo.RelicCheckDetailVo;
import cn.wegfan.relicsmanagement.vo.RelicVo;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

@Component
public class RelicMapping implements OrikaMapperFactoryConfigurer {

    @Override
    public void configure(MapperFactory mapperFactory) {
        mapperFactory.classMap(Relic.class, RelicVo.class)
                .byDefault()
                .register();
        mapperFactory.classMap(RelicInfoDto.class, Relic.class)
                .mapNulls(false)
                .byDefault()
                .register();
    }

}
