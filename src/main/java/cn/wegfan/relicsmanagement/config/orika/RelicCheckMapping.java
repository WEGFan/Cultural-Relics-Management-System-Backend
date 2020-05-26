package cn.wegfan.relicsmanagement.config.orika;

import cn.wegfan.relicsmanagement.entity.RelicCheck;
import cn.wegfan.relicsmanagement.entity.RelicCheckDetail;
import cn.wegfan.relicsmanagement.entity.User;
import cn.wegfan.relicsmanagement.vo.RelicCheckDetailVo;
import cn.wegfan.relicsmanagement.vo.RelicCheckVo;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

@Component
public class RelicCheckMapping implements OrikaMapperFactoryConfigurer {

    @Override
    public void configure(MapperFactory mapperFactory) {
        mapperFactory.classMap(RelicCheck.class, RelicCheckVo.class)
                // .fieldMap("operator", "operatorName").converter("operatorNameConvert").add()
                .byDefault()
                .register();
    }

}
