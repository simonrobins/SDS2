package admin;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.start;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import misc.ServicepackContents;
import misc.Settings;

import org.eclipse.jdt.annotation.Nullable;

import play.Logger;
import data.AbstractNode;
import data.LanguageMap;
import data.LanguageNode;
import data.ProductMap;
import data.ProductNode;

public class DirectoryTemplate
{
	private static class Config
	{
		public final int version;
		public final int number;
		public final String[] tars;
		public final String[] exes;
		public final String[] oass;

		public Config(int version, int number, String[] tars, String[] exes, String[] oass)
		{
			this.version = version;
			this.number = number;
			this.tars = tars;
			this.exes = exes;
			this.oass = oass;
		}

		private String getVersion()
		{
			double ver = ((double) version) / 10;

			return Settings.VERSION_FORMAT.format(ver);
		}
	}

	private static Config[] configs =
		{
			new Config(112, 5, ServicepackContents.TARS112, ServicepackContents.EXES112, ServicepackContents.OASS112), new Config(113, 6, ServicepackContents.TARS113, ServicepackContents.EXES113, ServicepackContents.OASS113), new Config(120, 7, ServicepackContents.TARS120, ServicepackContents.EXES120, ServicepackContents.OASS120),
		};

	public static final void main(String[] args)
	{
		start(fakeApplication());

		for(Config config : configs)
			DirectoryTemplate.populateServicepack(config);

		DirectoryTemplate.populateReleases();
		DirectoryTemplate.populateLanguages();
	}

	private static void populateReleases()
	{
		final File src = Settings.RELEASES_DIR;

		final ProductNode tree = ProductMap.getProductMap();

		for(final AbstractNode level1 : tree.getChildren())
		{
			final File file1 = new File(src, level1.name);
			System.out.println(level1.name);

			for(final AbstractNode level2 : level1.getChildren())
			{
				final File file2 = new File(file1, level2.name);
				System.out.println("  " + level2.name);

				for(final AbstractNode level3 : level2.getChildren())
				{
					final File file3 = new File(file2, level3.name);
					System.out.println("    " + level3.name);

					try
					{
						if(!file3.exists())
						{
							file3.createNewFile();
							final FileWriter fw = new FileWriter(file3);
							final BufferedWriter bw = new BufferedWriter(fw);
							bw.write("Releases/" + level1.name + "/" + level2.name + "/" + level3.name);
							bw.close();
						}
					}
					catch(final Exception e)
					{
						Logger.error("populateLanguages", e);
					}
				}
			}
		}
	}

	private static void populateLanguages()
	{
		final File src = Settings.LANGUAGEPACKS_DIR;

		final LanguageNode tree = LanguageMap.getLanguageMap();

		for(final AbstractNode level1 : tree.getChildren())
		{
			final File file1 = new File(src, level1.name);
			System.out.println(level1.name);

			for(final AbstractNode level2 : level1.getChildren())
			{
				final File file2 = new File(file1, level2.name);
				System.out.println("  " + level2.name);

				for(final AbstractNode level3 : level2.getChildren())
				{
					final File file3 = new File(file2, level3.name);
					System.out.println("    " + level3.name);

					for(final AbstractNode level4 : level3.getChildren())
					{
						System.out.println("    " + level4.name);

						for(final AbstractNode level5 : level4.getChildren())
						{
							final File file5 = new File(file3, level5.name);
							System.out.println("    " + level5.name);

							try
							{
								if(!file5.exists())
								{
									file5.createNewFile();
									final FileWriter fw = new FileWriter(file5);
									final BufferedWriter bw = new BufferedWriter(fw);
									bw.write("Releases/" + level1.name + "/" + level2.name + "/" + level3.name + "/" + level4.name);
									bw.close();
								}
							}
							catch(final Exception e)
							{
								Logger.error("populateLanguages", e);
							}
						}
					}
				}
			}
		}
	}

	private static void populateServicepack(Config config)
	{
		populateJars(config);
		populateCoreClient(config);
		populateTars(config);
		populateExes(config);
		populateOass(config);
	}

	private static void populateJars(Config config)
	{
		final File base = makeDirs(config);

		for(String baseName : ServicepackContents.JARS)
		{
			String jarName = baseName + "-" + config.getVersion() + ".1234.jar";
			File jar = new File(base, jarName);

			createFile(jar, jarName);
		}
	}

	private static void populateCoreClient(Config config)
	{
		final File base = makeDirs(config, "core-client");

		for(String baseName : ServicepackContents.CORES)
		{
			String jarName = baseName + "-" + config.getVersion() + ".1234.exe";
			File jar = new File(base, jarName);

			createFile(jar, jarName);
		}
	}

	private static void populateTars(Config config)
	{
		final File base = makeDirs(config, "finance-app");

		for(String baseName : config.tars)
		{
			String jarName = baseName + "-" + config.getVersion() + ".1234.tar.Z";
			File jar = new File(base, jarName);

			createFile(jar, jarName);
		}
	}

	private static void populateExes(Config config)
	{
		final File base = makeDirs(config, "finance-app");

		for(String baseName : config.exes)
		{
			String jarName = baseName + "-" + config.getVersion() + ".1234.exe";
			File jar = new File(base, jarName);

			createFile(jar, jarName);
		}
	}

	private static void populateOass(Config config)
	{
		final File base = makeDirs(config, "finance-app");

		for(String baseName : config.oass)
		{
			String jarName = "V" + config.version + baseName;
			File jar = new File(base, jarName);

			createFile(jar, jarName);
		}
	}

	private static void createFile(File jar, String contents)
	{
		try
		{
			if(!jar.exists())
			{
				jar.createNewFile();
				final FileWriter fw = new FileWriter(jar);
				final BufferedWriter bw = new BufferedWriter(fw);
				bw.write(contents);
				bw.close();
			}
		}
		catch(final Exception e)
		{
			Logger.error("createFile", e);
		}
	}

	private static File makeDirs(Config config)
	{
		return makeDirs(config, null);
	}

	private static File makeDirs(Config config, @Nullable String dir)
	{
		final File root = Settings.SERVICEPACKS_FINANCE_DIR;

		File base = new File(root, config.getVersion() + "-SP" + config.number);
		if(dir != null)
			base = new File(base, dir);

		base.mkdirs();

		return base;
	}
}

