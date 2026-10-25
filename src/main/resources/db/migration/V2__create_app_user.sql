CREATE TABLE app_user (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password_hash VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    CONSTRAINT pk_app_user PRIMARY KEY (id),
    CONSTRAINT uk_app_user_username UNIQUE (username)
);

-- 僅供本機練習：密碼 1234 的 BCrypt 雜湊，不可將此預設帳號部署到正式環境。
INSERT INTO app_user (username, password_hash, role)
VALUES ('admin', '$2a$10$XAPn0cPo/OVX2zZMYIbnLuLRSqP4dab7IYBCSVZ0dRem/TSFvDUL.', 'USER');
