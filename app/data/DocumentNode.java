package data;

import java.io.File;
import java.util.Collection;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

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
	public void intersect(final @NonNull Collection<ProductVersion> allowed)
	{
	}

	@Override
	public boolean isNotAllowed(final @NonNull Set<Integer> allowed)
	{
		return false;
	}
}
