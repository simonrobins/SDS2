package finders;

import java.util.List;

import models.ServicePack;

import org.fest.assertions.Assertions;
import org.junit.Test;

import controllers.AbstractTest;

public class ServicePackFinderTest extends AbstractTest
{
	@Test
	public void testServicePackFinder()
	{
		ServicePackFinder spf = new ServicePackFinder();
		Assertions.assertThat(spf).isNotNull();
	}

	@Test
	public void testFindNonExistant()
	{
		ServicePack sp = ServicePackFinder.find(0);
		Assertions.assertThat(sp).isNull();
	}

	@Test
	public void testFindExistant()
	{
		ServicePack sp = ServicePackFinder.find(14);
		Assertions.assertThat(sp).isNotNull();
	}

	@Test
	public void testFindNonExistantVersionAndOrServicePackNumber()
	{
		List<ServicePack> sps = ServicePackFinder.find(0, 0);
		Assertions.assertThat(sps).isEmpty();
		sps = ServicePackFinder.find(214, 0);
		Assertions.assertThat(sps).isEmpty();
		sps = ServicePackFinder.find(0, 1);
		Assertions.assertThat(sps).isEmpty();
	}
}
