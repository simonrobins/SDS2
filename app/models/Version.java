package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.Ebean;

@Entity
@Table(name = "coda_version")
public class Version
{
	@Id
	@Column(name = "version_id")
	private int id;

	private String version;

	public static Version find(final int versionId)
	{
		return Ebean.find(Version.class, versionId);
	}

	@Override
	public String toString()
	{
		return ((version != null) ? version.trim() : "ERROR") + "[" + id + "]";
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}
}
