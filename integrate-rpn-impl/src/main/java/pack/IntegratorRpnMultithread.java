package pack;

public class IntegratorRpnMultithread
{
	private RpnFunctionX func;
	public IntegratorRpnMultithread()
	{
		this.func=null;
	}
	public IntegratorRpnMultithread(RpnFunctionX func)
	{
		this.func=func;
	}
	public void SetFunction(RpnFunctionX funct)
	{
		this.func = funct;
	}
	public IntegratorRpnMultithread(String func)
	{
    this.func = new RpnFunctionX(func);
	}
	public void SetFunction(String funct)
	{
		func = new RpnFunctionX(funct);
	}
	
	public double integrate(double left,double right)
	{
		if (right<=left)
			return 0.0;
		int threadNumber = 1 + (int)(Math.log(1+right-left));
		double temp = left;
		double diff = (right-left)/threadNumber;
		
		IntegrationUnit[] arr = new IntegrationUnit[threadNumber];
		Thread[] threads = new Thread[threadNumber];
		
		for (int i=0;i<threadNumber;++i)
		{
			arr[i] = new IntegrationUnit(func,temp,temp+diff);
			temp+=diff;
		}
		for (int i=0;i<threadNumber;++i)
		{
			threads[i] = new Thread(arr[i]);
		}
		
		for (int i=0;i<threadNumber;++i)
		{
			threads[i].start();
		}
		
		temp=0.0;
		
		for (int i=0;i<threadNumber;++i)
		{
			try{
			threads[i].join();}
			catch(InterruptedException e){
			e.printStackTrace();}
			temp+=arr[i].result;
		}
		
		return temp;
	}
	
	class IntegrationUnit implements Runnable
	{
		private double left;
		private double right;
		private IntegratorRpn integrator;
		
		public double result = 0.0;
		
		public IntegrationUnit(RpnFunctionX func,double left, double right)
		{
			this.left = left;
			this.right = right;
			this.integrator = new IntegratorRpn(func);
		}
		
		@Override
		public void run()
		{
			result = integrator.integrate(left,right);
		}
	}
}