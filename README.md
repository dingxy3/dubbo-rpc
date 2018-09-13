### 部署：
   将代码导入到idea下。
   启动zk  windows下 zkserver.cmd
   运行Provider.java的main方法启动服务提供方
   运行Consumer.java看看是否消费到服务了

### 代码结构：
```
 dubbo-rpc
      consumer-center
      provider-center
           provider-center-api
          provider-center-service
```

其中分为dubbo消费者（consumer-center）和服务提供者provider-center


### 三者关系：
可以查看maven里面pom.xml的配置

1、provider-center-service依赖provider-center-api也就是接口中心，并且实现provider-center-api模块下的接口，是真正服务的实现者。
2、consumer-center依赖provider-center-api也就是接口中心，并没有去依赖真正的实现类，用的时候直接调用接口，但是你会发现也能拿到方法的返回结果。

### 这样为什么叫分布式的？

  1、  项目部署的时候可以把provider-center-service部署在A机器上，
consumer-center部署在另外B机器上。
2、consumer-center只要依赖provider-center-api即接口就行，就可以调用provider-center-service上真正的实现类



### Xml配置

请查看dubbo-provider.xml 和dubbo-consumer.xml文件看看服务提供者和消费者怎么配置的。





### 为什么调用接口就行就能拿到真正方法返回值，原理观察dubbo服务注册情况：
1、	查看zookeeper下节点，
发现多出一个dubbo；
继续查看dubbo发现多出com.interfaces.DemoService节点：
![zk-pic](/image/zk1.png)
2、	继续查看com.interfaces.DemoService节点，发现下面有[consumers, configurators, routers, providers]这些节点
继续查看providers就是我们的服务提供方，发现这个节点下的节点就是
我们接口注册的服务请求地址方法信息

![zk-pic1](/image/zk2.png)

