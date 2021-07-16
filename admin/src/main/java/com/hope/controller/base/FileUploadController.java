package com.hope.controller.base;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.hope.exception.CustomException;
import com.hope.utils.AjaxResult;
import com.hope.utils.StringUtils;
import com.hope.utils.file.FileUploadUtils;
import com.upyun.RestManager;
import com.upyun.UpException;
import okhttp3.Response;
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
@RequestMapping("fileUpload")
public class FileUploadController {

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Value("${web.upload-path}")
    private String uploadPath;

    @Value("${upyun.BUCKET_NAME}")
    private String BUCKET_NAME;

    @Value("${upyun.OPERATOR_NAME}")
    private String OPERATOR_NAME;

    @Value("${upyun.OPERATOR_PWD}")
    private String OPERATOR_PWD;

    @Value("${upyun.YP_PATH}")
    private String YP_PATH;

    /**
     * 文件上传FTP存储
     *
     * @author aodeng
     */
    @PostMapping("/uploadFileToFTP")
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
     *
     * @param file
     * @param request
     * @return
     */
    @PostMapping("/uploadToLocal")
    public String uploadToLocal(MultipartFile file,
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
     * @param	request
     * @return java.lang.String
     */
    @PostMapping("/uploadToUpYun")
    public String uploadToUpYun(MultipartFile file,
                                HttpServletRequest request) throws IOException, UpException {
        RestManager manager = new RestManager(BUCKET_NAME, OPERATOR_NAME, OPERATOR_PWD);
        String path=YP_PATH;
        Response response=manager.writeFile(path, (File) file,null);
        return response.body().string();
    }
}
