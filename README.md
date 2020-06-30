# Cultural Relics Management System Backend

文物管理系统后端

项目地址：<https://relics.wegfan.cn/>  
帐号密码：`1` / `admin123`

## 部署

1. 安装 [OpenJDK 8](https://openjdk.java.net/)、[MySQL 5.7](https://www.mysql.com/)、[Maven 3.6+](https://maven.apache.org/)
2. 克隆代码 `git clone https://github.com/WEGFan/Cultural-Relics-Management-System-Backend && cd Cultural-Relics-Management-System-Backend`
3. 建数据库 `mysql -u root -p <sql/relics_manage_sys.sql`
4. 打包 `mvn clean package`

## 生成测试数据

`mvn test -Dtest=TestDataGenerator -DskipTests=false`

## 运行

`java -jar ./target/relicsmanagement-1.0.0.jar`
