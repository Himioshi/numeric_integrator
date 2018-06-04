package pack;

import java.util.ArrayDeque;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;
import io.netty.buffer.Unpooled;

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
				System.out.println("Sent solution: "+pack.task.function);
				ByteBuf out = Unpooled.wrappedBuffer(pack.task.function.getBytes());
				pack.ctx.writeAndFlush(out);
			}
			else
				try{Thread.sleep(10);}catch (InterruptedException e){e.printStackTrace();}
		}
	}
	
	public void close()
	{
		isThreadActive = false;
	}
}