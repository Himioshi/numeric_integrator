package pack;

import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.Locale;

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
		String str;
		Scanner sc = new Scanner(System.in).useLocale(Locale.US);
		System.out.println("Enter: borders and function or exit to disconnect");
		str = sc.nextLine();
		while (!str.contains("exit"))
		{
			ByteBuf out = Unpooled.wrappedBuffer(str.getBytes());
			ctx.writeAndFlush(out);
			synchronized (lock)
			{
				try
					{lock.wait();}
				catch (InterruptedException e)
					{e.printStackTrace();}
			}
			str = sc.nextLine();
		}
		ctx.close();
	}
}