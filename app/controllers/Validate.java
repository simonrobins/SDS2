package controllers;

import helpers.ProductHelper;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import misc.FileListing;
import misc.ServicepackContents;
import misc.Settings;

import org.eclipse.jdt.annotation.Nullable;

import play.libs.Akka;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;
import data.LanguageMap;
import data.LanguageNode;
import data.ProductMap;
import data.ProductNode;
import data.ProductVersion;

public class Validate extends Controller
{
	public static Result index()
	{
		return ok(views.html.validate.index.render());
	}

	public static Result missingProducts()
	{
		final Promise<List<String>> promiseOfList = Akka.future(new Callable<List<String>>()
			{
				@Override
				public List<String> call()
				{
					return findMissing(Settings.RELEASES_DIR, "productlist.txt");
				}
			});

		return async(promiseOfList.map(new Function<List<String>, Result>()
			{
				@Override
				public Result apply(final @Nullable List<String> releases)
				{
					return ok(views.html.validate.missingProducts.render(releases));
				}
			}));
	}

	public static Result unmappedProducts()
	{
		final Promise<List<String>> promiseOfList = Akka.future(new Callable<List<String>>()
			{
				@Override
				public List<String> call()
				{
					return findUnmapped(true, 4);
				}
			});

		return async(promiseOfList.map(new Function<List<String>, Result>()
			{
				@Override
				public Result apply(final @Nullable List<String> releases)
				{
					return ok(views.html.validate.missingProducts.render(releases));
				}
			}));
	}

	public static Result missingLanguages()
	{
		final Promise<List<String>> promiseOfList = Akka.future(new Callable<List<String>>()
			{
				@Override
				public List<String> call()
				{
					return findMissing(Settings.LANGUAGEPACKS_DIR, "languagelist.txt");
				}
			});

		return async(promiseOfList.map(new Function<List<String>, Result>()
			{
				@Override
				public Result apply(final @Nullable List<String> languages)
				{
					return ok(views.html.validate.missingLanguages.render(languages));
				}
			}));
	}

	public static Result unmappedLanguages()
	{
		final Promise<List<String>> promiseOfList = Akka.future(new Callable<List<String>>()
			{
				@Override
				public List<String> call()
				{
					return findUnmapped(false, 5);
				}
			});

		return async(promiseOfList.map(new Function<List<String>, Result>()
			{
				@Override
				public Result apply(final @Nullable List<String> languages)
				{
					return ok(views.html.validate.missingLanguages.render(languages));
				}
			}));
	}

	public static Result releases()
	{
		final Promise<ProductNode> promiseOfList = Akka.future(new Callable<ProductNode>()
			{
				@Override
				public ProductNode call()
				{
					return ProductMap._getProductMap();
				}
			});

		return async(promiseOfList.map(new Function<ProductNode, Result>()
			{
				@Override
				public Result apply(final @Nullable ProductNode products)
				{
					return ok(views.html.validate.releases.render(products));
				}
			}));
	}

	public static Result languages()
	{
		final Promise<LanguageNode> promiseOfList = Akka.future(new Callable<LanguageNode>()
			{
				@Override
				public LanguageNode call()
				{
					return LanguageMap.getLanguageMap();
				}
			});

		return async(promiseOfList.map(new Function<LanguageNode, Result>()
			{
				@Override
				public Result apply(final @Nullable LanguageNode languages)
				{
					return ok(views.html.validate.languages.render(languages));
				}
			}));
	}

	public static Result servicepacks()
	{
		final Promise<Map<File, List<String>>> promiseOfList = Akka.future(new Callable<Map<File, List<String>>>()
			{
				@Override
				public Map<File, List<String>> call()
				{
					return getMissing();
				}
			});

		return async(promiseOfList.map(new Function<Map<File, List<String>>, Result>()
			{
				@Override
				public Result apply(final @Nullable Map<File, List<String>> servicepacks)
				{
					return ok(views.html.validate.servicepacks.render(servicepacks));
				}
			}));
	}

	private final static FileFilter sps = new FileFilter()
		{
			@Override
			public boolean accept(final @Nullable File file)
			{
				if(file == null)
					return false;
				else
					return file.isDirectory() && file.getName().matches("\\d\\d\\.\\d\\d\\d\\-SP\\d\\d?");
			}
		};

