package finders;

import models.AccountContact;

import com.avaje.ebean.Ebean;

public class AccountContactFinder
{
	protected AccountContactFinder()
	{
	}

	public static AccountContact find(final int contactId)
	{
		return Ebean.find(AccountContact.class).where().eq("id", contactId).eq("active", "Y").eq("account.download", "Y").findUnique();
	}
}
