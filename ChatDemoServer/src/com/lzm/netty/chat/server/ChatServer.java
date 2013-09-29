package com.lzm.netty.chat.server;

import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import com.lzm.netty.chat.data.User;
import com.lzm.netty.handler.ServerHandler;


public class ChatServer {
	private static ConcurrentHashMap<ChannelHandlerContext, User> users = new ConcurrentHashMap<ChannelHandlerContext, User>();
	
	//添加用户
	public static void addUser(User user){
		users.put(user.ctx, user);
	}
	
	//根据ctx获取一个用户
	public static User getUserByCtx(ChannelHandlerContext ctx){
		return users.get(ctx);
	}
	
	//移除用户
	public static void removeUser(ChannelHandlerContext ctx){
		users.remove(ctx);
	}
	
	//是否登陆
	public static boolean isLogin(ChannelHandlerContext ctx){
		return users.get(ctx) != null;
	}
	
	//全服广播
	public static void sendToAll(int cmd,String []msgs) throws UnsupportedEncodingException{
		Iterator<ChannelHandlerContext> iterator = users.keySet().iterator();
		while(iterator.hasNext()){
			ServerHandler.sendMessages(iterator.next(), cmd, msgs);
		}
	}
	
}
