# ADR 001：前端統一使用 Axios API 層

- 狀態：Accepted
- 日期：2026-10-11

## 背景

前端需要呼叫登入及請假 CRUD API。若 View 或 Component 各自組合網址、設定 timeout 及處理錯誤，會形成重複邏輯，也會讓 API 契約難以測試與修改。

## 決策

使用 Axios 建立共用的 `services/http.ts`，只負責：

1. 設定 `baseURL`。
2. 設定 timeout。
3. 透過回應攔截器統一轉換錯誤。

功能端點分別由 `authApi.ts` 與 `leaveApi.ts` 封裝。View 和 Component 不直接使用 Axios，也不自行維護後端網址。

前端依賴方向固定為：

```text
types → api → composables → views/components
```

## 曾考慮的方案

- 在每個 Vue 元件直接使用原生 `fetch`：檔案較少，但網址、錯誤處理與 response parsing 會重複。
- 在每個 API 函式建立 Axios instance：隔離性高，但會重複設定 baseURL、timeout 與攔截器。

## 結果

### 正面影響

- API 網址、timeout 與 HTTP 錯誤處理有單一入口。
- UI 元件只負責呈現與事件，不知道 HTTP 細節。
- 單元測試可以 mock API 層，不再依賴已停用的 `fetch`。

### 負面影響

- 增加 services、api 與 composables 等分層。
- 修改後端契約時，仍需同步更新 TypeScript 型別及 API wrapper。
