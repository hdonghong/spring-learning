package pers.hdh.b_mvc.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.hdh.b_mvc.entity.EmployeeForm;
import pers.hdh.b_mvc.service.EmployeeService;

/**
 * EmployeeServiceImpl class<br/>
 *
 * @author hdonghong
 * @date 2018/06/18
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class EmployeeServiceImpl implements EmployeeService {

    @Override
    public EmployeeForm getEmployeeById(String id) {
        return null;
    }
}
