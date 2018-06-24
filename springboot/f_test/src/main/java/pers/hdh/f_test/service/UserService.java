package pers.hdh.f_test.service;


import pers.hdh.f_test.entity.User;

/**
 *
 * @author hdonghong
 * @date 2018/06/23
 */
public interface UserService {

	/**
	 * 获取积分
	 * @param userId
	 * @return
	 */
	int getCredit(int userId);

	/**
	 * 更新用户
	 * @param user
	 * @return
	 */
	boolean updateUser(User user);
}
