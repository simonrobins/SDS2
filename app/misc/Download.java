package misc;

public class Download
{
	private String doc;

	private double version;
	private int servicepack;
	private String platform;
	private String database;
	private String encoding;

	private boolean assets;
	private boolean billing;
	private boolean customiser;
	private boolean finance;
	private boolean pim;
	private boolean pop;
	private boolean fin;

	public String getDoc()
	{
		return doc;
	}

	public void setDoc(String doc)
	{
		this.doc = doc;
	}

	public double getVersion()
	{
		return version;
	}

	public void setVersion(double version)
	{
		this.version = version;
	}

	public int getServicepack()
	{
		return servicepack;
	}

	public void setServicepack(int servicepack)
	{
		this.servicepack = servicepack;
	}

	public String getPlatform()
	{
		return platform;
	}

	public void setPlatform(String platform)
	{
		this.platform = platform;
	}

	public String getDatabase()
	{
		return database;
	}

	public void setDatabase(String database)
	{
		this.database = database;
	}

	public String getEncoding()
	{
		return encoding;
	}

	public void setEncoding(String encoding)
	{
		this.encoding = encoding;
	}

	public boolean isAssets()
	{
		return assets;
	}

	public void setAssets(boolean assets)
	{
		this.assets = assets;
	}

	public boolean isBilling()
	{
		return billing;
	}

	public void setBilling(boolean billing)
	{
		this.billing = billing;
	}

	public boolean isCustomiser()
	{
		return customiser;
	}

	public void setCustomiser(boolean customiser)
	{
		this.customiser = customiser;
	}

	public boolean isFinance()
	{
		return finance;
	}

	public void setFinance(boolean finance)
	{
		this.finance = finance;
	}

	public boolean isPim()
	{
		return pim;
	}

	public void setPim(boolean pim)
	{
		this.pim = pim;
	}

	public boolean isPop()
	{
		return pop;
	}

	public void setPop(boolean pop)
	{
		this.pop = pop;
	}

	public boolean isFin()
	{
		return fin;
	}

	public void setFin(boolean fin)
	{
		this.fin = fin;
	}
}
