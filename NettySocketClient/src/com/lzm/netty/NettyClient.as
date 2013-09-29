package com.lzm.netty
{
	import com.lzm.netty.socket.NettySocket;
	import com.lzm.netty.socket.Packet;
	import com.lzm.netty.socket.SocketEvent;
	
	import flash.utils.ByteArray;
	

	public class NettyClient
	{
		
		private var _host:String;
		private var _port:int;
		private var _socket:NettySocket;
		
		private var _isConnect:Boolean = false;
		
		private var _commands:Object;
		
		public var onConnectFun:Function;//连接成功的回掉方法
		public var onCloseFun:Function;//连接关闭的回掉方法
		
		public function NettyClient(host:String,port:int){
			_host = host;
			_port = port;
			_socket = new NettySocket();
			_commands = new Object();
		}
		
		public function connect():void{
			_socket.connect(_host,_port);
			
			_socket.addEventListener(SocketEvent.CONNECT,onConnect);
			_socket.addEventListener(SocketEvent.CLOSE,onClose);
			_socket.addEventListener(SocketEvent.DATA,onData);
			_socket.addEventListener(SocketEvent.SECURITY_ERROR,onSecurityError);
			_socket.addEventListener(SocketEvent.IO_ERROR,onIOError);
		}
		
		/**
		 * 连接成功
		 * */
		private function onConnect(e:SocketEvent):void{
			_isConnect = true;
			if(onConnectFun) onConnectFun();
		}
		
		/**
		 * 连接关闭
		 * */
		private function onClose(e:SocketEvent):void{
			_isConnect = false;
			
			_socket.removeEventListener(SocketEvent.CONNECT,onConnect);
			_socket.removeEventListener(SocketEvent.CLOSE,onClose);
			_socket.removeEventListener(SocketEvent.DATA,onData);
			_socket.removeEventListener(SocketEvent.SECURITY_ERROR,onSecurityError);
			_socket.removeEventListener(SocketEvent.IO_ERROR,onIOError);
			
			if(onCloseFun) onCloseFun();
		}
		
		/**
		 * 接收到数据
		 * */
		private function onData(e:SocketEvent):void{
			var data:Packet = e.data;
			var commandId:int = data.readInt();
			var msgs:Array = parseData(data);
			var vector:Vector.<Function> = _commands[commandId];
			if(vector){
				var length:int = vector.length;
				for (var i:int = 0; i < length; i++) {
					vector[i](msgs);
				}
			}
		}
		
		/**
		 * 数据解析为数组
		 * */
		private function parseData(data:Packet):Array{
			var msgs:Array = [];
			var length:int = data.readInt();
			for (var i:int = 0; i < length; i++) {
				msgs.push(data.readString());
			}
			return msgs;
		}
		
		/**
		 * 沙箱错误
		 * */
		private function onSecurityError(e:SocketEvent):void{}
		
		/**
		 * io错误
		 * */
		private function onIOError(e:SocketEvent):void{}
		
		
		/**
		 * 注册命令 
		 * @param commandId 命令的id
		 * @param callBack	接受到该命令的回调
		 */
		public function registerCommand(commandId:int,callBack:Function):void{
			var vector:Vector.<Function> = _commands[commandId];
			if(vector == null){
				vector = new Vector.<Function>();
			}
			vector.push(callBack);
			_commands[commandId] = vector;			
		}
		
		/**
		 * 注销命令
		 */		
		public function removeCommand(commandId:int,callBack:Function=null):void{
			if(callBack == null){
				delete _commands[commandId];
			}else{
				var vector:Vector.<Function> = _commands[commandId];
				if(vector != null){
					var length:int = vector.length;
					for (var i:int = 0; i < length; i++) {
						if(vector[i] == callBack){
							vector.splice(i,1);
							break;
						}
					}
				}
			}
		}
		
		/**
		 * 发送消息
		 * */
		public function sendMessages(cmd:int,msgs:Array):void{
			var data:Packet = new Packet();
			var length:int = msgs.length;
			
			data.writeInt(cmd);
			data.writeInt(length);
			
			for (var i:int = 0; i < length; i++) {
				data.writeString(msgs[i]);
			}
			
			var sendBytes:ByteArray = new ByteArray();
			sendBytes.writeInt(data.array().length);
			sendBytes.writeBytes(data.array());
			
			_socket.writeBytes(sendBytes);
			_socket.flush();
		}
		
		/**
		 * 是否连接
		 * */
		public function get isConnect():Boolean{
			return _isConnect;
		}
		
	}
}