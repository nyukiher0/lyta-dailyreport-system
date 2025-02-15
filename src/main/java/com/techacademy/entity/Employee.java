package com.techacademy.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

import lombok.Data;

import org.hibernate.annotations.SQLRestriction;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "employees")
@SQLRestriction("delete_flg = false")
public class Employee {

    public static enum Role {
        GENERAL("一般"),
        ADMIN("管理者");

        private String name;

        private Role(String name) {
            this.name = name;
        }

        public String getValue() {
            return this.name;
        }
    }

    // ID
    @Id
    @Column(length = 10)
    @NotEmpty
    @Length(max = 10)
    private String code;

    // 名前
    @Column(length = 20, nullable = false)
    @NotEmpty
    @Length(max = 20)
    private String name;

    // 権限
    @Column(columnDefinition = "VARCHAR(10)", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    // パスワード（ 8文字以上16文字以下で、半角英数字のみ）
    @Column(length = 255, nullable = false)
    private String password;

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
