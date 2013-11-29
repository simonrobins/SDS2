package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "coda_service_pack_product")
public class ServicePackProduct
{
	@Id
	@Column(name = "service_pack_product_id")
	private int id;

	@Column(name = "service_pack_product")
	private String servicePackproduct;

	@ManyToOne
	@JoinColumn(name = "product_id", referencedColumnName = "product_id")
	private Product product;

	// public int getId()
	// {
	// return id;
	// }

	public void setId(int id)
	{
		this.id = id;
	}

	public String getServicePackproduct()
	{
		return servicePackproduct;
	}

	public void setServicePackproduct(String servicePackproduct)
	{
		this.servicePackproduct = servicePackproduct;
	}

	public Product getProduct()
	{
		return product;
	}

	public void setProduct(Product product)
	{
		this.product = product;
	}
}
