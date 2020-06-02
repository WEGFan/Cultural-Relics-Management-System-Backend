package cn.wegfan.relicsmanagement.config.orika;

import cn.wegfan.relicsmanagement.mapper.JobDao;
import cn.wegfan.relicsmanagement.mapper.PermissionDao;
import cn.wegfan.relicsmanagement.mapper.RelicStatusDao;
import cn.wegfan.relicsmanagement.mapper.UserDao;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.converter.builtin.BuiltinConverters;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryBuilderConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomOrikaMapperFactoryBuilderConfigurer implements OrikaMapperFactoryBuilderConfigurer {

    @Autowired
    private JobDao jobDao;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private RelicStatusDao relicStatusDao;

    @Autowired
    private UserDao userDao;

    @Override
    public void configure(DefaultMapperFactory.MapperFactoryBuilder<?, ?> mapperFactoryBuilder) {
        MapperFactory mapperFactory = mapperFactoryBuilder
                // 先不使用内置转换器，让自定义转换器先注册
                .useBuiltinConverters(false)
                .build();

        ConverterFactory converterFactory = mapperFactory.getConverterFactory();

        converterFactory.registerConverter(new FromEmptiableStringConverter());
        converterFactory.registerConverter(new EmptiableStringToBigDecimalConverter());
        converterFactory.registerConverter("jobNameConverter", new JobNameConverter(jobDao));
        converterFactory.registerConverter("extraPermissionNameConverter", new ExtraPermissionNameConverter());
        converterFactory.registerConverter("relicStatusNameConverter", new RelicStatusNameConverter(relicStatusDao));
        converterFactory.registerConverter(new BidirectionalPermissionIdEntitySetConverter(permissionDao));
        converterFactory.registerConverter("userNameConverter", new UserNameConverter(userDao));

        BuiltinConverters.register(converterFactory);
    }

}
