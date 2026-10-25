---
name: leave-api
description: Maintain and verify the Spring Boot leave API contract for leave operations, the minimal login skeleton, MySQL and Flyway, CORS, tests, Postman, and documentation.
---

# Leave API 維護與驗證

## 契約原則

- 資料庫密碼與 JWT 金鑰只能由環境變數注入，不得提供真實預設值或提交本機敏感設定；設定變更時同步 README 的啟動方式。

- 先以 Controller 註解與實際 JSON 為準，再同步 Service、Postman、前端型別與 README。
- 所有 API 使用 `ApiResponse<T>`，欄位固定為區分大小寫的 `data`、`code`、`message`。
- `LeaveStatus` 是封閉狀態，只接受 `PENDING`、`APPROVED`、`REJECTED`。
- 狀態更新 body 固定為 JSON object，例如 `{"status":"APPROVED"}`，不可改成純文字而不連動 Postman 與前端契約。
- 正常執行使用 MySQL，測試使用 H2；兩者都必須執行同一組 Flyway migration。
- `database/bootstrap.sql` 只建立 MySQL database；table、constraint 與 index 一律由 `db/migration` 的只向前版本管理。
- `DataInitializer` 只在 `dev`、`test` profile 執行，且不得在已有資料時重複寫入示範資料。
- Controller 處理 HTTP、輸入驗證及回應包裝；Service 負責資料操作、狀態更新及刪除交易。
- 登入由 `AuthController` 呼叫 `AuthService` 驗證帳密，再由 `AuthSessionManager` 管理登入憑證；錯誤帳密回傳 HTTP 401 與 `AUTHENTICATION_FAILED`。
- 使用者建表與登入串接分開驗證：新增資料表不代表登入已查詢資料庫；示範密碼雜湊必須驗證匹配，且已知預設帳號不得部署至正式環境。
- 全專案只保留一個統一的 `GlobalExceptionHandler`，新例外處理方法併入該類別，避免預設 Bean 名稱衝突。
- 目前 `LeaveRequest` 同時是 Entity 與 API 模型；若新增複雜驗證或公開欄位，先拆分 Create Request、Response DTO 與 Entity。

## 變更流程

1. 檢查 `LeavesController` 的實際 method、path、query parameter 與 request body。
2. 資料表變更只能新增下一版 Flyway migration，不可修改已套用的版本。
3. 同步 Entity 欄位型態、nullable、長度與 enum 儲存格式。
4. 依序同步 request/enum、Service、Repository、`ApiResponse`、Postman 與 `leave-web` 型別/API 層；完整 CRUD 必須包含 `DELETE /api/leaves/{id}`。
5. 新增或更新 `LeavesControllerTest`，至少涵蓋成功路徑與一個輸入錯誤。
6. 登入契約變更時更新 `AuthControllerTest`，同時驗證成功帳密及錯誤帳密的 HTTP 401 回應。
7. 執行 `mvn test`，確認 Flyway migrate 與 Hibernate validate 都成功。
8. 需要實際 HTTP 證據時，啟動服務並執行整個 Postman Collection；不可只用 Context 測試宣稱 API 穩定。
9. 最後更新 README 的資料庫建立方式、Migration、API 與驗證指令。
10. 架構邊界或重要技術決策改變時，同步更新 `docs/architecture` 的 C4 圖與 `docs/adr`；ADR 已接受的歷史決策不可直接覆寫，應新增 ADR 取代。

## 完成條件

- 建立、全部查詢、單筆查詢、狀態篩選、狀態更新與刪除契約一致。
- 單筆查詢、更新或刪除不存在的 ID 時回傳 HTTP 404 與 `LEAVE_NOT_FOUND`。
- 未知 query 狀態與無效 JSON body 回傳 HTTP 400，不落入 HTTP 500。
- `http://localhost:5173` 的 CORS 回應經測試確認。
- 測試設定必須位於 `src/test/resources/application.properties`，不可放在 `src/main/resources/test`。
- `spring.jpa.hibernate.ddl-auto` 保持 `validate`，不得用 `update` 取代 Migration。
- `mvn test` 通過，Postman 的 body 與 Controller 接收型別一致。
- 登入成功與錯誤帳密測試通過，且 `AuthService` 有可注入的 Spring Bean 實作。
- README 描述的是目前程式行為，不保留已過期的「尚未實作」狀態。
- C4 圖與 ADR 必須反映目前的容器責任、資料流及已知限制，並由 README 提供文件入口。

專案使用方式與 API 範例以 `README.md` 為準；本檔只保存重複維護時需要遵循的方法。
