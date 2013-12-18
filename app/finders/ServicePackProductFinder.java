package finders;

import java.util.LinkedHashSet;
import java.util.Set;

import models.ServicePackProduct;

import com.avaje.ebean.Ebean;

public class ServicePackProductFinder
{
	public static Set<Integer> getServicePackProducts()
	{
		final Set<ServicePackProduct> servicePackProducts = Ebean.find(ServicePackProduct.class).findSet();

		final Set<Integer> productIds = new LinkedHashSet<Integer>();

		for(final ServicePackProduct servicePackProduct : servicePackProducts)
		{
			productIds.add(servicePackProduct.getProduct().getId());
		}

		return productIds;
	}
}
