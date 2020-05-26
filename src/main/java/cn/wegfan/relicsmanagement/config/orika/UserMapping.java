package cn.wegfan.relicsmanagement.config.orika;

import cn.wegfan.relicsmanagement.dto.UserInfoDto;
import cn.wegfan.relicsmanagement.entity.Job;
import cn.wegfan.relicsmanagement.entity.Permission;
import cn.wegfan.relicsmanagement.entity.User;
import cn.wegfan.relicsmanagement.vo.JobVo;
import cn.wegfan.relicsmanagement.vo.UserVo;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapping implements OrikaMapperFactoryConfigurer {

    @Override
    public void configure(MapperFactory mapperFactory) {
        // mapperFactory
        //         .getConverterFactory()
        //         .registerConverter("permissionIdConvert", new CustomConverter<Set<Permission>, Set<Integer>>() {
        //             @Override
        //             public Set<Integer> convert(Set<Permission> extraPermissions, Type<? extends Set<Integer>> type, MappingContext mappingContext) {
        //                 // 把权限的id提取成列表
        //                 return extraPermissions.stream()
        //                         .map(Permission::getId)
        //                         .collect(Collectors.toSet());
        //             }
        //         });
        mapperFactory.classMap(User.class, UserVo.class)
                .fieldMap("extraPermissions", "extraPermissionsId").converter("permissionIdConvert").add()
                .byDefault()
                .register();
        mapperFactory.classMap(UserInfoDto.class, User.class)
                .mapNulls(false)
                .byDefault()
                .register();
    }

}
