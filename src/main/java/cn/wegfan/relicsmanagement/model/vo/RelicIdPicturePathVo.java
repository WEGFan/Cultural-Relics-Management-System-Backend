package cn.wegfan.relicsmanagement.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RelicIdPicturePathVo {

    /**
     * 文物编号
     */
    private Integer id;

    /**
     * 照片地址
     */
    private String picturePath;

}
