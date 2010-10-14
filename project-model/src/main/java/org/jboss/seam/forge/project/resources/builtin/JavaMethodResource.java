package org.jboss.seam.forge.project.resources.builtin;

import org.jboss.seam.forge.parser.java.Method;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.resources.ClassMemberResource;
import org.jboss.seam.forge.project.services.ResourceFactory;

import java.util.Collections;
import java.util.List;

/**
 * @author Mike Brock <cbrock@redhat.com>
 */
public class JavaMethodResource extends ClassMemberResource<Method>
{
   private Method method;

   public JavaMethodResource()
   {
      super(null);
   }

   public JavaMethodResource(Resource parent, Method method)
   {
      super(parent);
      this.method = method;
   }

   @Override
   public Resource<Method> createFrom(Method file)
   {
      throw new RuntimeException("not implemented");
   }


   @Override
   public List<Resource<?>> listResources(ResourceFactory factory)
   {
      return Collections.emptyList();
   }

   @Override
   public Method getUnderlyingResourceObject()
   {
      return method;
   }
}
