package com.zzd.msgmw.main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zzd.msgmw.producer.IProducer;

public abstract class AbstractNormalProducer implements IProducer {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractNormalProducer.class);

	protected Selector selector;

	private SocketChannel socketChannel1;

	private void initProducer() {
		try {
			socketChannel1 = SocketChannel.open(new InetSocketAddress(serverIp, serverPort));
			selector = Selector.open();
			socketChannel1.configureBlocking(false);
			socketChannel1.register(selector, SelectionKey.OP_READ);
		} catch (IOException e) {
			LOGGER.error("error occur when initProducer", e);
			e.printStackTrace();
		}
	}

	public void runProducer() {
		initProducer();

		while (true) {
			produceMessage(socketChannel1);
			try {
				int select = selector.select();
				if (select > 0) {
					Set<SelectionKey> keys = selector.selectedKeys();
					Iterator<SelectionKey> iter = keys.iterator();
					while (iter.hasNext()) {
						SelectionKey sk = iter.next();
						if (sk.isReadable()) {
							readMessage(sk);
						}
						iter.remove();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public abstract void produceMessage(SocketChannel socketChannel);

	public abstract void readMessage(SelectionKey sk);
}
