package pers.hdh.b_mvc.service;

import pers.hdh.b_mvc.entity.EmployeeForm;

/**
 * EmployeeService interface<br/>
 *
 * @author hdonghong
 * @date 2018/06/18
 */
public interface EmployeeService {

    EmployeeForm getEmployeeById(String id);
}
