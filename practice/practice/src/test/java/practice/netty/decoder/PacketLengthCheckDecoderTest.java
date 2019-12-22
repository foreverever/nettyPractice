package practice.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;


public class PacketLengthCheckDecoderTest {

    private ByteBuf byteBuf;
    private String inputData;
    private PacketLengthCheckDecoder packetLengthCheckDecoder = new PacketLengthCheckDecoder();
    private ChannelHandlerContext ctx;

    @Before
    public void setUp() throws Exception {
        byteBuf = PooledByteBufAllocator.DEFAULT.heapBuffer();
    }

    @Test
    public void 유효패킷_분할_데이터_크기_10() throws Exception {
        inputData = "00100123456789";
        byteBuf.capacity(inputData.length());
        byteBuf.writeBytes(inputData.getBytes());

        int packLength = packetLengthCheckDecoder.calcPacketLength(byteBuf, ctx);
        assertThat(packLength).isEqualTo(10);
    }

    @Test
    public void 유효패킷_분할_데이터_크기_5() throws Exception {
        inputData = "0005abcde";
        byteBuf.capacity(inputData.length());
        byteBuf.writeBytes(inputData.getBytes());

        int packetLength = packetLengthCheckDecoder.calcPacketLength(byteBuf, ctx);
        assertThat(packetLength).isEqualTo(5);
    }

    @Test
    public void 유효패킷_분할_데이터_크기_0() throws Exception {
        inputData = "0000";
        byteBuf.capacity(inputData.length());
        byteBuf.writeBytes(inputData.getBytes());

        int packetLength = packetLengthCheckDecoder.calcPacketLength(byteBuf, ctx);
        assertThat(packetLength).isEqualTo(0);
    }
}