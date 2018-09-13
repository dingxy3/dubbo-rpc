package com.consumer.dubbo.impl;

import com.dubbo.interfaces.DemoService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @version [版本号, 2018/9/12]
 * @Auther: dingxy
 * @Description:
 * @since [产品/模块版本]
 */
public class Consumer {
    public static void main(String[] args) {
        //测试常规服务
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("dubbo-consumer.xml");
        context.start();
        System.out.println("consumer start");
        DemoService demoService = context.getBean(DemoService.class);

       String abc = demoService.sayHello("123");

       System.out.println(abc);
    }
}
