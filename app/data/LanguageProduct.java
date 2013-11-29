package data;

import java.util.HashSet;
import java.util.Set;

import misc.Languages;
import models.Product;

public class LanguageProduct
{
	public final String language;
	public final int languageId;
	public final String display;

	private final Set<ProductVersion> products;

	public LanguageProduct(final String language, final int languageId, final Set<ProductVersion> products)
	{
		this.language = language;
		this.languageId = languageId;
		this.products = products;
		this.display = Languages.get(languageId);
	}

	public Set<ProductVersion> getProducts()
	{
		return new HashSet<ProductVersion>(this.products);
	}

	public Product getLanguage()
	{
		return Product.find(languageId);
	}
}
