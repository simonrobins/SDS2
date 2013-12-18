import helpers.Helpers;
import helpers.ProductHelper;
import misc.FileListing;
import misc.Versions;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.javascript.host.Document;

import controllers.Application;
import controllers.Ccm;
import controllers.Complete;
import controllers.Language;
import controllers.Releases;
import controllers.Secure;
import controllers.Servicepack;
import controllers.SupportArea;
import controllers.Time;
import controllers.Validate;

public class ContructorTest
{
	@Test
	public void controllers()
	{
		new Application();
		new Ccm();
		new Complete();
		new Document();
		new Language();
		new Releases();
		new Secure();
		new Servicepack();
		new SupportArea();
		new Time();
		new Validate();
	}

	@Test
	public void helpers()
	{
		new Helpers();
		new ProductHelper();
	}

	@Test
	public void misc()
	{
		new FileListing();
		new Versions();
	}
}
