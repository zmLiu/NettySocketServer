package com.lzm.netty.handler;

import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.lzm.netty.command.ICommand;
import com.lzm.utils.LogError;

/**
 * 命令执行线程
 * */
public class CommandExecuteThread extends Thread {

	private static ConcurrentLinkedQueue<Object[]> taskQueue = new ConcurrentLinkedQueue<Object[]>();
	
	private static CommandExecuteThread []threads;
	
	// 初始化命令执行线程
	public static void initExecuteCommandThread(int threadCount) throws Exception {
		if(threads != null){
			throw new Exception("ExecuteCommandThread already init.");
		}
		threads = new CommandExecuteThread[threadCount];
		for (int i = 0; i < threadCount; i++) {
			CommandExecuteThread thread = new CommandExecuteThread();
			thread.start();
			threads[i] = thread;
		}
	}

	//添加任务
	public static void addTask(ICommand command, ChannelHandlerContext ctx,String[] msgs) {
		taskQueue.add(new Object[] { command, ctx, msgs });
	}

	@Override
	public void run() {
		ICommand command;
		ChannelHandlerContext ctx;
		String[] msgs;
		while (true) {
			try {
				Object[] objects = taskQueue.poll();
				if (objects == null) {
					command = null;
					ctx = null;
					msgs = null;
					Thread.sleep(100L);
				} else {
					command = (ICommand) objects[0];
					ctx = (ChannelHandlerContext) objects[1];
					msgs = (String[]) objects[2];
					command.execute(ctx, msgs);
				}
			} catch (Exception e) {
				try {
					Thread.sleep(100L);
				} catch (InterruptedException e1) {
					LogError.error(e1);
				}
				LogError.error(e);
			}
		}
	}
}
