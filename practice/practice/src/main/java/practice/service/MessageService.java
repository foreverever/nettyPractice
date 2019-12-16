package practice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import practice.domain.Message;

@Component
public class MessageService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public void add(Message message) {
        redisTemplate.opsForHash().putAll(String.valueOf(message.getId()), message.findHashMapValue());
    }
}
