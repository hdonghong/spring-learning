package pers.hdh.f_test.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import pers.hdh.f_test.entity.User;

import static org.junit.Assert.*;

// 激活test作为profile
@ActiveProfiles("test")
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
@Sql({"/user.sql"})
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void testGetOne() {
        User user = userDao.getOne(99);
        Assert.assertNotNull(user);
        Assert.assertEquals("哈哈", user.getName());
    }

}