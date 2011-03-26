package org.jboss.seam.forge.shell;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.seam.forge.project.dependencies.Dependency;
import org.jboss.seam.forge.project.dependencies.DependencyBuilder;

public class PluginJar
{

   private static final String DELIM = "$";
   private final Dependency dep;
   private int version = 0;

   public PluginJar(String name) throws IllegalNameException
   {
      // group.Id_artifactId_4.jar
      // -------------------------0-12-------3-----4---5-------6---7
      Matcher m = Pattern.compile("^((.+?)\\$(.+?))(\\$(\\d+))+(\\$(.*?)).jar$").matcher(name);

      if (!m.matches())
      {
         throw new IllegalNameException("Invalid plugin file-name format detected: " + name);
      }

      DependencyBuilder builder = DependencyBuilder.create();
      builder.setGroupId(m.group(2));
      builder.setArtifactId(m.group(3));
      builder.setVersion(m.group(7));
      dep = builder;

      if (m.group(5) != null)
      {
         this.version = Integer.valueOf(m.group(5));
      }
   }

   public PluginJar(Dependency dep)
   {
      this.dep = dep;
   }

   public PluginJar(Dependency dep, int version)
   {
      this(dep);
      this.version = version;
   }

   /**
    * GroupId$ArtifactId$LoadedVersion$PluginVersion.jar
    */
   public String getFullName()
   {
      String result = getName();
      result += DELIM + version;
      result += DELIM + (dep.getVersion() == null ? "" : dep.getVersion());
      return result + ".jar";
   }

   /**
    * GroupId$ArtifactId
    * 
    * @return
    */
   public String getName()
   {
      return dep.getGroupId() + DELIM + dep.getArtifactId();
   }

   public Dependency getDependency()
   {
      return dep;
   }

   public int getVersion()
   {
      return version;
   }

   @Override
   public String toString()
   {
      return getFullName();
   }

   public class IllegalNameException extends RuntimeException
   {
      private static final long serialVersionUID = 3021789284719060665L;
      private String message;

      public IllegalNameException()
      {
         super.fillInStackTrace();
      }

      public IllegalNameException(String message)
      {
         this();
         this.message = message;
      }

      public IllegalNameException(String message, Throwable e)
      {
         this(message);
         super.initCause(e);
      }

      @Override
      public String getMessage()
      {
         return message;
      }
   }

   public boolean isSamePlugin(PluginJar jar)
   {
      return getName().equals(jar.getName());
   }
}
