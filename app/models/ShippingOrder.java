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
@Table(name = "coda_shipping_order")
public class ShippingOrder
{
	@Id
	@Column(name = "order_id")
	private int id;

	@ManyToOne
	@JoinColumn(name = "account_id", referencedColumnName = "account_id")
	private Account account;

	@ManyToOne
	@JoinColumn(name = "account_contact_id", referencedColumnName = "account_contact_id")
	private AccountContact contact;

	private int acctLocationId;

	private String orderNo;

	private Date orderDt = new Date();

	private Date changeDt = new Date();

	private String changeUid;

	public static int getNextOne(int reserve)
	{
		return Helpers.getNextOne("coda_shipping_order", "order_id", reserve);
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public Account getAccount()
	{
		return account;
	}

	public void setAccount(Account account)
	{
		this.account = account;
	}

	public AccountContact getContact()
	{
		return contact;
	}

	public void setContact(AccountContact contact)
	{
		this.contact = contact;
	}

	public int getAcctLocationId()
	{
		return acctLocationId;
	}

	 public void setAcctLocationId(int acctLocationId)
	 {
		 this.acctLocationId = acctLocationId;
	 }

	public String getOrderNo()
	{
		return orderNo;
	}

	public void setOrderNo(String orderNo)
	{
		this.orderNo = orderNo;
	}

	public String getChangeUid()
	{
		return changeUid;
	}

	public void setChangeUid(String changeUid)
	{
		this.changeUid = changeUid;
	}

	public Date getOrderDt()
	{
		return orderDt;
	}

	 public void setOrderDt(Date orderDt)
	 {
		 this.orderDt = orderDt;
	 }

	public Date getChangeDt()
	{
		return changeDt;
	}

	 public void setChangeDt(Date changeDt)
	 {
		 this.changeDt = changeDt;
	 }
}
