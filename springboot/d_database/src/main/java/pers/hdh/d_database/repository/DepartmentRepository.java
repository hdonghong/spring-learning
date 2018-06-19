package pers.hdh.d_database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pers.hdh.d_database.dataobject.Department;

/**
 * DepartmentRepository interface<br/>
 *
 * @author hdonghong
 * @date 2018/06/19
 */
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}
