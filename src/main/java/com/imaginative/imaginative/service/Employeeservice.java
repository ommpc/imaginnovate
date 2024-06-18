package com.imaginative.imaginative.service;

import com.imaginative.imaginative.model.Employee;
import com.imaginative.imaginative.model.TaxResponse;
import com.imaginative.imaginative.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Employeeservice {
    @Service
    @Transactional
    public class EmployeeService {

        @Autowired
        private EmployeeRepository employeeRepository;

        public Employee saveEmployee(Employee employee) {
            return employeeRepository.save(employee);
        }

        public TaxResponse calculateTax(String employeeId) {
            Employee employee = employeeRepository.findByEmployeeId(employeeId);
            if (employee == null) {
                throw new RuntimeException("Employee not found");
            }

            LocalDate now = LocalDate.now();
            LocalDate startOfFinancialYear = now.getMonthValue() >= 4 ? LocalDate.of(now.getYear(), 4, 1) : LocalDate.of(now.getYear() - 1, 4, 1);
            LocalDate doj = employee.getDoj();

            double monthlySalary = employee.getSalary();
            double totalSalary = 0;

            if (doj.isBefore(startOfFinancialYear)) {
                totalSalary = monthlySalary * 12;
            } else {
                long monthsWorked = ChronoUnit.MONTHS.between(doj.withDayOfMonth(1), now.withDayOfMonth(1)) + 1;
                double daysWorkedInFirstMonth = ChronoUnit.DAYS.between(doj, doj.withDayOfMonth(doj.lengthOfMonth())) + 1;
                totalSalary = (monthsWorked - 1) * monthlySalary + (daysWorkedInFirstMonth / 30.0) * monthlySalary;
            }

            double yearlySalary = totalSalary;
            double taxAmount = calculateTaxAmount(yearlySalary);
            double cessAmount = yearlySalary > 2500000 ? (yearlySalary - 2500000) * 0.02 : 0;

            TaxResponse response = new TaxResponse();
            response.setEmployeeId(employee.getEmployeeId());
            response.setFirstName(employee.getFirstName());
            response.setLastName(employee.getLastName());
            response.setYearlySalary(yearlySalary);
            response.setTaxAmount(taxAmount);
            response.setCessAmount(cessAmount);

            return response;
        }

        private double calculateTaxAmount(double yearlySalary) {
            if (yearlySalary <= 250000) {
                return 0;
            } else if (yearlySalary <= 500000) {
                return (yearlySalary - 250000) * 0.05;
            } else if (yearlySalary <= 1000000) {
                return (250000 * 0) + (250000 * 0.05) + (yearlySalary - 500000) * 0.10;
            } else {
                return (250000 * 0) + (250000 * 0.05) + (500000 * 0.10) + (yearlySalary - 1000000) * 0.20;
            }
        }
    }
}
