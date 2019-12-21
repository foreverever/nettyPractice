package practice.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import practice.netty.decoder.PacketLengthCheckDecoder;
import practice.netty.decoder.StatusCodeCheckDecoder;

@Component
@ChannelHandler.Sharable
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final PacketLengthCheckDecoder MESSAGE_DECODER = new PacketLengthCheckDecoder();

    @Autowired
    private ChannelInboundHandlerAdapter messageHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        logger.debug("channel!!!!! : {}", socketChannel.toString());
        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast(new LoggingHandler(LogLevel.INFO));
        pipeline.addLast(new PacketLengthCheckDecoder());
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new StatusCodeCheckDecoder());
        pipeline.addLast(messageHandler);
    }
}
