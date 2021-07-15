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

    public static void main(String[] args) throws IOException, UpException {
        RestManager manager = new RestManager("ddxxm-aodeng", "aodeng",
                "hdsBLUHVy9ERg3LuoEjG0TCs0sa06ReV");
        String path="/image/";
        Response response = manager.readDirIter(path,null);
        System.out.println(response.body().string());
    }
}
