import io.netty5.bootstrap.Bootstrap;
import io.netty5.channel.ChannelInitializer;
import io.netty5.channel.nio.NioHandler;
import io.netty5.channel.socket.SocketChannel;
import io.netty5.channel.socket.nio.NioSocketChannel;
import io.netty5.channel.*;
import io.netty5.channel.MultithreadEventLoopGroup;
import io.netty5.handler.logging.LogLevel;
import io.netty5.handler.logging.LoggingHandler;
import io.netty5.util.concurrent.Future;

class EchoClient {
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8008"));

    public static void main(String[] args) throws Exception {
        // Configure the client.
        EventLoopGroup group = new MultithreadEventLoopGroup(NioHandler.newFactory());
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });

            // Start the client.
            Future<Channel> f = b.connect(HOST, PORT).sync();

            // Wait until the connection is closed.
            f.get().closeFuture().sync();
        } finally {
            // Shut down the event loop to terminate all threads.
            group.shutdownGracefully();
        }
    }
}