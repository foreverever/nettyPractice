package practice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import practice.configuration.redis.RedisRepository;
import practice.domain.Message;

@Component
public class MessageService {

    @Autowired
    private RedisRepository redisRepository;

    public void add(Message message) {
        redisRepository.save(message);
    }

    public Message findById(String id) {
        return redisRepository.findById(id)
                .orElseThrow(NullPointerException::new);
    }
}
