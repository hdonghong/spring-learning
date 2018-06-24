package pers.hdh.f_test.service.impl;

import org.springframework.stereotype.Service;
import pers.hdh.f_test.service.CreditSystemService;

/**
 * @author hdonghong
 */
@Service
public class CreditSystemServiceImpl implements CreditSystemService {

	@Override
	public int getUserCredit(int userId) {
		throw new UnsupportedOperationException("积分系统未完成，不能调用");
	}

	@Override
	public boolean addCedit(int userId, int score) {
		throw new UnsupportedOperationException("积分系统未完成，不能调用");
	}

}
