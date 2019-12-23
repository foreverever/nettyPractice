package practice.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import practice.domain.Message;
import practice.service.MessageService;

import java.util.ArrayList;
import java.util.List;

@Component
@PropertySource(value = "classpath:/application.properties")
public class RedisScheduler {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MessageService messageService;

    @Scheduled(fixedRateString = "${batch.time}")
    public void syncRedisAndMariaDB() {
        long start = System.currentTimeMillis();
        logger.debug("스케줄러 작동!!!!");

        List<Message> messagesOfRedis = messageService.range("messages", 0, messageService.size("messages"));
        List<Message> messages = new ArrayList<>();

        for (Message message : messagesOfRedis) {
            messages.add(message);
            logger.debug("messageAll : {}", message.toString());
        }
        messages.sort((Message a, Message b) -> a.getStartTime().compareTo(b.getStartTime()));

        messageService.saveAll(messages);
        deleteRedisData(messages.size());
        long end = System.currentTimeMillis();

        logger.debug("레디스 셀렉트 시간 : " + (end - start) / 1000.0);
    }

    private void deleteRedisData(int size) {
        while (size > 0) {
            messageService.leftPop("messages");
            size--;
        }
    }
}
