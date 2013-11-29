package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import misc.Settings;
import misc.Utilities;

import org.eclipse.jdt.annotation.Nullable;

import play.Logger;
import play.cache.Cache;

public class ProductMap
{
	public static Map<String, Set<ProductVersion>> parseProducts(final File file)
	{
		final Map<String, Set<ProductVersion>> products = new TreeMap<String, Set<ProductVersion>>();

		BufferedReader br = null;

		try
		{
			br = new BufferedReader(new FileReader(file));
			String line;

			while((line = br.readLine()) != null)
			{
				if(line.length() == 0 || (line.charAt(0) == '#'))
					continue;

				final String[] parts = line.split(",");

				int productId = 0;
				int versionId = 0;
				int buildId = 0;

				final Set<ProductVersion> list = new LinkedHashSet<ProductVersion>();
				for(int i = 1; i < parts.length; i++)
				{
					final String[] idParts = parts[i].split("\\.");
					productId = Integer.parseInt(idParts[0].trim());
					if(idParts.length > 1)
						versionId = Integer.parseInt(idParts[1].trim());
					if(idParts.length > 2)
						buildId = Integer.parseInt(idParts[2].trim());

					list.add(new ProductVersion(productId, versionId, buildId));
				}

				products.put(parts[0], list);
			}
		}
		catch(final Exception e)
		{
			Logger.info("Parsing file " + file);
			Logger.info(e.getMessage());
		}
		finally
		{
			if(br != null)
			{
				try
				{
					br.close();
				}
				catch(final IOException e)
				{
					e.printStackTrace();
				}
			}
		}

		return products;
	}

	private static File[] listFiles(final File file)
	{
		return file.listFiles(new FilenameFilter()
			{
				@Override
				public boolean accept(final @Nullable File dir, final @Nullable
				String name)
				{
					if(name == null)
						return false;
					else
						return !(name.equalsIgnoreCase("info.txt") || name.equalsIgnoreCase("information.txt"));
				}
			});
	}

	public static ProductNode getProductMap()
	{
		ProductNode products = (ProductNode) Cache.get("data.ProductMap.getProductMap3");
		if(products == null)
		{
			products = _getProductMap();
			Cache.set("data.ProductMap.getProductMap3", products, Settings.PRODUCTS_CACHE_INTERVAL);
		}
		return products;
	}

	public static ProductNode _getProductMap()
	{
		final File root = Settings.RELEASES_DIR;

		String info = Utilities.getInformation(root);
		final ProductNode products = new ProductNode(info);

		for(final File product : listFiles(root))
		{
			info = Utilities.getInformation(product);
			if(product.isDirectory())
			{
				final ProductNode level1 = new ProductNode(product);
				products.appendChild(level1);

				for(final File version : listFiles(product))
				{
					info = Utilities.getInformation(version);
					if(version.isDirectory())
					{
						final ProductNode level2 = new ProductNode(version);
						level1.appendChild(level2);

						final File productTxt = new File(version, "productlist.txt");
						if(productTxt.exists())
						{
							final Map<String, Set<ProductVersion>> productIds = parseProducts(productTxt);
							for(final String key : productIds.keySet())
							{
								final File file = new File(version, key);
								final ProductNode level3 = new ProductNode(file, productIds.get(key));
								level2.appendChild(level3);
							}
						}
					}
				}
			}
		}

		return products;
	}
}
