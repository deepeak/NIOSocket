package com.zzd.msgmw.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MessageMO {

	private String uuid;

	private String deviceId;

	private int msgType;

	private long sendTime;

	private Map<String, Object> bodyMap;

	public MessageMO(String uuid, String deviceId, int msgType) {
		this.uuid = uuid;
		this.deviceId = deviceId;
		this.msgType = msgType;
		this.sendTime = new Date().getTime();
		Map<String, Object> bodyMap = new HashMap<String, Object>();
		bodyMap.put("testAttr", "testVal");
		this.bodyMap = bodyMap;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public long getSendTime() {
		return sendTime;
	}

	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}

	public Map<String, Object> getBodyMap() {
		return bodyMap;
	}

	public void setBodyMap(Map<String, Object> bodyMap) {
		this.bodyMap = bodyMap;
	}
}
