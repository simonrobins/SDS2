package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import misc.Settings;
import misc.Utilities;
import play.Logger;

public class LanguageMap
{
	private static Pattern	COMMA	= Pattern.compile(",");
	private static Pattern	DOT		= Pattern.compile("\\.");

	private static File[] listFiles(final File file)
	{
		return file.listFiles(new FilenameFilter()
		{
			@SuppressWarnings("null")
			@Override
			public boolean accept(final File dir, final String name)
			{
				return !(name.equalsIgnoreCase("info.txt") || name.equalsIgnoreCase("information.txt"));
			}
		});
	}

	public static LanguageNode getLanguageMap()
	{
		final File root = Settings.LANGUAGEPACKS_DIR;

		String info = Utilities.getInformation(root);
		final LanguageNode languageTree = new LanguageNode(info);

		for (final File product : listFiles(root))
		{
			if (product.isDirectory())
			{
				final LanguageNode level1 = new LanguageNode(product);
				languageTree.appendChild(level1);

				for (final File version : listFiles(product))
				{
					if (version.isDirectory())
					{
						final LanguageNode level2 = new LanguageNode(version);
						level1.appendChild(level2);

						for (final File release : listFiles(version))
						{
							if (release.isFile())
							{
								final LanguageNode level3 = new LanguageNode(release);
								level2.appendChild(level3);
							}
							if (release.isDirectory())
							{
								final LanguageNode level3 = new LanguageNode(release);
								level2.appendChild(level3);

								final File productTxt = new File(release, "languagelist.txt");
								if (productTxt.exists() && productTxt.isFile())
								{
									final Map<Integer, LanguageNode> languages = new HashMap<Integer, LanguageNode>();
									final Map<String, LanguageProduct> productIds = parseLanguages(productTxt);

									for (final String key : productIds.keySet())
									{
										final LanguageProduct languageProduct = productIds.get(key);
										LanguageNode language = languages.get(languageProduct.languageId);
										if (language == null)
										{
											language = new LanguageNode(languageProduct.languageId);
											languages.put(languageProduct.languageId, language);
											level3.appendChild(language);
										}

										final File file = new File(release, key);
										final LanguageNode level4 = new LanguageNode(file, languageProduct);
										language.appendChild(level4);
									}
								}
							}
						}
					}
				}
			}
		}

		return languageTree;
	}

	private static Map<String, LanguageProduct> parseLanguages(final File file)
	{
		final Map<String, LanguageProduct> products = new LinkedHashMap<String, LanguageProduct>();

		BufferedReader br = null;

		try
		{
			br = new BufferedReader(new FileReader(file));
			String line;

			while ((line = br.readLine()) != null)
			{
				line = line.trim();

				if (line.length() == 0 || (line.charAt(0) == '#'))
					continue;

				final String[] parts = COMMA.split(line);

				final Set<ProductVersion> list = new LinkedHashSet<ProductVersion>();

				int subProductId = 0;

				int productId = 0;
				int versionId = 0;

				for (int i = 1; i < parts.length; i++)
				{
					if (i == 1)
					{
						subProductId = Integer.parseInt(parts[i].trim());
					}
					else
					{
						final String[] idParts = DOT.split(parts[i]);
						productId = Integer.parseInt(idParts[0].trim());
						if (idParts.length > 1)
							versionId = Integer.parseInt(idParts[1].trim());

						list.add(new ProductVersion(productId, versionId));
					}
				}

				products.put(parts[0], new LanguageProduct(parts[0], subProductId, list));
			}
		}
		catch (final Exception e)
		{
			Logger.info("Parsing file " + file);
			Logger.info(e.getMessage());
		}
		finally
		{
			Utilities.quietClose(br);
		}

		return products;
	}
}
