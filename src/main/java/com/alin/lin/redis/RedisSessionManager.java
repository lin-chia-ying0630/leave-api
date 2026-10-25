package com.alin.lin.redis;

import com.alin.lin.security.AuthSession;
import com.alin.lin.security.AuthSessionManager;
import com.alin.lin.security.AuthenticatedUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

/**
 * 程式說明： RedisSessionManager
 * 程式版本： 1.0
 * 建立時間：2026/10/12上午11:33
 * 開發者：linjiaying
 **/
@Service
@ConditionalOnProperty(
        name = "app.auth.session-mode",
        havingValue = "redis"
)
public class RedisSessionManager implements AuthSessionManager {

    private static final String KEY_PREFIX = "auth:session:";
    private static final Duration TTL = Duration.ofMinutes(30);

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisSessionManager(
            StringRedisTemplate redisTemplate,
            ObjectMapper objectMapper
    ) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public AuthSession create(AuthenticatedUser user) {
        String sessionId = UUID.randomUUID().toString();

        RedisSessionData data = new RedisSessionData(
                user.id(),
                user.username(),
                user.roles()
        );

        try {
            String json = objectMapper.writeValueAsString(data);

            redisTemplate.opsForValue()
                    .set(key(sessionId), json, TTL);

            return new AuthSession(sessionId, TTL);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("無法建立登入 Session", ex);
        }
    }

    @Override
    public Optional<AuthenticatedUser> resolve(String sessionId) {
        String json = redisTemplate.opsForValue().get(key(sessionId));

        if (json == null) {
            return Optional.empty();
        }

        try {
            RedisSessionData data =
                    objectMapper.readValue(json, RedisSessionData.class);

            return Optional.of(new AuthenticatedUser(
                    data.userId(),
                    data.username(),
                    data.roles()
            ));
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Redis Session 格式錯誤", ex);
        }
    }

    @Override
    public void invalidate(String sessionId) {
        redisTemplate.delete(key(sessionId));
    }


    private static final String USER_SESSIONS_PREFIX = "auth:user-sessions:";

    @Override
    public AuthSession create(AuthenticatedUser user) {
        String sessionId = UUID.randomUUID().toString();
        // ……原本寫入 auth:session:<id> 的程式不變……

        // 新增：把 sessionId 記到這個使用者的清單裡
        String userKey = userKey(user.id());
        redisTemplate.opsForSet().add(userKey, sessionId);
        redisTemplate.expire(userKey, TTL);   // 清單也要會過期，不然會一直累積

        return new AuthSession(sessionId, TTL);
    }

    @Override
    public void invalidateAllByUser(Long userId) {
        String userKey = userKey(userId);
        Set<String> sessionIds = redisTemplate.opsForSet().members(userKey);

        if (sessionIds != null && !sessionIds.isEmpty()) {
            List<String> keys = sessionIds.stream().map(this::key).toList();
            redisTemplate.delete(keys);   // 一次刪掉所有 session
        }
        redisTemplate.delete(userKey);    // 清單本身也刪掉
    }

    private String userKey(Long userId) {
        return USER_SESSIONS_PREFIX + userId;
    }


    private String key(String sessionId) {
        return KEY_PREFIX + sessionId;
    }
}