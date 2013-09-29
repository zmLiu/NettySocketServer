package com.lzm.netty.decoder;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * ---处理粘包---
 * */
public class BytesDecoder extends ByteToMessageDecoder {
	
	/**
	 * 当前需要读取数据的长度
	 * */
	private int readLength = 4;
	
	/**
	 * 是否读取消息体长度
	 * */
	private boolean readStringLength = true;
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,List<Object> out) throws Exception {
		//消息体不够长 直接返回
		if(in.readableBytes() < readLength) return;
		//读取消息体长度
		if(readStringLength) readLength = in.readInt();
		
		//读取消息体
		if(in.readableBytes() >= readLength){
			out.add(in.readBytes(readLength));
			readLength = 4;
			readStringLength = true;
		}else{
			//消息体不全，下一次继续读取消息体
			readStringLength = false;
		}
		
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)throws Exception {
		ctx.close();
	}

}
