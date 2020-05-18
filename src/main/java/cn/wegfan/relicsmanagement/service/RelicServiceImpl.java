package cn.wegfan.relicsmanagement.service;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.wegfan.relicsmanagement.dto.RelicInfoDto;
import cn.wegfan.relicsmanagement.entity.Relic;
import cn.wegfan.relicsmanagement.entity.RelicStatus;
import cn.wegfan.relicsmanagement.mapper.RelicDao;
import cn.wegfan.relicsmanagement.mapper.RelicStatusDao;
import cn.wegfan.relicsmanagement.mapper.ShelfDao;
import cn.wegfan.relicsmanagement.mapper.WarehouseDao;
import cn.wegfan.relicsmanagement.util.BusinessErrorEnum;
import cn.wegfan.relicsmanagement.util.BusinessException;
import cn.wegfan.relicsmanagement.util.PermissionCodeEnum;
import cn.wegfan.relicsmanagement.util.RelicStatusEnum;
import cn.wegfan.relicsmanagement.vo.PageResultVo;
import cn.wegfan.relicsmanagement.vo.RelicIdPicturePathVo;
import cn.wegfan.relicsmanagement.vo.RelicVo;
import cn.wegfan.relicsmanagement.vo.SuccessVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class RelicServiceImpl implements RelicService {

    @Autowired
    private RelicStatusDao relicStatusDao;

    @Autowired
    private RelicDao relicDao;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private WarehouseDao warehouseDao;

    @Autowired
    private ShelfDao shelfDao;

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    private MapperFacade mapperFacade;

    public RelicServiceImpl() {
        mapperFactory.classMap(Relic.class, RelicVo.class)
                .byDefault()
                .register();
        mapperFactory.classMap(RelicInfoDto.class, Relic.class)
                .mapNulls(false)
                .byDefault()
                .register();
        mapperFacade = mapperFactory.getMapperFacade();
    }

    @Override
    public List<RelicStatus> listAllRelicStatus() {
        List<RelicStatus> relicStatusList = relicStatusDao.selectList(null);
        return relicStatusList;
    }

    @Override
    public PageResultVo<RelicVo> searchNotDeletedRelicsByPage(String name, Integer status, String dateType,
                                                              Date startTime, Date endTime,
                                                              long pageIndex, long pageSize) {
        Page<Relic> page = new Page<>(pageIndex, pageSize);

        // 检测是否填写了开始或结束时间，但是没填写时间类型
        if (dateType == null && (startTime != null || endTime != null)) {
            throw new BusinessException(BusinessErrorEnum.NoDateType);
        }

        // 检测字符串是否符合格式
        if (dateType != null && dateType.matches("^(enter|leave|lend|fix)$")) {
            dateType += "_time";
        } else {
            dateType = null;
        }

        Page<Relic> pageResult = relicDao.selectPageNotDeletedByCondition(page, name, status, dateType,
                startTime, endTime);
        // log.debug(String.valueOf(result.getRecords()));
        // log.debug("current={} size={} total={} pages={}", result.getCurrent(), result.getSize(), result.getTotal(), result.getPages());
        // log.debug("{} {}", result.hasPrevious(), result.hasNext());
        List<Relic> relicList = pageResult.getRecords();
        log.debug(relicList.toString());
        List<RelicVo> relicVoList = mapperFacade.mapAsList(relicList, RelicVo.class);

        // 获取当前登录的用户权限
        Set<String> permissionCodeSet = permissionService.listAllPermissionCodeByCurrentLoginUser();

        relicVoList.forEach(i -> i.clearFieldsByPermissionCode(permissionCodeSet));
        return new PageResultVo<RelicVo>(relicVoList, pageResult);
    }

    @Override
    public RelicVo getRelicById(Integer relicId) {
        // 根据文物编号查找未删除的文物中是否存在该文物
        Relic relic = relicDao.selectNotDeletedByRelicId(relicId);
        if (relic == null) {
            throw new BusinessException(BusinessErrorEnum.RelicNotExists);
        }

        RelicVo relicVo = mapperFacade.map(relic, RelicVo.class);
        log.debug(relicVo.toString());

        // 获取当前登录的用户权限
        Set<String> permissionCodeSet = permissionService.listAllPermissionCodeByCurrentLoginUser();

        relicVo.clearFieldsByPermissionCode(permissionCodeSet);
        return relicVo;
    }

    @Override
    public RelicIdPicturePathVo addRelicByPicturePath(String tempPath) {
        File tempFile = new File(tempPath);
        // 获取文件真实类型
        String fileType = FileTypeUtil.getType(tempFile);
        log.debug(fileType);
        if (!"jpg".equals(fileType) && !"png".equals(fileType)) {
            throw new BusinessException(BusinessErrorEnum.FileNotJpgOrPng);
        }
        String fileExtension = "." + fileType;
        File tempFileWithExtension = new File(tempFile + fileExtension);
        // 给文件加上真实扩展名，不然后面图片格式转换会失败
        FileUtil.move(tempFile, tempFileWithExtension, true);

        Relic relic = new Relic();
        // 设置为待评估
        relic.setStatusId(1);
        relic.setCreateTime(new Date());
        relic.setUpdateTime(new Date());

        // 先插入到数据库并获取id
        relicDao.insert(relic);

        String fileName = relic.getId() + ".jpg";
        File file = FileUtil.touch(tempFile.getParentFile().getParentFile(), fileName);
        // 转换成jpg格式
        ImgUtil.convert(tempFileWithExtension, file);

        relic.setPicturePath("/api/files/relics/images/" + fileName);
        relicDao.updateById(relic);

        return new RelicIdPicturePathVo(relic.getId(), relic.getPicturePath());
    }

    @Override
    public SuccessVo deleteRelicById(Integer relicId) {
        int result = relicDao.deleteRelicById(relicId);
        return new SuccessVo(result > 0);
    }

    @Override
    public RelicVo updateRelicInfo(Integer relicId, RelicInfoDto relicInfo) {
        // 根据文物编号查找未删除的文物中是否存在该文物
        Relic relic = relicDao.selectNotDeletedByRelicId(relicId);
        if (relic == null) {
            throw new BusinessException(BusinessErrorEnum.RelicNotExists);
        }

        Integer oldRelicStatusId = relic.getStatusId();
        Integer newRelicStatusId = relicInfo.getStatusId();

        boolean relicMoved = !Objects.equals(relic.getWarehouseId(), relicInfo.getWarehouseId()) ||
                !Objects.equals(relic.getShelfId(), relicInfo.getShelfId());

        // 获取当前登录的用户权限
        Set<String> permissionCodeSet = permissionService.listAllPermissionCodeByCurrentLoginUser();
        // 根据权限检查是否存在不能修改的字段
        boolean fieldAllValid = relicInfo.checkAndClearFieldsByPermissionCode(permissionCodeSet);
        if (!fieldAllValid) {
            throw new BusinessException(BusinessErrorEnum.Unauthorized);
        }

        // 如果用户只有修改文物状态为入馆的权限
        if (permissionCodeSet.contains(PermissionCodeEnum.RELIC_ENTER_MUSEUM)) {
            // 判断修改前和修改后的权限是否为待评估或入馆
            if (oldRelicStatusId > RelicStatusEnum.InMuseum.getStatusId() ||
                    newRelicStatusId > RelicStatusEnum.InMuseum.getStatusId()) {
                throw new BusinessException(BusinessErrorEnum.Unauthorized);
            }
        }

        // 如果文物的仓库和货架不为空，检测货架是否存在
        if ((relicInfo.getWarehouseId() != null || relicInfo.getShelfId() != null) &&
                shelfDao.selectNotDeletedByWarehouseIdAndShelfId(relicInfo.getWarehouseId(), relicInfo.getShelfId()) == null) {
            throw new BusinessException(BusinessErrorEnum.ShelfNotExists);
        }

        mapperFacade.map(relicInfo, relic);

        relic.setUpdateTime(new Date());
        // 如果修改了文物状态
        if (!newRelicStatusId.equals(oldRelicStatusId)) {
            // 入馆 设置入馆时间
            if (newRelicStatusId.equals(RelicStatusEnum.InMuseum.getStatusId())) {
                relic.setEnterTime(new Date());
            } else if (newRelicStatusId > RelicStatusEnum.InMuseum.getStatusId()) {
                // 清除时间
                relic.setFixTime(null);
                relic.setLendTime(null);
                relic.setLeaveTime(null);
                relic.setMoveTime(null);
                // 清除仓库和货架信息
                relic.setWarehouseId(null);
                relic.setShelfId(null);
                // 根据状态设置时间
                if (newRelicStatusId.equals(RelicStatusEnum.Lend.getStatusId())) {
                    relic.setLendTime(new Date());
                } else if (newRelicStatusId.equals(RelicStatusEnum.Fix.getStatusId())) {
                    relic.setFixTime(new Date());
                } else if (newRelicStatusId.equals(RelicStatusEnum.LeaveMuseum.getStatusId())) {
                    relic.setLeaveTime(new Date());
                }
            }

        }

        // 如果移动了文物则设置移动时间
        if (relicMoved) {
            relic.setMoveTime(new Date());
        }

        log.debug("{}", relic);
        relicDao.updateById(relic);

        RelicVo relicVo = mapperFacade.map(relic, RelicVo.class);
        return relicVo;
    }

}
