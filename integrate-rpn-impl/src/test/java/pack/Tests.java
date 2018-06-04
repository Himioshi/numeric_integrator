package pack;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class Tests
{
	public static final double ACCURACY = IntegratorRpn.ACCURACY;
	@Test
	public void testIntegration()
	{
		RpnFunctionX func = new RpnFunctionX("x");
		IntegratorRpn integrator = new IntegratorRpn(func);
		
		assertTrue(Math.abs((integrator.integrate(2.0,6.0)-(6.0*6.0-2.0*2.0)/2.0))<=ACCURACY);
		
		double temp;
		func.SetFunction("1/x");
		integrator.SetFunction(func);
		for (int i=5;i<50;++i)
		{
			temp = (double)i;
			assertTrue(Math.abs((integrator.integrate(1.0,temp)-(Math.log(temp)-Math.log(1.0))))<=ACCURACY);
		}
	}
	@Test
	public void testIntegrationMultithread()
	{
		RpnFunctionX func = new RpnFunctionX("x");
		IntegratorRpnMultithread integrator = new IntegratorRpnMultithread(func);
		
		assertTrue(Math.abs((integrator.integrate(2.0,6.0)-(6.0*6.0-2.0*2.0)/2.0))<=ACCURACY);
		
		double temp;
		func.SetFunction("1/x");
		integrator.SetFunction(func);
		for (int i=5;i<50;++i)
		{
			temp = (double)i;
			assertTrue(Math.abs((integrator.integrate(1.0,temp)-(Math.log(temp)-Math.log(1.0))))<=ACCURACY);
		}
	}
}