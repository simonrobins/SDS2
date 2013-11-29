package data;

import java.io.File;
import java.util.Collection;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

public class LanguageNode extends AbstractNode
{
	private final LanguageProduct language;

	public LanguageNode(final File file)
	{
		super(file);
		this.language = null;
	}

	public LanguageNode(final String info)
	{
		super(info);
		this.language = null;
	}

	public LanguageNode(final File file, boolean isDirectory)
	{
		super(file, isDirectory);
		this.language = null;
	}

	public LanguageNode(final int languageId)
	{
		super(languageId);
		this.language = null;
	}

	public LanguageNode(final File file, final LanguageProduct language)
	{
		super(file, true);
		this.language = language;
	}

	public LanguageProduct getLanguage()
	{
		return language;
	}

	@Override
	public void intersect(final @NonNull Collection<ProductVersion> allowed)
	{
		language.getProducts().retainAll(allowed);
	}

	@Override
	public boolean isEmpty()
	{
		return language.getProducts().isEmpty();
	}

	public Set<ProductVersion> getProducts()
	{
		return language.getProducts();
	}

	@Override
	public boolean isNotAllowed(final @NonNull Set<Integer> allowed)
	{
		return allowed.contains(language.languageId) == false;
	}
}
