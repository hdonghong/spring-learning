package pers.hdh.f_test.service.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import pers.hdh.f_test.service.UserService;

@Transactional
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @MockBean
    private CreditSystemServiceImpl creditSystemService;

    @Test
    public void getCredit() throws Exception {
        int expectedCredit = 100;
        // 注解@MockBean 可以自动注入Spring管理的Service，这里creditSystemService由Mockito工具生产，given方法模拟一个service方法调用返回
        BDDMockito.given(creditSystemService.getUserCredit(BDDMockito.anyInt())).willReturn(expectedCredit);

        int userId = 10;
        int credit = userService.getCredit(userId);

        Assert.assertEquals(expectedCredit, credit);
    }

    @Test
    public void updateUser() throws Exception {
    }

}