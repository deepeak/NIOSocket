package com.zzd.msgmw.producer.impl;

import java.util.Random;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.zzd.msgmw.model.MessageMO;

public class MessageBuilder {

	private static final Random random = new Random();

	private static final String DEVICE_ID = "ne-10001";

	private MessageBuilder() {
	}

	protected String buildMessage() {
		MessageMO messageMO = new MessageMO(UUID.randomUUID().toString(), DEVICE_ID, random.nextInt(2));
		return JSON.toJSONString(messageMO);
	}

	enum InstanceEnum {
		INSTANCE;

		private MessageBuilder messageBuilder;

		private InstanceEnum() {
			messageBuilder = new MessageBuilder();
		}

		public MessageBuilder getInstance() {
			return messageBuilder;
		}

	}
}
