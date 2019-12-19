package practice.netty.decoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import practice.exception.NotValidStatusCodeException;
import practice.support.StatusCode;

import java.util.List;

import static practice.support.StatusCode.DATA;
import static practice.support.StatusCode.NACK;

public class StatusCodeCheckDecoder extends MessageToMessageDecoder<String> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int STATUS_CODE_FIELD = 4;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, String packet, List<Object> list) throws Exception {
        String inputStatusCodeText = packet.substring(0, STATUS_CODE_FIELD);
        logger.debug("statusCode : {}", inputStatusCodeText);

        StatusCode statusCode = StatusCode.of(inputStatusCodeText);
        //데이터가 오는 경우
        if (DATA == statusCode) {
            String contents = packet.substring(STATUS_CODE_FIELD, STATUS_CODE_FIELD + 4);
            logger.debug("contents information : {}", contents);
            list.add(contents);
        }
        //약속된 Status Code가 아닌 경우
        else if (NACK == statusCode) {
            exceptionCaught(channelHandlerContext,
                    new NotValidStatusCodeException("올바른 Status Code 가 아닙니다."));
        }
    }
}
