package cn.wegfan.relicsmanagement.util;

public class PermissionCodeEnum {

    /**
     * 管理员
     */
    public static final String ADMIN = "admin";

    /**
     * 创建、修改、删除仓库
     */
    public static final String WAREHOUSE = "warehouse";

    /**
     * 拍照创建文物
     */
    public static final String ADD_RELIC = "relic:add";

    /**
     * 查看文物详细信息
     */
    public static final String VIEW_RELIC_INFO = "relic:info:view";

    /**
     * 修改文物详细信息
     */
    public static final String EDIT_RELIC_INFO = "relic:info:edit";

    /**
     * 文物入馆
     */
    public static final String RELIC_ENTER_MUSEUM = "relic:status:enter";

    /**
     * 文物入馆、外借、送修、离馆
     */
    public static final String EDIT_RELIC_STATUS = "relic:status:edit";

    /**
     * 查看、修改文物价值信息
     */
    public static final String VIEW_EDIT_RELIC_PRICE = "relic:price";

    /**
     * 盘点文物
     */
    public static final String CHECK_RELIC = "relic:check";

    /**
     * 移动文物
     */
    public static final String MOVE_RELIC = "relic:move";

    /**
     * 查询文物盘点记录
     */
    public static final String EXPORT_RELIC_CHECK_LOG = "relic:export:check";

    /**
     * 查询、导出文物一览表
     */
    public static final String EXPORT_RELIC = "relic:export:relics";

    /**
     * 查询、导出某仓库文物一览表
     */
    public static final String EXPORT_WAREHOUSE_RELIC = "relic:export:warehouse";

    /**
     * 查询、导出文物流水表
     */
    public static final String EXPORT_RELIC_CHANGE_LOG = "relic:export:changes";

}
// public enum PermissionCodeEnum {
//
//     Admin("admin", "管理员"),
//     Warehouse("warehouse", "创建、修改、删除仓库"),
//     AddRelic("relic:add", "拍照创建文物"),
//     ViewRelicInfo("relic:info:view", "查看文物详细信息"),
//     EditRelicInfo("relic:info:edit", "修改文物详细信息"),
//     EditRelicStatus("relic:status:edit", "文物入库、外借、送修、离馆"),
//     ViewEditRelicPrice("relic:price", "查看、修改文物价值信息"),
//     CheckRelic("relic:check", "盘点文物"),
//     MoveRelic("relic:move", "移动文物"),
//     ExportRelicCheckLog("relic:export:check", "查询文物盘点记录"),
//     ExportRelic("relic:export:relics", "查询、导出文物一览表"),
//     ExportWarehouseRelic("relic:export:warehouse", "查询、导出某仓库文物一览表"),
//     ExportRelicChangeLog("relic:export:changes", "查询、导出文物流水表");
//
//     private final String code;
//
//     private final String name;
//
//     PermissionCodeEnum(String code, String name) {
//         this.code = code;
//         this.name = name;
//     }
//
// }
