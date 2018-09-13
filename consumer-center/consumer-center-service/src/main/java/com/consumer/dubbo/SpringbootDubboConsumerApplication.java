package com.consumer.dubbo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * @version [版本号, 2018/9/13]
 * @Auther: dingxy
 * @Description:
 * @since [产品/模块版本]
 */
@SpringBootApplication
@ImportResource("dubbo-consumer.xml")
@ComponentScan(basePackages = {"com.dubbo","com.consumer.dubbo"})
public class SpringbootDubboConsumerApplication {
    private static Logger logger = LoggerFactory.getLogger(SpringbootDubboConsumerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringbootDubboConsumerApplication.class,args);
        logger.error("恭喜你启动成功了！--SpringbootDubboConsumerApplication 启动成功...");
    }
}
