package models;

import helpers.Helpers;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "coda_company_download")
public class CompanyDownload
{
	@Id
	@Column(name = "download_id")
	private int id;

	private String type;

	@ManyToOne
	@JoinColumn(name = "product_id", referencedColumnName = "product_id")
	private Product product;

	@Column(name = "sub_product_id")
	private Integer subProductId;

	@ManyToOne
	@JoinColumn(name = "version_id", referencedColumnName = "version_id")
	private Version version;

	@ManyToOne
	@JoinColumn(name = "account_id", referencedColumnName = "account_id")
	private Account account;

	@ManyToOne
	@JoinColumn(name = "account_contact_id", referencedColumnName = "account_contact_id")
	private AccountContact accountContact;

	private String databaseType;
	private String serverOs;
	private String encoding;

	private Character success;

	@Column(name = "download_dt")
	private Date startDate;
	@Column(name = "end_dt")
	private Date endDate;

	@Column(name = "bytes_sent")
	private long bytesSent;

	private String downloadRef;

	@ManyToOne
	@JoinColumn(name = "service_pack_id", referencedColumnName = "service_pack_id")
	private ServicePack servicePack;

	public static int getNextOne(int reserve)
	{
		return Helpers.getNextOne("coda_company_download", "download_id", reserve);
	}

	@Override
	public String toString()
	{
		return downloadRef + " " + product + " - " + version + "(" + success + ")";
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public Product getProduct()
	{
		return product;
	}

	public void setProduct(Product product)
	{
		this.product = product;
	}

	public Integer getSubProductId()
	{
		return subProductId;
	}

	public void setSubProductId(Integer subProductId)
	{
		this.subProductId = subProductId;
	}

	public Version getVersion()
	{
		return version;
	}

	public void setVersion(Version version)
	{
		this.version = version;
	}

	public Account getAccount()
	{
		return account;
	}

	public void setAccount(Account account)
	{
		this.account = account;
	}

	public AccountContact getAccountContact()
	{
		return accountContact;
	}

	public void setAccountContact(AccountContact accountContact)
	{
		this.accountContact = accountContact;
	}

	public String getDatabaseType()
	{
		return databaseType;
	}

	public void setDatabaseType(String databaseType)
	{
		this.databaseType = databaseType;
	}

	public String getServerOs()
	{
		return serverOs;
	}

	public void setServerOs(String serverOs)
	{
		this.serverOs = serverOs;
	}

	public String getEncoding()
	{
		return encoding;
	}

	public void setEncoding(String encoding)
	{
		this.encoding = encoding;
	}

	public Character getSuccess()
	{
		return success;
	}

	public void setSuccess(Character success)
	{
		this.success = success;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public Date getEndDate()
	{
		return endDate;
	}

	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

	public long getBytesSent()
	{
		return bytesSent;
	}

	public void setBytesSent(long bytesSent)
	{
		this.bytesSent = bytesSent;
	}

	public String getDownloadRef()
	{
		return downloadRef;
	}

	public void setDownloadRef(String downloadRef)
	{
		this.downloadRef = downloadRef;
	}

	public ServicePack getServicePack()
	{
		return servicePack;
	}

	public void setServicePack(ServicePack servicePack)
	{
		this.servicePack = servicePack;
	}
}
