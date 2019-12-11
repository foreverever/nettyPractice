package practice.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.Arrays;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class ServiceHandlerTest {
    private ByteBuf byteBuf;
    private String inputDate;

    @Before
    public void setUp() throws Exception {
        byteBuf = PooledByteBufAllocator.DEFAULT.heapBuffer(11);    //버퍼풀 : 생성된 버퍼 재사용 가능, 버퍼를 매번 할당하고 해제할때 GC작업 횟수 줄어드는 장점
//        byteBuf = Unpooled.buffer(11);  //버퍼풀X
    }

    @Test
    public void ByteBuffSizeTest() {
        assertThat(byteBuf.capacity()).isEqualTo(11);
    }

    @Test
    public void ByteBuffWriteTest() {
        inputDate = "hello world";
        byteBuf.writeBytes(inputDate.getBytes());
        assertThat(byteBuf.readableBytes()).isEqualTo(11);
        assertThat(byteBuf.writableBytes()).isEqualTo(0);
        assertThat("hello world").isEqualTo(byteBuf.toString(Charset.defaultCharset()));
    }

    @Test
    public void getBytes() {
        inputDate = "hello world";
        System.out.println(Arrays.toString(inputDate.getBytes()));  //아스키코드
    }

    @Test
    public void timeStamp() {
        StringBuilder sb = new StringBuilder();
        System.out.println(sb.length());
        sb.append("asd");
        sb.append("qwe");
        sb.insert(0,sb.length());
        System.out.println(sb.toString());
    }
}
