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

    private Message message;

    @Before
    public void setUp() throws Exception {
        message = new Message(0L, "contents");
    }

    @Test
    public void Redis_문자열_저장() {
        redisTemplate.opsForValue().set("testKey", message.getContents());
        assertThat(redisTemplate.opsForValue().get("test")).isEqualTo(message.getContents());
    }

    @Test
    public void Redis_해시_저장() {
        redisTemplate.opsForHash().putAll("hashTestKey", message.findHashMapValue());
        assertThat(redisTemplate.opsForHash().get("hashTestKey", "id")).isEqualTo(message.getId());
        assertThat(redisTemplate.opsForHash().get("hashTestKey", "contents")).isEqualTo(message.getContents());
        logger.debug("hashValue : {}", redisTemplate.opsForHash().entries("hashTestKey"));
    }

    @Test
    public void Redis_Json_저장() {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("key", message);

        Message readMessage = (Message) valueOperations.get("key");
        System.out.println(readMessage);
    }
}
