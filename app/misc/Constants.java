package misc;

public interface Constants
{
	public interface ProductId
	{
		public static int		FINANCE_112	= 261;
		public static int		FINANCE_113	= 281;
		public static int		FINANCE_120	= 325;
		public static Integer	CCM			= 78;
	}

	public interface Products
	{
		public static int	FRAMEWORK_APP	= 1 << 0;
		public static int	FRAMEWORK_WEB	= 1 << 1;
		public static int	ASSETS_APP		= 1 << 2;
		public static int	ASSETS_WEB		= 1 << 3;
		public static int	BILLING_APP		= 1 << 4;
		public static int	BILLING_WEB		= 1 << 5;
		public static int	CUSTOMISER		= 1 << 6;
		public static int	FINANCE_WEB		= 1 << 7;
		public static int	PIM_APP			= 1 << 8;
		public static int	PIM_WEB			= 1 << 9;
		public static int	POP_APP			= 1 << 10;
		public static int	POP_WEB			= 1 << 11;
		public static int	CORE_CLIENT		= 1 << 12;
		public static int	FINANCIALS		= 1 << 13;
	}
}
