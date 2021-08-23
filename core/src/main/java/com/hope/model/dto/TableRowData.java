package com.hope.model.dto;

import lombok.Data;

/**
 * 导出excel文件流实体
 *
 * @author AoDeng
 * @date 10:30 21-8-23
 */
@Data
public class TableRowData {
    //列1
    private String Column1;
    //列2
    private String Column2;
    //列3...
    private String Column3;

}
