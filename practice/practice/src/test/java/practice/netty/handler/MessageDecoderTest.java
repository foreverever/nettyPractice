package practice.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;


public class MessageDecoderTest {

    private ByteBuf byteBuf;
    private String inputData;
    private MessageDecoder messageDecoder = new MessageDecoder();

    @Before
    public void setUp() throws Exception {
        byteBuf = PooledByteBufAllocator.DEFAULT.heapBuffer();
    }

    @Test
    public void 유효패킷_분할_데이터_크기_10() {
        inputData = "00100123456789";
        byteBuf.capacity(inputData.length());
        byteBuf.writeBytes(inputData.getBytes());

        int packLength = messageDecoder.calcPacketLength(byteBuf);
        assertThat(packLength).isEqualTo(10);
    }

    @Test
    public void 유효패킷_분할_데이터_크기_5() {
        inputData = "0005abcde";
        byteBuf.capacity(inputData.length());
        byteBuf.writeBytes(inputData.getBytes());

        int packetLength = messageDecoder.calcPacketLength(byteBuf);
        assertThat(packetLength).isEqualTo(5);
    }

    @Test
    public void 유효패킷_분할_데이터_크기_0() {
        inputData = "0000";
        byteBuf.capacity(inputData.length());
        byteBuf.writeBytes(inputData.getBytes());

        int packetLength = messageDecoder.calcPacketLength(byteBuf);
        assertThat(packetLength).isEqualTo(0);
    }
}