package pack;

import java.util.*;

public interface RpnFunctionXInterface
{
	public boolean SetFunction(String str);
	public boolean SetArgument();
	public boolean SetArgument(String f_arg);
	public double solve();
	public double solve(double arg);
}