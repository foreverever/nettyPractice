package practice.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import practice.domain.Message;
import practice.domain.MessageMapper;
import practice.service.MessageService;

import java.net.InetSocketAddress;
import java.time.LocalDateTime;

import static practice.support.StatusCode.OACK;


@Component
@ChannelHandler.Sharable
public class MessageHandler extends ChannelInboundHandlerAdapter {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static final String PACKET_LENGTH = "0004";

    private int handlerCnt = 0;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private MessageService messageService;

    //소켓채널이 최초 활성화 되었을 때(연결되면) 발생하는 이벤트
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Active!!!!!!!!!!!!!!!!");
        ByteBuf oack = ByteBufAllocator.DEFAULT.buffer();
        oack.writeBytes(("0004OACK").getBytes());
        ctx.writeAndFlush(oack);
        channels.add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("channelInactive is called!!");
        channels.remove(ctx.channel());
    }

    //메세지(데이터)가 들어올 때마다 호출된다
    //데이터 수신 이벤트 처리 메서드, 클라이언트로부터 데이터 수신이 이루어지는 경우 네티가 자동적으로 호출
    //데이터 로직 처리
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("startTime" + LocalDateTime.now());
        System.out.println(msg);
        callCnt();
        String content = (String) msg;
        Message message = new Message(findIpAddress(ctx.channel()), Integer.parseInt(content), LocalDateTime.now());
        logger.debug("message information : {}", message);

        //메세지 전달
        ByteBuf oack = ByteBufAllocator.DEFAULT.buffer();
        ctx.writeAndFlush(oack.writeBytes((PACKET_LENGTH + OACK.name()).getBytes()));
        messageService.add(message, Integer.toString(handlerCnt));
    }


    private String findIpAddress(Channel channel) {
        InetSocketAddress socketAddress = (InetSocketAddress) channel.remoteAddress();
        return socketAddress.getAddress().getHostAddress();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("id!!!! : " + ctx.channel().id());
        ctx.flush();
        super.channelReadComplete(ctx);
        logger.debug("messageHandlerCnt : {}", handlerCnt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error(cause.getMessage(), cause);
        ctx.close();
    }

    public synchronized void callCnt() {
        handlerCnt++;
    }
}