package tags;

import java.util.Set;

public class Servicepack
{
	public static String checked(final Integer product, final Set<Integer> products)
	{
		return (products.contains(product)) ? "checked=\"checked\"" : "";
	}
}
