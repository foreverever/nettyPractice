package practice.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;


/**
 * The type Service handler.
 */
@Component
@ChannelHandler.Sharable
public class ServiceHandler extends ChannelInboundHandlerAdapter {

    /**
     * The Logger.
     */
    Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The Channels.
     */
    private final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * Channel active.
     *
     * @param ctx the ctx
     * @throws Exception the exception
     */
    //소켓채널이 최초 활성화 되었을 때(연결되면) 발생하는 이벤튼
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("time : " + System.currentTimeMillis());
//        channels.add(ctx.channel());
        System.out.println("endActive!!");
    }

    /**
     * Channel read.
     *
     * @param ctx the ctx
     * @param msg the msg
     * @throws Exception the exception
     */

    //메세지(데이터)가 들어올 때마다 호출된다
    //데이터 수신 이벤트 처리 메서드, 클라이언트로부터 데이터 수신이 이루어지는 경우 네티가 자동적으로 호출
    //데이터 로직 처리
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("msg : " + msg.toString());
        ByteBuf byteBuf = (ByteBuf) msg;    //여기서 임의의 데이터를 생성?? tcp기반 서버가 보낸 순서대로 바이트를 수신할 수 있게 보장한다.
        System.out.println("!!!!!!!!!!!!!!");
        System.out.println("message : {} " + byteBuf.toString(Charset.defaultCharset()));
        channels.writeAndFlush(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        System.out.println("channelReadComplete call!!");
    }
}