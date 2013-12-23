package misc;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.annotation.Nullable;

import play.Logger;
import play.mvc.Http.Request;

public class Utilities
{
	public static boolean isNotEmpty(String string)
	{
		return (string != null) && (string.trim().length() > 0);
	}

	public static void quietClose(final @Nullable Closeable stream)
	{
		if (stream != null)
		{
			try
			{
				stream.close();
			}
			catch (final IOException ioe)
			{
				ioe.printStackTrace();
			}
		}
	}

	public static Double getInternetExplorerVersion(final Request request)
	{
		Double version = 0.0;

		final Map<String, String[]> headers = request.headers();
		final String[] header = headers.get("User-Agent");

		if (header == null || header.length < 1)
			return version;

		final String userAgent = header[0];

		Logger.info(userAgent);

		final Pattern p = Pattern.compile("MSIE (\\d+\\.\\d+)b?;");
		final Matcher m = p.matcher(userAgent);
		while (m.find())
		{
			version = Double.parseDouble(userAgent.substring(m.start(1), m.end(1)));
		}

		return version;
	}

	public static File fileFromPathComponents(File root, final String... components)
	{
		for (final String component : components)
			root = new File(root, component);

		return root;
	}

	public static String getInfo(final File root, final String... components)
	{
		return getInfoText(fileFromPathComponents(root, components), "info.txt");
	}

	public static String getInformation(final File root, final String... components)
	{
		return getInfoText(fileFromPathComponents(root, components), "information.txt");
	}

	private static String getInfoText(final File dir, final String filename)
	{
		final StringBuffer infoText = new StringBuffer();
		final File info = new File(dir, filename);
		try
		{
			final BufferedReader br = new BufferedReader(new FileReader(info));
			String line;

			while ((line = br.readLine()) != null)
			{
				infoText.append(line);
				infoText.append("\n");
			}

			br.close();
		}
		catch (final Exception e)
		{
		}
		return infoText.length() > 0 ? infoText.toString() : "";
	}
}
