package pack;

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

public class RequestGenThread implements Runnable
{
	private ChannelHandlerContext ctx;
	private Object lock;
	public RequestGenThread(ChannelHandlerContext ctx, Object lock)
	{
		this.lock = lock;
		this.ctx=ctx;
	}
	
	@Override
	public void run()
	{
		for (int i=0; i<50;++i)
		{
			Task task = new Task();
			System.out.println("Generated task: integrate "+task.function+" from "+task.leftBorder+" to "+task.rightBorder);
			String s = Double.toString(task.leftBorder)+" "+Double.toString(task.rightBorder)+" "+task.function;
			ByteBuf out = Unpooled.wrappedBuffer(s.getBytes());
			ctx.writeAndFlush(out);
			synchronized (lock)
			{
				try
					{lock.wait();}
				catch (InterruptedException e)
					{e.printStackTrace();}
			}
			try
					{Thread.sleep(700);}
				catch (InterruptedException e)
					{e.printStackTrace();}
		}
		ctx.close();
	}
}