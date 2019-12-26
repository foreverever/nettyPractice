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
import practice.domain.Message;
import practice.service.MessageService;

import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Random;

import static practice.support.NettyUtils.*;
import static practice.support.StatusCode.*;

@Component
@ChannelHandler.Sharable
public class MessageHandler extends SimpleChannelInboundHandler<String> {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static final String REDIS_KEY = "messages";
    private final static Random GENERATOR = new SecureRandom();

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
        Message message;

        if (content.contains(FAKE.name())) {
            String fakeContent = content.substring(FAKE_COUNT_FIELD, FAKE_COUNT_FIELD + 4);
            message = Message.makeData(makeRandomIpAddress(), Integer.parseInt(fakeContent), LocalDateTime.now());
        } else {
            message = Message.makeData(findIpAddress(ctx.channel()), Integer.parseInt(content), LocalDateTime.now());
        }
        logger.debug("messageOfRedis information : {}", message);
        ctx.write(PACKET_LENGTH + OACK.name());

        /*하나의 키를 사용하기보다는 키를 분산할 수 있는 방법을 생각해보면 좋아요 */
        /*방법 예시
            Sharding 생각해보기
            키 단위를 재생각
         */
        messageService.add(REDIS_KEY, message);
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
        response = "000" + response.length() + response;
        logger.error(cause.getMessage(), cause);
        ctx.writeAndFlush(response);
//        ctx.close();
    }

    private String findIpAddress(Channel channel) {
        InetSocketAddress socketAddress = (InetSocketAddress) channel.remoteAddress();
        return socketAddress.getAddress().getHostAddress();
    }

    /*
    Random, SecureRandom 차이점 알아보면 좋아요
    Random 객체는 매번 생성하지 않아도 되요
    */
    private String makeRandomIpAddress() {
        StringBuilder fakeIpAddress = new StringBuilder();
        fakeIpAddress.append(192).append('.')
                .append(GENERATOR.nextInt(IP_ADDRESS_RANGE))
                .append('.')
                .append(GENERATOR.nextInt(IP_ADDRESS_RANGE))
                .append('.')
                .append(GENERATOR.nextInt(IP_ADDRESS_RANGE));
        return fakeIpAddress.toString();
    }
}