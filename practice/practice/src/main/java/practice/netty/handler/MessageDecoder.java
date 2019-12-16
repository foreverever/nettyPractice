package practice.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.List;

public class MessageDecoder extends ByteToMessageDecoder {
    private static final int PACKET_LENGTH_FIELD = 4;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        //length 필드가 짤려서 오는 경우(즉 4바이트를 넘지 못하는 경우 재요청 받야아 한다)
        if (byteBuf.readableBytes() < PACKET_LENGTH_FIELD) {
            logger.debug("렝스필드가 짤려서 온 경우");
            return;
        }

        int packetLength = calcPacketLength(byteBuf);
        if (byteBuf.readableBytes() < packetLength) {
            byteBuf.resetReaderIndex();
            return;
        }
        logger.debug("decode string length : {}", packetLength);
        list.add(byteBuf.readBytes(packetLength));
        byteBuf.markReaderIndex();  //현재 readIndex를 마크, resetReaderIndex()시 마크된 index으로 이동
        logger.debug("byteBufReadableBytes : {} ", byteBuf.readableBytes());
        logger.debug("byteBufCapacity : {}", byteBuf.capacity());

    }

    int calcPacketLength(ByteBuf byteBuf) {
        String length = byteBuf.readBytes(PACKET_LENGTH_FIELD).toString(Charset.defaultCharset());
        logger.debug("length value : {}", length);
        return Integer.parseInt(length);
    }
}
