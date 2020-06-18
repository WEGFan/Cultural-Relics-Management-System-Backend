package cn.wegfan.relicsmanagement.model.vo;

import lombok.Data;

@Data
public class RelicIdPicturePathVo {

    /**
     * 文物编号
     */
    private Integer id;

    /**
     * 照片地址
     */
    private String picturePath;

    public RelicIdPicturePathVo(Integer id, String picturePath) {
        this.id = id;
        this.picturePath = picturePath;
    }

}
