package pack;

import java.util.concurrent.Callable;

public class IntegratorRpn implements IntegratorInterface
{
	RpnFunctionX func;
	public IntegratorRpn(RpnFunctionX func)
	{
		this.func = func;
	}
	public void SetFunction(RpnFunctionX func)
	{
		this.func = func;
	}
	public double solve(double x)
	{
		return func.solve(x);
	}
}