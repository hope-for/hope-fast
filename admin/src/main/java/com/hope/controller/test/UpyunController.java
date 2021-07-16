package com.hope.controller.test;

import com.upyun.RestManager;
import com.upyun.UpException;
import okhttp3.Response;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * TODO 又拍云测试类
 *
 * @author AoDeng
 * @date 16:42 21-7-14
 */
@RestController
public class UpyunController {

    private static final String BUCKET_NAME = "ddxxm-aodeng";
    private static final String OPERATOR_NAME = "aodeng";
    private static final String OPERATOR_PWD = "hdsBLUHVy9ERg3LuoEjG0TCs0sa06ReV";

    public static void main(String[] args) throws IOException, UpException {
        RestManager manager = new RestManager(BUCKET_NAME, OPERATOR_NAME, OPERATOR_PWD);
        String path="/image/";
        Response response = manager.readDirIter(path,null);
        System.out.println(response.body().string());
    }
}
