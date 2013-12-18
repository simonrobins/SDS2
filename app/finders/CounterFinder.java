package finders;

import models.Counter;

import com.avaje.ebean.Ebean;

public class CounterFinder
{
	public static Counter find(final String id)
	{
		return Ebean.find(Counter.class, id);
	}
}
