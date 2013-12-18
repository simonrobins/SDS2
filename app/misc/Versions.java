package misc;

import java.util.Map;

import play.Play;

public class Versions
{
	public static Integer get(String version)
	{
		return getObject().get(version);
	}

	public static boolean containsKey(String version)
	{
		return getObject().containsKey(version);
	}

	@SuppressWarnings("unchecked")
	private static Map<String, Integer> getObject()
	{
		Object object = Play.application().configuration().getObject("versions.mapping");
		return (Map<String, Integer>) object;
	}
}
