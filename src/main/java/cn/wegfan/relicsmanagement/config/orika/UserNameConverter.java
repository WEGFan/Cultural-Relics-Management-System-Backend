package cn.wegfan.relicsmanagement.config.orika;

import cn.wegfan.relicsmanagement.mapper.UserDao;
import cn.wegfan.relicsmanagement.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserNameConverter extends CustomConverter<Integer, String> {

    private UserDao userDao;

    public UserNameConverter(UserDao userDao) {
        super();
        this.userDao = userDao;
    }

    @Override
    public String convert(Integer source, Type<? extends String> destinationType, MappingContext mappingContext) {
        User user = userDao.selectByUserId(source);
        if (user == null) {
            return null;
        }
        return user.getName();
    }

}
