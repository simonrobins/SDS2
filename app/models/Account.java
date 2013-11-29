package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "account")
public class Account
{
	@Id
	@Column(name = "account_id")
	private int id;
	private String name;
	@SuppressWarnings("unused")
	private String download;
	@OneToMany(mappedBy = "account")
	private List<AccountContact> contacts;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	 public String getDownload()
	 {
		 return download;
	 }

	public void setDownload(String download)
	{
		this.download = download;
	}

	public List<AccountContact> getContacts()
	{
		return contacts;
	}

	public void setContacts(List<AccountContact> contacts)
	{
		this.contacts = contacts;
	}
}
