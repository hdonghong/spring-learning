package pers.hdh.e_configure.config.bean;

/**
 * MyBean1 class<br/>
 *
 * @author hdonghong
 * @date 2018/06/23
 */
public class MyBean1 {
    public MyBean1(MyBean2 myBean2) {
        System.out.println("1 依赖 2");
    }


}
