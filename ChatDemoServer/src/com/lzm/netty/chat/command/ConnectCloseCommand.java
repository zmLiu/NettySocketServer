package com.lzm.netty.chat.command;

import io.netty.channel.ChannelHandlerContext;

import com.lzm.netty.chat.data.User;
import com.lzm.netty.chat.server.ChatServer;
import com.lzm.netty.chat.server.Cmds;
import com.lzm.netty.command.ICommand;

public class ConnectCloseCommand implements ICommand {

	@Override
	public int getCmd() {
		return Cmds.leave;
	}

	@Override
	public void execute(ChannelHandlerContext ctx, String[] msgs)throws Exception {
		User user = ChatServer.getUserByCtx(ctx);
		
		if(user != null){
			ChatServer.removeUser(ctx);
			
			ChatServer.sendToAll(getCmd(), new String[]{user.name + " Àë¿ªÁÄÌìÊÒ"});
			
			System.out.println(user.name + " ConnectClose");
		}
	}

}
