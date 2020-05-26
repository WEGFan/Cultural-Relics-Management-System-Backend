# Cultural Relics Management System Backend

软件工程7组 文物管理系统后端

## TODO

- 接口
  - 用户管理
    - [X] 用户登录 <3.4.1>
    - [X] 用户退出登录 <3.4.1>
    - [x] 修改用户密码
    - [X] 获取所有用户信息【管理员】
    - [X] 获取所有职务 <3.1>
    - [X] 获取所有权限 <3.1>
    - [ ] 修改用户信息【管理员】 <3.3.2> 差修改密码后清除用户的session缓存
    - [X] 增加新用户【管理员】 <3.3.1>
    - [ ] 删除用户【管理员】 <3.3.3> 差清除用户的session缓存
    - [ ] 导出用户信息 Excel 表【管理员】<3.2.3 (1)>
  - 文物管理
    - [X] 获取所有文物状态
    - [X] 获取所有文物信息
    - [X] 获取文物详细信息
    - [X] 删除文物
    - [X] 创建文物（拍照上传）【拍照人员】 <3.2.1>
    - [X] 修改文物详细信息【文职员工】 <3.2.2/7>
    - [X] 入馆后修改文物状态信息/移动【仓库管理员】 <3.2.4/5/6>
    - [X] 修改文物价值【资产科】 <3.2.9>
    - [ ] 导出文物一览 Excel 表【文职人员（无价值）、管理员】 <3.2.3 (2)>
    - [ ] 导出某仓库文物一览 Excel 表【仓库管理员、管理员】 <3.2.3 (3)>
    - [ ] 导出文物流水 Excel 表【管理员】 <3.2.3 (4)>
  - 仓库管理
    - [x] 获取所有仓库信息【仓库管理员】
    - [X] 创建仓库【仓库管理员】
    - [X] 修改仓库信息【仓库管理员】
    - [X] 删除仓库【仓库管理员】
  - 货架管理
    - [X] 获取某仓库的所有货架【仓库管理员】
    - [X] 创建货架【仓库管理员】
    - [X] 修改货架信息【仓库管理员】
    - [X] 删除货架【仓库管理员】
  - 文物盘点
    - [ ] 获取盘点列表
    - [ ] 获取某次盘点的文物列表
    - [ ] 开始一次盘点
    - [ ] 结束当前盘点
    - [ ] 提交当前文物位置和状态信息
    - [ ] 导出某次盘点的文物 Excel 表
  - 其他
    - [ ] 获取所有数据库备份信息【管理员】
    - [ ] 数据库备份【管理员】
    - [ ] 数据库恢复【管理员】
    - [ ] 导出操作动态 Excel 表【管理员】 <3.2.3 (5)>
  - [ ] 日志
  - [ ] swagger
  - shiro
    - [X] 登录
    - [X] 权限
    - [ ] 集成redis
    - [ ] 修改用户信息后清除缓存
    - [ ] 持久化
  - [ ] redis
  - [ ] 单元测试
  - [ ] 代码清理
  - [ ] 数据校验

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
