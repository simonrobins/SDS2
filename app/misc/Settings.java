package misc;

import java.io.File;
import java.text.DecimalFormat;

import play.Play;

public class Settings
{
	public static final String USER_IDENTIFIER = "DOWNLOAD";

	public static final String APPLICATION_SECRET = getString("application.secret");

	public static final String MASTER_PASSWORD = getString("sds.master.password");
	public static final String SECURE_PASSWORD = getString("sds.secure.password");

	public static final File RELEASES_DIR = getFile("sds.releases.dir");
	public static final File SERVICEPACKS_CCM_DIR = getFile("sds.servicepacks.ccm.dir");
	public static final File SERVICEPACKS_FINANCE_DIR = getFile("sds.servicepacks.finance.dir");
	public static final File LANGUAGEPACKS_DIR = getFile("sds.languagepacks.dir");

	public static final File SUPPORT_DIR = getFile("sds.support.dir");
	public static final File DOWNLOAD_DIR = getFile("sds.download.dir");
	public static final File TEMPORARY_DIR = getFile("sds.temporary.dir");
	public static final File DOCUMENTS_DIR = getFile("sds.documents.dir");

	public static final DecimalFormat SP_VERSION_FORMAT = new DecimalFormat("#0.0");
	public static final DecimalFormat VERSION_FORMAT = new DecimalFormat("#0.000");
	public static final DecimalFormat REFERENCE_FORMAT = new DecimalFormat("00000000");
	public static final DecimalFormat BUILD_FORMAT = new DecimalFormat("000");

	public static final Integer SERVICEPACK_CACHE_INTERVAL = getInteger("sds.servicepacks.cache.interval");
	public static final Integer RELEASES_CACHE_INTERVAL = getInteger("sds.releases.cache.interval");
	public static final Integer LANGUAGEPACKS_CACHE_INTERVAL = getInteger("sds.languagepacks.cache.interval");
	public static final Integer PRODUCTS_CACHE_INTERVAL = getInteger("sds.products.cache.interval");

	public static final Integer ARCHIVE_MAX_RETAIN_PERIOD = getInteger("sds.archive.max.retain.period");
	public static final Integer ARCHIVE_MIN_RETAIN_PERIOD = getInteger("sds.archive.min.retain.period");
	public static final Long ARCHIVE_MIN_FREE_SPACE = getLong("sds.archive.min.free.space");

	private static String getString(final String property)
	{
		return Play.application().configuration().getString(property);
	}

	private static Integer getInteger(final String property)
	{
		return Play.application().configuration().getInt(property);
	}

	private static Long getLong(final String property)
	{
		return Play.application().configuration().getLong(property);
	}

	private static File getFile(final String property)
	{
		return new File(getString(property));
	}
}
