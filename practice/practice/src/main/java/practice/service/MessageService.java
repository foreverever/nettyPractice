package practice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import practice.domain.Message;

import java.util.concurrent.TimeUnit;

@Component
public class MessageService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void add(Message message, String cnt) {
        redisTemplate.opsForValue().set(cnt, message, 10, TimeUnit.MINUTES);
    }

    public Message findMessage(String key) {
        return (Message) redisTemplate.opsForValue().get(key);
    }
}
