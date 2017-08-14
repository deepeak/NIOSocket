package com.zzd.msgmw.server.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.zzd.msgmw.server.IServer;
import com.zzd.msgmw.server.handler.IHandler;

@Component("nioServer")
public class NIOServer implements IServer {

	private static final Logger LOGGER = LoggerFactory.getLogger(NIOServer.class);

	private static final int SOCKET_TIMEOUT = 300 * 1000;

	private Selector selector;

	@Autowired
	@Qualifier("commonNIOHandler")
	private IHandler handler;

	public void start() {
		LOGGER.info("Begin to start NIOServer.......");
		ServerSocketChannel serverSocketChannel;
		try {
			selector = Selector.open();
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

			while (true) {
				// 等待某信道就绪(或超时)
				if (selector.select(SOCKET_TIMEOUT) == 0) {// 监听注册通道，当其中有注册的 IO
															// 操作可以进行时，该函数返回，并将对应的
															// SelectionKey 加入
															// selected-key
															// set
					LOGGER.info("NIOServer waitting......");
					continue;
				}
				// 取得迭代器.selectedKeys()中包含了每个准备好某一I/O操作的信道的SelectionKey
				// Selected-key Iterator 代表了所有通过 select() 方法监测到可以进行 IO 操作的
				// channel，这个集合可以通过 selectedKeys() 拿到
				Iterator<SelectionKey> keyIter = selector.selectedKeys().iterator();
				// 处理keyset
				processKeySet(keyIter);
			}
		} catch (ClosedChannelException closedChannelException) {
			LOGGER.info("closedChannelException occur, regist socket channel failed", closedChannelException);
		} catch (IOException ioe) {
			LOGGER.info("IOException occur, open/bind/select action may failed", ioe);
		} catch (Exception e) {
			LOGGER.info("unknow exception occur when start NIOServer", e);
		}
	}

	public void processKeySet(Iterator<SelectionKey> keyIter) {
		while (keyIter.hasNext()) {
			SelectionKey key = keyIter.next();
			if (key.isAcceptable()) {
				LOGGER.info("NIOServer : handle accept request");
				// 有客户端连接请求时
				handler.handleAccept(key, selector);
			} else if (key.isReadable()) {// 判断是否有数据发送过来
				LOGGER.info("NIOServer : handle readable data");
				// 从客户端读取数据
				handler.handleRead(key, selector);
			} else if (key.isValid() && key.isWritable()) {// 判断是否有效及可以发送给客户端
				LOGGER.info("NIOServer : go write someting");
				// 客户端可写时
				handler.handleWrite(key, selector);
			}
			// 移除处理过的键
			keyIter.remove();
		}
	}

}
