package cn.wegfan.relicsmanagement.config.orika;

import cn.wegfan.relicsmanagement.mapper.UserDao;
import cn.wegfan.relicsmanagement.model.entity.User;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;
import org.springframework.stereotype.Component;

/**
 * 用户编号到姓名转换器
 */
@Component
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
