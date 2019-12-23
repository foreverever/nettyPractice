package practice.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import practice.exception.HeaderContentException;

import java.nio.charset.Charset;
import java.util.List;

import static practice.support.NettyUtils.PACKET_LENGTH_FIELD;
import static practice.support.NettyUtils.isNotNumber;

public class PacketLengthCheckDecoder extends ByteToMessageDecoder {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private int decodeCnt = 0;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        logger.debug("decode byteBuf : {}", byteBuf.toString(Charset.defaultCharset()));

        //length 필드가 짤려서 오는 경우(즉 4바이트를 넘지 못하는 경우 재요청 받야아 한다)
        if (byteBuf.readableBytes() < PACKET_LENGTH_FIELD) {
            logger.debug("렝스필드가 짤려서 온 경우");
            return;
        }

        int packetLength = calcPacketLength(byteBuf, ctx);

        if (byteBuf.readableBytes() < packetLength) {
            byteBuf.resetReaderIndex();
            return;
        }

        logger.debug("data length : {}", packetLength);
        list.add(byteBuf.readBytes(packetLength));
        byteBuf.markReaderIndex();  //현재 readIndex를 마크, resetReaderIndex()시 마크된 index으로 이동
        decodeCnt++;
        logger.debug("decodeCnt : {}", decodeCnt);
    }

    int calcPacketLength(ByteBuf byteBuf, ChannelHandlerContext ctx) throws Exception {
        String length = byteBuf.readBytes(PACKET_LENGTH_FIELD).toString(Charset.defaultCharset());
        if (isNotNumber(length)) {
            exceptionCaught(ctx, new HeaderContentException("wrhc"));    //wrong header content -> header(body length)를 해석할 수 없음
        }
        return Integer.parseInt(length);
    }
}
