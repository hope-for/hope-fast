package com.hope.model.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 杂项管理表
 * 一些业务配置，经常变更的配置，存数据库，避免发版-方便维护
 *
 * @author aodeng
 */
@Data
@TableName("t_m_cls")
public class TmConfig {

    private Integer id;
    private String itemFirstCls;
    private String itemSecondCls;
    private String itemValue;
    private String remark1;
    private String remark2;
    private String remark3;
    private String remark4;
}