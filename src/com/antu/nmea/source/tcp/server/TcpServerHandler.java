package com.antu.nmea.source.tcp.server;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.antu.nmea.codec.CodecManager;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TcpServerHandler extends ChannelInboundHandlerAdapter {
	
	static private Log logger = LogFactory.getLog(TcpServerHandler.class); 
	
	private TcpServerDataSourceSetting setting;
	
	private CodecManager manager;
	
	private ChannelHandlerContext context;

	public TcpServerHandler(TcpServerDataSourceSetting setting, CodecManager manager) {
		assert(setting != null);
		
		this.setting = setting;
		this.manager = manager;
	}

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	
    	this.context = ctx;
    	
    	TcpServerHandler.logger.info("connection established: " + ctx.channel());
    	
    	byte[] username = this.setting.getUsername().getBytes("ASCII");
    	byte[] password = this.setting.getPassword().getBytes("ASCII");
    	
    	byte[] logon = new byte[username.length + password.length + 3];
    	logon[0] = 1;
    	
    	System.arraycopy(username, 0, logon, 1, username.length);
    	logon[1 + username.length] = 0;
    	System.arraycopy(password, 0, logon, 2 + username.length, password.length);
    	logon[logon.length - 1] = 0;
    	
    	ByteBuf buffer = Unpooled.buffer(logon.length);
    	buffer.writeBytes(logon);
    	
    	ctx.channel().writeAndFlush(buffer);
    	TcpServerHandler.logger.info("logon information sent, username: " + this.setting.getUsername() + ", password: " + this.setting.getPassword());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	
    	this.manager.decode((String) msg);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
    	TcpServerHandler.logger.error("exception in netty handler receive", cause);
        ctx.fireExceptionCaught(cause);
        ctx.close();
        this.context = null;
    }
    
    public void send(List<String> strings) {
    	
    	if (this.context == null)
    		return;
    	
		try {
	    	for (String str : strings) {
	    		
	    		byte[] bytes = str.getBytes("ASCII");
	    		ByteBuf buffer = Unpooled.buffer(bytes.length);
	    		buffer.writeBytes(bytes);
	    		this.context.writeAndFlush(buffer);
	    	}
		} catch (UnsupportedEncodingException e) {
	    	TcpServerHandler.logger.error("exception in netty handler send", e);
		}
    }
}
