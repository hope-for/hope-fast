package com.hope.controller.base;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.hope.exception.CustomException;
import com.hope.handler.file.UpOssFileHandler;
import com.hope.model.dto.UploadResult;
import com.hope.utils.AjaxResult;
import com.hope.utils.StringUtils;
import com.hope.utils.file.FileUploadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * 文件上传控制类
 *
 * @author aodeng
 */
@RestController
@RequestMapping("fileUpload")
public class FileUploadController {

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Value("${web.upload-path}")
    private String uploadPath;

    @Autowired
    private UpOssFileHandler upOssFileHandler;

    /**
     * 文件上传FTP存储
     *
     * @author aodeng
     */
    @PostMapping("/toFTP")
    @ResponseBody
    public AjaxResult toFTP(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String fileName = RandomUtil.randomNumbers(10) + "_" + file.getOriginalFilename();
            String avatar = FileUploadUtils.upload("/equipment", file, fileName);
            if (StringUtils.isNotEmpty(avatar)) {
                AjaxResult ajax = AjaxResult.success();
                ajax.put("imgUrl", avatar);
                logger.info(avatar);
                return ajax;
            }
        }
        return AjaxResult.error("上传图片异常");
    }

    /**
     * 文件上传本地存储
     *
     * @param file
     * @param request
     * @return
     */
    @PostMapping("/toLocal")
    public String toLocal(MultipartFile file, HttpServletRequest request) {
        String format = DateUtil.format(new Date(), "yyyy/MM/dd");
        File folder = new File(uploadPath + format);
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }

        // 对上传的文件重命名，避免文件重名
        String oldName = file.getOriginalFilename();
        String newName = UUID.randomUUID().toString()
                + oldName.substring(oldName.lastIndexOf("."), oldName.length());
        try {
            // 文件保存
            file.transferTo(new File(folder, newName));

            // 返回上传文件的访问路径
            String filePath = request.getScheme() + "://" + request.getServerName()
                    + ":" + request.getServerPort() + "/" + format + "/" + newName;
            //...
            return filePath;
        } catch (IOException e) {
            throw new CustomException(e.getMessage());
        }

    }

    /**
     * 文件上传又拍云存储
     * @param	file
     * @return java.lang.String
     */
    @PostMapping("/toUpYun")
    public UploadResult toUpYun(MultipartFile file){
        UploadResult uploadResult = upOssFileHandler.upload(file);
        return uploadResult;
    }

    /**
     * 文件删除:又拍云
     * @param	key
     * @return com.hope.utils.AjaxResult
     */
    @DeleteMapping("/delUpYunFile/{key}")
    public AjaxResult delUpYunFile(@PathVariable(value = "key") String key) {
        upOssFileHandler.delete(key);
        return AjaxResult.success();
    }
}
