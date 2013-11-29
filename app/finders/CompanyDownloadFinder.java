package finders;

import java.util.Set;

import models.CompanyDownload;

import com.avaje.ebean.Ebean;

public class CompanyDownloadFinder
{
	public static Set<CompanyDownload> findReference(String ref)
	{
		Set<CompanyDownload> cds = Ebean.find(CompanyDownload.class).where().eq("downloadRef", ref).eq("success", null).findSet();

		return cds;
	}

	public static Set<CompanyDownload> findAccountReference(String account, String ref)
	{
		Set<CompanyDownload> cds = Ebean.find(CompanyDownload.class).where().eq("account_id", account).eq("downloadRef", ref).isNull("success").findSet();

		return cds;
	}
}
