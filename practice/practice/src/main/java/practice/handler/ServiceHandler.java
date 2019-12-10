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
import org.springframework.stereotype.Component;
import practice.domain.Message;
import practice.domain.MessageRepository;

import java.util.List;


/**
 * The type Service handler.
 */
@Component
@ChannelHandler.Sharable
public class ServiceHandler extends ChannelInboundHandlerAdapter {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

//    @Resource(name = "messageMapper")
//    private MessageMapper messageMapper;

    @Autowired
    private MessageRepository messageRepository;

    //소켓채널이 최초 활성화 되었을 때(연결되면) 발생하는 이벤트
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive called!!");
//        channels.add(ctx.channel());
    }

    //메세지(데이터)가 들어올 때마다 호출된다
    //데이터 수신 이벤트 처리 메서드, 클라이언트로부터 데이터 수신이 이루어지는 경우 네티가 자동적으로 호출
    //데이터 로직 처리
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf byteBuf = (ByteBuf) msg;    //여기서 임의의 데이터를 생성?? tcp기반 서버가 보낸 순서대로 바이트를 수신할 수 있게 보장한다.
//        System.out.println("message : {} " + byteBuf.toString(Charset.defaultCharset()));
        String stringMessage = (String) msg;
        messageRepository.save(new Message(stringMessage));
        System.out.println(stringMessage);

        List<Message> messages = messageRepository.findAll();

        for (Message message : messages) {
            System.out.println(message.getId() + " : " + message.getContents());
        }
        channels.writeAndFlush(stringMessage);

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        System.out.println("channelReadComplete called!!");
    }
}