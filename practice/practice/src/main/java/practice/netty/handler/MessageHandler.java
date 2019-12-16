package practice.netty.handler;

import io.netty.buffer.ByteBuf;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import practice.domain.Message;
import practice.domain.MessageMapper;
import practice.netty.NettyServer;
import practice.service.MessageService;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@ChannelHandler.Sharable
public class MessageHandler extends ChannelInboundHandlerAdapter {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    List<Message> messageList = new ArrayList<>();

    private final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static int totalCnt = 0;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    MessageService messageService;

    //소켓채널이 최초 활성화 되었을 때(연결되면) 발생하는 이벤트
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        logger.debug("channelActive is called!!");
        channels.add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        logger.debug("channelInactive is called!!");
        channels.remove(ctx.channel());
    }

    //메세지(데이터)가 들어올 때마다 호출된다
    //데이터 수신 이벤트 처리 메서드, 클라이언트로부터 데이터 수신이 이루어지는 경우 네티가 자동적으로 호출
    //데이터 로직 처리
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf byteBuf = (ByteBuf) msg;    //tcp기반 서버가 보낸 순서대로 바이트를 수신할 수 있게 보장한다.
//        System.out.println("message : {} " + byteBuf.toString(Charset.defaultCharset()));
//        logger.debug("channelRead is called!!");
//        String packet = (String) msg;
//        System.out.println("message id : " + ctx.channel().id() + " - " + packet);
//        System.out.println("!!!!!!!!!!:" + msg);

        //todo : packet을 유효 데이터 단위로 쪼개야함 (IP Address/Contents/RequestTime)
//        Message message = new Message(packet);
//        logger.debug("message : {}", message.getContents());
//        logger.debug("messageSize : {}", message.getSize());
//        logger.debug("messageId : {}", message.getId());

        //todo : 레디스 연결부는 서비스 레이어를 따로 만들어서 처리하는게 깔끔할 듯?

        //메세지 전달
//        channels.writeAndFlush(message);
    }

    private String findIpAddress(Channel channel) {
        InetSocketAddress socketAddress = (InetSocketAddress) channel.remoteAddress();
        return socketAddress.getAddress().getHostAddress();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        logger.debug("totalCnt : {}", totalCnt);
//        logger.debug("channelReadComplete is called!!");
//        NettyServer.channel.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error(cause.getMessage(), cause);
        ctx.close();
    }
}