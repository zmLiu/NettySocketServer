package com.lzm.netty.socket
{
	import flash.events.Event;
	import flash.events.IOErrorEvent;
	import flash.events.ProgressEvent;
	import flash.events.SecurityErrorEvent;
	import flash.net.Socket;
	import flash.utils.ByteArray;

	public class NettySocket extends Socket
	{
		private var _readLength:int = 4;//当前需要读取数据的长度
		private var _readStringLength:Boolean = true;//是否读取消息体长度
		private var _firstMessage:Boolean = true;//是否为第一次接受到消息
		
		public function NettySocket()
		{
			addListeners();
		}
		
		private function addListeners():void{
			addEventListener(Event.CONNECT,connectHanler);
			addEventListener(Event.CLOSE,closeHanler);
			addEventListener(ProgressEvent.SOCKET_DATA,receivedHandler);
			addEventListener(SecurityErrorEvent.SECURITY_ERROR,securityErrorHandler);
			addEventListener(IOErrorEvent.IO_ERROR,ioErrorHanler);
		}
		
		private function removeListeners():void{
			removeEventListener(Event.CONNECT,connectHanler);
			removeEventListener(Event.CLOSE,closeHanler);
			removeEventListener(ProgressEvent.SOCKET_DATA,receivedHandler);
			removeEventListener(SecurityErrorEvent.SECURITY_ERROR,securityErrorHandler);
			removeEventListener(IOErrorEvent.IO_ERROR,ioErrorHanler);
		}
		
		/**
		 * 连接成功
		 * */
		private function connectHanler(e:Event):void{
			writeMultiByte("<policy-file-request/>\n", "utf-8");
			flush();
		}
		
		/**
		 * 连接关闭
		 * */
		private function closeHanler(e:Event):void{
			dispatchEvent(new SocketEvent(SocketEvent.CLOSE));
		}
		
		/**
		 * 接收到数据
		 * */
		private function receivedHandler(e:ProgressEvent):void{
			if(_firstMessage){//第一次收到的是策略策略文件
				readBytes(new ByteArray(),0,bytesAvailable);
				_firstMessage = false;
				dispatchEvent(new SocketEvent(SocketEvent.CONNECT));//收到策略文件才算连接成功
			}else if(bytesAvailable >= _readLength){//需要有足够的可读字节
				//读取消息体长度
				if(_readStringLength) _readLength = readInt();
				
				//读取消息体
				if(bytesAvailable >= _readLength){
					var data:ByteArray = new ByteArray();
					readBytes(data,0,_readLength);
					dispatchEvent(new SocketEvent(SocketEvent.DATA,new Packet(data)));
					
					_readLength = 4;
					_readStringLength = true;
				}else{
					//消息体不全，下一次继续读取消息体
					_readStringLength = false;
				}
			}
		}
		
		/**
		 * 沙箱错误
		 * */
		private function securityErrorHandler(e:SecurityErrorEvent):void{
			dispatchEvent(new SocketEvent(SocketEvent.SECURITY_ERROR));
		}
		
		/**
		 * io错误
		 * */
		private function ioErrorHanler(e:IOErrorEvent):void{
			dispatchEvent(new SocketEvent(SocketEvent.IO_ERROR));
		}
		
	}
}