package models;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.avaje.ebean.Ebean;

import data.ProductVersion;

@Entity
@Table(name = "coda_sds_validate")
public class CodaSdsValidate
{
	@ManyToOne
	@JoinColumn(name = "account_id", referencedColumnName = "account_id")
	private Account account;

	@ManyToOne
	@JoinColumn(name = "product_id", referencedColumnName = "product_id")
	private Product product;

	@ManyToOne
	@JoinColumn(name = "version_id", referencedColumnName = "version_id")
	private Version version;

	public static Map<Integer, Set<Integer>> getProductVersionMap(final int accountId)
	{
		final Set<CodaSdsValidate> set = Ebean.find(CodaSdsValidate.class).where().eq("account_id", accountId).findSet();

		final Map<Integer, Set<Integer>> map = new LinkedHashMap<Integer, Set<Integer>>();

		for(final CodaSdsValidate validate : set)
		{
			Set<Integer> list = map.get(validate.product.getId());
			if(list == null)
			{
				list = new LinkedHashSet<Integer>();
				map.put(validate.product.getId(), list);
			}
			// This is to exclude the language products which can have a null
			// version
			if(validate.version != null)
				list.add(validate.version.getId());
		}

		return map;
	}

	public static Set<ProductVersion> getProductVersionMap2(final int accountId, boolean excluded)
	{
		final Set<CodaSdsValidate> set = Ebean.find(CodaSdsValidate.class).where().eq("account_id", accountId).findSet();

		final Set<ProductVersion> products = new LinkedHashSet<ProductVersion>();

		for(final CodaSdsValidate validate : set)
		{
			if(validate.product != null && validate.version != null)
			{
				products.add(new ProductVersion(validate.product.getId(), validate.version.getId()));
				if(excluded)
					products.add(new ProductVersion(-validate.product.getId(), validate.version.getId()));
			}
		}

		return products;
	}

	public static Set<ProductVersion> getProductVersionMap2(final int accountId)
	{
		return getProductVersionMap2(accountId, false);
	}

	public static Map<Integer, Set<Integer>> getProductVersionMap(final int accountId, final int versionId)
	{
		final Set<CodaSdsValidate> set = Ebean.find(CodaSdsValidate.class).where().eq("account_id", accountId).eq("version.id", versionId).findSet();

		final Map<Integer, Set<Integer>> map = new LinkedHashMap<Integer, Set<Integer>>();

		for(final CodaSdsValidate validate : set)
		{
			Set<Integer> list = map.get(validate.product.getId());
			if(list == null)
			{
				list = new LinkedHashSet<Integer>();
				map.put(validate.product.getId(), list);
			}
			if(validate.version != null)
				list.add(validate.version.getId());
		}

		return map;
	}

	public Account getAccount()
	{
		return account;
	}

	public void setAccount(Account account)
	{
		this.account = account;
	}

	public Product getProduct()
	{
		return product;
	}

	public void setProduct(Product product)
	{
		this.product = product;
	}

	public Version getVersion()
	{
		return version;
	}

	public void setVersion(Version version)
	{
		this.version = version;
	}
}
