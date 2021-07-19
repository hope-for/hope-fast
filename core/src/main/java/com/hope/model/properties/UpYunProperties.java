package com.hope.model.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author AoDeng
 * @date 11:17 21-7-19
 */
@Data
@Component
@ConfigurationProperties(prefix = "upyun")
public class UpYunProperties {

    /**
     * 空间名称
     */
    private String BUCKET_NAME;
    /**
     * 操作员名称
     */
    private String OPERATOR_NAME;
    /**
     * 操作员密码
     */
    private String OPERATOR_PWD;
    /**
     * 图片上传路径
     */
    private String YP_IMAGE_PATH;
    /**
     * 其他文件上传路径
     */
    private String YP_FILE_PATH;

}