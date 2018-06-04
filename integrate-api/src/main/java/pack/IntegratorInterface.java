package pack;

import java.util.concurrent.Callable;

public interface IntegratorInterface
{
	public static final double ACCURACY = 0.00001;
	public static final double arr[] = {7.0,32.0,12.0,32.0,7.0};
	
	default public double integrate(double left, double right)
	{
		return integrate(left, right, ACCURACY);
	}
	
	default public double integrate(double left, double right, double accuracy)
	{
		double prev_result=0.0,result=0.0;
		long partition=10L;
		result = integrate(left, right, partition);
		do
		{
			partition=(long)(((double)partition)*1.3+1.0);
			prev_result = result;
			result = integrate(left,right,partition);
		}  while(Math.abs(result-prev_result)>ACCURACY);
		return result;
	}
	
	default public double integrate(double left, double right, long partition)
	{
	if (right-left<=0.0)
        return 0.0;
	if (partition==0)
        return 0.0;
    double h = (right-left)/((double)partition);
    double z = h/4.0;
    double tmp = left, res=0.0, x;
    for (long i=0; i<partition; ++i)
    {
        x = tmp;
        res+= 2*(z/45)*arr[0]*solve(x);
        for (int j=1; j<5; ++j)
        {
            x+=z;
            res+=2*(z/45)*arr[j]*solve(x);
        }
        tmp+=h;
    }
    return res;
	}
	
	public double solve(double x);
}