	private static Map<File, List<String>> getMissing()
	{
		final Map<File, List<String>> missing = new LinkedHashMap<File, List<String>>();

		File[] spDirs = Settings.SERVICEPACKS_FINANCE_DIR.listFiles(sps);

		if(spDirs != null)
		{
			for(final File spDir : spDirs)
			{
				final String version = spDir.getName().substring(0, 6);
				final String shortVersion = version.substring(0, 2) + version.substring(3, 4);

				final List<File> files = FileListing.getFileListing(spDir);

				if(files.size() == 0)
					continue;

				final String firstFile = files.get(0).getName();
				final String build = firstFile.substring(firstFile.length() - 8, firstFile.length() - 4);

				String[] tars = ServicepackContents.TARS120;
				String[] exes = ServicepackContents.EXES120;
				String[] oass = ServicepackContents.OASS120;

				if("11.200".equals(version))
				{
					tars = ServicepackContents.TARS112;
					exes = ServicepackContents.EXES112;
					oass = ServicepackContents.OASS112;
				}
				else if("11.300".equals(version))
				{
					tars = ServicepackContents.TARS113;
					exes = ServicepackContents.EXES113;
					oass = ServicepackContents.OASS113;
				}

				found: for(final String jar : ServicepackContents.JARS)
				{
					final String name = jar + "-" + version + "." + build + ".jar";

					for(final File file : files)
					{
						if(file.getName().equals(name))
							continue found;
					}

					addMissing(missing, spDir, name);
				}

				found: for(final String tar : tars)
				{
					final String name = tar + "-" + version + "." + build + ".tar.Z";

					for(final File file : files)
					{
						if(file.getName().equals(name))
							continue found;
					}

					addMissing(missing, spDir, name);
				}

				found: for(final String exe : exes)
				{
					final String name = exe + "-" + version + "." + build + ".exe";

					for(final File file : files)
					{
						if(file.getName().equals(name))
							continue found;
					}

					addMissing(missing, spDir, name);
				}

				found: for(final String oas : oass)
				{
					final String name = "V" + shortVersion + oas;

					for(final File file : files)
					{
						if(file.getName().equals(name))
							continue found;
					}

					addMissing(missing, spDir, name);
				}

				found: for(final String core : ServicepackContents.CORES)
				{
					final String name = core + "-" + version + "." + build + ".exe";

					for(final File file : files)
					{
						if(file.getName().equals(name))
							continue found;
					}

					addMissing(missing, spDir, name);
				}

				found: for(final String pdf : ServicepackContents.PDFS)
				{
					final String name = pdf + spDir.getName() + ".pdf";
					for(final File file : files)
					{
						if(file.getName().equals(name))
							continue found;
					}

					addMissing(missing, spDir, name);
				}
			}
		}
		return missing;
	}

	private static void addMissing(final Map<File, List<String>> missing, final File spDir, final String name)
	{
		List<String> list = missing.get(spDir);

		if(list == null)
			list = new ArrayList<String>();

		list.add(name);

		missing.put(spDir, list);
	}

	private static List<String> findMissing(final File dir, final String filename)
	{
		final List<String> missing = new ArrayList<String>();

		for(final File file : FileListing.getFileListing(dir, filename))
		{
			final Map<String, Set<ProductVersion>> fileList = ProductMap.parseProducts(file);

			for(final String name : fileList.keySet())
			{
				final File filex = new File(file.getParentFile(), name);
				if(!filex.exists())
				{
					final String relativePath = filex.getAbsolutePath().substring(dir.getAbsolutePath().length());
					missing.add(relativePath);
				}

				ProductHelper.validateProductIds(fileList.get(name));
			}
		}
		Collections.sort(missing);
		return missing;
	}

	private static List<String> findUnmapped(final boolean releases, final int length)
	{
		final List<String> missing = new ArrayList<String>();

		final File root = releases ? Settings.RELEASES_DIR : Settings.LANGUAGEPACKS_DIR;
		final List<File> files = FileListing.getFileListing(root);

		for(final File file : files)
		{
			final String path = file.getAbsolutePath().substring(root.getAbsolutePath().length());
			String[] parts = path.split("\\\\");

			if(parts.length == length && parts[0].length() == 0)
			{
				final String[] normalisedParts = new String[length - 1];
				System.arraycopy(parts, 1, normalisedParts, 0, length - 1);
				parts = normalisedParts;
			}

			if(parts.length != length - 1)
				missing.add(path);
			else
			{
				String pattern;

				if(releases)
					pattern = "^(productlist\\.txt)|(info\\.txt)|(.+\\.md5)|(.+\\.gz)|(.+~)$";
				else
					pattern = "^(languagelist\\.txt)|(info\\.txt)|(.+\\.md5)|(.+\\.gz)|(.+~)$";

				final boolean matches = parts[length - 2].matches(pattern);

				if(matches == false)
				{
					try
					{
						if(releases)
						{
							final ProductNode products = ProductMap.getProductMap();
							final ProductNode product = products.get(parts[0], parts[1], parts[2]);
							if(product == null)
								missing.add(path);
						}
						else
						{
							final LanguageNode languages = LanguageMap.getLanguageMap();
							final LanguageNode language = languages.find(parts[0], parts[1], parts[2], parts[3]);
							if(language == null)
								missing.add(path);
						}
					}
					catch(final Exception ex)
					{
						missing.add(path);
					}
				}
			}
		}

		return missing;
	}
}
