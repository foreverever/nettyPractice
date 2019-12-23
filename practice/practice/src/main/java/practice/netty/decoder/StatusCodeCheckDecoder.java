package practice.netty.decoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import practice.domain.redis.RedisRepository;
import practice.exception.StatusCodeException;
import practice.service.MessageService;
import practice.support.StatusCode;

import java.util.List;

import static practice.support.StatusCode.*;

public class StatusCodeCheckDecoder extends MessageToMessageDecoder<String> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int STATUS_CODE_FIELD = 4;

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private MessageService messageService;

    @Override
    protected void decode(ChannelHandlerContext ctx, String packet, List<Object> list) throws Exception {
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
            exceptionCaught(ctx,
                    new StatusCodeException("wrsc")); //wrong statusCode content -> statusCode를 해석할 수 없음
        }
        //조작 클라이언트인 경우 FAKE/0002
        else if (statusCode == FAKE) {
            list.add(packet);
        }
    }
}
