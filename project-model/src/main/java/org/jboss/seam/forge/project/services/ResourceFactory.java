package org.jboss.seam.forge.project.services;

import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.ResourceHandles;
import org.jboss.seam.forge.project.resources.builtin.DirectoryResource;
import org.jboss.seam.forge.project.resources.builtin.UnknownFileResource;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessBean;
import javax.inject.Singleton;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Mike Brock <cbrock@redhat.com>
 */
@Singleton
public class ResourceFactory implements Extension
{
   private BeanManager manager;

   private List<ResourceGenerator> resourceGenerators = new ArrayList<ResourceGenerator>();
   /**
    * Most directories will tend to contain the same type of file (such as .java, .jar, .xml, etc).  So
    * we will remember the last resource type we tested against and always re-try on subsequent queries
    * before doing a comprehensive match.
    */
   private volatile ResourceGenerator lastTypeLoaded = new ResourceGenerator()
   {
      @Override
      public boolean matches(String name)
      {
         return false;
      }
   };

   public void scan(@Observes final ProcessBean<?> event, final BeanManager manager)
   {
      this.manager = manager;

      Bean<?> bean = event.getBean();
      Class<?> clazz = bean.getBeanClass();

      if (Resource.class.isAssignableFrom(clazz) && clazz.isAnnotationPresent(ResourceHandles.class))
      {
         for (String pspec : clazz.getAnnotation(ResourceHandles.class).value())
         {
            Pattern p = Pattern.compile(pathspecToRegEx(pspec));
            CreationalContext<?> creationalCtx = manager.createCreationalContext(bean);
            Resource rInst = (Resource) manager.getReference(bean, bean.getBeanClass(), creationalCtx);

            resourceGenerators.add(new ResourceGenerator(p, rInst));
         }
      }
   }

   public Resource<File> getResourceFrom(final File file)
   {
      /**
       * Special case for directories required.
       */
      if (file.isDirectory())
      {
         return new DirectoryResource(this, file);
      }

      final String name = file.getName();

      synchronized (this)
      {
         if (lastTypeLoaded.matches(name))
         {
            return lastTypeLoaded.getResource(File.class).createFrom(file);
         }

         for (ResourceGenerator gen : resourceGenerators)
         {
            if (gen.matches(name))
            {
               return (lastTypeLoaded = gen).getResource(File.class).createFrom(file);
            }
         }
      }

      return new UnknownFileResource(this, file);
   }

   static class ResourceGenerator
   {
      private Pattern pattern;
      private Resource resource;

      ResourceGenerator()
      {
      }

      ResourceGenerator(Pattern pattern, Resource resource)
      {
         this.pattern = pattern;
         this.resource = resource;
      }

      public boolean matches(String name)
      {
         return pattern.matcher(name).matches();
      }

      @SuppressWarnings({"unchecked"})
      public <T> Resource<T> getResource(Class<T> type)
      {
         return resource;
      }
   }

   private static String pathspecToRegEx(String pathSpec)
   {
      return "^" + pathSpec.replaceAll("\\*", "\\.\\*").replaceAll("\\?", "\\.") + "$";
   }
}
