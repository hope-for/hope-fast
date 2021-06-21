package com.hope.controller;

/**
 * @className: Test2Controller
 * @Description: TODO 个人测试代码 可删除 双重校验锁实现对象单理（线程安全）
 * @version: v1.0.0
 * @author: 低调小熊猫
 * @date: 17:35 21-6-17
 */
public class Test2Controller {

    private volatile static Test2Controller test2Controller;

    private Test2Controller(){
    }

    public static Test2Controller getTest2Controller(){
        if (test2Controller==null){
            synchronized (Test2Controller.class){
                if (test2Controller==null){
                    test2Controller=new Test2Controller();
                }
            }
        }
        return test2Controller;
    }
}
