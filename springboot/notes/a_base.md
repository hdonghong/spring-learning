# SpringBoot2学习笔记（一）SpringBoot基础入门


> 看完了Spring Boot 2精髓这本书，打算写一系列Spring Boot的文章做下总结。这本书在网上的评价偏低，其中作者常推销自己的轮子是一方面原因，但我认为它是一本快速入门学习Spring Boot 2的好书，对我的帮助蛮大的。

# 一、创建SpringBoot项目
进入Spring官网：https://start.spring.io/ 使用Initalizr创建也可在idea中直接使用Initalizr插件创建。这里我们添加Web依赖（内置tomcat、springmvc）和DevTools依赖（用于热部署）并选择2.0.3发行版。
![这里写图片描述](https://img-blog.csdn.net/20180617124310730?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2hvbmhvbmcxMDI0/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

使用idea打开项目后可以看到XxxApplication类，这个是Spring Boot项目的入口类，通过 `@SpringBootApplication` 注解标识。
```
@SpringBootApplication
public class ABaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(ABaseApplication.class, args);
	}
}
```


# 二、编写Controller

现在我们创建一个HelloController。
```
@RestController
public class HelloController {

    @GetMapping("/hello/{id}")
    public String hello(@PathVariable("id") Integer id) {
        return "componentBean = " + componentBean + ", id = " + id;
    }
}
```
@RestController：SpringBoot用于支持Rest服务，相当于SpringMVC的@Controller + @ResponseBody。
@GetMapping：SpringBoot简化SpringMVC的RequestMapping(method=RequestMethod.GET)，此外还有 `PostMapping` 、`PutMapping` 、`DeleteMapping` 、`PatchMapping` 。

# 三、最后
至此，一个SpringBoot项目创建完毕，项目代码可至此获取：[传送门](https://github.com/hdonghong/spring-learning/tree/master/springboot/a_base)

另外，若想在启动时看到自己设计的一些有趣的banner或者公司的Logo，而不是Spring自己的，可在项目resources/目录下新建banner.txt，写下自己的东西，Spring Boot检查到后会自行替换默认的banner。

附上自己用的：
```
${AnsiColor.BRIGHT_YELLOW}  
////////////////////////////////////////////////////////////////////  
//                          _ooOoo_                               //  
//                         o8888888o                              //  
//                         88" . "88                              //  
//                         (| ^_^ |)                              //  
//                         O\  =  /O                              //  
//                      ____/`---'\____                           //  
//                    .'  \\|     |//  `.                         //  
//                   /  \\|||  :  |||//  \                        //  
//                  /  _||||| -:- |||||-  \                       //  
//                  |   | \\\  -  /// |   |                       //  
//                  | \_|  ''\---/''  |   |                       //  
//                  \  .-\__  `-`  ___/-. /                       //  
//                ___`. .'  /--.--\  `. . ___                     //  
//              ."" '<  `.___\_<|>_/___.'  >'"".                  //  
//            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //  
//            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //  
//      ========`-.____`-.___\_____/___.-`____.-'========         //  
//                           `=---='                              //  
//      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
//            佛祖保佑       永不宕机     永无BUG                    //
////////////////////////////////////////////////////////////////////  
```

博客地址：https://blog.csdn.net/honhong1024/article/details/80718116