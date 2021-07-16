package com.hope.model.support;

import lombok.Data;
import lombok.ToString;
import org.springframework.http.MediaType;

/**
 * Upload result dto.
 *
 * @author AoDeng
 * @date 16:20 21-7-16
 */
@Data
@ToString
public class UploadResult {

    private String fileName;

    private String filePath;

    private String key;

    private String thumbPath;

    private String suffix;

    private MediaType mediaType;

    private Long size;

    private Integer width;

    private Integer height;

}
