package cn.wegfan.relicsmanagement.vo;

import lombok.Data;

import java.util.List;
import java.util.StringJoiner;

@Data
public class UserVo {

    /**
     * 用户编号
     */
    private Integer id;

    /**
     * 工号
     */
    private Integer workId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 职务
     */
    private Integer jobId;

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 权限id列表
     */
    private List<Integer> permissionId;

    // @Override
    // public String toString() {
    //     return new StringJoiner(", ", UserVo.class.getSimpleName() + "[", "]")
    //             .add("id=" + id)
    //             .add("workId=" + workId)
    //             .add("name='" + name + "'")
    //             .add("jobId=" + jobId)
    //             .add("telephone='" + telephone + "'")
    //             .add("permissionId=" + permissionId)
    //             .toString();
    // }

}
