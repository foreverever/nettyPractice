package practice.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.List;

public class PacketLengthCheckDecoder extends ByteToMessageDecoder {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final int PACKET_LENGTH_FIELD = 4;

    private int decodeCnt = 0;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        logger.debug("decode byteBuf : {}", byteBuf.toString(Charset.defaultCharset()));

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

        logger.debug("data length : {}", packetLength);
        list.add(byteBuf.readBytes(packetLength));
        byteBuf.markReaderIndex();  //현재 readIndex를 마크, resetReaderIndex()시 마크된 index으로 이동
        decodeCnt++;
        logger.debug("decodeCnt : {}", decodeCnt);
    }

    int calcPacketLength(ByteBuf byteBuf) {
        String length = byteBuf.readBytes(PACKET_LENGTH_FIELD).toString(Charset.defaultCharset());
        return Integer.parseInt(length);
    }
}
