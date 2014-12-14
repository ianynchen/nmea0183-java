package com.antu.nmea.source.tcp.client;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.antu.nmea.codec.CodecManager;
import com.antu.nmea.source.AbstractNmeaDataSource;
import com.antu.nmea.source.AcceptanceSetting;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TcpClientHandler extends ChannelInboundHandlerAdapter {
	
	static private Log logger = LogFactory.getLog(TcpClientHandler.class); 
	
	private ChannelHandlerContext handler;
	
	private CodecManager manager;
	
	private TcpClientDataSource dataSource;

	public TcpClientHandler(TcpClientDataSource dataSource, AcceptanceSetting setting) {
		
		manager = new CodecManager();
		AbstractNmeaDataSource.updateAcceptanceList(manager, setting);
		this.dataSource = dataSource;
	}

	public void send(List<String> strings) {
		
		if (handler != null) {
			try {
		    	for (String str : strings) {
		    		
		    		byte[] bytes = str.getBytes("ASCII");
		    		ByteBuf buffer = Unpooled.buffer(bytes.length);
		    		buffer.writeBytes(bytes);
		    		this.handler.writeAndFlush(buffer);
		    	}
			} catch (UnsupportedEncodingException e) {
				TcpClientHandler.logger.error("exception in netty handler send", e);
			}
		}
	}

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	
    	this.handler = ctx;
    	
    	TcpClientHandler.logger.info("connection established: " + ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	
    	TcpClientHandler.logger.info("string received from" + ctx.channel() + ", content: " + (String)msg);
    	this.manager.decode((String) msg);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
    	TcpClientHandler.logger.error("exception in netty handler receive", cause);
        ctx.fireExceptionCaught(cause);
        TcpClientHandler.logger.info("closing connection" + ctx.channel());
        ctx.close();
        TcpClientHandler.logger.info("connection closed");
        this.handler = null;
        this.dataSource.removeHandler(this);
    }
}
