package models;

import java.util.LinkedHashSet;
import java.util.Set;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;

public class SdsValidate
{
	public static final SdsValidate INSTANCE = new SdsValidate();

	private SdsValidate()
	{
	}

	// private static String S_SUBPRODUCT_SQL = "SELECT DISTINCT c.account_id, p.sub_product, p.sub_product_id FROM  service_contract_product AS c INNER JOIN account AS a ON a.account_id = c.account_id INNER JOIN coda_sub_product_download AS d ON c.prod_master_id = d.prod_master_id INNER JOIN coda_sub_product AS p ON d.sub_product_id = p.sub_product_id where c.account_id = :account";
	private static String S_SUBPRODUCT_SQL = "select sv.product_id from coda_sds_validate sv join coda_sub_product sp on sp.product_id = sv.product_id where account_id = :account and sub_product_type_id = 1";

	public Set<Integer> getSubProducts(final int account)
	{
		final SqlQuery sqlQuery = Ebean.createSqlQuery(S_SUBPRODUCT_SQL);
		sqlQuery.setParameter("account", account);

		final Set<Integer> products = new LinkedHashSet<Integer>();
		for(final SqlRow row : sqlQuery.findList())
		{
			products.add(row.getInteger("product_id"));
		}

		return products;
	}
}
