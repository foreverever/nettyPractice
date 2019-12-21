package practice.netty.decoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import practice.configuration.redis.RedisRepository;
import practice.domain.Message;
import practice.exception.NotValidStatusCodeException;
import practice.support.StatusCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static practice.support.StatusCode.*;

public class StatusCodeCheckDecoder extends MessageToMessageDecoder<String> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int STATUS_CODE_FIELD = 4;
    private static final int FAKE_COUNT_FIELD = 8;
    private static final int IP_ADDRESS_RANGE = 256;

    @Autowired
    private RedisRepository redisRepository;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, String packet, List<Object> list) throws Exception {
        String inputStatusCodeText = packet.substring(0, STATUS_CODE_FIELD);
        logger.debug("statusCode : {}", inputStatusCodeText);

        StatusCode statusCode = StatusCode.of(inputStatusCodeText);
        //데이터가 오는 경우
        if (statusCode == DATA) {
            String content = packet.substring(STATUS_CODE_FIELD, STATUS_CODE_FIELD + 4);
            logger.debug("contents information : {}", content);
            list.add(content);
        }
        //약속된 Status Code가 아닌 경우
        else if (statusCode == NACK) {
            exceptionCaught(channelHandlerContext,
                    new NotValidStatusCodeException("올바른 Status Code 가 아닙니다."));
        }
        //todo 조작 클라이언트에서 FAKE/0002/0150 -->2번 150개 조작, 식으로 패킷 구조를 추가해야 할 듯? 굳이 조작클라이언트가 150번 요청을 하는것보다 서버에서 150번 처리하는 게 낫자나?
        else if (statusCode == FAKE) {
            int fakeCount = Integer.parseInt(packet.substring(STATUS_CODE_FIELD, STATUS_CODE_FIELD + 4));
            String content = packet.substring(FAKE_COUNT_FIELD, STATUS_CODE_FIELD + 4);
            Message fakeMessage = new Message(makeRandomIpAddress(new Random()), Integer.parseInt(content), LocalDateTime.now());

            for (int i = 0; i < fakeCount; i++) {
                redisRepository.save(fakeMessage);
            }
        }
    }

    private String makeRandomIpAddress(Random random) {
        StringBuffer fakeIpAddress = new StringBuffer();
        fakeIpAddress.append(192).append('.')
                .append(random.nextInt(IP_ADDRESS_RANGE))
                .append('.')
                .append(random.nextInt(IP_ADDRESS_RANGE))
                .append('.')
                .append(random.nextInt(IP_ADDRESS_RANGE));

        return fakeIpAddress.toString();
    }
}
