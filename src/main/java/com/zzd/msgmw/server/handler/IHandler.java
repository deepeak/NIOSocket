package com.zzd.msgmw.server.handler;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

/**
 * @author zzd 2017年8月3日下午4:36:09
 * 
 */
public interface IHandler {

	/**
	 * 处理连接请求
	 * 
	 * @param selectionKey
	 * @param selector
	 */
	public void handleAccept(SelectionKey selectionKey, Selector selector);

	/**
	 * 处理输入流
	 * 
	 * @param selectionKey
	 * @param selector
	 */
	public void handleRead(SelectionKey selectionKey, Selector selector);

	/**
	 * 处理输出流
	 * 
	 * @param selectionKey
	 */
	public void handleWrite(SelectionKey selectionKey, Selector selector);
}
