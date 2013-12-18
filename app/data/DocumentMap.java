package data;

import java.io.File;
import java.io.FileFilter;

import misc.Settings;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

public class DocumentMap
{
	private static File[] listFiles(final File file)
	{
		return listFiles(file, null);
	}

	private static File[] listFiles(final File file, @Nullable final String extension)
	{
		File[] files = file.listFiles(new FileFilter()
			{
				@Override
				public boolean accept(final @Nullable
				File file)
				{
					if(file == null)
						return false;
					if(extension == null)
						return true;
					if(file.isDirectory())
						return true;
					if(file.getName().toLowerCase().endsWith(extension))
						return true;
					return false;
				}
			});

		return files != null ? files : new File[0];
	}

	public static DocumentNode getDocumentMap()
	{
		final File root = Settings.DOCUMENTS_DIR;

		final DocumentNode documents = new DocumentNode();

		for(final File product : listFiles(root))
		{
			if(product.isDirectory())
			{
				final DocumentNode level1 = new DocumentNode(product);
				documents.appendChild(level1);

				for(final File version : listFiles(product))
				{
					if(version.isDirectory())
					{
						final DocumentNode level2 = new DocumentNode(version);
						level1.appendChild(level2);

						for(final @NonNull File document : listFiles(version, ".pdf"))
						{
							final DocumentNode level3 = new DocumentNode(document);
							level2.appendChild(level3);

							if(document.isDirectory())
							{
								for(final File document1 : listFiles(document, ".pdf"))
								{
									final DocumentNode level4 = new DocumentNode(document1);
									level3.appendChild(level4);
								}
							}
						}
					}
				}
			}
		}

		return documents;
	}

	public static DocumentNode getSupportMap(final String accountId)
	{
		final File root = new File(Settings.SUPPORT_DIR, accountId);

		final DocumentNode documents = new DocumentNode();

		final File[] accounts = listFiles(root);

		if(accounts != null)
		{
			for(final File account : accounts)
			{
				final DocumentNode level1 = new DocumentNode(account);
				documents.appendChild(level1);

				if(account.isDirectory())
				{
					for(final File file : listFiles(account))
					{
						if(file.isFile())
						{
							final DocumentNode level2 = new DocumentNode(file);
							level1.appendChild(level2);
						}
					}
				}
			}
		}

		return documents;
	}
}
