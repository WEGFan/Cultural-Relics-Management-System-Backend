package cn.wegfan.relicsmanagement.config.orika;

import cn.wegfan.relicsmanagement.mapper.RelicStatusDao;
import cn.wegfan.relicsmanagement.model.entity.RelicStatus;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
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
