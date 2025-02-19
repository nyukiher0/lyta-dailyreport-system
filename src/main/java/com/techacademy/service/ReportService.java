package com.techacademy.service;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.repository.ReportRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {
    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public Report findById(Long id) {
        Optional<Report> report = reportRepository.findById(id);
        return report.orElse(null);
    }

    public List<Report> findByEmployee(Employee employee) {
        return reportRepository.findByEmployee(employee);
    }

    // 日報保存処理
    @Transactional
    public ErrorKinds save(Report report) {
        ErrorKinds result = reportContentCheck(report);
        // レポートコンテンツチェック
        if (ErrorKinds.CHECK_OK != result) {
            return result;
        }

        report.setDeleteFlg(false);

        LocalDateTime now = LocalDateTime.now();

        report.setCreatedAt(now);
        report.setUpdatedAt(now);

        reportRepository.save(report);
        return ErrorKinds.SUCCESS;
    }

    // 日報削除処理
    @Transactional
    public ErrorKinds delete(Long id) {
        Report report = findById(id);
        LocalDateTime now = LocalDateTime.now();
        report.setUpdatedAt(now);
        report.setDeleteFlg(true);
        return ErrorKinds.SUCCESS;
    }

    // 従業員一覧表示処理
    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    // 新規登録する場合にログインユーザーと同一の従業員コード、かつ同一日付の日報が存在するかを判定する
    public ErrorKinds canCreate(Employee employee, LocalDate reportDate) {
        Optional<Report> report =
                reportRepository.findByEmployeeAndReportDate(employee, reportDate);
        if (!report.isEmpty()) {
            return ErrorKinds.DATECHECK_ERROR;
        } else {
            return ErrorKinds.CHECK_OK;
        }
    }

    // 更新処理をする場合にログインユーザーで編集中の日報を除いて同一の従業員コード、かつ同一日付の日報が存在するかを判定する
    public ErrorKinds canUpdate(Employee employee, LocalDate reportDate, Long id) {
        List<Report> report =
                reportRepository.findByEmployeeAndReportDateAndIdNot(employee, reportDate, id);
        if (!report.isEmpty()) {
            return ErrorKinds.DATECHECK_ERROR;
        } else {
            return ErrorKinds.CHECK_OK;
        }
    }

    // 日報内容チェック
    private ErrorKinds reportContentCheck(Report report) {
        // 日付入力が空で登録の場合にエラー
        if (report.getReportDate() == null) {
            return ErrorKinds.BLANK_ERROR;
        }
        // タイトルが空で登録の場合にエラー
        if (report.getTitle().isEmpty()) {
            return ErrorKinds.BLANK_ERROR;
        }
        // 内容が空で登録の場合にエラー
        if (report.getContent().isEmpty()) {
            return ErrorKinds.BLANK_ERROR;
        }
        // タイトルが100文字を超える場合にはエラー
        if (report.getTitle().length() > 100) {
            return ErrorKinds.RANGECHECK_ERROR;
        }
        // 内容が600文字を超える場合にはエラー
        if (report.getContent().length() > 600) {
            return ErrorKinds.RANGECHECK_ERROR;
        }
        return ErrorKinds.CHECK_OK;
    }
}
