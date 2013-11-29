package tags;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import misc.Settings;
import misc.Versions;
import models.ServicePack;

import org.eclipse.jdt.annotation.Nullable;

import play.Logger;
import play.cache.Cache;
import finders.ServicePackFinder;

public class Lists
{
	// TDOD Add build number to save checking at download time.
	public static Map<String, List<Integer>> getVersions()
	{
		@SuppressWarnings("unchecked")
		Map<String, List<Integer>> versions = (Map<String, List<Integer>>) Cache.get("Lists.getVersions");

		if(versions == null)
		{
			Logger.info("Servicepack index lines cache missed");

			versions = new LinkedHashMap<String, List<Integer>>();

			final File[] files = Settings.SERVICEPACKS_FINANCE_DIR.listFiles(new FileFilter()
				{
					@Override
					public boolean accept(final @Nullable File dir)
					{
						return dir != null && dir.isDirectory() && dir.getName().matches("\\d\\d\\.\\d\\d\\d-SP\\d\\d?");
					}
				});

			if(files != null)
			{
				for(final File file : files)
				{
					final String version = file.getName().substring(0, 6);
					final Integer servicepackNo = Integer.parseInt(file.getName().substring(9));

					if(Versions.containsKey(version))
					{
						final List<ServicePack> sp = ServicePackFinder.find(Versions.get(version), servicepackNo);
						if(sp.size() > 0)
						{
							List<Integer> spIds;
							if(versions.containsKey(version))
							{
								spIds = versions.get(version);
							}
							else
							{
								spIds = new ArrayList<Integer>();
								versions.put(version, spIds);
							}
							spIds.add(servicepackNo);
						}
					}
				}
			}

			Cache.set("Lists.getVersions", versions, Settings.SERVICEPACK_CACHE_INTERVAL);
		}

		return versions;
	}

	public static List<Integer> getVersions(final String version)
	{
		List<Integer> versions = getVersions().get(version);

		return versions == null ? new ArrayList<Integer>() : versions;
	}
}
