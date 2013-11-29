package misc;

import java.util.HashMap;
import java.util.Map;

public class Versions
{
	@SuppressWarnings("serial")
	private static Map<String, Integer> VERSIONS = new HashMap<String, Integer>()
		{
			{
				put("11.200", 261);
				put("11.300", 281);
				put("12.000", 325);
			}
		};

	public static Integer get(String version)
	{
		Integer versionId = VERSIONS.get(version);

		if(versionId == null)
			throw new NullPointerException(version);

		return versionId;
	}

	public static boolean containsKey(String version)
	{
		return VERSIONS.containsKey(version);
	}
}
