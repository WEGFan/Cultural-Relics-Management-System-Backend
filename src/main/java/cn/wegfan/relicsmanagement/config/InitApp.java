package cn.wegfan.relicsmanagement.config;

import cn.wegfan.relicsmanagement.dto.UserInfoDto;
import cn.wegfan.relicsmanagement.mapper.UserDao;
import cn.wegfan.relicsmanagement.service.UserService;
import cn.wegfan.relicsmanagement.util.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.util.HashSet;

@Slf4j
@Component
public class InitApp implements ApplicationRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ServletContext context;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // File file = FileUtil.touch("data/images/1.txt");
        // log.debug(file.getAbsolutePath());
        // FileUtil.writeBytes(new byte[] {47, 52, 53, 56}, file);
        // Resource resource = new ClassPathResource("");
        // String userDirectory = Paths.get("")
        //         .toAbsolutePath()
        //         .toString();
        // log.debug(userDirectory);
        // log.debug(resource.getFile().getAbsolutePath());
        // log.debug(Paths.get("1111").resolve("333").toString());
        // ApplicationHome home = new ApplicationHome(CulturalRelicsManagementSystemBackendApplication.class);
        // log.debug(home.getDir().getAbsolutePath());    // returns the folder where the jar is. This is what I wanted.
        // log.debug(home.getSource().getAbsolutePath());   // returns the jar absolute path.
        // Path path = Paths.get(context.getRealPath("uploads") + "12321321");
        // log.debug(path.toString());
        // 新增管理员帐号，方便调试
        Integer workId = 10000;
        if (userDao.selectByWorkId(workId) == null) {
            UserInfoDto userInfo = new UserInfoDto();

            userInfo.setName("admin");
            userInfo.setPassword("admin");
            userInfo.setWorkId(workId);
            userInfo.setJobId(5);
            userInfo.setTelephone("0");
            userInfo.setExtraPermissionsId(new HashSet<>());

            try {
                userService.addUser(userInfo);
            } catch (BusinessException e) {
                log.error("", e);
            }
        }
    }

}
