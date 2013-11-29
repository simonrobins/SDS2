package helpers;

import java.util.Set;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;

import data.ProductVersion;

public class ProductHelper
{
	public static boolean validateProductIds(final Set<ProductVersion> products)
	{
		if(products.size() == 0)
			return true;

		final StringBuffer sql = new StringBuffer("select count(*) as count from coda_product where product_id in (");

		for(final ProductVersion product : products)
		{
			sql.append(product.productId);
			sql.append(",");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(")");

		final SqlQuery sqlQuery = Ebean.createSqlQuery(sql.toString());
		final Integer count = sqlQuery.findUnique().getInteger("count");

		return count == products.size();
	}
}
