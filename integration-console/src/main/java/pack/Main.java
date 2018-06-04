package pack;

import java.util.Scanner;
import java.util.Locale;

public class Main
{
	public static void main(String[] args)
	{
		double l=0.0,r=0.0;
		String str="";
		Scanner sc = new Scanner(System.in).useLocale(Locale.US);
		IntegratorRpnMultithread rpf = new IntegratorRpnMultithread();
		System.out.println("Enter: left border, right border, function");
		if (sc.hasNextDouble())
			l=sc.nextDouble();
		if (sc.hasNextDouble())
			r=sc.nextDouble();
		str = sc.nextLine();
		str=str.replaceAll(" ","");
		while (!str.contains("exit"))
		{
			rpf.SetFunction(str);
			System.out.println("Result: "+rpf.integrate(l,r));
			if (sc.hasNextDouble())
				l=sc.nextDouble();
			if (sc.hasNextDouble())
				r=sc.nextDouble();
			str = sc.nextLine();
			str=str.replaceAll(" ","");
		}
	}
}