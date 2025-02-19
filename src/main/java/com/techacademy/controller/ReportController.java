package com.techacademy.controller;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.constants.ErrorMessage;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Employee.Role;
import com.techacademy.entity.Report;
import com.techacademy.service.ReportService;
import com.techacademy.service.UserDetail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @ModelAttribute("report")
    public Report setUpReport() {
        return new Report();
    }

    @GetMapping
    public String list(@AuthenticationPrincipal UserDetail userDetail, Model model) {

        // ログイン中の社員を抜き出す
        Employee employee = userDetail.getEmployee();
        // その社員のロールを抜き出す
        Role role = employee.getRole();
        List<Report> repoList = new ArrayList<>();
        // ロールが管理者の場合、全ての社員の日報を表示する
        if (role.equals(Role.ADMIN)) {
            repoList = reportService.findAll();
        }
        // ロールが一般の場合には、自分の日報のみを表示する
        if (role.equals(Role.GENERAL)) {
            repoList = reportService.findByEmployee(employee);
        }

        model.addAttribute("listSize", repoList.size());
        model.addAttribute("reportList", repoList);
        return "reports/list";
    }

    // 日報新規登録画面
    @GetMapping(value = "/add")
    public String create(Model model, @AuthenticationPrincipal UserDetail userDetail) {
        Report report = new Report();
        report.setEmployee(userDetail.getEmployee());
        model.addAttribute("report", report);
        model.addAttribute("employeeName", userDetail.getEmployee().getName());
        return "reports/new";
    }

    // 　日報新規登録処理
    @PostMapping(value = "/add")
    public String add(
            @ModelAttribute @Validated Report report,
            BindingResult res,
            Model model,
            @AuthenticationPrincipal UserDetail userDetail) {

        if (res.hasErrors()) {
            model.addAttribute("report", report);
            model.addAttribute("employeeName", userDetail.getEmployee().getName());
            return "reports/new";
        }
        // ログイン中の社員情報をセット
        report.setEmployee(userDetail.getEmployee());

        ErrorKinds createResult =
                reportService.canCreate(report.getEmployee(), report.getReportDate());

        if (ErrorMessage.contains(createResult)) {
            model.addAttribute(
                    ErrorMessage.getErrorName(createResult),
                    ErrorMessage.getErrorValue(createResult));
            model.addAttribute("report", report);
            model.addAttribute("employeeName", userDetail.getEmployee().getName());
            return "reports/new";
        }

        ErrorKinds saveResult = reportService.save(report);

        if (ErrorMessage.contains(saveResult)) {
            model.addAttribute(
                    ErrorMessage.getErrorName(saveResult), ErrorMessage.getErrorValue(saveResult));
            model.addAttribute("report", report);
            model.addAttribute("employeeName", userDetail.getEmployee().getName());
            return "reports/new";
        }
        return "redirect:/reports";
    }

    // 日報詳細表示画面
    @GetMapping(value = "/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        Employee employee = reportService.findById(id).getEmployee();
        model.addAttribute("employee", employee);
        model.addAttribute("report", reportService.findById(id));
        return "reports/detail";
    }

    // 日報編集画面
    @GetMapping(value = "/{id}/update")
    public String edit(
            @PathVariable("id") Long id,
            Model model,
            @AuthenticationPrincipal UserDetail userDetail) {

        model.addAttribute("report", reportService.findById(id));
        model.addAttribute("employeeName", userDetail.getEmployee().getName());
        return "reports/update";
    }

    // 日報編集処理
    @PostMapping(value = "/{id}/update")
    public String update(
            @PathVariable("id") Long id,
            @ModelAttribute @Validated Report report,
            BindingResult res,
            Model model,
            @AuthenticationPrincipal UserDetail userDetail) {

        if (res.hasErrors()) {
            model.addAttribute("report", report);
            model.addAttribute("employeeName", userDetail.getEmployee().getName());
            return "reports/update";
        }
        // ログイン中の社員情報をセット
        report.setEmployee(userDetail.getEmployee());

        ErrorKinds updateResult =
                reportService.canUpdate(report.getEmployee(), report.getReportDate(), id);

        if (ErrorMessage.contains(updateResult)) {
            model.addAttribute(
                    ErrorMessage.getErrorName(updateResult),
                    ErrorMessage.getErrorValue(updateResult));
            model.addAttribute("report", report);
            model.addAttribute("employeeName", userDetail.getEmployee().getName());
            return "reports/update";
        }

        ErrorKinds saveResult = reportService.save(report);

        if (ErrorMessage.contains(saveResult)) {
            model.addAttribute(
                    ErrorMessage.getErrorName(saveResult), ErrorMessage.getErrorValue(saveResult));
            model.addAttribute(
                    ErrorMessage.getErrorName(updateResult),
                    ErrorMessage.getErrorValue(updateResult));
            model.addAttribute("report", report);
            model.addAttribute("employeeName", userDetail.getEmployee().getName());
            return "reports/update";
        }
        return "redirect:/reports";
    }

    // 日報削除処理
    @PostMapping(value = "/{id}/delete")
    public String delete(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal UserDetail userDetail,
            Model model) {

        ErrorKinds result = reportService.delete(id);
        if (ErrorMessage.contains(result)) {
            model.addAttribute(
                    ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
            model.addAttribute("report", reportService.findById(id));
            return detail(id, model);
        }
        return "redirect:/reports";
    }
}
