package com.example.service.impl;

import com.example.service.SeatLockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeatLockServiceImpl implements SeatLockService {

    private final StringRedisTemplate redisTemplate;
    private static final String UNLOCK_SCRIPT = """
        if redis.call('get', KEYS[1]) == ARGV[1] then
            return redis.call('del', KEYS[1])
        else
            return 0
        end
        """;

    @Override
    public String lockSeats(Long showId, List<Long> showSeatIds) {

        String lockToken = UUID.randomUUID().toString();

        for (Long showSeatId : showSeatIds) {

            String key = "seat-lock:" + showId + ":" + showSeatId;

            Boolean locked = redisTemplate.opsForValue()
                    .setIfAbsent(key, lockToken, Duration.ofMinutes(10));

            if (!Boolean.TRUE.equals(locked)) {
                releaseSeats(showId, showSeatIds, lockToken);
                return null;
            }
        }

        return lockToken;
    }

    @Override
    public void releaseSeats(
            Long showId,
            List<Long> showSeatIds,
            String lockToken) {

        for (Long showSeatId : showSeatIds) {

            String key = "seat-lock:" + showId + ":" + showSeatId;

            redisTemplate.execute(
                    new DefaultRedisScript<>(UNLOCK_SCRIPT, Long.class),
                    List.of(key),
                    lockToken
            );
        }
    }
}
