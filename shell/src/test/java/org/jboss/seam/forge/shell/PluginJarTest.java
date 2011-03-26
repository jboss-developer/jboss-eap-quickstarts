package org.jboss.seam.forge.shell;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PluginJarTest
{
   @Test
   public void testStringCreation() throws Exception
   {
      PluginJar jar = new PluginJar("com.example$example-plugin$5$1.0.0-SNAPSHOT.jar");
      assertEquals("com.example", jar.getDependency().getGroupId());
      assertEquals("example-plugin", jar.getDependency().getArtifactId());
      assertEquals(5, jar.getVersion());
      assertEquals("1.0.0-SNAPSHOT", jar.getDependency().getVersion());
   }

   @Test
   public void testNoArtifactVersion() throws Exception
   {
      PluginJar jar = new PluginJar("com.example$example-plugin$5$.jar");
      assertEquals("com.example", jar.getDependency().getGroupId());
      assertEquals("example-plugin", jar.getDependency().getArtifactId());
      assertEquals(5, jar.getVersion());
      assertEquals("", jar.getDependency().getVersion());
   }
}
