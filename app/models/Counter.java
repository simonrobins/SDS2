package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "counter")
public class Counter
{
	@Id
	private String item;

	@Column(name = "last_one")
	private int lastOne;

	public String getItem()
	{
		return item;
	}

	public void setItem(String item)
	{
		this.item = item;
	}

	public int getLastOne()
	{
		return lastOne;
	}

	public void setLastOne(int lastOne)
	{
		this.lastOne = lastOne;
	}
}
