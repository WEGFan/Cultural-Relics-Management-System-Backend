package cn.wegfan.relicsmanagement.util;

public class PaginationUtil {

    public static long clampPageCount(long pageCount) {
        final long maxPageCount = 20;
        return Math.max(Math.min(pageCount, maxPageCount), 1);
    }

}
