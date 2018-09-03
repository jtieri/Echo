package com.esoteric.echo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {
    public static void main(String[] args) throws Exception {
        EchoServer.create(12321).start();
    }

    private final int port;

    private ServerBootstrap bootstrap;
    private EventLoopGroup group;
    private EchoServerHandler serverHandler;

    private EchoServer(int port) {
        this.port = port;
    }

    public static EchoServer create(int port) {
        return new EchoServer(port);
    }

    public void start() throws Exception{
        this.serverHandler = new EchoServerHandler();
        this.group = new NioEventLoopGroup();

        try {
            this.bootstrap = new ServerBootstrap();
            this.bootstrap.group(group)
                    .channel(NioServerSocketChannel.class) // Use NIO transport channel
                    .localAddress(new InetSocketAddress(port)) // Set socket address
                    .childHandler(new ChannelInitializer<SocketChannel>() { // Add EchoServerHandler
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(serverHandler);
                        }
                    });

            ChannelFuture future = bootstrap.bind().sync(); // Binds the server asynchronously & waits for the bind to complete
            future.channel().closeFuture().sync(); // Get close future and block thread till complete
        } finally {
            group.shutdownGracefully().sync(); // Shutdown and release all resources
        }
    }
}
