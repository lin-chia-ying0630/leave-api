# Leave API

以 Spring Boot、Spring Data JPA、MySQL 與 Flyway 建立的請假管理練習 API，提供請假單 CRUD、狀態篩選及最小登入骨架。測試環境使用 H2；前端專案位於同層的 `leave-web`。

## 技術與環境

- Java 17 以上
- Maven 3.9 以上
- Spring Boot 3.5
- Spring Data JPA
- Flyway Migration
- MySQL（應用程式執行）
- H2（測試專用）
- Spring Security（目前為本機練習設定，API 全部放行）

## 建立資料庫

先用具有建立資料庫權限的 MySQL 帳號執行：

```bash
mysql -u root -p < database/bootstrap.sql
```

這只會建立 `leavedb` database。應用程式啟動時，Flyway 會執行 `db/migration/V1__create_leave_request.sql` 建立 `leave_request` table、狀態限制與查詢索引。

`V2__create_app_user.sql` 新增 `app_user`，包含自動遞增主鍵、唯一的 username、BCrypt password_hash 與單一 role，並建立本機練習帳號 `admin`／`1234`（角色 `USER`）。此 migration 不受 Spring profile 限制，會在所有使用這組 migration 的環境執行，因此不可直接用於正式環境。

此步驟只建立資料表及示範資料；目前 `AuthServiceImpl` 仍使用寫死帳密，尚未查詢 `app_user`。後續需串接使用者 Repository 與 BCrypt 密碼驗證。不要手動重複執行 INSERT，也不要修改已套用的 migration。

## 啟動

以環境變數提供連線資訊，避免把密碼寫入版本庫：

```bash
export DB_URL='jdbc:mysql://localhost:3306/leavedb?serverTimezone=Asia/Taipei&characterEncoding=utf8'
export DB_USERNAME='root'
export DB_PASSWORD='你的 MySQL 密碼'
export JWT_SECRET="$(openssl rand -hex 32)"
mvn spring-boot:run
```

`DB_PASSWORD` 與 `JWT_SECRET` 都是必填環境變數，不提供內建密碼或金鑰。上例產生隨機 JWT 金鑰；若需讓既有 JWT 在重新啟動後仍有效，請在本機安全保存並重用同一把金鑰，勿提交至 Git。更換金鑰會使舊 JWT 失效。

目前 `JwtUtil` 仍會在 Redis 模式建立，因此 Redis 模式也要提供 `JWT_SECRET`。使用 IntelliJ 啟動時，請在 Run Configuration 的 Environment variables 設定這兩項；一般 `.env` 檔不會由 Spring Boot 自動載入。IDE 設定、環境檔及金鑰檔已由 `.gitignore` 排除。

如需載入三筆示範資料，啟用 `dev` profile：

```bash
SPRING_PROFILES_ACTIVE=dev mvn spring-boot:run
```

`DataInitializer` 只在 `dev` 或 `test` profile 執行，而且資料表已有資料時不會重複寫入。

API 預設位於 `http://localhost:8080`。

前端開發伺服器預設為 `http://localhost:5173`，後端 CORS 只允許此來源存取 `/api/**`。

## API 契約

所有回應都使用同一個包裝：

```json
{
  "data": {},
  "code": null,
  "message": null
}
```

成功時 `code` 與 `message` 為 `null`；可預期的輸入錯誤會回傳 HTTP 400，並提供錯誤代碼與訊息。

| 方法     | 路徑                         | 說明                                     |
| -------- | ---------------------------- | ---------------------------------------- |
| `GET`    | `/api/leaves`                | 查詢全部請假單                           |
| `GET`    | `/api/leaves?status=PENDING` | 依狀態篩選                               |
| `GET`    | `/api/leaves/{id}`           | 依 ID 查詢                               |
| `POST`   | `/api/leaves`                | 建立請假單，未指定狀態時預設為 `PENDING` |
| `PUT`    | `/api/leaves/{id}/status`    | 更新請假狀態                             |
| `DELETE` | `/api/leaves/{id}`           | 刪除請假單                               |
| `POST`   | `/api/auth/login`            | 以本週寫死帳密驗證登入動作               |

狀態固定為 `PENDING`、`APPROVED`、`REJECTED`。

登入目前只接受寫死的 `admin`／`1234`，錯誤帳密回傳 HTTP 401 與 `AUTHENTICATION_FAILED`。成功後由 `AuthSessionManager` 建立登入憑證並透過 Cookie 回傳；新增的 `app_user` 尚未接入帳密驗證。

建立請假單範例：

```json
{
  "applicant": "王小明",
  "leaveType": "特休",
  "startDate": "2026-10-10",
  "endDate": "2026-10-11",
  "reason": "家庭旅遊"
}
```

更新狀態的 request body：

```json
{
  "status": "APPROVED"
}
```

## 專案結構

```text
src/main/java/com/alin/lin/
├── configure/   # CORS、Security、例外處理與測試資料
├── controller/  # HTTP 路由與回應包裝
├── repository/  # JPA 資料存取
├── request/     # Entity、狀態 enum 與狀態更新輸入
├── response/    # 共用 ApiResponse
└── service/     # 查詢、建立、狀態更新與刪除流程

src/main/resources/db/migration/
├── V1__create_leave_request.sql
└── V2__create_app_user.sql

src/test/resources/
└── application.properties       # 測試專用 H2 設定

database/
└── bootstrap.sql                # 建立 MySQL database

docs/
├── architecture/c4-container.md # C4 Container 架構圖
└── adr/                          # Architecture Decision Records
```

目前是學習用的小型專案，`LeaveRequest` 同時作為 JPA Entity 與 API 資料模型。正式產品應再拆分 Entity、建立請求及回應 DTO。

## 架構文件

- [C4 Container Diagram](docs/architecture/c4-container.md)
- [ADR 001：前端統一使用 Axios API 層](docs/adr/001-use-axios-api-layer.md)
- [ADR 002：使用 Flyway 管理 MySQL Schema](docs/adr/002-use-flyway-for-schema.md)
- [ADR 003：本階段採用無 Session 的最小登入骨架](docs/adr/003-minimal-login-without-session.md)

## 驗證

執行後端整合測試：

```bash
mvn test
```

測試會自動改用 H2，並先執行與 MySQL 相同的 Flyway migration，再由 Hibernate 驗證 schema。測試涵蓋 Spring Context、登入、全部查詢、狀態篩選、單筆查詢、建立、狀態更新、刪除、404、錯誤狀態與 CORS。

如需人工或 Newman 驗證，先啟動後端，再匯入 `postman/leave-api.postman_collection.json`，依序執行整個 Collection：

```bash
npx newman run postman/leave-api.postman_collection.json
```

Collection 會先驗證登入，再建立資料、保存回傳的 `leaveId`，接著驗證單筆查詢、狀態更新、篩選與刪除。

## 目前邊界

- 找不到 ID 時回傳 HTTP 404、`LEAVE_NOT_FOUND` 與可讀訊息。
- 登入目前仍為寫死帳密；`app_user` 已有建表 migration，但尚未接入資料庫帳密驗證。
- 目前 Security 設定僅供本機練習，不可直接當成正式環境設定。
- MySQL database 必須先由 `database/bootstrap.sql` 建立；Flyway 負責 database 內的版本化物件。
