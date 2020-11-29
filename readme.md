# 简单旅行预订系统

功能:

- 航班、大巴、宾馆、客户的数据管理
- 预订/取消预订
- 查询客户的预订信息，并检查路线完整性

## 运行

在运行程序之前请确保导入数据库(`simple_travel_reservation.sql`)到MySQL8.0以上，

并且确保`src/main/java/com/raiix/travelreservationsystem/MySQLDriver.java`中用户名`USER`变量和密码`PWD`变量设置正确。

```bash
mvn clean package
java -jar target/TravelReservationSystem-1.0-SNAPSHOT.jar
```
