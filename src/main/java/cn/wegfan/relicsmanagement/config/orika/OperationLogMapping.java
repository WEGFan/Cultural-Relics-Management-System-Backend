package cn.wegfan.relicsmanagement.config.orika;

import cn.wegfan.relicsmanagement.entity.OperationLog;
import cn.wegfan.relicsmanagement.vo.OperationLogExcelVo;
import cn.wegfan.relicsmanagement.vo.OperationLogVo;
import ma.glasnost.orika.MapperFactory;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

@Component
public class OperationLogMapping implements OrikaMapperFactoryConfigurer {

    @Override
    public void configure(MapperFactory mapperFactory) {
        mapperFactory.classMap(OperationLog.class, OperationLogVo.class)
                .fieldMap("operatorId", "operatorName").converter("userNameConverter").add()
                .byDefault()
                .register();
        mapperFactory.classMap(OperationLogVo.class, OperationLogExcelVo.class)
                .byDefault()
                .register();
    }

}
