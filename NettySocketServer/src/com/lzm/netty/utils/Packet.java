package com.lzm.netty.utils;

import io.netty.buffer.ByteBuf;

import java.io.UnsupportedEncodingException;

public class Packet {
	
	private ByteBuf buf;

	public Packet(ByteBuf buf) {
		this.buf = buf;
	}

	public void writeChar(char value) {
		buf.writeChar(value);
	}

	public void writeByte(byte value) {
		buf.writeByte(value);
	}

	public void writeFloat(float value) {
		buf.writeFloat(value);
	}

	public void writeLong(long value) {
		buf.writeLong(value);
	}

	public void writeDouble(double value) {
		buf.writeDouble(value);
	}

	public void writeInt(int value) {
		buf.writeInt(value);
	}

	public void writeShort(short value) {
		buf.writeShort(value);
	}

	public void writeBytes(byte[] bytes) {
		
	}

	public void writeString(String str) {
		byte[] str_bytes = str.getBytes();
		int len = str_bytes.length;
		buf.writeInt(len);
		buf.writeBytes(str_bytes);
	}

	public void writeString(String str, String charset) throws UnsupportedEncodingException {
		byte[] str_bytes = str.getBytes(charset);
		int len = str_bytes.length;
		buf.writeInt(len);
		buf.writeBytes(str_bytes);
	}

	public char readChar() {
		return buf.readChar();
	}

	public byte readByte() {
		return buf.readByte();
	}

	public float readFloat() {
		return buf.readFloat();
	}

	public long readLong() {
		return buf.readLong();
	}

	public double readDouble() {
		return buf.readDouble();
	}

	public int readInt() {
		return buf.readInt();
	}

	public short readShort() {
		return buf.readShort();
	}

	public String readString() {
		int len = buf.readInt();
		byte[] _bytes = new byte[len];
		buf.readBytes(_bytes);
		return new String(_bytes);
	}

	public String readString(String charset) throws UnsupportedEncodingException {
		int len = buf.readInt();
		byte[] _bytes = new byte[len];
		buf.readBytes(_bytes);
		return new String(_bytes, charset);
	}

	public ByteBuf buf() {
		return this.buf;
	}
}
