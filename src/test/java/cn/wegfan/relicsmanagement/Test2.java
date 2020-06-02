package cn.wegfan.relicsmanagement;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.wegfan.relicsmanagement.entity.Permission;
import cn.wegfan.relicsmanagement.entity.Relic;
import cn.wegfan.relicsmanagement.entity.User;
import cn.wegfan.relicsmanagement.entity.Warehouse;
import cn.wegfan.relicsmanagement.mapper.RelicDao;
import cn.wegfan.relicsmanagement.mapper.UserDao;
import cn.wegfan.relicsmanagement.mapper.WarehouseDao;
import cn.wegfan.relicsmanagement.service.UserExtraPermissionService;
import cn.wegfan.relicsmanagement.util.OperationLogUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.metadata.TypeBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@SpringBootTest
public class Test2 {

    @Data
    public static class RelicVo {

        /**
         * 文物编号
         */
        private Integer id;

        /**
         * 名称
         */
        private String name;

        /**
         * 数量
         */
        private Integer count;

        /**
         * 照片地址
         */
        private String picturePath;

        /**
         * 年代
         */
        private String year;

        /**
         * 年号
         */
        private String reign;

        /**
         * 器型
         */
        private String type;

        /**
         * 来源
         */
        private String source;

        /**
         * 尺寸
         */
        private String size;

        /**
         * 重量 kg
         */
        private Double weight;

        /**
         * 收储仓库id
         */
        private Integer warehouseId;

        /**
         * 收储地点
         */
        private String place;

        /**
         * 入馆价值【资产科】
         */
        private BigDecimal enterPrice;

        /**
         * 离馆价值【资产科】
         */
        private BigDecimal leavePrice;

        /**
         * 状态id
         */
        private Integer statusId;

        /**
         * 最后盘点时间【仓库管理员】
         */
        private Date lastCheckTime;

        /**
         * 入馆时间【仓库管理员】
         */
        private Date enterTime;

        /**
         * 离馆时间【仓库管理员】
         */
        private Date leaveTime;

        /**
         * 移入仓库时间【仓库管理员】
         */
        private Date moveTime;

        /**
         * 出借时间【仓库管理员】
         */
        private Date lendTime;

        /**
         * 送修时间【仓库管理员】
         */
        private Date fixTime;

        /**
         * 备注
         */
        private String comment;

        /**
         * 录入时间
         */
        private Date updateTime;

        /**
         * @param permissionCode
         */
        public void clearFieldsByPermission(String permissionCode) {

        }

        public void test() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
            name = "!11";
            Field field = getClass().getDeclaredField("name");
            field.setAccessible(true);
            log.debug("{}", field.get(this));
            field.set(this, null);
            log.debug("{}", field.get(this));
            // Method method = getClass().getDeclaredMethod("setName", String.class);
            // method.invoke(this, "1111");
        }

    }

    @Data
    private static class A {

        private String value;

    }

    @Data
    private static class B {

        private Integer value;

    }

    @Data
    private static class C {

        private List<Integer> list = new ArrayList<>();

    }

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private RelicDao relicDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private WarehouseDao warehouseDao;

    @Test
    void test2() {
        Relic relic = relicDao.selectNotDeletedByRelicId(1007);
        String newWarehouseString = Optional.ofNullable(relic.getWarehouseId())
                .map(i -> warehouseDao.selectNotDeletedById(i))
                .map(Warehouse::getName)
                .orElse("");
        log.debug(newWarehouseString);
    }

    @Test
    void test() {
        User user = userDao.selectNotDeletedById(23);
        User oldUser = ObjectUtil.clone(user);
        log.debug("{}", oldUser);
        Set<Permission> permissionSet = user.getExtraPermissions();
        log.debug("{}", permissionSet);
        Set<Integer> permissionIdSet = mapperFacade.map(permissionSet,
                new TypeBuilder<Set<Permission>>() {
                }.build(),
                new TypeBuilder<Set<Integer>>() {
                }.build());
        log.debug("{}", permissionIdSet);
        Set<Permission> realPermission = mapperFacade.map(permissionIdSet, Set.class);
        log.debug("{}", realPermission);
    }

    @Autowired
    private UserExtraPermissionService userExtraPermissionService;

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, InstantiationException {
        User user = new User();
        user.setName("asdasd");
        user.setWorkId(111);
        user.setTelephone("121212");

        User oldUser = ObjectUtil.clone(user);
        // Set<Integer> realPermissionIdSet = userExtraPermissionService.updateUserExtraPermissions(user.getId(), user.getJobId(), userInfo.getExtraPermissionsId());
        // Set<Permission> realPermission = mapperFacade.map(realPermissionIdSet, Set.class);
        Map a = OperationLogUtil.getDifferenceFieldMap(null, user, User.class);
        log.debug("{}", a);
        // log.debug("{}", OperationLogUtil.getCreateItemDetailLog(a));

        if (true) {
            return;
        }
        Path path = Paths.get("a", "b", "..");
        Path path2 = path
                .resolve("ddd")
                .toAbsolutePath();
        log.debug("{} {}", path, path2);
        if (true) {
            return;
        }
        C c = new C();
        c.getList().addAll(Arrays.asList(1, 2, 3));
        log.debug(String.valueOf(c.getList()));
        BigDecimal bigDecimal = new BigDecimal("0x6");
        log.debug(String.valueOf(bigDecimal));
        // MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        // mapperFactory
        //         .getConverterFactory()
        //         .registerConverter("aaConvert", new AllowEmptyStringFromStringConverter());
        // MapperFacade mapperFacade;
        //
        // mapperFactory.classMap(A.class, B.class)
        //         .fieldMap("value").converter("aaConvert").add()
        //         .byDefault()
        //         .register();
        // Integer e = Integer.valueOf("");
        // mapperFacade = mapperFactory.getMapperFacade();
        // A a = new A();
        // a.setValue("null");
        // B b = mapperFacade.map(a, B.class);
        // log.debug("{}", b.value);
        if (true) {
            return;
        }
        // BigDecimal a = new BigDecimal("1.2222").setScale(2, BigDecimal.ROUND_HALF_UP);
        // log.debug(a.toPlainString());
        // Set<Integer> set = new HashSet<>();
        // set.stream();
        // File file = FileUtil.touch("data/images/1.txt");
        // log.debug(file.getAbsolutePath());
        // FileUtil.writeBytes(new byte[] {47, 52, 53, 56}, file);
        RelicVo relicVo = new RelicVo();
        relicVo.test();
        log.debug(relicVo.toString());

        ImgUtil.convert(new File("D:\\My Documents\\Idea\\Cultural-Relics-Management-System-Backend\\data\\images\\4.png"),
                new File("444.jpg"));
        // try {
        //     int a = 1 / 0;
        //     throw new NotImplementedException();
        // } catch (Exception e) {
        //     log.error("{}", e.getMessage());
        // }
    }

}
