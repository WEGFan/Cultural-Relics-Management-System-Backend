package cn.wegfan.relicsmanagement.config.orika;

import cn.wegfan.relicsmanagement.entity.RelicCheckDetail;
import cn.wegfan.relicsmanagement.entity.User;
import cn.wegfan.relicsmanagement.vo.RelicCheckDetailVo;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

@Component
public class RelicCheckDetailMapping implements OrikaMapperFactoryConfigurer {

    @Override
    public void configure(MapperFactory mapperFactory) {
        mapperFactory
                .getConverterFactory()
                .registerConverter("operatorNameConvert", new CustomConverter<User, String>() {
                    @Override
                    public String convert(User user, Type<? extends String> type, MappingContext mappingContext) {
                        return user.getName();
                    }
                });
        mapperFactory.classMap(RelicCheckDetail.class, RelicCheckDetailVo.class)
                .fieldMap("relic.id", "relicId").add()
                .fieldMap("relic.name", "name").add()
                .fieldMap("relic.picturePath", "picturePath").add()
                .fieldMap("operator", "operatorName").converter("operatorNameConvert").add()
                .byDefault()
                .register();
    }

}
