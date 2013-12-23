package data;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import misc.Languages;

import org.eclipse.jdt.annotation.Nullable;

public class LanguageNode implements Comparable<LanguageNode>
{
	final public String						language;
	final public String						info;

	private File							file		= null;
	private LanguageProduct					product		= null;

	private LanguageNode					parent		= null;

	private long							size		= -1;

	private final Map<String, LanguageNode>	children	= new ConcurrentHashMap<String, LanguageNode>();

	public LanguageNode(final File file)
	{
		this.file = file;
		this.language = file.getName();
		this.info = null;
	}

	public LanguageNode(final String info)
	{
		this.info = info;
		this.language = null;
	}

	public LanguageNode(final int languageId)
	{
		this.language = Languages.get(languageId);
		this.info = null;
	}

	public LanguageNode(final File file, final LanguageProduct product)
	{
		this.file = file;
		this.product = product;
		this.language = file.getName();
		this.info = null;
	}

	public @Nullable
	LanguageProduct getLanguage()
	{
		return product;
	}

	public void intersect(final Collection<ProductVersion> allowed)
	{
		product.getProducts().retainAll(allowed);
	}

	public boolean isEmpty()
	{
		return product.getProducts().isEmpty();
	}

	public Set<ProductVersion> getProducts()
	{
		return product.getProducts();
	}

	public boolean isNotAllowed(final Set<Integer> allowed)
	{
		return allowed.contains(product.languageId) == false;
	}

	public LanguageNode get(final String a, final String b, final String c, final String d, final String e)
	{
		final LanguageNode level1 = this.children.get(a);
		if (level1 == null)
			throw new NullPointerException("level1 is null");
		final LanguageNode level2 = level1.children.get(b);
		if (level2 == null)
			throw new NullPointerException("level2 is null");
		final LanguageNode level3 = level2.children.get(c);
		if (level3 == null)
			throw new NullPointerException("level3 is null");
		final LanguageNode level4 = level3.children.get(d);
		if (level4 == null)
			throw new NullPointerException("level4 is null");
		final LanguageNode level5 = level4.children.get(e);
		if (level5 == null)
			throw new NullPointerException("level5 is null");
		return level5;
	}

	void appendChild(final LanguageNode child)
	{
		this.children.put(child.language, child);
		child.parent = this;
	}

	public void removeChild(final LanguageNode child)
	{
		this.children.remove(child.language);
	}

	public void removeIfNoChildren(final LanguageNode child)
	{
		if (child.children.isEmpty())
			removeChild(child);
	}

	public Collection<LanguageNode> getChildren()
	{
		if (children.isEmpty())
			return Collections.emptyList();
		else
			return new ArrayList<LanguageNode>(this.children.values());
	}

	public @Nullable
	LanguageNode find(final String a, final String b, final String c, final String d)
	{
		final LanguageNode level1 = this.children.get(a);
		if (level1 == null)
			throw new NullPointerException("level1 is null");
		final LanguageNode level2 = level1.children.get(b);
		if (level2 == null)
			throw new NullPointerException("level2 is null");
		final LanguageNode level3 = level2.children.get(c);
		if (level3 == null)
			throw new NullPointerException("level3 is null");
		for (final LanguageNode level4 : level3.children.values())
		{
			final LanguageNode level5 = level4.children.get(d);
			if (level5 != null)
				return (LanguageNode) level5;
		}

		return null;
	}

	public String getDisplayLanguage()
	{
		LanguageProduct language = getLanguage();
		if (language == null)
			return "Language not found";

		final String[] langs = Languages.get(language.languageId).split(" ");

		for (final String lang : langs)
			if (!language.language.contains(lang))
				return language.language + " - ERROR (" + language.display + ")";

		return language.display + ":" + language.language;
	}

	public List<LanguageNode> getSortedChildren()
	{
		if (children.isEmpty())
			return Collections.emptyList();
		else
		{
			final List<LanguageNode> sortedChildren = new ArrayList<LanguageNode>(this.children.values());
			Collections.sort(sortedChildren);
			return sortedChildren;
		}
	}

	@Override
	public int compareTo(final @Nullable LanguageNode o)
	{
		if (o == null)
			throw new NullPointerException();

		return this.language.compareTo(o.language);
	}

	public String getClassString()
	{
		StringBuffer sb = new StringBuffer();

		for (LanguageNode parent = this; parent != null && parent.parent != null; parent = parent.parent)
		{
			sb.insert(0, parent.isLastChild() ? "1" : "0");
		}

		return sb.toString().substring(1);
	}

	public boolean isLastChild()
	{
		if (this.parent == null)
			return false;

		List<LanguageNode> children = this.parent.getSortedChildren();

		return children.get(children.size() - 1).equals(this);
	}

	public long getSize()
	{
		if (size == -1 && file != null)
			size = file.length();

		return size;
	}
}
