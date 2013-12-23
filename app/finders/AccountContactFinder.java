package finders;

import models.AccountContact;

import com.avaje.ebean.Ebean;

public class AccountContactFinder
{
	public static AccountContact find(final int contactId)
	{
		AccountContact contact = Ebean.find(AccountContact.class).where().eq("id", contactId).eq("active", "Y").eq("account.download", "Y").findUnique();
		return contact;
	}
}
