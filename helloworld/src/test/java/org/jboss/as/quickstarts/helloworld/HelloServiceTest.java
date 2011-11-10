/**
 * 
 */
package org.jboss.as.quickstarts.helloworld;

import javax.inject.Inject;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.quickstarts.helloworld.HelloService;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author mnovotny
 *
 * Simple Arquillian test class for {@link org.jboss.as.quickstarts.helloworld.HelloService} 
 */
@RunWith(Arquillian.class)
public class HelloServiceTest {

	@Deployment
    public static JavaArchive deployment() {
        return ShrinkWrap.create(JavaArchive.class).addClass(HelloService.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
    }
	
	@Inject HelloService helloService;
	
	/**
	 * Test method for {@link org.jboss.as.quickstarts.helloworld.HelloService#createHelloMessage(java.lang.String)}.
	 */
	@Test
	public void testCreateHelloMessage() {
		
		Assert.assertNotNull(helloService);
		Assert.assertEquals("Hello DemoDay!", helloService.createHelloMessage("DemoDay"));
	}

}
