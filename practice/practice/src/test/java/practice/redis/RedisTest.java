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
import practice.domain.redis.RedisRepository;
import practice.domain.redis.MessageOfRedis;

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

    private MessageOfRedis testMessageOfRedis1;
    private MessageOfRedis testMessageOfRedis2;

    @Before
    public void setUp() throws Exception {
        testMessageOfRedis1 = new MessageOfRedis("127.0.0.0", 1);
        testMessageOfRedis2 = new MessageOfRedis("127.0.0.1", 2);
    }

    @Test
    public void Redis_문자열_저장() {
        redisTemplate.opsForValue().set("testKey", testMessageOfRedis1.getContent());
        assertThat(redisTemplate.opsForValue().get("testKey")).isEqualTo(testMessageOfRedis1.getContent());
    }

    @Test
    public void Redis_Json_저장() {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("key", testMessageOfRedis1);

        MessageOfRedis readMessageOfRedis = (MessageOfRedis) valueOperations.get("key");
        System.out.println(readMessageOfRedis);
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
        List<MessageOfRedis> messageOfRedisList = (ArrayList<MessageOfRedis>) redisRepository.findAll();
        for (MessageOfRedis messageOfRedis1 : messageOfRedisList) {
            System.out.println(messageOfRedis1);
        }
    }

    @Test
    public void RedisRepository_추가() {
        testMessageOfRedis2.setRedisKey("1");
        redisRepository.save(testMessageOfRedis1);
        redisRepository.save(testMessageOfRedis2);
        System.out.println(redisRepository.findById("0"));
    }

    @Test
    public void RedisRepository_자동_Key값_증가() {
        System.out.println(redisRepository.count());
    }
}
