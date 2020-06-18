package cn.wegfan.relicsmanagement.config.orika;

import cn.wegfan.relicsmanagement.mapper.PermissionDao;
import cn.wegfan.relicsmanagement.model.entity.Permission;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 双向权限编号与权限对象转换器
 */
@Component
@Slf4j
public class BidirectionalPermissionIdEntitySetConverter extends BidirectionalConverter<Set<Permission>, Set<Integer>> {

    private Map<Integer, Permission> permissionNameMap;

    public BidirectionalPermissionIdEntitySetConverter(PermissionDao permissionDao) {
        super();
        permissionNameMap = permissionDao.selectPermissionList()
                .stream()
                .collect(Collectors.toMap(Permission::getId, v -> v));
    }

    @Override
    public Set<Integer> convertTo(Set<Permission> source, Type<Set<Integer>> destinationType, MappingContext mappingContext) {
        return source.stream()
                .map(Permission::getId)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Permission> convertFrom(Set<Integer> source, Type<Set<Permission>> destinationType, MappingContext mappingContext) {
        return source.stream()
                .map(permissionNameMap::get)
                .collect(Collectors.toSet());
    }

}
