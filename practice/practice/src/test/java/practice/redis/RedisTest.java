package practice.redis;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;
import practice.configuration.redis.RedisRepository;
import practice.domain.Message;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisRepository redisRepository;

    private Message testMessage1;
    private Message testMessage2;

    @Before
    public void setUp() throws Exception {
        testMessage1 = new Message("127.0.0.0", 1);
        testMessage2 = new Message("127.0.0.1", 2);
    }

    @Test
    public void Redis_문자열_저장() {
        redisTemplate.opsForValue().set("testKey", testMessage1.getContent());
        assertThat(redisTemplate.opsForValue().get("test")).isEqualTo(testMessage1.getContent());
    }

    @Test
    public void Redis_Json_저장() {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("key", testMessage1);

        Message readMessage = (Message) valueOperations.get("key");
        System.out.println(readMessage);
    }

    @Test
    public void Redis_모든값_읽기() {
//        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
//        int cnt = 100;
//        while (cnt > 0) {
//            Message readMessage = (Message) valueOperations.get(Integer.toString(cnt));
//            System.out.println(cnt + " : " + readMessage);
//            cnt--;
//        }
//        System.out.println(redisRepository.findById("1"));
        List<Message> messageList = (ArrayList<Message>) redisRepository.findAll();
        for (Message message1 : messageList) {
            System.out.println(message1);
        }
    }

    @Test
    public void RedisRepository_추가() {
        testMessage2.setId("1");
        redisRepository.save(testMessage1);
        redisRepository.save(testMessage2);
        System.out.println(redisRepository.findById("0"));
    }

    @Test
    public void RedisRepository_자동_Key값_증가() {
        System.out.println(redisRepository.count());
    }
}
