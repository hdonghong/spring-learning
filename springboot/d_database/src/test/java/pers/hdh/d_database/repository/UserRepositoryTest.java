package pers.hdh.d_database.repository;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import pers.hdh.d_database.dataobject.User;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * UserRepositoryTest class<br/>
 *
 * @author hdonghong
 * @date 2018/06/19
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    public void testFindAll() throws JsonProcessingException {
        // 查询所有
        List<User> userList = userRepository.findAll();
        Assert.assertTrue(userList.size() > 0);
        userList.forEach(System.out::println);

        // 分页查询
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("gmtCreate")));
        Page<User> userPage = userRepository.findAll(pageRequest);
        Assert.assertNotNull(userPage);
        System.out.println(objectMapper.writeValueAsString(userPage));

        // 分页查询 + 条件查询（id > 2）
        Specification<User> specification = (root, query, builder) -> {
            /*
                root：实体对象引用，这里是user
                query：规则查询对象
                builder：规则构建对象
             */
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.greaterThan(root.get("id").as(Integer.class), 2));
            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        userPage = userRepository.findAll(specification, pageRequest);
        Assert.assertNotNull(userPage);
        System.out.println(objectMapper.writeValueAsString(userPage));

    }

    @Test
    public void testFindById() {
        Optional<User> user = userRepository.findById(1);
        Assert.assertNotNull(user);
        System.out.println(user.orElse(null));
    }

    @Test
    @Transactional
    public void testSave() {
        // 测试插入
        User user = new User();
        user.setGmtCreate(new Date());
        user.setName("insert");
        User result = userRepository.save(user);
        Assert.assertNotNull(result);
        System.out.println(result);

        // 测试修改
        result.setName("update");
        result = userRepository.save(result);
        Assert.assertNotNull(result);
        System.out.println(result);

    }

    @Test
    public void testFindByNameLike() throws JsonProcessingException {
/*
    Hibernate:
    select
        user0_.user_id as user_id1_1_, user0_.department_id as departme4_1_, user0_.gmt_create as gmt_crea2_1_, user0_.name as name3_1_
    from
        user user0_
    where
        user0_.name like ?

 */
        List<User> userList = userRepository.findByNameLike("%est%");
        Assert.assertTrue(userList.size() > 0);
        System.out.println(objectMapper.writeValueAsString(userList));
    }

    @Test
    public void testGetOneByJpql() throws JsonProcessingException {
        User user = userRepository.getOneByJpql(1);
        Assert.assertNotNull(user);
        System.out.println(objectMapper.writeValueAsString(user));
    }

    @Test
    public void testGetOneBySql() throws JsonProcessingException {
        User user = userRepository.getOneBySql(1);
        Assert.assertNotNull(user);
        System.out.println(objectMapper.writeValueAsString(user));
    }

    @Test
    public void testQueryUsers() throws JsonProcessingException {
        // // 分页查询 + 条件查询（id > 2）
        Page<User> userPage = userRepository
                .queryUsers(2, PageRequest.of(0, 10, Sort.by(Sort.Order.desc("gmtCreate"))));
        Assert.assertTrue(userPage.getTotalElements() > 0);
        System.out.println(objectMapper.writeValueAsString(userPage));
    }

    @Test
    @Transactional
    public void testUpdate() {
        Assert.assertEquals(1, userRepository.update("狮子", 2));
    }
}