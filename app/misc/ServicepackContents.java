package misc;

import java.util.List;

import play.Configuration;
import play.Play;

public class ServicepackContents
{
	public static String[]	JARS	= getList("servicepack.contents.jars");
	public static String[]	TARS112	= getList("servicepack.contents.tars.112");
	public static String[]	TARS113	= getList("servicepack.contents.tars.113");
	public static String[]	TARS120	= getList("servicepack.contents.tars.120");
	public static String[]	EXES112	= getList("servicepack.contents.exes.112");
	public static String[]	EXES113	= getList("servicepack.contents.exes.113");
	public static String[]	EXES120	= getList("servicepack.contents.exes.120");
	public static String[]	OASS112	= getList("servicepack.contents.oass.112");
	public static String[]	OASS113	= getList("servicepack.contents.oass.113");
	public static String[]	OASS120	= getList("servicepack.contents.oass.120");
	public static String[]	CORES	= getList("servicepack.contents.cores");
	public static String[]	PDFS	= getList("servicepack.contents.pdfs");

	private static String[] getList(String property)
	{
		Configuration configuration = Play.application().configuration();
		List<Object> list = configuration.getList(property);
		String[] strings = list.toArray(new String[0]);
		return strings;
	}
}
