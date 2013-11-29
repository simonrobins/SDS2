package admin;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;

public class Populate
{
	public static final void main(String[] args)
	{
		new Populate().execute();
	}

	public void execute()
	{
		QueryRunner run1 = newQueryRunner("com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver://srv-har-applix6;databaseName=Enterprise", "srobins", "srobins");
		QueryRunner run2 = newQueryRunner("org.h2.Driver", "jdbc:h2:./db/master", "sa", "");

		try
		{
			copyTable(run1, run2, "account", "account_id INT PRIMARY KEY, name VARCHAR(255), download CHAR(1)", "account_id = 999999");
			copyTable(run1, run2, "account_contact", "account_contact_id INT PRIMARY KEY, username VARCHAR(255), account_id INT, active CHAR(1)", "account_id = 999999");

			copyTable(run1, run2, "coda_product", "product_id INT PRIMARY KEY, product VARCHAR(255)");
			copyTable(run1, run2, "coda_sub_product", "sub_product_id INT PRIMARY KEY, sub_product VARCHAR(255), sub_product_type_id INT, product_id INT");
			copyTable(run1, run2, "coda_version", "version_id INT PRIMARY KEY, version VARCHAR(255)");
			copyTable(run1, run2, "coda_build", "build_id INT PRIMARY KEY, build VARCHAR(255)");
			copyTable(run1, run2, "coda_sds_validate", "account_id INT, product_id INT, version_id INT");
			copyTable(run1, run2, "coda_service_pack", "service_pack_id INT PRIMARY KEY, product_id INT, version_id INT, service_pack_number INT, status VARCHAR(22)");
			copyTable(run1, run2, "coda_service_pack_product", "service_pack_product_id INT PRIMARY KEY, service_pack_product VARCHAR(255), product_id INT");

			recreateTable(run2, "coda_shipping_order", "order_id INT PRIMARY KEY, account_id INT, account_contact_id INT, acct_location_id INT, order_no VARCHAR(255), order_dt DATETIME, change_dt DATETIME, change_uid VARCHAR(255)");
			recreateTable(run2, "coda_shipping_order_product", "product_order_id INT PRIMARY KEY, order_id INT, account_contact_id INT, type VARCHAR(80), product_id INT, version_id INT, build_id INT, service_pack_id INT, unicode VARCHAR(1), change_dt DATETIME, change_uid VARCHAR(255), shipping_options INT");
			recreateTable(run2, "coda_company_download", "download_id INT PRIMARY KEY, type VARCHAR(255), product_id INT, sub_product_id INT, version_id INT, account_id INT, account_contact_id INT, database_type VARCHAR(255), server_os VARCHAR(255), encoding VARCHAR(255), success CHAR(1), download_dt DATETIME, end_dt DATETIME, bytes_sent LONG, download_ref VARCHAR(255), service_pack_id INT");
			recreateTable(run2, "counter", "item VARCHAR(64) PRIMARY KEY, last_one INT");
		}
		catch(Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}

	private static QueryRunner newQueryRunner(String driver, String url, String username, String password)
	{
		DataSource ds = getDataSource(driver, url, username, password);
		return new QueryRunner(ds);
	}

	private static void copyTable(QueryRunner run1, QueryRunner run2, String table, String columns)
		throws Exception
	{
		String selectColumns = changeColumnNameSyntax(columns);

		recreateTable(run2, table, columns);
		copyRows(run1, run2, table, selectColumns);
	}

	private static void copyTable(QueryRunner run1, QueryRunner run2, String table, String columns, String where)
		throws Exception
	{
		String selectColumns = changeColumnNameSyntax(columns);

		recreateTable(run2, table, columns);
		copyRows(run1, run2, table, selectColumns, where);
	}

	private static String changeColumnNameSyntax(String columns)
	{
		StringBuffer sb = new StringBuffer();
		for(String field : columns.split(","))
		{
			if(sb.length() > 0)
				sb.append(", ");
			String name = field.trim().split(" ")[0];
			sb.append(name);
		}
		return sb.toString();
	}

	private static void recreateTable(QueryRunner run, String table, String columns)
		throws SQLException
	{
		dropTable(run, table);
		createTable(run, table, columns);
	}

	private static void dropTable(QueryRunner run, String table)
		throws SQLException
	{
		run.update("DROP TABLE " + table);
	}

	private static void createTable(QueryRunner run, String table, String columns)
		throws SQLException
	{
		run.update("CREATE TABLE " + table + " (" + columns + ")");
	}

	private static void copyRows(QueryRunner from, QueryRunner to, String table, String columns)
		throws SQLException
	{
		insertRows(to, table, selectRows(from, table, columns));
	}

	private static void copyRows(QueryRunner from, QueryRunner to, String table, String columns, String where)
		throws SQLException
	{
		insertRows(to, table, selectRows(from, table, columns, where));
	}

	private static List<Object[]> selectRows(QueryRunner run, String table, String columns, String where)
		throws SQLException
	{
		return run.query("SELECT " + columns + " FROM " + table + " WHERE " + where, new ArrayListHandler());
	}

	private static List<Object[]> selectRows(QueryRunner run, String table, String columns)
		throws SQLException
	{
		return run.query("SELECT " + columns + " FROM " + table, new ArrayListHandler());
	}

	private static void insertRows(QueryRunner run, String table, List<Object[]> rows)
		throws SQLException
	{
		StringBuffer sql = new StringBuffer("INSERT INTO " + table + " VALUES(");
		for(@SuppressWarnings("unused")
		Object o : rows.get(0))
			sql.append("?, ");
		sql = sql.delete(sql.length() - 2, sql.length());
		sql.append(")");

		for(Object[] row : rows)
			run.update(sql.toString(), row);
	}

	private static DataSource getDataSource(String className, String url, String username, String password)
	{
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(className);
		ds.setUrl(url);
		ds.setUsername(username);
		ds.setPassword(password);
		return ds;
	}
}
