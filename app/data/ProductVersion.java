package data;

import models.Product;
import models.Version;

import org.eclipse.jdt.annotation.Nullable;

public class ProductVersion
{
	public int productId;
	public int versionId;
	public int buildId;

	private Product p = null;
	private Version v = null;

	public ProductVersion(final int productId, final int versionId)
	{
		this(productId, versionId, -1);
	}

	public ProductVersion(final int productId, final int versionId, final int buildId)
	{
		this.productId = productId;
		this.versionId = versionId;
		this.buildId = buildId;
	}

	public boolean valid()
	{
		populate();

		return p != null && v != null;
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
		if(obj == null)
			return false;

		final ProductVersion pv = (ProductVersion) obj;

		return (productId == pv.productId) && (versionId == pv.versionId);
	}

	@Override
	public String toString()
	{
		populate();

		final String product = (p == null) ? "null[" + productId + "]" : p.getProduct().trim() + "[" + p.getId() + "]";
		final String version = (v == null) ? "null[" + versionId + "]" : v.getVersion().trim() + "[" + v.getId() + "]";

		return product + " - " + version;
	}

	private void populate()
	{
		if(p == null)
			p = Product.find(productId);
		if(v == null)
			v = Version.find(versionId);
	}
}
