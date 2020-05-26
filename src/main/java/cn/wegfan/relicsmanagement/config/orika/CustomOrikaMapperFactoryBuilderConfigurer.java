package cn.wegfan.relicsmanagement.config.orika;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.converter.builtin.BuiltinConverters;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryBuilderConfigurer;
import org.springframework.stereotype.Component;

@Component
public class CustomOrikaMapperFactoryBuilderConfigurer implements OrikaMapperFactoryBuilderConfigurer {

    @Override
    public void configure(DefaultMapperFactory.MapperFactoryBuilder<?, ?> mapperFactoryBuilder) {
        MapperFactory mapperFactory = mapperFactoryBuilder
                // 先不使用内置转换器，让自定义转换器先注册
                .useBuiltinConverters(false)
                .build();

        ConverterFactory converterFactory = mapperFactory.getConverterFactory();

        converterFactory.registerConverter(new AllowEmptyStringFromStringConverter());

        BuiltinConverters.register(converterFactory);
    }

}
