package pers.hdh.f_test.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import pers.hdh.f_test.entity.User;

public interface UserDao extends JpaRepository<User, Integer> {

}
