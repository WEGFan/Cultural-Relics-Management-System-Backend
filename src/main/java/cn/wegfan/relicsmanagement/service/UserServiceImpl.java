package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.dto.UserInfoDto;
import cn.wegfan.relicsmanagement.entity.Permission;
import cn.wegfan.relicsmanagement.entity.User;
import cn.wegfan.relicsmanagement.mapper.PermissionDao;
import cn.wegfan.relicsmanagement.mapper.UserDao;
import cn.wegfan.relicsmanagement.util.BusinessErrorEnum;
import cn.wegfan.relicsmanagement.util.BusinessException;
import cn.wegfan.relicsmanagement.vo.SuccessVo;
import cn.wegfan.relicsmanagement.vo.UserIdVo;
import cn.wegfan.relicsmanagement.vo.UserVo;
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

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private UserPermissionService userPermissionService;

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    private MapperFacade mapperFacade;

    public UserServiceImpl() {
        mapperFactory
                .getConverterFactory()
                .registerConverter("permissionConvert", new CustomConverter<List<Permission>, List<Integer>>() {
                    @Override
                    public List<Integer> convert(List<Permission> permissions, Type<? extends List<Integer>> type, MappingContext mappingContext) {
                        // 把权限的id提取成列表
                        return permissions.stream()
                                .map(Permission::getId)
                                .collect(Collectors.toList());
                    }
                });
        mapperFactory.classMap(User.class, UserVo.class)
                .fieldMap("permissions", "permissionId").converter("permissionConvert").add()
                .byDefault()
                .register();
        mapperFactory.classMap(UserInfoDto.class, User.class)
                .byDefault()
                .register();
        mapperFacade = mapperFactory.getMapperFacade();
    }

    @Override
    public List<UserVo> listAllUsers() {
        // MapperFacade mapperFacade = mapperFactory.getMapperFacade();

        List<User> userList = userDao.selectList(null);
        for (User user : userList) {
            user.setPermissions(permissionDao.selectListByUserId(user.getId()));
        }

        log.debug(userList.toString());
        List<UserVo> userVoList = mapperFacade.mapAsList(userList, UserVo.class);
        return userVoList;
    }

    @Override
    public UserVo getUserById(Integer userId) {
        // MapperFacade mapperFacade = mapperFactory.getMapperFacade();

        User user = userDao.selectById(userId);
        user.setPermissions(permissionDao.selectListByUserId(user.getId()));
        log.debug(user.toString());
        UserVo userVo = mapperFacade.map(user, UserVo.class);
        return userVo;
    }

    @Override
    public UserIdVo addUser(UserInfoDto userInfo) {
        // MapperFacade mapperFacade = mapperFactory.getMapperFacade();

        // 检查工号是否重复
        if (userDao.selectByWorkId(userInfo.getWorkId()) != null) {
            throw new BusinessException(BusinessErrorEnum.DuplicateWorkId);
        }

        User user = mapperFacade.map(userInfo, User.class);
        user.setSalt("");
        user.setCreateTime(new Date());
        log.debug(user.toString());

        userDao.insert(user);
        userPermissionService.updateUserPermissions(user.getId(), userInfo.getPermissionId());
        return new UserIdVo(user.getId());
    }

    @Override
    public SuccessVo updateUserInfo(UserInfoDto userInfo) {
        // 检查工号是否重复
        if (userDao.selectByWorkId(userInfo.getWorkId()) != null) {
            throw new BusinessException(BusinessErrorEnum.DuplicateWorkId);
        }

        User user = mapperFacade.map(userInfo, User.class);
        user.setUpdateTime(new Date());

        userDao.updateById(user);
        userPermissionService.updateUserPermissions(user.getId(), userInfo.getPermissionId());
        return new SuccessVo(true);
    }

}
