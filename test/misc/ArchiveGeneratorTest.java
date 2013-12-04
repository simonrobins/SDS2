package misc;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.start;
import static play.test.Helpers.stop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.fest.assertions.Assertions;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import play.test.FakeApplication;

public class ArchiveGeneratorTest
{
	private static String ARCHIVE = "archive.zip";

	private static FakeApplication app;

	private long maxRetain;
	private long minRetain;
	private long minFreeSpace;

	@BeforeClass
	public static void setUpClass()
	{
		app = fakeApplication();
		start(app);
	}

	@AfterClass
	public static void tearDown()
	{
		stop(app);
	}

	@Before
	public void setUp()
	{
		final File archive = new File(Settings.DOWNLOAD_DIR, ARCHIVE);
		archive.delete();

		final File temp = new File(Settings.TEMPORARY_DIR, ARCHIVE);
		temp.delete();

		maxRetain = Settings.ARCHIVE_MAX_RETAIN_PERIOD;
		minRetain = Settings.ARCHIVE_MIN_RETAIN_PERIOD;
		minFreeSpace = Settings.ARCHIVE_MIN_FREE_SPACE;
	}

	@Test
	public void testArchiveGenerator()
	{
		File root = Settings.SERVICEPACKS_FINANCE_DIR;
		File dir = new File(root, "12.000-SP7");

		List<File> files = new ArrayList<File>();
		for(String name : ServicepackContents.JARS)
			files.add(new File(dir, name + "-12.000.1234.jar"));

		ArchiveGenerator generator = new ArchiveGenerator(ARCHIVE, files, maxRetain, minRetain, minFreeSpace);
		ArchiveGenerator.Status status = generator.run();

		Assertions.assertThat(status).isEqualTo(ArchiveGenerator.Status.OK);
		Assertions.assertThat(new File(Settings.DOWNLOAD_DIR, ARCHIVE)).exists();
		Assertions.assertThat(new File(Settings.TEMPORARY_DIR, ARCHIVE)).doesNotExist();
	}

	@Test
	public void testArchiveGeneratorNoFiles()
	{
		List<File> files = new ArrayList<File>();

		(new File(Settings.TEMPORARY_DIR, ARCHIVE)).delete();
		
		ArchiveGenerator generator = new ArchiveGenerator(ARCHIVE, files, maxRetain, minRetain, minFreeSpace);
		ArchiveGenerator.Status status = generator.run();

		Assertions.assertThat(status).isEqualTo(ArchiveGenerator.Status.ZERO_FILES);
		Assertions.assertThat(new File(Settings.TEMPORARY_DIR, ARCHIVE)).doesNotExist();
		Assertions.assertThat(new File(Settings.DOWNLOAD_DIR, ARCHIVE)).doesNotExist();
	}

	@Test
	public void testArchiveGeneratorAlreadyExists()
	{
		try
		{
			(new File(Settings.DOWNLOAD_DIR, ARCHIVE)).createNewFile();

			List<File> files = new ArrayList<File>();
			files.add(new File("."));

			ArchiveGenerator generator = new ArchiveGenerator(ARCHIVE, files, maxRetain, minRetain, minFreeSpace);
			ArchiveGenerator.Status status = generator.run();

			Assertions.assertThat(status).isEqualTo(ArchiveGenerator.Status.EXISTS);
			Assertions.assertThat(new File(Settings.TEMPORARY_DIR, ARCHIVE)).doesNotExist();
			Assertions.assertThat(new File(Settings.DOWNLOAD_DIR, ARCHIVE)).exists();
		}
		catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
