package practice.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import practice.domain.redis.RedisRepository;
import practice.domain.Message;
import practice.domain.redis.MessageOfRedis;
import practice.domain.MessageRepository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@PropertySource(value = "classpath:/application.properties")
public class RedisScheduler {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Resource(name = "redisTemplate")
    private ListOperations<String, MessageOfRedis> listOperations;

    @Scheduled(fixedRateString = "${batch.time}")
    public void syncRedisAndMariaDB() {
        long start = System.currentTimeMillis();
        logger.debug("스케줄러 작동!!!!");

        List<MessageOfRedis> messagesOfRedis = listOperations.range("messages", 0, listOperations.size("messages"));
        List<Message> messages = new ArrayList<>();

        for (MessageOfRedis messageOfRedis : messagesOfRedis) {
            messages.add(messageOfRedis.createMessage());
            logger.debug("messageAll : {}", messageOfRedis.toString());
        }
        messages.sort((Message a, Message b) -> a.getStartTime().compareTo(b.getStartTime()));

        messageRepository.saveAll(messages);
        deleteRedisData(messages.size());
        long end = System.currentTimeMillis();

        logger.debug("레디스 셀렉트 시간 : " + (end - start) / 1000.0);
    }

    private void deleteRedisData(List<String> keys) {
        for (String key : keys) {
            redisRepository.deleteById(key);
        }
    }

    private void deleteRedisData(int size) {
        while (size > 0) {
            listOperations.leftPop("messages");
            size--;
        }
    }
}
