package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.entity.User;
import cn.wegfan.relicsmanagement.entity.Permission;
import cn.wegfan.relicsmanagement.mapper.UserDao;
import cn.wegfan.relicsmanagement.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.*;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
    
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
        
        mapperFactory
                .getConverterFactory()
                .registerConverter("permissionConvert", new CustomConverter<List<Permission>, List<Integer>>() {
                    @Override
                    public List<Integer> convert(List<Permission> permissions, Type<? extends List<Integer>> type) {
                        // return Arrays.asList(1, 2, 3);
                        return permissions.stream()
                                .map(Permission::getId)
                                .collect(Collectors.toList());
                    }
                });
        mapperFactory.classMap(User.class, UserVo.class)
                .fieldMap("permissions", "permissionId").converter("permissionConvert").add()
                .byDefault()
                .register();
    }

    private List<Integer> convertToPermissionIdList(List<Permission> src) {
        return src.stream()
                .map(Permission::getId)
                .collect(Collectors.toList());
    }

@Override
    public List<UserVo> listAllUsers() {
        MapperFacade mapperFacade = mapperFactory.getMapperFacade();

        List<User> userList = userDao.selectList();
        List<UserVo> userVoList = mapperFacade.mapAsList(userList, UserVo.class);


        return userVoList;
    }

    @Override
    public UserVo getUserById(Integer userId) {
         // MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        
        MapperFacade mapperFacade = mapperFactory.getMapperFacade();
        User user = userDao.selectById(userId);
        // log.debug(user.toString());
        UserVo userVo = mapperFacade.map(user, UserVo.class);
        // UserVo  userVo2 = mapperFacade.map(user, UserVo.class);
        // userVo.setPermissionIdList(convertToPermissionIdList(user.getPermissions()));
        return userVo;

    }

}
