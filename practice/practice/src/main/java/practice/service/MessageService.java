package practice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import practice.domain.redis.RedisRepository;
import practice.domain.redis.MessageOfRedis;

import java.time.LocalDateTime;

@Component
public class MessageService {

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private RedisTemplate<String, String> redisRedisTemplate;

    public void add(MessageOfRedis messageOfRedis) {
        messageOfRedis.setProcessingTime(LocalDateTime.now());
        redisRepository.save(messageOfRedis);

        if (!redisRedisTemplate.hasKey("newCount")) {
            redisRedisTemplate.opsForValue().set("newCount", "0");
        }
        redisRedisTemplate.opsForValue().increment("newCount", 1L);

    }

    public MessageOfRedis findById(String id) {
        return redisRepository.findById(id)
                .orElseThrow(NullPointerException::new);
    }
}
