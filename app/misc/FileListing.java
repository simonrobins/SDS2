package misc;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.Nullable;

public class FileListing
{
	public static List<File> getFileListing(final File dir)
	{
		final List<File> files = new ArrayList<File>();

		getFileListing(files, dir);

		return files;
	}

	public static List<File> getFileListing(final File dir, final String filename)
	{
		final FileFilter ff = new FileFilter()
		{
			@Override
			public boolean accept(final @Nullable File file)
			{
				if (file.isDirectory())
					return true;
				else if (filename.equals(file.getName()))
					return true;
				else
					return false;
			}
		};

		final List<File> files = new ArrayList<File>();

		getFilteredFileListing(files, dir, ff);

		return files;
	}

	private static void getFilteredFileListing(final List<File> files, final File dir, final FileFilter ff)
	{
		final File[] filesAndDirs = dir.listFiles(ff);

		for (final File file : filesAndDirs)
		{
			if (file.isDirectory())
			{
				getFilteredFileListing(files, file, ff);
			}
			else
			{
				files.add(file);
			}
		}
	}

	private static void getFileListing(final List<File> files, final File dir)
	{
		final File[] filesAndDirs = dir.listFiles();

		for (final File file : filesAndDirs)
		{
			if (file.isDirectory())
			{
				getFileListing(files, file);
			}
			else
			{
				files.add(file);
			}
		}
	}
}
