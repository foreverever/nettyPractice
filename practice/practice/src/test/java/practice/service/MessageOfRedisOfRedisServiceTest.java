package practice.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import practice.domain.redis.MessageOfRedis;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageOfRedisOfRedisServiceTest {

    private static final MessageOfRedis MESSAGE_OF_REDIS = new MessageOfRedis("127.0.0.0", 1);
    private static String testKey = "1000000";

    @Autowired
    MessageService messageService;

    @Before
    public void setUp() throws Exception {
        MESSAGE_OF_REDIS.setRedisKey(testKey);
        messageService.add(MESSAGE_OF_REDIS);
    }

    @Test
    public void findMessage() {
        MessageOfRedis messageOfRedis = messageService.findById(testKey);
        assertThat(messageOfRedis.getContent()).isEqualTo(1);
        assertThat(messageOfRedis.getRequestIpAddress()).isEqualTo("127.0.0.0");
    }
}