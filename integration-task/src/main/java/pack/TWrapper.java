package pack;

import io.netty.channel.ChannelHandlerContext;

public class TWrapper
{
	public Task task;
	public ChannelHandlerContext ctx;
	
	public TWrapper()
	{
		this.task=null;
		this.ctx=null;
	}
	public TWrapper(Task task, ChannelHandlerContext ctx)
	{
		this.task=task;
		this.ctx=ctx;
	}
}