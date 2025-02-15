package com.techacademy.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

import lombok.Data;

import org.hibernate.annotations.SQLRestriction;
import org.hibernate.validator.constraints.Length;

import java.sql.Date;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "reports")
@SQLRestriction("delete_flg = false")
public class Report {

    // ID（主キー、AUTO_INCREMENT、必須）
    @Id
    @NotEmpty
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 　日付（DATE型、必須）
    @NotEmpty private Date reportDate;

    // タイトル（VARCHAR、100文字以内、必須）
    @NotEmpty
    @Length(max = 100)
    @Column(columnDefinition = "VARCHAR(100)", length = 100, nullable = false)
    private String title;

    // 内容（LONGTEXT、必須）
    @NotEmpty
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String content;

    // 社員番号（Employeesテーブルと結合、社員番号employeeCodeがcodeのFKになる）
    @ManyToOne
    @JoinColumn(name = "employee_code", referencedColumnName = "code", nullable = false)
    private Employee employee;

    // 削除フラグ(論理削除を行うため)
    @Column(columnDefinition = "TINYINT", nullable = false)
    private boolean deleteFlg;

    // 登録日時
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // 更新日時
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
