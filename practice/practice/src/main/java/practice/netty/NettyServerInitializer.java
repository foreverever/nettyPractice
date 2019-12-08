package practice.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import practice.handler.ServiceHandler;

@Component
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private ServiceHandler serviceHandler;  //이경우는 안됨. 컴포넌트로 빈등록해주고 주입하면 이상없이 되는데, 서버 실행하면 동작X

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast(new LoggingHandler(LogLevel.INFO));
        pipeline.addLast(serviceHandler);
    }
}
