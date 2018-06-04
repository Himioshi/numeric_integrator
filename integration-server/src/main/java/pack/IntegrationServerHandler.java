package pack;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.ArrayDeque;

import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.Locale;

public class IntegrationServerHandler extends ChannelInboundHandlerAdapter
{
	private BlockingQueue<TWrapper> qIn;
	IntegrationServerHandler(BlockingQueue<TWrapper> qIn)
	{
		this.qIn = qIn;
	}
	
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
	{
        ByteBuf in = (ByteBuf) msg;
		String str, f;
		double l,r;
		try
		{
			str = in.toString(Charset.forName("utf-8"));
			Scanner sc = new Scanner(str).useLocale(Locale.US);
			l = sc.nextDouble();
			r = sc.nextDouble();
			f = sc.nextLine();
			f=f.replaceAll(" ","");
			System.out.println("Got task: from "+l+" to "+r+" "+f);
			synchronized(qIn)
			{
				qIn.add(new TWrapper(new Task(l,r,f),ctx));
			}
		}
		finally
		{
			ReferenceCountUtil.release(msg);
		}
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	{
        cause.printStackTrace();
        ctx.close();
    }
}