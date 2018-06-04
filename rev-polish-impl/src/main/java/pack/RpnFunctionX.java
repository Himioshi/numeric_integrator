package pack;

import java.util.*;

public class RpnFunctionX implements RpnFunctionXInterface
{
	private static final int OPERAND = 0;
	private static final int ARGUEMENT = 1;
	private static final int OP_NONE = 2;
	private static final int OP_PLUS = 3;
	private static final int OP_UN_PLUS = 4;
	private static final int OP_MINUS = 5;
	private static final int OP_UN_MINUS = 6;
	private static final int OP_MULT = 7;
	private static final int OP_DIV = 8;
	private static final int OP_MOD = 9;
	private static final int OP_POWER = 10;
	private static final int OP_SIN = 11;
	private static final int OP_COS = 12;
	private static final int OP_LN = 13;
	private static final int L_BRACKET = 14;
	private static final int R_BRACKET = 15;
	
	private ArrayList<Unit> fun = new ArrayList<Unit>();
	private String arg;
	
	public RpnFunctionX()
	{
		this.arg = "x";
	}
	
	public RpnFunctionX(String str)
	{
		this(str, "x");
	}
	
	public RpnFunctionX(String str, String f_arg)
	{
		this.arg = f_arg;
		SetFunction(str);
	}
	
	public boolean SetFunction(String str)
	{
		fun.clear();
		str = str.toLowerCase();
		int pos = 0;
		ArrayList<Unit> op_parted = new ArrayList<Unit>();
		RefObject<Integer> tempRef_pos = new RefObject<Integer>(pos);
		Unit unit = ReadOp(str, tempRef_pos);
		pos = tempRef_pos.argValue;
		while (unit.op != OP_NONE)
		{
			op_parted.add(unit);
			RefObject<Integer> tempRef_pos2 = new RefObject<Integer>(pos);
			unit = ReadOp(str, tempRef_pos2);
			pos = tempRef_pos2.argValue;
		}
		if (!op_parted.isEmpty())
		{
			if (op_parted.get(0).op == OP_MINUS)
			{
				op_parted.get(0).op = OP_UN_MINUS;
			}
			else if (op_parted.get(0).op == OP_PLUS)
			{
				op_parted.get(0).op = OP_UN_PLUS;
			}
		}
		for (int i = 1; i < op_parted.size(); ++i)
		{
			if (op_parted.get(i).op == OP_MINUS)
			{
				if (op_parted.get(i - 1).op != OPERAND && op_parted.get(i - 1).op != ARGUEMENT && op_parted.get(i - 1).op != R_BRACKET)
				{
					op_parted.get(i).op = OP_UN_MINUS;
				}
			}
			else if (op_parted.get(i).op == OP_PLUS)
			{
				if (op_parted.get(i - 1).op != OPERAND && op_parted.get(i - 1).op != ARGUEMENT && op_parted.get(i - 1).op != R_BRACKET)
				{
					op_parted.get(i).op = OP_UN_PLUS;
				}
			}
		}
		ArrayList<Unit> op_stack = new ArrayList<Unit>();
		for (int i = 0; i < op_parted.size(); ++i)
		{
			if (op_parted.get(i).op == OP_NONE)
			{
				return false;
			}
			else if (op_parted.get(i).op == OPERAND || op_parted.get(i).op == ARGUEMENT)
			{
				fun.add(op_parted.get(i));
			}
			else if (op_parted.get(i).op == L_BRACKET)
			{
				op_stack.add(op_parted.get(i));
			}
			else if (op_parted.get(i).op == R_BRACKET)
			{
				while (op_stack.get(op_stack.size() - 1).op != L_BRACKET)
				{
					fun.add(op_stack.get(op_stack.size() - 1));
					op_stack.remove(op_stack.size() - 1);
				}
				op_stack.remove(op_stack.size() - 1);
			}
			else
			{
				if (!op_stack.isEmpty())
				{
					while (!op_stack.isEmpty())
					{
						if (GetPriority(op_stack.get(op_stack.size() - 1)) >= GetPriority(op_parted.get(i)))
						{
							fun.add(op_stack.get(op_stack.size() - 1));
							op_stack.remove(op_stack.size() - 1);
						}
						else
						{
							break;
						}
					}
				}
				op_stack.add(op_parted.get(i));
			}
		}
		while (!op_stack.isEmpty())
		{
			fun.add(op_stack.get(op_stack.size() - 1));
			op_stack.remove(op_stack.size() - 1);
		}
		return true;
	}
	
