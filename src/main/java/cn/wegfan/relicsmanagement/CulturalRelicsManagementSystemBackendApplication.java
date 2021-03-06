package cn.wegfan.relicsmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class CulturalRelicsManagementSystemBackendApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(CulturalRelicsManagementSystemBackendApplication.class);
        ApplicationContext context = application.run(args);
    }

}
