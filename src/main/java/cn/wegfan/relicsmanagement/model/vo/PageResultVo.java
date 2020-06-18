package cn.wegfan.relicsmanagement.model.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 接口返回的分页对象
 */
@Getter
@Setter
@ToString
public class PageResultVo<T> {

    /**
     * 内容
     */
    private List<T> content;

    /**
     * 当前页码
     */
    private Integer currentPage;

    /**
     * 每页个数
     */
    private Integer pageSize;

    /**
     * 总共页数
     */
    private Integer totalPages;

    /**
     * 总共条数
     */
    private Integer totalRecords;

    /**
     * 是否有上一页
     */
    private Boolean hasPrevious;

    /**
     * 是否有下一页
     */
    private Boolean hasNext;

    /**
     * 使用 {@code content} 作为内容，{@code pageResult} 作为分页属性
     */
    @SuppressWarnings("unchecked")
    public PageResultVo(List<T> content, Page<?> pageResult) {
        this((Page<T>)pageResult);
        this.content = content;
    }

    /**
     * 使用 {@code pageResult} 作为内容和分页属性
     */
    public PageResultVo(Page<T> pageResult) {
        content = pageResult.getRecords();
        currentPage = Math.toIntExact(pageResult.getCurrent());
        pageSize = Math.toIntExact(pageResult.getSize());
        totalPages = Math.toIntExact(pageResult.getPages());
        totalRecords = Math.toIntExact(pageResult.getTotal());
        hasPrevious = pageResult.hasPrevious();
        hasNext = pageResult.hasNext();
    }

}
