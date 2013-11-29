package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.Ebean;

@Entity
@Table(name = "coda_product")
public class Product
{
	@Id
	@Column(name = "product_id")
	private int id;

	private String product;

	public static Product find(final int id)
	{
		return Ebean.find(Product.class, id < 0 ? -id : id);
	}

	@Override
	public String toString()
	{
		return ((product != null) ? product.trim() : "ERROR") + "[" + id + "]";
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getProduct()
	{
		return product;
	}

	public void setProduct(String product)
	{
		this.product = product;
	}
}
