package cn.wegfan.relicsmanagement.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import java.util.List;
import java.util.StringJoiner;

@Getter
@Setter
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

    public PageResultVo(List<T> content, Page pageResult) {
        this(pageResult);
        this.content = content;
    }

    public PageResultVo(Page<T> pageResult) {
        content = pageResult.getRecords();
        currentPage = Math.toIntExact(pageResult.getCurrent());
        pageSize = Math.toIntExact(pageResult.getSize());
        totalPages = Math.toIntExact(pageResult.getPages());
        totalRecords = Math.toIntExact(pageResult.getTotal());
        hasPrevious = pageResult.hasPrevious();
        hasNext = pageResult.hasNext();
    }

    // @Override
    // public String toString() {
    //     return new StringJoiner(", ", PageResultVo.class.getSimpleName() + "[", "]")
    //             .add("content=" + content)
    //             .add("currentPage=" + currentPage)
    //             .add("pageSize=" + pageSize)
    //             .add("totalPages=" + totalPages)
    //             .add("totalRecords=" + totalRecords)
    //             .add("hasPrevious=" + hasPrevious)
    //             .add("hasNext=" + hasNext)
    //             .toString();
    // }

}
