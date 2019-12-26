package practice.netty.domain;

import org.junit.Test;
import practice.domain.Message;

public class MessageTest {

    private Message testMessage = Message.testData("127.0.0.1", 1);

    @Test
    public void 시간_초() {
        Integer.parseInt(testMessage.calcSecond());
        System.out.println(testMessage.getEndTime());
        System.out.println(testMessage.calcSecond());
    }
}
