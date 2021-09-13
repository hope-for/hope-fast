package com.hope.controller.base;

import cn.hutool.core.date.DateUtil;
import com.hope.exception.*;
import com.hope.support.HttpStatus;
import com.hope.utils.AjaxResult;
import com.upyun.UpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常
 *
 * @author aodeng
 */
@ControllerAdvice
public class BizController {

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(BizController.class);

    /**
     * 业务异常
     */
    @ExceptionHandler(value = CustomException.class)
    @ResponseBody
    public AjaxResult biz(HttpServletRequest req, Exception e) {
        logger.error("发生自定义业务异常！\n原因是：[{}",
                e.getMessage() + "]-[具体原因查看下面详细打印,当前时间是:" + DateUtil.date() + "]");
        e.printStackTrace();
        return AjaxResult.error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(value = TokenException.class)
    @ResponseBody
    public AjaxResult biz1(HttpServletRequest req, Exception e) {
        logger.error("发生Token校验异常！\n原因是：[{}",
                e.getMessage() + "]-[具体原因查看下面详细打印,当前时间是:" + DateUtil.date() + "]");
        e.printStackTrace();
        return AjaxResult.error(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(value = CaptchaException.class)
    @ResponseBody
    public AjaxResult biz2(HttpServletRequest req, Exception e) {
        logger.error("发生图片验证码校验异常！\n原因是：[{}",
                e.getMessage() + "]-[具体原因查看下面详细打印,当前时间是:" + DateUtil.date() + "]");
        e.printStackTrace();
        return AjaxResult.error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(value = ImageFormatException.class)
    @ResponseBody
    public AjaxResult biz3(HttpServletRequest req, Exception e) {
        logger.error("发生图片格式校验异常！\n原因是：[{}",
                e.getMessage() + "]-[具体原因查看下面详细打印,当前时间是:" + DateUtil.date() + "]");
        e.printStackTrace();
        return AjaxResult.error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(value = FileOperationException.class)
    @ResponseBody
    public AjaxResult biz4(HttpServletRequest req, Exception e) {
        logger.error("发生文件异常！\n原因是：[{}",
                e.getMessage() + "]-[具体原因查看下面详细打印,当前时间是:" + DateUtil.date() + "]");
        e.printStackTrace();
        return AjaxResult.error(HttpStatus.BAD_REQUEST, e.getMessage());
    }


    /**
     * 业务异常
     */
    @ExceptionHandler(value = UpException.class)
    @ResponseBody
    public AjaxResult biz5(HttpServletRequest req, Exception e) {
        logger.error("又拍云SDK操作异常！\n原因是：[{}",
                e.getMessage() + "]-[具体原因查看下面详细打印,当前时间是:" + DateUtil.date() + "]");
        e.printStackTrace();
        return AjaxResult.error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    /**
     * 系统未捕获的异常
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public AjaxResult biz100(HttpServletRequest req, Exception e) {
        logger.error("发生系统未捕获的异常！\n原因是：[{}",
                e.getMessage() + "]-[具体原因查看下面详细打印,当前时间是:" + DateUtil.date() + "]");
        e.printStackTrace();
        return AjaxResult.error(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
