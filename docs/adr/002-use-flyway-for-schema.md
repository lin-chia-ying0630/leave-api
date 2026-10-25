# ADR 002：使用 Flyway 管理 MySQL Schema

- 狀態：Accepted
- 日期：2026-10-11

## 背景

請假系統以 MySQL 保存資料。若依賴 Hibernate 自動新增或修改欄位，不容易追蹤 Schema 歷史，也可能讓本機、測試與其他環境出現不同結構。

## 決策

- `database/bootstrap.sql` 只負責建立 `leavedb` database。
- Table、constraint 與 index 全部由 `src/main/resources/db/migration` 的 Flyway Migration 建立。
- `spring.jpa.hibernate.ddl-auto` 使用 `validate`，只驗證 Entity 與 Schema 是否一致。
- 正常執行使用 MySQL；自動測試使用 H2 的 MySQL 相容模式。
- 已發布的 Migration 不回頭修改；後續 Schema 變更必須新增下一版 Migration。

## 曾考慮的方案

- 使用 `ddl-auto=update`：開發快速，但資料庫變更不可審查，也無法可靠重現。
- 手動執行 SQL：容易理解，但缺少版本、順序及環境一致性保證。

## 結果

### 正面影響

- 資料庫結構可版本化並可重現。
- 測試啟動時會先執行相同 Migration，再由 Hibernate 驗證 Schema。
- Schema 變更能和程式碼及測試一起接受審查。

### 負面影響

- 每次 Schema 變更都需要新增 Migration。
- H2 相容模式無法完全取代真實 MySQL 驗證。
