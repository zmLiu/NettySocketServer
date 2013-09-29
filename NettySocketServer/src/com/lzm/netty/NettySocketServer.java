package com.lzm.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import com.lzm.netty.decoder.BytesDecoder;
import com.lzm.netty.decoder.BytesDecoder_Crossdomain;
import com.lzm.netty.handler.IdleHandler;
import com.lzm.netty.handler.ServerHandler;

public class NettySocketServer {
	//服务器监听端口
	public int port;
	//是否返回策略文件(在web中运行需要)
	public boolean needCrossdomain = true;
	//连接空闲时间
	public int idleTimeSeconds = 60;
	
	/**
	 * 创建服务器
	 * @param	port	监听端口
	 * */
	public NettySocketServer(int port) {
		this.port = port;
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
						ch.pipeline().addLast(new BytesDecoder_Crossdomain(),new ServerHandler(),new IdleStateHandler(idleTimeSeconds, 0, 0),new IdleHandler());
					}else{
						ch.pipeline().addLast(new BytesDecoder(),new ServerHandler(),new IdleStateHandler(idleTimeSeconds, 0, 0),new IdleHandler());
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
