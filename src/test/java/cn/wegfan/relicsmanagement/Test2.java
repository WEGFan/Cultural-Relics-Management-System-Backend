package cn.wegfan.relicsmanagement;

import cn.hutool.core.img.ImgUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Slf4j
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

        public void test() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

            Method[] methods = getClass().getDeclaredMethods();
            Method method = getClass().getDeclaredMethod("setName", String.class);
            method.invoke(this, "1111");
        }

    }

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Set<Integer> set = new HashSet<>();
        set.stream();
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
