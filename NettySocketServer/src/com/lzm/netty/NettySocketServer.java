package com.lzm.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import com.lzm.netty.decoder.BytesDecoder;
import com.lzm.netty.decoder.BytesDecoder_Crossdomain;
import com.lzm.netty.handler.ServerHandler;

public class NettySocketServer {
	private int port;
	private boolean needCrossdomain;
	
	/**
	 * 创建服务器
	 * @param	port	监听端口
	 * @param	needCrossdomain	是否返回策略文件(在web中运行需要)
	 * */
	public NettySocketServer(int port,boolean needCrossdomain) {
		this.port = port;
		this.needCrossdomain = needCrossdomain;
	}
	
	public void run() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch)throws Exception {
					if(needCrossdomain){
						ch.pipeline().addLast(new BytesDecoder_Crossdomain(),new ServerHandler());
					}else{
						ch.pipeline().addLast(new BytesDecoder(),new ServerHandler());
					}
				}
			}).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

			ChannelFuture f = b.bind(port).sync();
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
		
	}
	
}
