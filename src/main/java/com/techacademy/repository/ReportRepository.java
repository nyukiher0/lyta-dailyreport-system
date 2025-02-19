package com.techacademy.repository;

import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    // 指定された日付と従業員IDに基づいて日報を検索するメソッド
    Optional<Report> findByEmployeeAndReportDate(Employee employee, LocalDate reportDate);

    List<Report> findByEmployeeAndReportDateAndIdNot(
            Employee employee, LocalDate reportDate, Long id);

    List<Report> findByEmployee(Employee employee);
}
