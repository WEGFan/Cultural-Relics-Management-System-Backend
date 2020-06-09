package cn.wegfan.relicsmanagement.config.orika;

import cn.wegfan.relicsmanagement.model.entity.Permission;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.StringJoiner;

@Component
@Slf4j
public class ExtraPermissionNameConverter extends CustomConverter<Set<Permission>, String> {

    @Override
    public String convert(Set<Permission> source, Type<? extends String> destinationType, MappingContext mappingContext) {
        StringJoiner stringJoiner = new StringJoiner("；");
        source.forEach(permission -> stringJoiner.add(permission.getName()));
        return stringJoiner.toString();
    }

}
