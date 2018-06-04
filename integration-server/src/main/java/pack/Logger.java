package pack;

import java.util.ArrayDeque;

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

public class Logger implements Runnable
{
	private ArrayDeque<TWrapper> qOut;
	
	private boolean isThreadActive;
	
	public Logger(ArrayDeque<TWrapper> qOut)
	{
		this.qOut=qOut;
		this.isThreadActive = true;
	}
	
	@Override
	public void run()
	{
		while (isThreadActive)
		{
			TWrapper pack = null;
			synchronized(qOut)
			{
				pack = qOut.poll();
			}
			if (pack!=null)
			{
				ChannelHandlerContext ctx = pack.ctx;
				System.out.println("Sent solution: "+pack.task.function);
				ByteBuf out = Unpooled.wrappedBuffer(pack.task.function.getBytes());
				final ChannelFuture f = ctx.writeAndFlush(out);
				/*f.addListener(new ChannelFutureListener()
				{
					@Override
					public void operationComplete(ChannelFuture future)
					{
						assert f == future;
						ctx.close();
					}
				});*/
			}
			else
				try{Thread.sleep(100);}catch (InterruptedException e){e.printStackTrace();}
		}
	}
	
	public void close()
	{
		isThreadActive = false;
	}
}