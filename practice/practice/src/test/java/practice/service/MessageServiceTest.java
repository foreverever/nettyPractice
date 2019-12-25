package practice.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import practice.domain.Message;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageServiceTest {

    private static final Message TEST_MESSAGE = new Message("127.0.0.0", 1);
    private static final Message TEST_MESSAGE2 = new Message("127.0.0.1", 2);
    private static final String TEST_KEY = "testKey";

    @Autowired
    MessageService messageService;

    @Before
    public void setUp() throws Exception {
        if (messageService.size(TEST_KEY) != 0) {
            messageService.deleteAll(TEST_KEY);
        }
        messageService.add(TEST_KEY, TEST_MESSAGE);
    }

    @Test
    public void redis_사이즈() {
        assertThat(messageService.size(TEST_KEY)).isEqualTo(1);
    }

    @Test
    public void redis_특정키_리스트_가져오기() {
        List<Message> messages = messageService.range(TEST_KEY, 0, messageService.size(TEST_KEY));
        Message messageFromRedis = messages.get(0);

        assertThat(messageFromRedis.getContent()).isEqualTo(1);
        assertThat(messageFromRedis.getIp()).isEqualTo("127.0.0.0");
    }

    @Test
    public void redis_특정키_리스트_추가() {
        messageService.add(TEST_KEY, TEST_MESSAGE2);
        List<Message> messages = messageService.range(TEST_KEY, 0, messageService.size(TEST_KEY));

        assertThat(messages.get(messages.size() - 1)).isEqualTo(TEST_MESSAGE2);
        assertThat(messageService.size(TEST_KEY)).isEqualTo(2);
    }

    @Test
    public void redis_특정키_리스트_모두삭제() {
        messageService.add(TEST_KEY, TEST_MESSAGE2);
        messageService.deleteAll(TEST_KEY);

        assertThat(messageService.size(TEST_KEY)).isEqualTo(0);
    }

    @Test
    public void redis_특정키_리스트_leftPop() {
        messageService.add(TEST_KEY, TEST_MESSAGE2);
        messageService.leftPop(TEST_KEY);
        List<Message> messages = messageService.range(TEST_KEY, 0, messageService.size(TEST_KEY));

        assertThat(messages.size()).isEqualTo(1);
        assertThat(messages.get(0)).isEqualTo(TEST_MESSAGE2);
    }
}