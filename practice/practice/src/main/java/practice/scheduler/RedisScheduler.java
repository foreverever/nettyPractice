package practice.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import practice.domain.redis.RedisRepository;
import practice.domain.Message;
import practice.domain.redis.MessageOfRedis;
import practice.domain.MessageRepository;

import java.util.ArrayList;
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

    @Scheduled(fixedRateString = "${batch.time}")
    public void syncRedisAndMariaDB() {
        long start = System.currentTimeMillis();
        logger.debug("스케줄러 작동!!!!");

        List<MessageOfRedis> messagesOfRedis = StreamSupport.stream(redisRepository.findAll().spliterator(), false).collect(Collectors.toList());
        List<String> keys = new ArrayList<>();
        List<Message> messages = new ArrayList<>();

        for (MessageOfRedis messageOfRedis : messagesOfRedis) {
//            messages.add(new Message(messageOfRedis));
            messages.add(messageOfRedis.createMessage());
            keys.add(messageOfRedis.getRedisKey());
            logger.debug("messageAll : {}", messageOfRedis.toString());
        }
        messageRepository.saveAll(messages);

        //todo saveAll해서 RDB에 저장기능 구현
        //todo RDB 동기화후 레디스에서 삭제
        deleteRedisData(keys);
        long end = System.currentTimeMillis();

        logger.debug("레디스 셀렉트 시간 : " + (end - start) / 1000.0);
    }

    private void deleteRedisData(List<String> keys) {
        for (String key : keys) {
            redisRepository.deleteById(key);
        }
    }
}
