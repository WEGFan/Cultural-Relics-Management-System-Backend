package cn.wegfan.relicsmanagement.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class JobPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 职务编号
     */
    private Integer jobId;

    /**
     * 权限编号
     */
    private Integer permissionId;

}
