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
import practice.domain.Message;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

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
        assertThat(redisTemplate.opsForValue().get("testKey")).isEqualTo(testMessage1.getContent());
    }

    @Test
    public void Redis_Json_저장() {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("key", testMessage1);

        Message readMessage = (Message) valueOperations.get("key");
        System.out.println(readMessage);
    }
}
