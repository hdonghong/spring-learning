package pers.hdh.f_test.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.hdh.f_test.entity.User;
import pers.hdh.f_test.service.UserService;

/**
 * @author Lenovo
 * @since 2018/06/23
 */
@RestController
public class UserController {

	@Autowired
	UserService userService;
	
	@RequestMapping("/user/{id}")
	public String getUser(@PathVariable Integer id){
		return String.valueOf(userService.getCredit(id));
	}
	
	@RequestMapping("/user/{id}/{name}")
	public String updateUser(@PathVariable Integer id,@PathVariable String name){
		User user = new User();
		user.setId(id);
		user.setName(name);
		userService.updateUser(user);
		return "{\"success\":true}";
	}
	
	
	
	
}
