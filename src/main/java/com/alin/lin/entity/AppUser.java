package com.alin.lin.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 程式說明： AppUser
 * 程式版本： 1.0
 * 建立時間：2026/10/24下午12:50
 * 開發者：linjiaying
 **/
@Entity
@Table(name = "app_user")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String passwordHash;   // 自動對應 password_hash
    private String role;
}