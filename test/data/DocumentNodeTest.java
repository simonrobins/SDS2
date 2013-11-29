package data;

import org.fest.assertions.Assertions;
import org.junit.Test;

public class DocumentNodeTest
{
	@Test
	public void testDocumentNodeFile()
	{
		DocumentNode node = new DocumentNode();
		Assertions.assertThat(node).isNotNull();
	}

	@Test
	public void testDocumentNode()
	{
		DocumentNode node = new DocumentNode();
		Assertions.assertThat(node).isNotNull();
	}
}
