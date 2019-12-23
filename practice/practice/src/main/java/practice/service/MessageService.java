package practice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import practice.domain.Message;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Component
public class MessageService {

    @Autowired
    private RedisTemplate<String, String> redisRedisTemplate;

    @Resource(name = "redisTemplate")
    private ListOperations<String, Message> listOperations;

    public void add(Message message) {
        message.setEndTime(LocalDateTime.now());
        listOperations.rightPush("messages", message);   //value 타입을 list로 바꿈

        if (redisRedisTemplate.hasKey("newCount") != null) {
            redisRedisTemplate.opsForValue().set("newCount", "0");
        }
        redisRedisTemplate.opsForValue().increment("newCount", 1L);
    }
}
