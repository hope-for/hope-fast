package com.hope.handler.file;

import com.hope.exception.FileOperationException;
import com.hope.model.dto.UploadResult;
import com.hope.model.properties.UpYunProperties;
import com.hope.support.AttachmentTypeEnum;
import com.hope.utils.FilenameUtils;
import com.hope.utils.ImageUtils;
import com.upyun.RestManager;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Up oss file handler.
 *
 * @author AoDeng
 * @date 13:45 21-7-19
 */
@Slf4j
@Component
public class UpOssFileHandler implements FileHandler{

    @Autowired
    private UpYunProperties upYunProperties;

    @Override
    public UploadResult upload(MultipartFile file) {
        Assert.notNull(file, "Multipart file must not be null");

        RestManager manager=new RestManager(upYunProperties.getBUCKET_NAME(), upYunProperties.getOPERATOR_NAME(), upYunProperties.getOPERATOR_PWD());
        manager.setTimeout(60*10);
        manager.setApiDomain(RestManager.ED_AUTO);

        Map<String,String> params=new HashMap<>();

        try {
            // Get file basename
            String basename = FilenameUtils.getBasename(Objects.requireNonNull(file.getOriginalFilename()));
            // Get file extension
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            // Get md5 value of the file
            String md5OfFile = DigestUtils.md5DigestAsHex(file.getInputStream());
            // Build file path
            String upFilePath = StringUtils.appendIfMissing(upYunProperties.getYP_FILE_PATH(), "/") + md5OfFile + '.' + extension;
            // Set md5Content
            params.put(RestManager.PARAMS.CONTENT_MD5.getValue(), md5OfFile);
            // Write file
            Response result = manager.writeFile(upFilePath, file.getInputStream(), params);
            if (!result.isSuccessful()){
                throw new FileOperationException("上传附件 " + file.getOriginalFilename() + " 到又拍云失败" + upFilePath);
            }

            String filePath =upYunProperties.getYP_PROTOCOL() + StringUtils.removeEnd(upYunProperties.getYP_DOMAN(), "/") + upFilePath;

            // Build upload result
            UploadResult uploadResult = new UploadResult();
            uploadResult.setFilename(basename);
            uploadResult.setFilePath(filePath);
            uploadResult.setKey(upFilePath);
            uploadResult.setMediaType(MediaType.valueOf(Objects.requireNonNull(file.getContentType())));
            uploadResult.setSuffix(extension);
            uploadResult.setSize(file.getSize());

            // Handle thumbnail
            handleImageMetadata(file, uploadResult, () -> {
                if (ImageUtils.EXTENSION_ICO.equals(extension)) {
                    uploadResult.setThumbPath(filePath);
                    return filePath;
                } else {
                    return filePath;
                }
            });

            //TODO 系统附件上传日志记录...

            return uploadResult;

        }catch (Exception e){
            throw new FileOperationException("上传附件 " + file.getOriginalFilename() + " 到又拍云失败", e);
        }
    }

    @Override
    public void delete(String key) {
        Assert.notNull(key, "File key must not be blank");

        RestManager manager=new RestManager(upYunProperties.getBUCKET_NAME(), upYunProperties.getOPERATOR_NAME(), upYunProperties.getOPERATOR_PWD());
        manager.setTimeout(60 * 10);
        manager.setApiDomain(RestManager.ED_AUTO);

        try {
            Response result = manager.deleteFile(key, null);
            if (!result.isSuccessful()) {
                log.warn("附件 " + key + " 从又拍云删除失败");
                throw new FileOperationException("附件 " + key + " 从又拍云删除失败");
            }

            //TODO 系统附件删除日志记录...

        } catch (Exception e) {
            e.printStackTrace();
            throw new FileOperationException("附件 " + key + " 从又拍云删除失败", e);
        }
    }

    @Override
    public AttachmentTypeEnum getAttachmentType() {
        return AttachmentTypeEnum.UPOSS;
    }
}
