package com.antu.nmea.source.tcp.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.antu.nmea.source.AcceptanceSetting;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class TcpConnectionHandler extends ChannelInitializer<SocketChannel> {
	
	static private Log logger = LogFactory.getLog(TcpConnectionHandler.class); 
	
	private TcpClientDataSource observer;
	
	private AcceptanceSetting acceptanceSetting;

	public TcpConnectionHandler(TcpClientDataSource observer, AcceptanceSetting accSetting) {
		assert(observer != null);
		this.observer = observer;
	}

	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		
		TcpClientHandler handler = new TcpClientHandler(this.observer, this.acceptanceSetting);

		TcpConnectionHandler.logger.info("Incoming connection at: " + channel.toString());
		channel.pipeline().addLast(new StringDecoder());
		channel.pipeline().addLast(handler);
		this.observer.addHandler(handler);
	}
}
