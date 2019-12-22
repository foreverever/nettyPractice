package practice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import practice.domain.redis.RedisRepository;
import practice.domain.redis.MessageOfRedis;

@Component
public class MessageService {

    @Autowired
    private RedisRepository redisRepository;

    public void add(MessageOfRedis messageOfRedis) {
        redisRepository.save(messageOfRedis);
    }

    public MessageOfRedis findById(String id) {
        return redisRepository.findById(id)
                .orElseThrow(NullPointerException::new);
    }
}
