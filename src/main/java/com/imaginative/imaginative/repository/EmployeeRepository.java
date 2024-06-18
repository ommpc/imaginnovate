package com.imaginative.imaginative.repository;

import com.imaginative.imaginative.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;


    public interface EmployeeRepository extends JpaRepository<Employee, Long> {
        Employee findByEmployeeId(String employeeId);
}
