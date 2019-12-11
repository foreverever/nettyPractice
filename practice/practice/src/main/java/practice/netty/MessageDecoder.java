package practice.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MessageDecoder extends ByteToMessageDecoder {
    private static final int PACKET_MAX_SIZE = 16;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() < PACKET_MAX_SIZE) {
            return;
        }
        list.add(byteBuf.readBytes(PACKET_MAX_SIZE));
    }
}
