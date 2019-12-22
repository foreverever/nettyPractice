package practice.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendHandler extends SimpleOutBoundHandler<String> {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void write0(ChannelHandlerContext ctx, String msg, ChannelPromise promise) {
        logger.debug("msg value : {}", msg);
        ctx.writeAndFlush(makeResponse(msg));
    }

    private ByteBuf makeResponse(String response) {
        return ByteBufAllocator.DEFAULT.buffer()
                .writeBytes(response.getBytes());
    }
}
