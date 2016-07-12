package com.casic.watermeter.codec;

import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageEncoder;
import com.casic.watermeter.msg.Msg;

public class WaterMeterCodecFactory extends DemuxingProtocolCodecFactory {
	
	private MessageDecoder decoder;
	private MessageEncoder<Msg> encoder;
	/*
	 * 构造函数，生成编码器和解码器的工厂
	 * */
	public WaterMeterCodecFactory(MessageDecoder decoder,MessageEncoder<Msg> encoder) {
		this.decoder = decoder;
		this.encoder = encoder;
		addMessageDecoder(this.decoder);
		addMessageEncoder(Msg.class, this.encoder);
	}
}