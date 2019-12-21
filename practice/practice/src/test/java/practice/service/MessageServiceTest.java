package practice.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import practice.domain.Message;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageServiceTest {

    private static final Message testMessage = new Message("127.0.0.0", 1);
    private static String testKey = "1000000";

    @Autowired
    MessageService messageService;

    @Before
    public void setUp() throws Exception {
        testMessage.setId(testKey);
        messageService.add(testMessage);
    }

    @Test
    public void findMessage() {
        Message message = messageService.findById(testKey);
        assertThat(message.getContent()).isEqualTo(1);
        assertThat(message.getRequestIpAddress()).isEqualTo("127.0.0.0");
    }
}