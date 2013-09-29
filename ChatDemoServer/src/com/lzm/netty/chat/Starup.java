package com.lzm.netty.chat;

import com.lzm.netty.NettyServer;
import com.lzm.netty.chat.command.ChatMessageCommand;
import com.lzm.netty.chat.command.ConnectCloseCommand;
import com.lzm.netty.chat.command.LoginCommand;
import com.lzm.netty.chat.server.Cmds;
import com.lzm.netty.handler.ServerHandler;

public class Starup {
	public static void main(String[] args) {
		try {
			
			ServerHandler.registerConnectCloseCommand(new ConnectCloseCommand());
			
			ServerHandler.registerCommand(Cmds.login, new LoginCommand());
			ServerHandler.registerCommand(Cmds.chatMessage, new ChatMessageCommand());
			
			if(args.length > 0){
				new NettyServer(Integer.valueOf(args[0]), true).run();
			}else{
				new NettyServer(8888, true).run();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
