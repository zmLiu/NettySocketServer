package com.lzm.netty.command;

import io.netty.channel.ChannelHandlerContext;

public interface ICommand {
	/**
	 * 获取命令号
	 * */
	int getCmd();
	
	/**
	 * 接受到消息
	 * */
	void execute(ChannelHandlerContext ctx,String []msgs) throws Exception;
}
