package com.alin.lin.repository;

import com.alin.lin.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 程式說明： AppUserRepository
 * 程式版本： 1.0
 * 建立時間：2026/10/24下午12:52
 * 開發者：linjiaying
 **/
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
}