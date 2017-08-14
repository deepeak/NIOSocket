package com.zzd.msgmw.main;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.zzd.msgmw.server.IServer;

@Component
public class ServerLauncher {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServerLauncher.class);

	@Autowired
	@Qualifier("nioServer")
	private IServer server;

	@PostConstruct
	public void launchServer() {
		LOGGER.info("Start to launch server......");
		server.start();
	}

}
