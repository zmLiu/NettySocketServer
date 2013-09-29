package com.lzm.netty.chat.command;

import io.netty.channel.ChannelHandlerContext;

import com.lzm.netty.chat.data.User;
import com.lzm.netty.chat.server.ChatServer;
import com.lzm.netty.chat.server.Cmds;
import com.lzm.netty.command.ICommand;

public class ChatMessageCommand implements ICommand {

	@Override
	public int getCmd() {
		return Cmds.chatMessage;
	}

	@Override
	public void execute(ChannelHandlerContext ctx, String[] msgs)throws Exception {
		User user = ChatServer.getUserByCtx(ctx);
		if(user != null){
			String []sendMsgs = new String[]{user.name,msgs[0]};
			ChatServer.sendToAll(getCmd(), sendMsgs);
		}
	}

}
