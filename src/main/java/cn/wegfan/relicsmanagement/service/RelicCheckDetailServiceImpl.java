package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.entity.RelicCheck;
import cn.wegfan.relicsmanagement.entity.User;
import cn.wegfan.relicsmanagement.mapper.*;
import cn.wegfan.relicsmanagement.vo.RelicCheckVo;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class RelicCheckDetailServiceImpl implements RelicCheckDetailService {

    @Autowired
    private RelicStatusDao relicStatusDao;

    @Autowired
    private RelicDao relicDao;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private WarehouseDao warehouseDao;

    @Autowired
    private RelicCheckDao relicCheckDao;

    @Autowired
    private ShelfDao shelfDao;

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    private MapperFacade mapperFacade;

    public RelicCheckDetailServiceImpl() {
        mapperFactory
                .getConverterFactory()
                .registerConverter("operatorNameConvert", new CustomConverter<User, String>() {
                    @Override
                    public String convert(User user, Type<? extends String> type, MappingContext mappingContext) {
                        return user.getName();
                    }
                });
        mapperFactory.classMap(RelicCheck.class, RelicCheckVo.class)
                .fieldMap("operator", "operatorName").converter("operatorNameConvert").add()
                .byDefault()
                .register();
        mapperFacade = mapperFactory.getMapperFacade();
    }

}
