package com.antu.nmea.source.tcp.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import com.antu.nmea.sentence.INmeaSentence;
import com.antu.nmea.source.AbstractNmeaDataSource;
import com.antu.nmea.source.AcceptanceSetting;

public class TcpClientDataSource extends AbstractNmeaDataSource {
	
	static private Log logger = LogFactory.getLog(TcpClientDataSource.class); 
	
	private TcpClientDataSourceSetting setting;
	
	private List<TcpClientHandler> handlers = new ArrayList<TcpClientHandler>();

	public TcpClientDataSource(String talker, AcceptanceSetting accSetting, TcpClientDataSourceSetting setting) {
		super(talker, accSetting);
		
		assert(setting != null);
		this.setting = setting;
	}
	
	void addHandler(TcpClientHandler handler) {
		synchronized(this) {
			this.handlers.add(handler);
		}
	}
	
	void removeHandler(TcpClientHandler handler) {
		synchronized(this) {
			this.handlers.remove(handler);
		}
	}

	@Override
	public String getName() {
		return setting.getName();
	}

	@Override
	public void stop() {
	}

	@Override
	public void send(INmeaSentence sentence) {

		List<String> strings = this.manager.encode(talker, sentence);
		
		List<TcpClientHandler> hs = null;
		synchronized(this) {
			hs = new ArrayList<TcpClientHandler>(this.handlers);
		}
		for (TcpClientHandler h : hs) {
			h.send(strings);
		}
	}

	@Override
	public void run() {
		
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			
			ServerBootstrap sb = new ServerBootstrap();
			sb.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class);
			
			sb.option(ChannelOption.SO_BACKLOG, 1024);
			sb.childHandler(new TcpConnectionHandler(this, this.acceptanceSetting));
			TcpClientDataSource.logger.info("starting server...");
			
			ChannelFuture f = sb.bind(setting.getPort()).sync();
			TcpClientDataSource.logger.info("server started at port " + this.setting.getPort() + "...");
			f.channel().closeFuture().sync();
			TcpClientDataSource.logger.info("Server shutdown");
		} catch (Exception e) {
			TcpClientDataSource.logger.error("unrecoverable error", e);
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

}
