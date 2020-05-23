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
