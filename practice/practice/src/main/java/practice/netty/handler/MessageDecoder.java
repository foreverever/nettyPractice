package practice.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.List;

public class MessageDecoder extends ByteToMessageDecoder {
    private static final int PACKET_LENGTH = 4;

    Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        int packetLength = calcPacketLength(byteBuf);

        if (byteBuf.readableBytes() < packetLength) {
            return;
        }
        logger.debug("decode string length : {}", packetLength);
        list.add(byteBuf.readBytes(packetLength));
    }

    int calcPacketLength(ByteBuf byteBuf) {
        String length = byteBuf.readBytes(PACKET_LENGTH).toString(Charset.defaultCharset());
        logger.debug("length value : {}", length);
        return Integer.parseInt(length);
    }
}
