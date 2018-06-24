package pers.hdh.f_test.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.hdh.f_test.dao.UserDao;
import pers.hdh.f_test.entity.User;
import pers.hdh.f_test.service.CreditSystemService;
import pers.hdh.f_test.service.UserService;

/**
 *
 * @author hdonghong
 * @since 2018/06/23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

	@Autowired
	CreditSystemService creditSystemService;
	
	@Autowired
	UserDao userDao;
	
	@Override
	public int getCredit(int userId) {
	
		return creditSystemService.getUserCredit(userId);
		
	}

	@Override
	public boolean updateUser(User user) {
		return userDao.save(user) != null;
	}

}
