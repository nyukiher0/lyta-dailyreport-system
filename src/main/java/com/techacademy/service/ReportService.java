package com.techacademy.service;

import com.techacademy.entity.Report;
import com.techacademy.repository.ReportRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {
    private final ReportRepository reportRepository;
    private final PasswordEncoder passwordEncoder;

    public ReportService(ReportRepository reportRepository, PasswordEncoder passwordEncoder) {
        this.reportRepository = reportRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 従業員一覧表示処理
    public List<Report> findAll() {
        return reportRepository.findAll();
    }
}
