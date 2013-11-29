package models;

public class AccountAddon
{
	public final String serverOs;
	public final String databaseType;
	public final String encoding;

	public AccountAddon(final String serverOs, final String databaseType, final String encoding)
	{
		this.serverOs = serverOs;
		this.databaseType = databaseType;
		this.encoding = encoding;
	}
}
