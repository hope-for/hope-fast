package com.hope.controller.base;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.hope.exception.CustomException;
import com.hope.utils.AjaxResult;
import com.hope.utils.StringUtils;
import com.hope.utils.file.FileUploadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
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
@RequestMapping("file")
public class FileController {

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    //绑定文件上传路径到uploadPath
    @Value("${web.upload-path}")
    private String uploadPath;

    /**
    * 文件上传FTP
    * @author aodeng
    */
    @PostMapping("/uploadFileFTP")
    @ResponseBody
    public AjaxResult uploadFileFTP(MultipartFile file) throws IOException {
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
     * @param file
     * @param request
     * @return
     */
    @PostMapping("/uploadLocal")
    public String upload(MultipartFile file,
                         HttpServletRequest request) {
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
                    + ":" + request.getServerPort()  +"/"+ format +"/"+ newName;
            //...
            return filePath;
        } catch (IOException e) {
            throw new CustomException(e.getMessage());
        }

    }
}
