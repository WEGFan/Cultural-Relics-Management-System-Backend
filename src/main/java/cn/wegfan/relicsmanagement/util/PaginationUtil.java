package cn.wegfan.relicsmanagement.util;

/**
 * 分页工具类
 */
public class PaginationUtil {

    /**
     * 将每页个数固定在 1 到 20 之间
     *
     * @param pageCount 每页个数
     */
    public static long clampPageCount(long pageCount) {
        final long maxPageCount = 20;
        return Math.max(Math.min(pageCount, maxPageCount), 1);
    }

}
