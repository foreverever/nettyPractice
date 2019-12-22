package practice.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public abstract class SimpleOutBoundHandler<T> extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        write0(ctx, (String) msg, promise);
    }

    protected abstract void write0(ChannelHandlerContext ctx, String msg, ChannelPromise promise);
}
