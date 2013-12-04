package tags;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Scripts
{
	@SuppressWarnings("serial")
	public final static Map<String, String> S_PLATFORMS = new LinkedHashMap<String, String>()
		{
			{
				put("AIX", "AIX");
				put("HI", "HP-UX (Itanium)");
				put("HP", "HP-UX PA-RISC");
				put("IBM", "IBM i");
				put("RED", "Red Hat");
				put("SOL", "Solaris");
				put("WIN", "Windows Server");
			}
		};

	@SuppressWarnings("serial")
	public final static Map<String, String> S_DATABASES = new LinkedHashMap<String, String>()
		{
			{
				put("DB2", "DB2");
				put("ORA", "Oracle");
				put("MSQ", "SQL Server");
				put("SYB", "Sybase");
				put("NON", "None");
			}
		};

	@SuppressWarnings("serial")
	public final static Map<String, String> S_ENCODINGS = new LinkedHashMap<String, String>()
		{
			{
				put("ASC", "ASCII");
				put("CP285", "EBCDIC - CP285");
				put("CP37", "EBCDIC - CP37");
				put("UNI", "Unicode");
				put("NON", "None");
			}
		};

	public static String servicepacks()
	{
		final Map<String, List<Integer>> servicepackList = Lists.getVersions();

		final List<String> keys = new ArrayList<String>(servicepackList.keySet());
		Collections.sort(keys);

		final StringBuffer sb = new StringBuffer();

		sb.append("var servicepackList = {");
		for(final String key : keys)
		{
			final List<Integer> servicepacks = servicepackList.get(key);
			Collections.sort(servicepacks);
			Collections.reverse(servicepacks);

			sb.append("\"" + key + "\":[");
			for(int index = 0; index < servicepacks.size(); index++)
			{
				sb.append("\"" + servicepacks.get(index) + "\",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("],");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("};");

		return sb.toString();
	}

	public static String platforms()
	{
		final StringBuffer sb = new StringBuffer();

		sb.append("var platforms = {");
		for(final String key : S_PLATFORMS.keySet())
		{
			sb.append("\"" + key + "\":\"" + S_PLATFORMS.get(key) + "\",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("};");

		return sb.toString();
	}

	public static String databases()
	{
		final StringBuffer sb = new StringBuffer();

		sb.append("var databases = {");
		for(final String key : S_DATABASES.keySet())
		{
			sb.append("\"" + key + "\":\"" + S_DATABASES.get(key) + "\",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("};");

		return sb.toString();
	}

	public static String encodings()
	{
		final StringBuffer sb = new StringBuffer();

		sb.append("var encodings = {");
		for(final String key : S_ENCODINGS.keySet())
		{
			sb.append("\"" + key + "\":\"" + S_ENCODINGS.get(key) + "\",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("};");

		return sb.toString();
	}
}
