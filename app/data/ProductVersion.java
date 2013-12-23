package data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import models.Product;
import models.Version;

import org.eclipse.jdt.annotation.Nullable;

public class ProductVersion
{
	public int								productId;
	public int								versionId;
	public int								buildId;

	private static Map<Integer, Product>	products	= Collections.synchronizedMap(new HashMap<Integer, Product>());
	private static Map<Integer, Version>	versions	= Collections.synchronizedMap(new HashMap<Integer, Version>());

	public ProductVersion(final int productId, final int versionId)
	{
		this(productId, versionId, -1);
	}

	public ProductVersion(final int productId, final int versionId, final int buildId)
	{
		this.productId = productId;
		this.versionId = versionId;
		this.buildId = buildId;

		if (products.containsKey(productId) == false)
			products.put(productId, Product.find(productId));
		if (versions.containsKey(versionId) == false)
			versions.put(versionId, Version.find(versionId));
	}

	public boolean valid()
	{
		return (products.containsKey(productId) == true) && (versions.containsKey(versionId) == true);
	}

	@Override
	public int hashCode()
	{
		int result = 17;
		result = 37 * result + productId;
		result = 37 * result + versionId;
		return result;
	}

	@Override
	public boolean equals(@Nullable final Object obj)
	{
		if (obj == null)
			return false;

		final ProductVersion pv = (ProductVersion) obj;

		return (productId == pv.productId) && (versionId == pv.versionId);
	}

	@Override
	public String toString()
	{
		Product p = products.get(productId);
		Version v = versions.get(versionId);

		final String product = (p == null) ? "null[" + productId + "]" : p.getProduct().trim() + "[" + productId + "]";
		final String version = (v == null) ? "null[" + versionId + "]" : v.getVersion().trim() + "[" + productId + "]";

		return product + " - " + version;
	}
}
