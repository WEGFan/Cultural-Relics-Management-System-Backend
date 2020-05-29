# Cultural Relics Management System Backend

软件工程7组 文物管理系统后端

<https://relics.wegfan.cn/>

## 踩到的坑

- mybatis plus 中 mapper 继承 basemapper 后，如果自己写的方法跟 basemapper 中的重名，会导致自己写的方法有概率失效
- mybatis 注解 @ResultMap，如果 @Result 里映射一对一/一对多关系用到了 column，则需要多写一个 @Result 手动实现数据库列名到属性的映射

```java
public interface RelicCheckDao extends BaseMapper<RelicCheck> {
    @Results(id = "relicCheckResultMap", value = {
            @Result(property = "warehouseId", column = "warehouse_id"), // 不能省略
            @Result(property = "warehouse", column = "warehouse_id", javaType = Warehouse.class,
                    one = @One(select = "cn.wegfan.relicsmanagement.mapper.WarehouseDao.selectByWarehouseId",
                            fetchType = FetchType.EAGER))
    })
    Page<RelicCheck> selectPageByWarehouseId(Page<?> page, Integer warehouseId);
}
```

- springboot 的数据库连接池默认为 10
- 先用全为 String 的 DTO 接收从前端发来的值，数据校验后再进行转换
- shiro 默认 session 失效时间为 30 分钟
- mybatis 的模糊查找要转义 `%_\`
- mybatis plus 分页插件，`count` 为负数的时候返回所有结果
