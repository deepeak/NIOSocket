package com.zzd.msgmw.producer.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zzd.msgmw.main.AbstractNormalProducer;

public class NormalProducer extends AbstractNormalProducer {
	private static final Logger LOGGER = LoggerFactory.getLogger(NormalProducer.class);

	private ByteBuffer writeBuffer = ByteBuffer.allocate(2048);

	private ByteBuffer readBuffer = ByteBuffer.allocate(2048);

	private MessageBuilder msgBuilder = MessageBuilder.InstanceEnum.INSTANCE.getInstance();

	@Override
	public void produceMessage(SocketChannel socketChannel) {
		LOGGER.info("Begin produce message....");
		try {
			String message = msgBuilder.buildMessage();
			writeBuffer.clear();
			writeBuffer.put(message.getBytes("UTF-8"));
			writeBuffer.flip();
			if (writeBuffer.hasRemaining()) {
				socketChannel.write(writeBuffer);
			}
		} catch (IOException e) {
			LOGGER.error("Error occur when producer connect server and attempt to produce message!", e);
		}

	}

	@Override
	public void readMessage(SelectionKey sk) {
		SocketChannel curSc = (SocketChannel) sk.channel();
		try {
			while (curSc.read(readBuffer) > 0) {
				readBuffer.flip();
				System.out.println("Receive message from server:" + new String(readBuffer.array(), "UTF-8"));
				readBuffer.clear();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
