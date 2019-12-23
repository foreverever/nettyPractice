package practice.performance;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.test.context.junit4.SpringRunner;
import practice.domain.Message;
import practice.domain.MessageRepository;
import practice.domain.redis.MessageOfRedis;
import practice.domain.redis.RedisRepository;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PerformanceTest {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    RedisRepository redisRepository;

    @Resource(name = "redisTemplate")
    private ListOperations<String, MessageOfRedis> listOperations;

    @Test
    public void RDB에서_데이터_조회_1000개() {
        List<Message> messageOfRedis = messageRepository.findAll();
        assertThat(messageOfRedis.size()).isEqualTo(1000);
    }

    @Test
    public void Redis에서_데이터_조회_1000개_해시_테이블() {
        List<MessageOfRedis> messagesOfRedis = StreamSupport.stream(redisRepository.findAll().spliterator(), false).collect(Collectors.toList());
        assertThat(messagesOfRedis.size()).isEqualTo(1000);
    }

    @Test
    public void Redis에서_데이터_조회_1000개_리스트() {
        listOperations.range("messages",0,1000);
        assertThat(listOperations.size("messages")).isEqualTo(1000);
    }
}
