package pack;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.ArrayDeque;

public class TaskExecutor implements Runnable
{
	private BlockingQueue<TWrapper> qIn;
	private ArrayDeque<TWrapper> qOut;
	
	private boolean isThreadActive;
	
	private IntegratorRpnMultithread integrator;
	
	public TaskExecutor(BlockingQueue<TWrapper> qIn,ArrayDeque<TWrapper> qOut)
	{
		this.qIn=qIn;
		this.qOut=qOut;
		this.isThreadActive = true;
		this.integrator = new IntegratorRpnMultithread();
	}
	
	@Override
	public void run()
	{
		while (isThreadActive)
		{
			TWrapper pack = null;
			synchronized(qIn)
			{
				pack = qIn.poll();
			}
			if (pack!=null)
			{
				integrator.SetFunction(pack.task.function);
				double result = integrator.integrate(pack.task.leftBorder,pack.task.rightBorder);
				pack.task.function = Double.toString(result);
				synchronized (qOut)
				{
					qOut.add(pack);
				}
			}
			try{Thread.sleep(100);}catch (InterruptedException e){e.printStackTrace();}
		}
	}
	
	public void close()
	{
		isThreadActive = false;
	}
}