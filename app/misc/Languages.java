package misc;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import models.SubProduct;

import com.avaje.ebean.Ebean;

public class Languages
{
	private static Map<Integer, String> s_languages = new HashMap<Integer, String>();

	public static synchronized String get(final Integer id)
	{
		if(s_languages.size() == 0)
		{
			final Set<SubProduct> languages = Ebean.find(SubProduct.class).where().eq("subProductTypeId", 1).findSet();

			for(final SubProduct language : languages)
			{
				s_languages.put(language.getProductId(), language.getSubProduct());
			}
		}

		return s_languages.get(id);
	}
}
