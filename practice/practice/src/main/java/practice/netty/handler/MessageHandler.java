package practice.netty.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import practice.domain.redis.MessageOfRedis;
import practice.exception.DataContentException;
import practice.service.MessageService;

import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.Random;

import static practice.support.NettyUtils.*;
import static practice.support.StatusCode.*;

@Component
@ChannelHandler.Sharable
public class MessageHandler extends SimpleChannelInboundHandler<String> {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Autowired
    private MessageService messageService;

    //소켓채널이 최초 활성화 되었을 때(연결되면) 발생하는 이벤트
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.write(PACKET_LENGTH + OACK.name());
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
        if (isNotNumber(content)) {
            exceptionCaught(ctx, new DataContentException("wrdc"));    //wrong data content -> data를 해석할 수 없는 경우(문자 포함 한 경우)
            return;
        }

        MessageOfRedis messageOfRedis;

        if (content.contains(FAKE.name())) {
            String fakeContent = content.substring(FAKE_COUNT_FIELD, FAKE_COUNT_FIELD + 4);
            messageOfRedis = new MessageOfRedis(makeRandomIpAddress(new Random()), Integer.parseInt(fakeContent), LocalDateTime.now());
        } else {
            messageOfRedis = new MessageOfRedis(findIpAddress(ctx.channel()), Integer.parseInt(content), LocalDateTime.now());
        }
        logger.debug("messageOfRedis information : {}", messageOfRedis);
        ctx.write(PACKET_LENGTH + OACK.name());
        messageService.add(messageOfRedis);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.debug("exceptionCaught is called!!!");
        String response = NACK.name() + cause.getMessage();
        //todo 하드코딩 수정필요
        response = "00" + response.length() + response;
        logger.error(cause.getMessage(), cause);
        ctx.writeAndFlush(response);
        ctx.close();
    }

    private String findIpAddress(Channel channel) {
        InetSocketAddress socketAddress = (InetSocketAddress) channel.remoteAddress();
        return socketAddress.getAddress().getHostAddress();
    }

    private String makeRandomIpAddress(Random random) {
        StringBuffer fakeIpAddress = new StringBuffer();
        fakeIpAddress.append(192).append('.')
                .append(random.nextInt(IP_ADDRESS_RANGE))
                .append('.')
                .append(random.nextInt(IP_ADDRESS_RANGE))
                .append('.')
                .append(random.nextInt(IP_ADDRESS_RANGE));

        return fakeIpAddress.toString();
    }
}