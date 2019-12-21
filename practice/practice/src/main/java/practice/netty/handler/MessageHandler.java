package practice.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
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
public class MessageHandler extends SimpleChannelInboundHandler<String> {

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
        ctx.writeAndFlush(makeResponse("0004OACK"));
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
    protected void channelRead0(ChannelHandlerContext ctx, String content) {
        System.out.println("startTime" + LocalDateTime.now());
        System.out.println(content);
        callCnt();
        Message message = new Message(findIpAddress(ctx.channel()), Integer.parseInt(content), LocalDateTime.now());
        logger.debug("message information : {}", message);

        ctx.writeAndFlush(makeResponse(PACKET_LENGTH + OACK.name()));
        messageService.add(message);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("id!!!! : " + ctx.channel().id());
        super.channelReadComplete(ctx);
        logger.debug("messageHandlerCnt : {}", handlerCnt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error(cause.getMessage(), cause);
        ctx.writeAndFlush(makeResponse("0004NACK"));
        ctx.close();
    }

    private ByteBuf makeResponse(String response) {
        return ByteBufAllocator.DEFAULT.buffer()
                .writeBytes(response.getBytes());
    }

    private String findIpAddress(Channel channel) {
        InetSocketAddress socketAddress = (InetSocketAddress) channel.remoteAddress();
        return socketAddress.getAddress().getHostAddress();
    }

    public synchronized void callCnt() {
        handlerCnt++;
    }
}