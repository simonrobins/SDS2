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
import misc.Utilities;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

@NonNullByDefault
public abstract class AbstractNode implements Comparable<AbstractNode>
{
	public final String name;

	private AbstractNode parent = null;

	private final File file;
	private long size = -1;
	private Boolean isDirectory = Boolean.FALSE;
	private String info = null;

	private final Map<String, AbstractNode> children = new ConcurrentHashMap<String, AbstractNode>();

	public abstract void intersect(Collection<ProductVersion> allowed);

	public abstract boolean isNotAllowed(Set<Integer> allowed);

	public boolean isEmpty()
	{
		return children.isEmpty();
	}

	protected AbstractNode()
	{
		this.file = null;
		this.name = "root";
	}

	protected AbstractNode(final String info)
	{
		this.file = null;
		this.name = "root";
	}

	protected AbstractNode(final int languageId)
	{
		this.file = null;
		this.name = Languages.get(languageId);
	}

	protected AbstractNode(final File file)
	{
		this.file = file;
		this.name = file.getName();
	}

	protected AbstractNode(final File file, boolean isDirectory)
	{
		this.file = file;
		this.name = file.getName();
		this.isDirectory = isDirectory;
	}

	public long getSize()
	{
		if(size == -1 && file != null)
			size = file.length();

		return size;
	}

	public boolean isDirectory()
	{
		if(this.isDirectory == null && this.file != null)
			this.isDirectory = this.file.isDirectory();

		if(this.isDirectory == null)
			return true;

		return isDirectory;
	}

	public @Nullable
	String getInfo()
	{
		if(this.info == null && this.file != null && isDirectory())
			this.info = Utilities.getInformation(this.file);

		return this.info;
	}

	void appendChild(final AbstractNode child)
	{
		this.children.put(child.name, child);
		child.parent = this;
	}

	public Collection<AbstractNode> getChildren()
	{
		if(children.isEmpty())
			return Collections.emptyList();
		else
			return new ArrayList<AbstractNode>(this.children.values());
	}

	public List<AbstractNode> getSortedChildren()
	{
		if(children.isEmpty())
			return Collections.emptyList();
		else
		{
			final List<AbstractNode> sortedChildren = new ArrayList<AbstractNode>(this.children.values());
			Collections.sort(sortedChildren);
			return sortedChildren;
		}
	}

	public String getClassString()
	{
		StringBuffer sb = new StringBuffer();

		for(AbstractNode parent = this; parent != null && parent.parent != null; parent = parent.parent)
		{
			sb.insert(0, parent.isLastChild() ? "1" : "0");
		}

		return sb.toString().substring(1);
	}

	public boolean isLastChild()
	{
		if(this.parent == null)
			return false;

		List<AbstractNode> children = this.parent.getSortedChildren();

		return children.get(children.size() - 1).equals(this);
	}

	@Override
	public int compareTo(final @Nullable AbstractNode o)
	{
		if(o == null)
			throw new NullPointerException();

		return name.compareTo(o.name);
	}

	public void removeIfNoChildren(final AbstractNode child)
	{
		if(child.children.isEmpty())
			removeChild(child);
	}

	public void removeChild(final AbstractNode child)
	{
		this.children.remove(child.name);
	}

	public @Nullable
	ProductNode get(final String a, final String b, final String c)
	{
		final AbstractNode level1 = this.children.get(a);
		if(level1 == null)
			throw new NullPointerException("level1 is null");
		final AbstractNode level2 = level1.children.get(b);
		if(level2 == null)
			throw new NullPointerException("level2 is null");
		final AbstractNode level3 = level2.children.get(c);

		return (ProductNode) level3;
	}

	public LanguageNode get(final String a, final String b, final String c, final String d, final String e)
	{
		final AbstractNode level1 = this.children.get(a);
		if(level1 == null)
			throw new NullPointerException("level1 is null");
		final AbstractNode level2 = level1.children.get(b);
		if(level2 == null)
			throw new NullPointerException("level2 is null");
		final AbstractNode level3 = level2.children.get(c);
		if(level3 == null)
			throw new NullPointerException("level3 is null");
		final AbstractNode level4 = level3.children.get(d);
		if(level4 == null)
			throw new NullPointerException("level4 is null");
		final AbstractNode level5 = level4.children.get(e);
		if(level5 == null)
			throw new NullPointerException("level5 is null");
		return (LanguageNode) level5;
	}

	public @Nullable
	LanguageNode find(final String a, final String b, final String c, final String d)
	{
		final AbstractNode level1 = this.children.get(a);
		if(level1 == null)
			throw new NullPointerException("level1 is null");
		final AbstractNode level2 = level1.children.get(b);
		if(level2 == null)
			throw new NullPointerException("level2 is null");
		final AbstractNode level3 = level2.children.get(c);
		if(level3 == null)
			throw new NullPointerException("level3 is null");
		for(final AbstractNode level4 : level3.children.values())
		{
			final AbstractNode level5 = level4.children.get(d);
			if(level5 != null)
				return (LanguageNode) level5;
		}

		return null;
	}

	public String getDisplayLanguage()
	{
		LanguageNode self = (LanguageNode) this;
		LanguageProduct language = self.getLanguage();

		if(language == null)
			return "Language not found";

		final String[] langs = Languages.get(language.languageId).split(" ");

		for(final String lang : langs)
			if(!language.language.contains(lang))
				return language.language + " - ERROR (" + language.display + ")";

		return language.display + ":" + language.language;
	}

	@Override
	public String toString()
	{
		return name + "\n" + children;
	}
}
