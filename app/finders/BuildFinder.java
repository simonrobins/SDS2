package finders;

import models.Build;

import com.avaje.ebean.Ebean;

public class BuildFinder
{
	protected BuildFinder()
	{
	}

	public static Build find(final int id)
	{
		return Ebean.find(Build.class, id);
	}
}
