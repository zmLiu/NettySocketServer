package com.lzm.netty.socket
{
	import flash.events.Event;
	
	public class SocketEvent extends Event
	{
		/**连接成功*/
		public static const CONNECT:String = "NettySocket_Connect";
		/**连接关闭*/
		public static const CLOSE:String = "NettySocket_Close";
		/**接收到数据*/
		public static const DATA:String = "NettySocket_Data";
		/**沙箱错误*/
		public static const SECURITY_ERROR:String = "NettySocket_Security_Error";
		/**IO错误*/
		public static const IO_ERROR:String = "NettySocket_IO_Error";
		
		public var data:Packet;
		
		public function SocketEvent(type:String, data:Packet=null, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
			this.data = data;
		}
	}
}