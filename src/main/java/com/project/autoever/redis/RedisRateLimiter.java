package com.project.autoever.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class RedisRateLimiter {

    private final StringRedisTemplate redisTemplate;
    private DefaultRedisScript<Long> script;

    @PostConstruct
    public void init() {
        script = new DefaultRedisScript<>();
        script.setScriptText("""
            local current
            current = redis.call("incr", KEYS[1])
            if tonumber(current) == 1 then
                redis.call("expire", KEYS[1], ARGV[1])
            end
            if tonumber(current) > tonumber(ARGV[2]) then
                return 0
            end
            return 1
            """);
        script.setResultType(Long.class);
    }

    public boolean tryAcquire(String key, int windowSeconds, int limit) {
        Long result = redisTemplate.execute(
                script,
                Collections.singletonList(key),         // KEYS[1]
                String.valueOf(windowSeconds),         // ARGV[1]
                String.valueOf(limit)                  // ARGV[2]
        );
        return result != null && result == 1;
    }
}
