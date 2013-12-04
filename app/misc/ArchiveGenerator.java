package misc;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import play.Logger;

public class ArchiveGenerator
{
	public enum Status
	{
		OK, GENERATING, EXISTS, CANT_DELETE, CANT_RENAME, EXCEPTION, NO_SPACE, ZERO_FILES
	}

	private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.

	private static final Set<String> locks = Collections.synchronizedSet(new LinkedHashSet<String>());

	private final List<File> files;
	private final String archive;
	private final long maxRetainPeriod;
	private final long minRetainPeriod;
	private final long minFreeSpace;

	public ArchiveGenerator(final String archive, final List<File> files, final long maxRetainPeriod, final long minRetainPeriod, final long minFreeSpace)
	{
		this.files = files;
		this.archive = archive;
		this.maxRetainPeriod = maxRetainPeriod;
		this.minRetainPeriod = minRetainPeriod;
		this.minFreeSpace = minFreeSpace;
	}

	public Status run()
	{
		if(locks.add(archive) == false)
		{
			Logger.info(archive + " already being generated");
			return Status.GENERATING; // someone got here before us.
		}

		if(this.files.size() == 0)
		{
			Logger.info("No files to ");
			locks.remove(archive);
			return Status.ZERO_FILES;
		}

		final File archiveFile = new File(Settings.DOWNLOAD_DIR, archive);

		if(archiveFile.exists())
		{
			Logger.info(archive + " already exists");
			locks.remove(archive);
			return Status.EXISTS;
		}

		// Don't keep anything older than six weeks (a service pack cycle)
		housekeep(maxRetainPeriod);

		long retainPeriod = maxRetainPeriod;

		// Try a make sure that there is enough space for the archive
		while((Settings.TEMPORARY_DIR.getUsableSpace() < minFreeSpace) && (retainPeriod > minRetainPeriod))
			housekeep(--retainPeriod);

		long usableSpace = Settings.TEMPORARY_DIR.getUsableSpace();
		if(usableSpace > 0 && usableSpace < minFreeSpace)
		{
			Logger.info("no space to generate " + archive);
			return Status.NO_SPACE;
		}

		Logger.info("generating " + archive);

		final File tempFile = new File(Settings.TEMPORARY_DIR, archive);

		ZipOutputStream output = null;
		try
		{
			tempFile.getParentFile().mkdirs();
			output = new ZipOutputStream(new FileOutputStream(tempFile));

			for(final File file : files)
			{
				Status status = putNextEntry(output, file);
				if(Status.OK != status)
					return status;
			}

			output.close();

			if(archiveFile.delete())
			{
				Logger.error("Could not delete " + archiveFile);
				return Status.CANT_DELETE;
			}
			if(tempFile.renameTo(archiveFile) == false)
			{
				Logger.error("Could not rename " + tempFile + " to " + archiveFile);
				return Status.CANT_RENAME;
			}
		}
		catch(final Exception e)
		{
			tempFile.delete();
			Logger.error("Error occurred generating " + archive, e);
			return Status.EXCEPTION;
		}
		finally
		{
			Utilities.quietClose(output);
			locks.remove(archive);
		}
		return Status.OK;
	}

	private void housekeep(final long period)
	{
		final long now = System.currentTimeMillis();
		final long oneMonthAgo = now - (period * 24L * 60L * 60L * 1000L);
		final File downloadDir = Settings.DOWNLOAD_DIR;

		if(downloadDir != null)
		{
			File[] files = downloadDir.listFiles();
			if(files != null)
			{
				for(final File file : downloadDir.listFiles())
				{
					if(file.lastModified() < oneMonthAgo)
					{
						Logger.info("Deleting out-of-date service pack: " + file.getName() + " - lastModified: " + file.lastModified());
						file.delete();
					}
				}
			}
		}
	}

	private static Status putNextEntry(final ZipOutputStream output, final File file)
		throws Exception
	{
		final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		InputStream input = null;

		if(!file.exists())
		{
			Logger.error("file (" + file + ") does not exist");
			return Status.EXISTS;
		}

		try
		{
			final FileInputStream is = new FileInputStream(file);
			input = new BufferedInputStream(is, DEFAULT_BUFFER_SIZE);
			output.putNextEntry(new ZipEntry(file.getName()));
			for(int length = 0; (length = input.read(buffer)) > 0;)
			{
				output.write(buffer, 0, length);
				output.flush();
			}
			output.closeEntry();

			return Status.OK;
		}
		finally
		{
			Utilities.quietClose(input);
		}
	}
}
