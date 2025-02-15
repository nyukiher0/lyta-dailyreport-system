package com.techacademy.repository;

import com.techacademy.entity.Report;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Integer> {}
