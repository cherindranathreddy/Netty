import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty5.buffer.api.Buffer;
import io.netty5.channel.ChannelHandler;
import io.netty5.channel.ChannelHandlerContext;

@ChannelHandler.Sharable
public class EchoClientHandler implements ChannelHandler {
    private final String firstMessage = "Hello, Server!";;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Buffer buf = ctx.bufferAllocator().allocate(firstMessage.length());
        buf.writeCharSequence(firstMessage, java.nio.charset.StandardCharsets.UTF_8);
        ctx.writeAndFlush(buf);
    }

    @Override
    public void channelRead(io.netty5.channel.ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(io.netty5.channel.ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(io.netty5.channel.ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
