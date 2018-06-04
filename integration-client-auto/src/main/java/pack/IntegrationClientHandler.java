package pack;

import java.util.Scanner;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;
import io.netty.buffer.Unpooled;

import io.netty.util.concurrent.BlockingOperationException;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.*;

import java.util.Locale;

public class IntegrationClientHandler extends ChannelInboundHandlerAdapter
{
	private RequestGenThread gen;
	private Thread GenerationThread;
	private final Object lock = new Object();
	public IntegrationClientHandler()
	{
		this.gen=null;
		this.GenerationThread=null;
	}
    @Override
    public void channelActive(final ChannelHandlerContext ctx)
	{
		this.gen = new RequestGenThread(ctx,lock);
		this.GenerationThread = new Thread(gen);
		GenerationThread.setDaemon(true);
		GenerationThread.start();
    }
	
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
	{
		ByteBuf in = (ByteBuf) msg;
		String str;
		try
		{
			str = in.toString(Charset.forName("utf-8"));
			System.out.println("Recieved solution: "+str);
		}
		finally
		{
			ReferenceCountUtil.release(msg);
			synchronized (lock)
			{
				lock.notifyAll();
			}
		}
	}
	
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	{
        cause.printStackTrace();
        ctx.close();
    }
}
