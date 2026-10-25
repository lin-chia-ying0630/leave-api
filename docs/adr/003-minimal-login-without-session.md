# ADR 003：本階段採用無 Session 的最小登入骨架

- 狀態：Accepted
- 日期：2026-10-11

## 背景

第 3 週目標是先打通登入動作與頁面切換，但尚未決定正式系統要採用伺服器端 Session、JWT 或其他身分驗證方式。過早選定機制會擴大本週範圍。

## 決策

- 後端提供 `POST /api/auth/login`。
- 暫時只接受寫死的 `admin`／`1234`。
- 成功時回傳 `LoginResponse`；錯誤帳密回傳 HTTP 401 與 `AUTHENTICATION_FAILED`。
- 前端登入成功後使用 Vue Router 導向具名路由 `leave-list`。
- 本階段不建立 Session、JWT、使用者資料表、角色授權或 Router Guard。

## 曾考慮的方案

- 立即使用伺服器端 Session：可以保存登入狀態，但必須同時決定 cookie、跨來源與 Session 儲存方式。
- 立即使用 JWT：適合無狀態 API，但必須處理 token 儲存、到期、更新與撤銷。

## 結果

### 正面影響

- 可以先驗證 `LoginForm → authApi → Leave API → Vue Router` 的完整流程。
- 不會在需求尚未明確時綁定正式驗證技術。
- 實作範圍符合本週的登入骨架目標。

### 負面影響

- 重新整理後沒有登入狀態。
- 使用者仍可直接進入 `/leave`。
- 寫死帳密及目前的全放行 Security 設定不可用於正式環境。

## 後續決策

開始正式身分驗證前，必須新增 ADR，比較 Session 與 Token 的部署方式、安全性、登出、到期及撤銷需求；不得直接把本 ADR 當成正式登入設計。
