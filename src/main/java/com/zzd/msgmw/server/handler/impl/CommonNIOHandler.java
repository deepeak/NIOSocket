package com.zzd.msgmw.server.handler.impl;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.zzd.msgmw.server.handler.IHandler;

@Component("commonNIOHandler")
public class CommonNIOHandler implements IHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonNIOHandler.class);

	public void handleAccept(SelectionKey selectionKey, Selector selector) {
		LOGGER.info("processing accept");
		ServerSocketChannel socketChannel = (ServerSocketChannel) selectionKey.channel();
		try {
			SocketChannel socketChannelClient = socketChannel.accept();
			socketChannelClient.configureBlocking(false);
			socketChannelClient.register(selector, SelectionKey.OP_READ);
		} catch (Exception e) {
			LOGGER.info("error occur when processing accept", e);
		}

	}

	public void handleRead(SelectionKey selectionKey, Selector selector) {
		LOGGER.info("processing read");
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
		int readCount = 0;
		try {
			readCount = socketChannel.read(buffer);
			if (readCount > 0) {
				buffer.flip();
				String reciveString = new String(buffer.array());
				LOGGER.info("Server revice message from producer----:{}", reciveString);
				socketChannel.register(selector, SelectionKey.OP_WRITE);
			}
		} catch (Exception e) {
			LOGGER.info("error occur when processing read", e);
		}
	}

	public void handleWrite(SelectionKey selectionKey, Selector selector) {
		LOGGER.info("processing write");

		SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		try {
			buffer.put("ack message from server to producer".getBytes("UTF-8"));
			buffer.flip();
			socketChannel.write(buffer);
			socketChannel.register(selector, SelectionKey.OP_READ);
		} catch (Exception e) {
			LOGGER.info("error occur when processing write", e);
		}
	}
}
