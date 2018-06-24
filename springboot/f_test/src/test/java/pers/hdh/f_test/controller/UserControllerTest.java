package pers.hdh.f_test.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pers.hdh.f_test.service.UserService;

import javax.servlet.http.Cookie;

import static org.junit.Assert.*;

/**
 * Spring MVC Test中，Bean类不会被注册为Spring容器管理的Bean。
 */
@RunWith(SpringRunner.class)
// @WebMvcTest表示这是一个MVC测试，可传入多个待测试的Controller。
@WebMvcTest(UserController.class)
public class UserControllerTest {

    // MockMvc是Spring专门提供用于测试SpringMVC类
    @Autowired
    private MockMvc mockMvc;

    // @MockBean 用来模拟实现，因为在Spring MVC测试中并不会初始化@Service注解的类，需要自己模拟service实现。
    @MockBean
    private UserService userService;

    @Test
    public void getUser() throws Exception {
        int userId = 1;
        BDDMockito.given(userService.getCredit(userId)).willReturn(100);
        // perform，完成一次MVC调用，Spring MVC Test是servlet容器内部的模拟测试，不会发起真正的HTTP请求。
        // get，模拟一次GET请求。
        // andExpect，表示请求期望的返回结果
        mockMvc.perform(MockMvcRequestBuilders.get("/user/{id}", userId))
                .andExpect(MockMvcResultMatchers.content().string(String.valueOf(100)));
        String[] parameters = {};
//      模拟Post请求
//        mockMvc.perform(MockMvcRequestBuilders.post("uri", parameters));

//      模拟文件上传
//        mockMvc.perform(MockMvcRequestBuilders.multipart("uri").file("fileName", "file".getBytes("UTF-8")));

//      模拟session和cookie
//        mockMvc.perform(MockMvcRequestBuilders.get("uri").sessionAttr("name", "value"));
//        mockMvc.perform(MockMvcRequestBuilders.get("uri").cookie(new Cookie("name", "value")));

//      设置HTTP Header
/*        mockMvc.perform(MockMvcRequestBuilders
                        .get("uri", parameters)
                        .contentType("application/x-www-form-urlencoded")
                        .accept("application/json")
                        .header("", ""));*/


    }

    @Test
    public void updateUser() throws Exception {
    }

}