package finders;

import java.util.List;

import models.ServicePack;

import com.avaje.ebean.Ebean;

public class ServicePackFinder
{
	public static ServicePack find(final int servicepackId)
	{
		return Ebean.find(ServicePack.class, servicepackId);
	}

	public static List<ServicePack> find(final int versionId, final int servicePackNumber)
	{
		return Ebean.find(ServicePack.class).where().eq("version.id", versionId).eq("servicePackNumber", servicePackNumber).like("status", "Available%").findList();
	}
}
