package cn.wegfan.relicsmanagement;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.lang.WeightRandom;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.wegfan.relicsmanagement.entity.*;
import cn.wegfan.relicsmanagement.mapper.*;
import cn.wegfan.relicsmanagement.service.RelicService;
import cn.wegfan.relicsmanagement.util.PasswordUtil;
import com.github.atomfrede.jadenticon.Jadenticon;
import lombok.extern.slf4j.Slf4j;
import org.apache.batik.transcoder.TranscoderException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
@Slf4j
public class TestDataGenerator {

    @Autowired
    private JobDao jobDao;

    @Autowired
    private RelicDao relicDao;

    @Autowired
    private WarehouseDao warehouseDao;

    @Autowired
    private RelicService relicService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ShelfDao shelfDao;

    private final int WAREHOUSE_COUNT = 10;

    private final int SHELF_PER_WAREHOUSE = 20;

    private final int RELIC_COUNT = 1000;

    @Test
    void generateTestData() throws IOException, TranscoderException {
        log.info("generating users");
        generateUser();
        log.info("generating {} warehouses", WAREHOUSE_COUNT);
        generateWarehouse();
        log.info("generating {} shelves", SHELF_PER_WAREHOUSE * WAREHOUSE_COUNT);
        generateShelf();
        log.info("generating {} relics", RELIC_COUNT);
        generateRelic();
    }

    private void generateUser() {
        List<Job> jobList = jobDao.selectJobList();
        for (Job job : jobList) {
            for (int i = 1; i <= 3; i++) {
                User user = new User();
                user.setJobId(job.getId());
                Integer workId = 1000 + 10 * job.getId() + i;
                user.setWorkId(workId);
                user.setName(StrUtil.format("{} #{}", job.getName(), i));
                user.setTelephone("1300000" + workId);
                user.setSalt(PasswordUtil.generateSalt(UUID.fastUUID().toString()));
                user.setPassword(PasswordUtil.encryptPassword("12345678", user.getSalt()));
                user.setCreateTime(new Date());
                user.setUpdateTime(new Date());
                userDao.insert(user);
            }
        }

    }

    private void generateJdenticon(String string, String filename, boolean override) throws IOException, TranscoderException {
        File filePath = Paths.get("data", "images")
                .resolve(filename + ".jpg")
                .toAbsolutePath()
                .toFile();
        FileUtil.mkdir(filePath.getParentFile());

        if (FileUtil.exist(filePath) && !override) {
            return;
        }

        File file = Jadenticon.from(string).withSize(200).png();

        BufferedImage background = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = background.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 200, 200);
        graphics.dispose();

        ImgUtil.pressImage(background, ImageIO.read(file), 0, 0, 1.0f);
        ImgUtil.write(background, filePath);
    }

    private void generateWarehouse() {
        for (int i = 1; i <= WAREHOUSE_COUNT; i++) {
            Warehouse warehouse = new Warehouse();
            warehouse.setName("仓库 #" + i);
            warehouse.setCreateTime(new Date());
            warehouse.setUpdateTime(new Date());
            warehouseDao.insert(warehouse);
        }
    }

    private void generateShelf() {
        for (int i = 1; i <= WAREHOUSE_COUNT; i++) {
            for (int j = 1; j <= SHELF_PER_WAREHOUSE; j++) {
                Shelf shelf = new Shelf();
                shelf.setName("仓库 #" + i + " 货架 #" + j);
                shelf.setWarehouseId(i);
                shelf.setCreateTime(new Date());
                shelf.setUpdateTime(new Date());
                shelfDao.insert(shelf);
            }
        }
    }

    private void generateRelic() throws IOException, TranscoderException {
        for (int i = 0; i < 1000; i++) {
            String randomString = RandomUtil.randomString(64);
            generateJdenticon(randomString, String.format("jdenticon-%03d", i), false);
        }
        List<Relic> relicList = new ArrayList<Relic>();
        for (int i = 1; i <= RELIC_COUNT; i++) {
            if (relicList.size() >= 10000) {
                relicService.saveBatch(relicList, 5000);
                relicList.clear();
            }
            Relic relic = new Relic();

            String name = "文物 #" + i;
            int[] statusWeight = new int[] {1, 5, 1, 1, 1};
            List<WeightRandom.WeightObj<Integer>> weightObjList = IntStream.rangeClosed(1, 5)
                    .mapToObj(j -> new WeightRandom.WeightObj<Integer>(j, statusWeight[j - 1]))
                    .collect(Collectors.toList());

            int statusId = RandomUtil.weightRandom(weightObjList).next();

            final long millisecondsPerHour = 60 * 60 * 1000;
            long baseTime = new Date().getTime() - 10 * millisecondsPerHour;

            relic.setStatusId(statusId);
            relic.setPicturePath(String.format("/api/files/relics/images/jdenticon-%03d.jpg", i % 1000));

            if (statusId == 1) {
                Date createTime = new Date(baseTime - RandomUtil.randomLong(2 * millisecondsPerHour));
                relic.setCreateTime(createTime);
                relic.setUpdateTime(createTime);
                relicList.add(relic);
                continue;
            }

            relic.setName(name);
            relic.setCreateTime(new Date(baseTime - RandomUtil.randomLong(2 * millisecondsPerHour)));
            relic.setUpdateTime(new Date(baseTime + RandomUtil.randomLong(7 * millisecondsPerHour, 9 * millisecondsPerHour)));
            relic.setComment1(name + "[备注1]");
            relic.setComment2(name + "[备注2]");
            relic.setQuantity(i);
            relic.setSize(String.format("%d x %d x %d", i, i, i));
            relic.setYear(name + "[年代]");
            relic.setReign(name + "[年号]");
            relic.setType(name + "[器型]");
            relic.setSource(name + "[来源]");
            relic.setWeight(RandomUtil.randomInt(1000000) / 100.0);
            relic.setEnterPrice(new BigDecimal(i * 100));

            switch (relic.getStatusId()) {
                case 2:
                    relic.setShelfId(i % (WAREHOUSE_COUNT * SHELF_PER_WAREHOUSE) + 1);
                    relic.setWarehouseId((relic.getShelfId() - 1) / SHELF_PER_WAREHOUSE + 1);

                    relic.setEnterTime(new Date(baseTime + RandomUtil.randomLong(2 * millisecondsPerHour, 3 * millisecondsPerHour)));
                    relic.setMoveTime(new Date(baseTime + RandomUtil.randomLong(4 * millisecondsPerHour, 5 * millisecondsPerHour)));
                    break;
                case 3:
                    relic.setLendTime(new Date(baseTime + RandomUtil.randomLong(3 * millisecondsPerHour, 4 * millisecondsPerHour)));
                    break;
                case 4:
                    relic.setFixTime(new Date(baseTime + RandomUtil.randomLong(5 * millisecondsPerHour, 6 * millisecondsPerHour)));
                    break;
                case 5:
                    relic.setLeaveTime(new Date(baseTime + RandomUtil.randomLong(6 * millisecondsPerHour, 8 * millisecondsPerHour)));
                    relic.setLeavePrice(new BigDecimal(i * 50));
                    break;
                default:
                    break;
            }

            relicList.add(relic);
        }
        relicService.saveBatch(relicList);
    }

}
