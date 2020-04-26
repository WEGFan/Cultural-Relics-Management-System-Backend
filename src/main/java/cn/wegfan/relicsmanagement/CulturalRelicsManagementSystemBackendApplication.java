package cn.wegfan.relicsmanagement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.wegfan.relicsmanagement.mapper")
public class CulturalRelicsManagementSystemBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CulturalRelicsManagementSystemBackendApplication.class, args);
    }

}
