package practice.handler;

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

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


/**
 * The type Service handler.
 */
@Component
@ChannelHandler.Sharable
public class ServiceHandler extends ChannelInboundHandlerAdapter {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private RedisTemplate<String, Object> template;

    //소켓채널이 최초 활성화 되었을 때(연결되면) 발생하는 이벤트
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("channelActive is called!!");
        channels.add(ctx.channel());
    }

    //메세지(데이터)가 들어올 때마다 호출된다
    //데이터 수신 이벤트 처리 메서드, 클라이언트로부터 데이터 수신이 이루어지는 경우 네티가 자동적으로 호출
    //데이터 로직 처리
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf byteBuf = (ByteBuf) msg;    //여기서 임의의 데이터를 생성?? tcp기반 서버가 보낸 순서대로 바이트를 수신할 수 있게 보장한다.
//        System.out.println("message : {} " + byteBuf.toString(Charset.defaultCharset()));
        String contents = (String) msg;
        Message message = new Message(contents);
        logger.debug("message : {}", message.getContents());
        logger.debug("messageSize : {}", message.getSize());
        template.opsForHash().putAll("String.valueOf(message.getId())", message.getHashMapValue());


//        messageMapper.save(new Message(stringMessage));
//        List<Message> messages = messageMapper.findAll();
//
//        for (Message message : messages) {
//            logger.debug("message info // id : {} , contents : {}", message.getId(), message.getContents());
//        }
        channels.writeAndFlush(contents);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        System.out.println("channelReadComplete is called!!");
//        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error(cause.getMessage(), cause);
        ctx.close();
    }
}