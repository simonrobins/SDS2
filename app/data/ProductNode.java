package data;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

public class ProductNode extends AbstractNode
{
	private final Set<ProductVersion> products;

	ProductNode(final String info)
	{
		super(info);
		this.products = new HashSet<ProductVersion>();
	}

	ProductNode(final File file)
	{
		super(file, true);
		this.products = new HashSet<ProductVersion>();
	}

	ProductNode(final File file, final Set<ProductVersion> products)
	{
		super(file, false);
		this.products = products;
	}

	public Set<ProductVersion> getProducts()
	{
		return new HashSet<ProductVersion>(this.products);
	}

	@Override
	public void intersect(final @NonNull Collection<ProductVersion> allowed)
	{
		products.retainAll(allowed);
	}

	@Override
	public boolean isEmpty()
	{
		if(products == null)
			return true;
		return products.isEmpty();
	}

	@Override
	public boolean isNotAllowed(final @NonNull Set<Integer> allowed)
	{
		// Do nothing, do not call for ProductTree - a bit of a code smell but
		// the alternative is much more messy
		return false;
	}

	public void removeDisallowed(Set<ProductVersion> allowed)
	{
		for(final AbstractNode level1 : getChildren())
		{
			for(final AbstractNode level2 : level1.getChildren())
			{
				for(final AbstractNode level3 : level2.getChildren())
				{
					level3.intersect(allowed);
					if(level3.isEmpty())
						level2.removeChild(level3);
				}
				level1.removeIfNoChildren(level2);
			}
			removeIfNoChildren(level1);
		}
	}
}
