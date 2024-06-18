package com.imaginative.imaginative.Controller;

import com.imaginative.imaginative.model.Employee;
import com.imaginative.imaginative.model.TaxResponse;
import com.imaginative.imaginative.service.Employeeservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

public class EmployeeController {
    @RestController
    @RequestMapping("/api/employees")
    @Validated
    public class EmployeeController {

        @Autowired
        private Employeeservice.EmployeeService employeeService;

        @PostMapping
        public ResponseEntity<Employee> addEmployee(@Valid @RequestBody Employee employee) {
            Employee savedEmployee = employeeService.saveEmployee(employee);
            return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
        }

        @GetMapping("/tax/{employeeId}")
        public ResponseEntity<TaxResponse> getEmployeeTax(@PathVariable String employeeId) {
            TaxResponse taxResponse = employeeService.calculateTax(employeeId);
            return new ResponseEntity<>(taxResponse, HttpStatus.OK);
        }
    }
}
