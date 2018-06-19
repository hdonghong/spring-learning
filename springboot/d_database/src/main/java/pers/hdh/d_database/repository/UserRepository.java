package pers.hdh.d_database.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pers.hdh.d_database.dataobject.User;

import java.util.List;

/**
 * UserRepository interface<br/>
 * 引入JpaSpecificationExecutor来做复杂查询，如分页加条件查询
 * @author hdonghong
 * @date 2018/06/19
 */
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    /**
     * 根据用户名模糊查询
     * @param name 需要包含%或者?
     * @return userList
     */
    List<User> findByNameLike(String name);

    /**
     * 演示@Query注解JPQL查询方式
     * @param id
     * @return
     */
    @Query(value = "select u from User u where u.id = ?1")
    User getOneByJpql(Integer id);

    /**
     * 演示@Query注解原生Sql查询方式
     * IDEA中使用原生Sql可能报错：
     * SQL dialect is not configured
     * 或：This inspection performs unresolved SQL references check
     * 注意配置好Database的数据源，指定数据库方言，指定Schema
     * @param id
     * @return
     */
    @Query(value = "select * from user where user_id =:id", nativeQuery = true)
    User getOneBySql(@Param("id") Integer id);

    /**
     * 条件查询 + 利用Pageable做分页查询
     * @param id
     * @param pageable
     * @return
     */
    @Query(value ="select u from User u where u.id > :id")
    Page<User> queryUsers(@Param("id") Integer id, Pageable pageable);

    /**
     * 注解@Query用于更新、删除语句时必须搭配@Modifying注解，且返回值只能是void或者int/Integer
     * @param name
     * @param id
     * @return
     */
    @Modifying
    @Query(value = "update User u set u.name = ?1 where u.id = ?2")
    int update(String name, Integer id);
}
