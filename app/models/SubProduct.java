package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "coda_sub_product")
public class SubProduct
{
	@Id
	@Column(name = "sub_product_id")
	private Integer id;
	private String subProduct;
	private Integer subProductTypeId;
	private Integer productId;

	 public Integer getId()
	 {
		 return id;
	 }

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getSubProduct()
	{
		return subProduct;
	}

	public void setSubProduct(String subProduct)
	{
		this.subProduct = subProduct;
	}

	public Integer getSubProductTypeId()
	{
		return subProductTypeId;
	}

	public void setSubProductTypeId(Integer subProductTypeId)
	{
		this.subProductTypeId = subProductTypeId;
	}

	public Integer getProductId()
	{
		return productId;
	}

	public void setProductId(Integer productId)
	{
		this.productId = productId;
	}
}
