package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.entity.RelicCheck;
import cn.wegfan.relicsmanagement.entity.User;
import cn.wegfan.relicsmanagement.mapper.*;
import cn.wegfan.relicsmanagement.util.BusinessErrorEnum;
import cn.wegfan.relicsmanagement.util.BusinessException;
import cn.wegfan.relicsmanagement.vo.CheckIdVo;
import cn.wegfan.relicsmanagement.vo.PageResultVo;
import cn.wegfan.relicsmanagement.vo.RelicCheckVo;
import cn.wegfan.relicsmanagement.vo.SuccessVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class RelicCheckServiceImpl implements RelicCheckService {

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

    public RelicCheckServiceImpl() {
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

    @Override
    public PageResultVo<RelicCheckVo> listByWarehouseIdAndPage(Integer warehouseId, long pageIndex, long pageSize) {
        Page<RelicCheck> page = new Page<>(pageIndex, pageSize);

        Page<RelicCheck> pageResult = relicCheckDao.selectPageByWarehouseId(page, warehouseId);

        List<RelicCheck> relicCheckList = pageResult.getRecords();
        log.debug(relicCheckList.toString());
        // TODO: checkCount
        List<RelicCheckVo> relicCheckVoList = mapperFacade.mapAsList(relicCheckList, RelicCheckVo.class);
        return new PageResultVo<RelicCheckVo>(relicCheckVoList, pageResult);
    }

    @Override
    public CheckIdVo startRelicCheck(Integer warehouseId) {
        // 获取当前登录的用户编号
        Integer currentLoginUserId = (Integer)SecurityUtils.getSubject().getPrincipal();

        // 检查仓库是否存在
        if (warehouseDao.selectNotDeletedById(warehouseId) == null) {
            throw new BusinessException(BusinessErrorEnum.WarehouseNotExists);
        }
        // 检查当前登录的用户是否有其他未完成的盘点
        if (relicCheckDao.selectNotEndByUserId(currentLoginUserId) != null) {
            throw new BusinessException(BusinessErrorEnum.HasNotEndedRelicCheck);
        }
        // 检查是否有其他用户在盘点该仓库
        if (relicCheckDao.selectNotEndByWarehouseId(warehouseId) != null) {
            throw new BusinessException(BusinessErrorEnum.WarehouseHasBeenChecking);
        }

        RelicCheck relicCheck = new RelicCheck();
        relicCheck.setOperatorId(currentLoginUserId);
        relicCheck.setWarehouseId(warehouseId);
        relicCheck.setStartTime(new Date());

        relicCheckDao.insert(relicCheck);
        return new CheckIdVo(relicCheck.getId());
    }

    @Override
    public SuccessVo endRelicCheck() {
        // 获取当前登录的用户编号
        Integer currentLoginUserId = (Integer)SecurityUtils.getSubject().getPrincipal();

        if (relicCheckDao.selectNotEndByUserId(currentLoginUserId) == null) {
            throw new BusinessException(BusinessErrorEnum.NoNotEndedRelicCheck);
        }

        int result = relicCheckDao.updateEndTimeByUserId(currentLoginUserId);

        return new SuccessVo(result > 0);
    }

}
