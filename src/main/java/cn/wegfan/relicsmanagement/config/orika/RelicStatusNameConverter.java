package cn.wegfan.relicsmanagement.config.orika;

import cn.wegfan.relicsmanagement.mapper.RelicStatusDao;
import cn.wegfan.relicsmanagement.model.entity.RelicStatus;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * 文物状态编号到文物状态名称转换器
 */
@Component
public class RelicStatusNameConverter extends CustomConverter<Integer, String> {

    private Map<Integer, String> relicStatusNameMap;

    public RelicStatusNameConverter(RelicStatusDao relicStatusDao) {
        super();
        relicStatusNameMap = relicStatusDao.selectRelicStatusList()
                .stream()
                .collect(Collectors.toMap(RelicStatus::getId, RelicStatus::getName));
    }

    @Override
    public String convert(Integer source, Type<? extends String> destinationType, MappingContext mappingContext) {
        return relicStatusNameMap.get(source);
    }

}
