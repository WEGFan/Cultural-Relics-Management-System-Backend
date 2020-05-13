package cn.wegfan.relicsmanagement.service;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.wegfan.relicsmanagement.entity.Relic;
import cn.wegfan.relicsmanagement.entity.RelicStatus;
import cn.wegfan.relicsmanagement.mapper.RelicDao;
import cn.wegfan.relicsmanagement.mapper.RelicStatusDao;
import cn.wegfan.relicsmanagement.util.BusinessErrorEnum;
import cn.wegfan.relicsmanagement.util.BusinessException;
import cn.wegfan.relicsmanagement.vo.PageResultVo;
import cn.wegfan.relicsmanagement.vo.RelicIdPicturePathVo;
import cn.wegfan.relicsmanagement.vo.RelicVo;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Date;
import java.util.List;
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

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    private MapperFacade mapperFacade;

    public RelicServiceImpl() {
        mapperFactory.classMap(Relic.class, RelicVo.class)
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
        return null;
    }

    @Override
    public RelicVo getRelicById(Integer relicId) {
        Relic relic = relicDao.selectNotDeletedByRelicId(relicId);
        if (relic == null) {
            throw new BusinessException(BusinessErrorEnum.RelicNotExists);
        }

        RelicVo relicVo = mapperFacade.map(relic, RelicVo.class);
        log.debug(relicVo.toString());

        // 获取当前登录的用户编号
        Integer currentLoginUserId = (Integer)SecurityUtils.getSubject().getPrincipal();

        Set<String> permissionCodeSet = permissionService.listAllPermissionCodeByUserId(currentLoginUserId);
        relicVo.clearFieldsByPermissionCode(permissionCodeSet);
        return relicVo;
    }

    @Override
    public RelicIdPicturePathVo addRelicByPicturePath(String tempPath) {
        File tempFile = new File(tempPath);
        // 获取文件真实类型
        String fileType = FileTypeUtil.getType(tempFile);
        log.debug(fileType);
        if (!fileType.equals("jpg") && !fileType.equals("png")) {
            throw new BusinessException(BusinessErrorEnum.FileNotJpgOrPng);
        }
        String fileExtension = "." + fileType;
        File tempFileWithExtension = new File(tempFile + fileExtension);
        // 给文件加上真实扩展名，不然后面图片格式转换会失败
        FileUtil.move(tempFile, tempFileWithExtension, true);

        Relic relic = new Relic();
        relic.setCreateTime(new Date());
        relic.setStatusId(1); // 设置为待评估
        // 先插入到数据库并获取id
        relicDao.insert(relic);

        String fileName = relic.getId() + ".jpg";
        File file = FileUtil.touch(tempFile.getParentFile().getParentFile(), fileName);
        // 转换成jpg格式
        ImgUtil.convert(tempFileWithExtension, file);

        relic.setPicturePath("/files/relics/images/" + fileName);
        relicDao.updateById(relic);

        return new RelicIdPicturePathVo(relic.getId(), relic.getPicturePath());
    }

}
