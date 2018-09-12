import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @version [版本号, 2018/9/12]
 * @Auther: dingxy
 * @Description:
 * @since [产品/模块版本]
 */
public class Provider {

        public static void main(String[] args) throws Exception {
            //Prevent to get IPV6 address,this way only work in debug mode
            //But you can pass use -Djava.net.preferIPv4Stack=true,then it work well whether in debug mode or not
//        System.setProperty("java.net.preferIPv4Stack", "true");
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"/dubbo-provider.xml"});
            context.start();
            System.out.println("----------------服务已启动，按任意键结束···········--------------------");
            System.in.read(); // press any key to exit
        }


}
