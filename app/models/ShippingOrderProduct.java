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
@Table(name = "coda_shipping_order_product")
public class ShippingOrderProduct
{
	@Id
	@Column(name = "product_order_id")
	private int id;

	private int orderId;

	@ManyToOne
	@JoinColumn(name = "account_contact_id", referencedColumnName = "account_contact_id")
	private AccountContact accountContact;

	private String type;

	@ManyToOne
	@JoinColumn(name = "product_id", referencedColumnName = "product_id")
	private Product product;

	@ManyToOne
	@JoinColumn(name = "version_id", referencedColumnName = "version_id")
	private Version version;

	@ManyToOne
	@JoinColumn(name = "build_id", referencedColumnName = "build_id")
	private Build build;

	@ManyToOne
	@JoinColumn(name = "service_pack_id", referencedColumnName = "service_pack_id")
	private ServicePack servicePack;

	private char unicode;

	private final Date changeDt = new Date();

	private String changeUid;

	private int shippingOptions;

	public static int getNextOne(int reserve)
	{
		return Helpers.getNextOne("coda_shipping_order_product", "product_order_id", reserve);
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getOrderId()
	{
		return orderId;
	}

	public void setOrderId(int orderId)
	{
		this.orderId = orderId;
	}

	public AccountContact getAccountContact()
	{
		return accountContact;
	}

	public void setAccountContact(AccountContact accountContact)
	{
		this.accountContact = accountContact;
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

	public Version getVersion()
	{
		return version;
	}

	public void setVersion(Version version)
	{
		this.version = version;
	}

	public Build getBuild()
	{
		return build;
	}

	public void setBuild(Build build)
	{
		this.build = build;
	}

	public ServicePack getServicePack()
	{
		return servicePack;
	}

	public void setServicePack(ServicePack servicePack)
	{
		this.servicePack = servicePack;
	}

	public char getUnicode()
	{
		return unicode;
	}

	// public void setUnicode(char unicode)
	// {
	// this.unicode = unicode;
	// }

	public String getChangeUid()
	{
		return changeUid;
	}

	public void setChangeUid(String changeUid)
	{
		this.changeUid = changeUid;
	}

	public int getShippingOptions()
	{
		return shippingOptions;
	}

	public void setShippingOptions(int shippingOptions)
	{
		this.shippingOptions = shippingOptions;
	}

	public Date getChangeDt()
	{
		return changeDt;
	}

	// public void setChangeDt(Date changeDt)
	// {
	// this.changeDt = changeDt;
	// }
}
