package practice.netty.decoder;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;


public class StatusCodeCheckDecoderTest {
    private Random random = new Random();
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void makeRandomIpAddress() {
        StringBuffer fakeIpAddress = new StringBuffer();
        fakeIpAddress.append(192).append('.')
                .append(random.nextInt(256))
                .append('.')
                .append(random.nextInt(256))
                .append('.')
                .append(random.nextInt(256));
        logger.debug(fakeIpAddress.toString());
    }
}