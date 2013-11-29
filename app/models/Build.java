package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "coda_build")
public class Build
{
	@Id
	@Column(name = "build_id")
	private int id;
	private String build;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getBuild()
	{
		return build;
	}

	public void setBuild(String build)
	{
		this.build = build;
	}
}
