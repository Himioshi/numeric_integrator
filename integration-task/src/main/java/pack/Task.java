package pack;

import java.util.Random;
public class Task
{
	public double leftBorder;
	public double rightBorder;
	public String function;
	
	private static final String[] arguements = {"x", "x^2","sin(x)","cos(x)","ln(x)"};
	private static final String[] operations = {"+","-","*"};
	private static Random r = new Random();
	public Task()
	{
		leftBorder = 1.0 + 10.0 * r.nextDouble();
		rightBorder = leftBorder + 2.0 + 10.0 * r.nextDouble();
		int n = r.nextInt(3) + 2;
		function = "";
		for (int i=0;i<n;++i)
		{
			function+=( arguements[r.nextInt(arguements.length)]+operations[r.nextInt(operations.length)]);
		}
		function+=arguements[r.nextInt(arguements.length)];
	}
	public Task(double leftBorder,double rightBorder,String function)
	{
		this.leftBorder=leftBorder;
		this.rightBorder=rightBorder;
		this.function=function;
	}
}