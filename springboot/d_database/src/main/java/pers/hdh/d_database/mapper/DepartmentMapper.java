package pers.hdh.d_database.mapper;

import pers.hdh.d_database.dataobject.Department;

/**
 * DepartmentMapper interface<br/>
 *
 * @author hdonghong
 * @date 2018/06/19
 */
public interface DepartmentMapper {

    Department getById(Integer id);
}
