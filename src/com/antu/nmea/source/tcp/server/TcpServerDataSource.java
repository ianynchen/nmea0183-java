package com.antu.nmea.source.tcp.server;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.antu.nmea.sentence.INmeaSentence;
import com.antu.nmea.source.AbstractNmeaDataSource;
import com.antu.nmea.source.AcceptanceSetting;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class TcpServerDataSource extends AbstractNmeaDataSource {
	
	static private Log logger = LogFactory.getLog(TcpServerDataSource.class); 
	
	private TcpServerDataSourceSetting setting;
	
	private TcpServerHandler handler;

	public TcpServerDataSource(String talker, TcpServerDataSourceSetting setting, AcceptanceSetting accSetting) {
		super(talker, accSetting);
		
		assert(setting != null);
		this.setting = setting;
		
		this.handler = new TcpServerHandler(setting, manager);
	}

	@Override
	public String getName() {
		return this.setting.getName();
	}

	@Override
	public void stop() {
	}

	@Override
	public void run() {

		do {
			EventLoopGroup g = new NioEventLoopGroup();
			
			try {
				Bootstrap bs = new Bootstrap().group(g).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true);
				bs.handler(new ChannelInitializer<SocketChannel>(){
		
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						
						ch.pipeline().addLast(new StringDecoder());
						ch.pipeline().addLast(handler);
					}
					
				});
				
				ChannelFuture f = bs.connect(this.setting.getHost(), this.setting.getPort()).sync();
				TcpServerDataSource.logger.info("connected to " + this.setting.getHost() + ":" + this.setting.getPort());
				f.channel().closeFuture().sync();
			} catch (InterruptedException e) {
				TcpServerDataSource.logger.error("exception during connected to " + this.setting.getHost() + ":" + this.setting.getPort(), e);
			} finally {
				g.shutdownGracefully();
				TcpServerDataSource.logger.info("client shutdown");
			}
		} while (this.setting.getAutoRecover());
	}

	@Override
	public void send(INmeaSentence sentence) {

		List<String> strings = this.manager.encode(talker, sentence);
		
		this.handler.send(strings);
	}

}
