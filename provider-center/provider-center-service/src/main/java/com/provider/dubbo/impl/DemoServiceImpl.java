package com.provider.dubbo.impl;

import com.alibaba.dubbo.rpc.RpcContext;
import com.dubbo.interfaces.DemoService;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @version [版本号, 2018/9/12]
 * @Auther: dingxy
 * @Description:
 * @since [产品/模块版本]
 */
public class DemoServiceImpl implements DemoService {

    public String sayHello(String name) {

      System.out.println("[" + new SimpleDateFormat("HH:mm:ss")
              .format(new Date()) + "] Hello " + name + ", request from consumer: " + RpcContext.getContext().getRemoteAddress());
        return "Hello Dubbo";
    }
}
