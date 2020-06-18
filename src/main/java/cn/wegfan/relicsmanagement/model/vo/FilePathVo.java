package cn.wegfan.relicsmanagement.model.vo;

import lombok.Data;

@Data
public class FilePathVo {

    /**
     * 文件地址
     */
    private String filePath;

    public FilePathVo(String filePath) {
        this.filePath = filePath;
    }

}
