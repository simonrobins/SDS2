package data;

import java.io.File;
import java.util.Collection;
import java.util.Set;

public class DocumentNode extends AbstractNode
{
	DocumentNode(final File file)
	{
		super(file, true);
	}

	DocumentNode()
	{
		super();
	}

	@Override
	public void intersect(final Collection<ProductVersion> allowed)
	{
	}

	@Override
	public boolean isNotAllowed(final Set<Integer> allowed)
	{
		return false;
	}
}
