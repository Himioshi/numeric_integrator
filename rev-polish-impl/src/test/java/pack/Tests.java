package pack;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class Tests
{
	public static final double ACCURACY = 0.00001;
	@Test
	public void testRpnFunction()
	{
		double temp;
		RpnFunctionX func = new RpnFunctionX("x");
		for (int i=-10;i<50;++i)
		{
			temp=(double)i;
			assertTrue(Math.abs(func.solve(temp)-temp)<=ACCURACY);
		}
		func.SetFunction("sin(x^2)+cos(x)/ln(x)");
		for (int i=3;i<50;++i)
		{
			temp=(double)i;
			assertTrue(Math.abs((Math.sin(temp*temp)+Math.cos(temp)/Math.log(temp)-func.solve(temp)))<=ACCURACY);
		}
		func.SetFunction("x%5");
		for (int i=3;i<50;++i)
		{
			temp=(double)i;
			assertTrue(Math.abs(func.solve(temp)-(temp%5))<=ACCURACY);
		}
	}
}