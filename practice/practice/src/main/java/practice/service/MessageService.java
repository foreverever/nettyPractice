package practice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import practice.domain.Message;
import practice.domain.MessageRepository;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class MessageService {
    private static final String NEW_COUNT = "newCount";

    @Autowired
    private RedisTemplate<String, String> redisRedisTemplate;

    @Resource(name = "redisTemplate")
    private ListOperations<String, Message> listOperations;

    @Autowired
    private MessageRepository messageRepository;

    public void add(String key, Message message) {
        message.setEndTime(LocalDateTime.now());
        listOperations.rightPush(key, message);   //value 타입을 list로 바꿈

        if (redisRedisTemplate.hasKey(NEW_COUNT) == null) {
            redisRedisTemplate.opsForValue().set(NEW_COUNT, "0");
        }
        redisRedisTemplate.opsForValue().increment(NEW_COUNT, 1L);
    }

    public List<Message> range(String key, long start, long last) {
        return listOperations.range(key, start, last);
    }

    public long size(String key) {
        return listOperations.size(key);
    }

    public void leftPop(String key) {
        listOperations.leftPop(key);
    }

    public void saveAll(List<Message> messages) {
        messageRepository.saveAll(messages);
    }

    public void deleteAll(String key) {
        long totalSize = listOperations.size(key);

        while (totalSize > 0) {
            listOperations.leftPop(key);
            totalSize--;
        }
    }
}