	public boolean SetArgument()
	{
		return SetArgument("x");
	}
	
	public boolean SetArgument(String f_arg)
	{
		arg = f_arg;
		return true;
	}
	
	public double solve()
	{
		return solve(0.0);
	}
	
	public double solve(double arg)
	{
		if (!IsOk())
		{
			return 0.0;
		}
		ArrayList<Double> op_stack = new ArrayList<Double>();
		for (int i = 0; i < fun.size(); ++i)
		{
			switch (fun.get(i).op)
			{
			case OPERAND:
				op_stack.add(fun.get(i).operand);
				break;
			case ARGUEMENT:
				op_stack.add(arg);
				break;
			case OP_PLUS:
				op_stack.set(op_stack.size() - 2, op_stack.get(op_stack.size() - 2) + op_stack.get(op_stack.size() - 1));
				op_stack.remove(op_stack.size() - 1);
				break;
			case OP_MINUS:
				op_stack.set(op_stack.size() - 2, op_stack.get(op_stack.size() - 2) - op_stack.get(op_stack.size() - 1));
				op_stack.remove(op_stack.size() - 1);
				break;
			case OP_MULT:
				op_stack.set(op_stack.size() - 2, op_stack.get(op_stack.size() - 2) * op_stack.get(op_stack.size() - 1));
				op_stack.remove(op_stack.size() - 1);
				break;
			case OP_DIV:
				if (op_stack.get(op_stack.size() - 1) == 0.0)
				{
					//"Division by 0 in RPNfunc!"<<std::endl;
					return 0.0;
				}
				op_stack.set(op_stack.size() - 2, op_stack.get(op_stack.size() - 2) / op_stack.get(op_stack.size() - 1));
				op_stack.remove(op_stack.size() - 1);
				break;
			case OP_MOD:
				if (op_stack.get(op_stack.size() - 1) == 0.0)
				{
					//"Division by 0 in RPNfunc!"<<std::endl;
					return 0.0;
				}
				op_stack.set(op_stack.size() - 2, op_stack.get(op_stack.size() - 2)%op_stack.get(op_stack.size() - 1));
				op_stack.remove(op_stack.size() - 1);
				break;
			case OP_POWER:
				op_stack.set(op_stack.size() - 2, Math.pow(op_stack.get(op_stack.size() - 2),op_stack.get(op_stack.size() - 1)));
				op_stack.remove(op_stack.size() - 1);
				break;
			case OP_UN_PLUS:
				break;
			case OP_UN_MINUS:
				op_stack.set(op_stack.size() - 1, -op_stack.get(op_stack.size() - 1));
				break;
			case OP_SIN:
				op_stack.set(op_stack.size() - 1, Math.sin(op_stack.get(op_stack.size() - 1)));
				break;
			case OP_COS:
				op_stack.set(op_stack.size() - 1, Math.cos(op_stack.get(op_stack.size() - 1)));
				break;
			case OP_LN:
				if (op_stack.get(op_stack.size() - 1) <= 0.0)
				{
					//"Negative number to log"
					return 0.0;
				}
				op_stack.set(op_stack.size() - 1, Math.log(op_stack.get(op_stack.size() - 1)));
				break;
			case OP_NONE:
			case L_BRACKET:
			case R_BRACKET:
				break;
			}
		}
		return op_stack.get(op_stack.size() - 1);
	}
	
