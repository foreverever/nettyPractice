package practice.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.*;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import practice.handler.MessageHandler;

import java.util.List;

@Component
@ChannelHandler.Sharable
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

    private static final StringDecoder DECODER = new StringDecoder();
    private static final StringEncoder ENCODER = new StringEncoder();

    @Autowired
    private MessageHandler messageHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast(new ByteToMessageDecoder() {
            @Override
            protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
                list.add(byteBuf.readBytes(byteBuf.readableBytes()));
            }
        });
        pipeline.addLast(new LoggingHandler(LogLevel.INFO));
        pipeline.addLast(new MessageDecoder());
        pipeline.addLast(DECODER);
        pipeline.addLast(ENCODER);
        pipeline.addLast(messageHandler);
    }
}
