package com.lzm.netty.socket
{
	import flash.utils.ByteArray;

	public class Packet
	{
		private var bytes:ByteArray;
		
		public function Packet(bytes:ByteArray=null){
			if(bytes != null){
				this.bytes = bytes;
			}else{
				this.bytes = new ByteArray();
			}
		}
		
		public function writeShort(value:int):void{
			bytes.writeShort(value);
		}
		public function writeInt(value:int):void{
			bytes.writeInt(value);
		}
		public function writeFloat(value:Number):void{
			bytes.writeFloat(value);
		}
		public function writeDouble(value:Number):void{
			bytes.writeDouble(value);
		}
		public function writeByte(value:int):void{
			bytes.writeByte(value);
		}
		public function writeBytes(b:ByteArray):void{
			bytes.writeBytes(b);
		}
		public function writeString(value:String,charset:String="utf-8"):void{
			var b:ByteArray = new ByteArray();
			b.writeMultiByte(value, charset);
			var len:int = b.length;
			bytes.writeInt(len);
			bytes.writeBytes(b);
		}
		
		
		public function readShort():int{
			return bytes.readShort();
		}
		public function readInt():int{
			return  bytes.readInt();
		}
		public function readFloat():Number{
			return bytes.readFloat();
		}
		public function readDouble():Number{
			return bytes.readDouble();
		}
		public function readByte():int{
			return bytes.readByte();
		}
		public function readBytes():ByteArray{
			var b:ByteArray = new ByteArray();
			bytes.readBytes(b);
			return b;
		}
		public function readString(charset:String="utf-8"):String{
			var str_len:int = bytes.readInt();
			return bytes.readMultiByte(str_len, charset);
		}
		
		public function clear():void{
			bytes.clear();
		}
		
		public function array():ByteArray{
			return bytes;
		}
	}
}