	public boolean IsOk()
	{
		if (!fun.isEmpty() && !arg.equals(""))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	/*std::string StrFromOp(Unit<T> unit)
	{
	    switch(unit.op)
	    {
	    case OPERAND:
	    {
	        std::stringstream strm;
	        strm<<unit.operand;
	        return strm.str();
	    }
	    case ARGUEMENT:
	        return "x";
	    case L_BRACKET:
	        return "(";
	    case R_BRACKET:
	        return ")";
	    case OP_PLUS:
	        return "+";
	    case OP_MINUS:
	        return "-";
	    case OP_MULT:
	        return "*";
	    case OP_DIV:
	        return "/";
	    case OP_MOD:
	        return "%";
	    case OP_POWER:
	        return "^";
	    case OP_UN_PLUS:
	        return "un+";
	    case OP_UN_MINUS:
	        return "un-";
	    case OP_SIN:
	        return "sin";
	    case OP_COS:
	        return "cos";
	    case OP_LN:
	        return "ln";
	    case OP_NONE:
	        return "_";
	    }
	    return "_";
	}*/
	
	private Unit ReadOp(String in, RefObject<Integer> pos)
	{
		String tmp = "";
		while (pos.argValue < in.length())
		{
			if (in.charAt(pos.argValue) == ' ')
			{
				++pos.argValue;
			}
			else
			{
				break;
			}
		}
		if (pos.argValue < in.length())
		{
			if ((in.charAt(pos.argValue) >= '0') && (in.charAt(pos.argValue) <= '9'))
			{
				tmp += in.charAt(pos.argValue++);
				while (pos.argValue < in.length())
				{
					if ((in.charAt(pos.argValue) >= '0') && (in.charAt(pos.argValue) <= '9'))
					{
						tmp += in.charAt(pos.argValue++);
					}
					else
					{
						break;
					}
				}
				double ret_var = Double.parseDouble(tmp);
				return (new Unit(ret_var, OPERAND));
			}
			else if (in.charAt(pos.argValue) == '+')
			{
				++pos.argValue;
				return new Unit(0.0, OP_PLUS);
			}
			else if (in.charAt(pos.argValue) == '-')
			{
				++pos.argValue;
				return new Unit(0.0, OP_MINUS);
			}
			else if (in.charAt(pos.argValue) == '*')
			{
				++pos.argValue;
				return new Unit(0.0, OP_MULT);
			}
			else if (in.charAt(pos.argValue) == '/')
			{
				++pos.argValue;
				return new Unit(0.0, OP_DIV);
			}
			else if (in.charAt(pos.argValue) == '%')
			{
				++pos.argValue;
				return new Unit(0.0, OP_MOD);
			}
			else if (in.charAt(pos.argValue) == '(')
			{
				++pos.argValue;
				return new Unit(0.0, L_BRACKET);
			}
			else if (in.charAt(pos.argValue) == ')')
			{
				++pos.argValue;
				return new Unit(0.0, R_BRACKET);
			}
			else if (in.charAt(pos.argValue) == '^')
			{
				++pos.argValue;
				return new Unit(0.0, OP_POWER);
			}
			else if ((in.charAt(pos.argValue) >= 'a') && (in.charAt(pos.argValue) <= 'z'))
			{
				tmp += in.charAt(pos.argValue++);
				while (pos.argValue < in.length())
				{
					if ((in.charAt(pos.argValue) >= 'a') && (in.charAt(pos.argValue) <= 'z'))
					{
						tmp += in.charAt(pos.argValue++);
					}
					else
					{
						break;
					}
				}
				if (tmp.equals("sin"))
				{
					return new Unit(0.0, OP_SIN);
				}
				else if (tmp.equals("cos"))
				{
					return new Unit(0.0, OP_COS);
				}
				else if (tmp.equals("ln"))
				{
					return new Unit(0.0, OP_LN);
				}
				else if (tmp.equals("x"))
				{
					return new Unit(0.0, ARGUEMENT);
				}
			}
		}
		return new Unit(0.0, OP_NONE);
	}
	
	private int GetPriority(Unit unit)
	{
		switch (unit.op)
		{
		case OPERAND:
			return 0;
		case ARGUEMENT:
			return 0;
		case L_BRACKET:
			return 1;
		case R_BRACKET:
			return 1;
		case OP_PLUS:
			return 2;
		case OP_MINUS:
			return 2;
		case OP_MULT:
			return 3;
		case OP_DIV:
			return 3;
		case OP_MOD:
			return 3;
		case OP_POWER:
			return 5;
		case OP_UN_PLUS:
			return 4;
		case OP_UN_MINUS:
			return 4;
		case OP_SIN:
			return 4;
		case OP_COS:
			return 4;
		case OP_LN:
			return 4;
		case OP_NONE:
			return -1;
		}
		return -1;
	}
}