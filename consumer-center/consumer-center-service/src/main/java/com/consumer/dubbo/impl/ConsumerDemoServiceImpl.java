package com.consumer.dubbo.impl;

import com.dubbo.api.interfaces.ConsumerDemoService;
import com.dubbo.interfaces.DemoService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @version [版本号, 2018/9/13]
 * @Auther: dingxy
 * @Description:
 * @since [产品/模块版本]
 */
public class ConsumerDemoServiceImpl implements ConsumerDemoService {

    @Autowired
    private DemoService demoService;

    public void sayHello(String s) {
      demoService.sayHello("123");
    }
}
