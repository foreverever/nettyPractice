package practice.netty.decoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import practice.domain.redis.RedisRepository;
import practice.exception.DataContentException;
import practice.exception.DataRangeException;
import practice.exception.StatusCodeException;
import practice.service.MessageService;
import practice.support.StatusCode;

import java.util.List;

import static practice.support.NettyUtils.isNotNumber;
import static practice.support.StatusCode.*;

public class StatusCodeCheckDecoder extends MessageToMessageDecoder<String> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int STATUS_CODE_FIELD = 4;
    private static final String WRONG_STATUS_CODE = "wrsc";
    private static final String WRONG_DATA = "wrdc";
    private static final String OUT_OF_RANGE_DATA = "orda";

    @Override
    protected void decode(ChannelHandlerContext ctx, String packet, List<Object> list) throws Exception {
        String inputStatusCodeText = packet.substring(0, STATUS_CODE_FIELD);
        logger.debug("statusCode : {}", inputStatusCodeText);

        StatusCode statusCode = StatusCode.of(inputStatusCodeText);
        //데이터가 오는 경우
        if (statusCode == DATA) {
            String content = packet.substring(STATUS_CODE_FIELD, STATUS_CODE_FIELD + 4);
            //데이터가 숫자가 아닌 경우
            if (isNotNumber(content)) {
                exceptionCaught(ctx, new DataContentException(WRONG_DATA));    //wrong data content -> data를 해석할 수 없는 경우(문자 포함 한 경우)
                return;
            }
            //데이터가 범위를 벗어 나는 경우(1번~9번 사이가 아닌 경우)
            if (isNotValidRange(content)) {
                exceptionCaught(ctx, new DataRangeException(OUT_OF_RANGE_DATA));
                return;
            }
            logger.debug("contents information : {}", content);
            list.add(content);
        }
        //약속된 Status Code가 아닌 경우
        else if (statusCode == NACK) {
            exceptionCaught(ctx, new StatusCodeException(WRONG_STATUS_CODE)); //wrong statusCode content -> statusCode를 해석할 수 없음
        }
        //조작 클라이언트인 경우 FAKE/0002
        else if (statusCode == FAKE) {
            list.add(packet);
        }
    }

    private boolean isNotValidRange(String content) {
        int number = Integer.parseInt(content);
        return number <= 0 || number >= 10;
    }
}
