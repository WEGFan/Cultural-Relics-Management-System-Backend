package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.dto.RelicMoveDto;
import cn.wegfan.relicsmanagement.entity.Relic;
import cn.wegfan.relicsmanagement.entity.RelicCheck;
import cn.wegfan.relicsmanagement.entity.RelicCheckDetail;
import cn.wegfan.relicsmanagement.entity.User;
import cn.wegfan.relicsmanagement.mapper.*;
import cn.wegfan.relicsmanagement.util.BusinessErrorEnum;
import cn.wegfan.relicsmanagement.util.BusinessException;
import cn.wegfan.relicsmanagement.vo.PageResultVo;
import cn.wegfan.relicsmanagement.vo.RelicCheckDetailVo;
import cn.wegfan.relicsmanagement.vo.RelicCheckVo;
import cn.wegfan.relicsmanagement.vo.SuccessVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class RelicCheckDetailServiceImpl extends ServiceImpl<RelicCheckDetailDao, RelicCheckDetail> implements RelicCheckDetailService {

    @Autowired
    private RelicStatusDao relicStatusDao;

    @Autowired
    private RelicCheckDetailDao relicCheckDetailDao;

    @Autowired
    private RelicDao relicDao;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RelicCheckService relicCheckService;

    @Autowired
    private WarehouseDao warehouseDao;

    @Autowired
    private RelicCheckDao relicCheckDao;

    @Autowired
    private ShelfDao shelfDao;

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    private MapperFacade mapperFacade;

    public RelicCheckDetailServiceImpl() {
        mapperFactory.classMap(RelicCheckDetail.class, RelicCheckDetailVo.class)
                .fieldMap("relic.id", "relicId").add()
                .fieldMap("relic.name", "name").add()
                .fieldMap("relic.picturePath", "picturePath").add()
                .byDefault()
                .register();
        mapperFacade = mapperFactory.getMapperFacade();
    }

    @Override
    public PageResultVo<RelicCheckDetailVo> listRelicCheckDetailByCheckIdAndPage(Integer checkId, long pageIndex, long pageSize) {
        Page<RelicCheckDetail> page = new Page<>(pageIndex, pageSize);

        Page<RelicCheckDetail> pageResult = relicCheckDetailDao.selectPageByCheckId(page, checkId);

        List<RelicCheckDetail> relicCheckDetailList = pageResult.getRecords();
        log.debug(relicCheckDetailList.toString());

        List<RelicCheckDetailVo> relicCheckDetailVoList = mapperFacade.mapAsList(relicCheckDetailList, RelicCheckDetailVo.class);
        return new PageResultVo<RelicCheckDetailVo>(relicCheckDetailVoList, pageResult);
    }

    @Override
    public SuccessVo addRelicCheckDetail(Integer checkId, Integer relicId, RelicMoveDto dto) {
        RelicCheck relicCheck = relicCheckDao.selectNotEndByCheckId(checkId);
        if (relicCheck == null) {
            throw new BusinessException(BusinessErrorEnum.RelicCheckEnded);
        }

        Relic relic = relicDao.selectNotDeletedByRelicId(relicId);
        if (relic == null) {
            throw new BusinessException(BusinessErrorEnum.RelicNotExists);
        }

        Integer warehouseId = dto.getWarehouseId();
        Integer shelfId = dto.getShelfId();
        if (warehouseId == null) {
            warehouseId = relic.getWarehouseId();
        }
        if (shelfId == null) {
            shelfId = relic.getShelfId();
        }

        // 获取当前登录的用户编号
        Integer currentLoginUserId = (Integer)SecurityUtils.getSubject().getPrincipal();
        Integer currentCheckWarehouseId = relicCheck.getWarehouseId();

        if (warehouseDao.selectNotDeletedById(warehouseId) == null) {
            throw new BusinessException(BusinessErrorEnum.WarehouseNotExists);
        }
        if (shelfDao.selectNotDeletedByWarehouseIdAndShelfId(warehouseId, shelfId) == null) {
            throw new BusinessException(BusinessErrorEnum.ShelfNotExists);
        }

        RelicCheckDetail relicCheckDetail = relicCheckDetailDao.selectByCheckIdAndRelicId(checkId, relicId);

        // 说明这个文物在盘点前不属于这个仓库
        if (relicCheckDetail == null) {
            relicCheckDetail = new RelicCheckDetail();
            relicCheckDetail.setOldWarehouseId(relic.getWarehouseId());
            relicCheckDetail.setOldShelfId(relic.getShelfId());
            relicCheckDetail.setCreateTime(new Date());
            relicCheckDetail.setCheckId(checkId);
            relicCheckDetail.setRelicId(relicId);
        }

        if (relicCheckDetail.getCheckTime() != null) {
            throw new BusinessException(BusinessErrorEnum.RelicAlreadyChecked);
        }

        relicCheckDetail.setOperatorId(currentLoginUserId);
        relicCheckDetail.setNewWarehouseId(warehouseId);
        relicCheckDetail.setNewShelfId(shelfId);
        relicCheckDetail.setCheckTime(new Date());
        
        saveOrUpdate(relicCheckDetail);

        relicDao.updateRelicWarehouseAndShelfById(relic.getId(), warehouseId, shelfId);

        return new SuccessVo(true);
    }

}
