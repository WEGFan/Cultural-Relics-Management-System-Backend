package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.dto.UserInfoDto;
import cn.wegfan.relicsmanagement.entity.Permission;
import cn.wegfan.relicsmanagement.entity.User;
import cn.wegfan.relicsmanagement.entity.Warehouse;
import cn.wegfan.relicsmanagement.mapper.UserDao;
import cn.wegfan.relicsmanagement.mapper.WarehouseDao;
import cn.wegfan.relicsmanagement.vo.PageResultVo;
import cn.wegfan.relicsmanagement.vo.UserVo;
import cn.wegfan.relicsmanagement.vo.WarehouseVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    private WarehouseDao warehouseDao;

    @Autowired
    private UserDao userDao;

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    private MapperFacade mapperFacade;

    public WarehouseServiceImpl() {
        mapperFactory.classMap(Warehouse.class, WarehouseVo.class)
                .byDefault()
                .register();
        mapperFacade = mapperFactory.getMapperFacade();
    }

    public PageResultVo<WarehouseVo> listWarehousesByNameAndPage(String name, long currentPage, long pageSize) {
        Page<Warehouse> page = new Page<>(currentPage, pageSize);
        
        LambdaQueryWrapper<Warehouse> queryWrapper = null;
        if (name != null) {
            // 按照仓库名筛选
            queryWrapper = new QueryWrapper<Warehouse>()
                    .lambda()
                    .like(Warehouse::getName, name);
        }
        Page<Warehouse> pageResult = warehouseDao.selectPage(page, queryWrapper);
        // log.debug(String.valueOf(result.getRecords()));
        // log.debug("current={} size={} total={} pages={}", result.getCurrent(), result.getSize(), result.getTotal(), result.getPages());
        // log.debug("{} {}", result.hasPrevious(), result.hasNext());
        List<Warehouse> warehouseList = pageResult.getRecords();
        List<WarehouseVo> warehouseVoList = mapperFacade.mapAsList(warehouseList, WarehouseVo.class);

        return new PageResultVo<WarehouseVo>(warehouseVoList, pageResult);
    }

}
