package com.lzm.netty.chat.command;

import io.netty.channel.ChannelHandlerContext;

import com.lzm.netty.chat.data.User;
import com.lzm.netty.chat.server.ChatServer;
import com.lzm.netty.chat.server.Cmds;
import com.lzm.netty.command.ICommand;

public class LoginCommand implements ICommand {

	@Override
	public void execute(ChannelHandlerContext ctx, String[] msgs)throws Exception {
		User user = new User();
		user.name = msgs[0];
		user.ctx = ctx;

		ChatServer.addUser(user);
		
		ChatServer.sendToAll(getCmd(), new String[]{user.name + " º”»Î¡ƒÃÏ “"});
		
		System.out.println(user.name + " Connected");
	}

	@Override
	public int getCmd() {
		return Cmds.login;
	}

}
