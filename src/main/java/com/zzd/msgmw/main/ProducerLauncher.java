package com.zzd.msgmw.main;

import com.zzd.msgmw.producer.IProducer;
import com.zzd.msgmw.producer.impl.NormalProducer;

public class ProducerLauncher {

	public static void main(String[] args) {
		IProducer producer = new NormalProducer();
		producer.runProducer();
	}
}
