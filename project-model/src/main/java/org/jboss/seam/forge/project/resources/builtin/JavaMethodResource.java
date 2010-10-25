package org.jboss.seam.forge.project.resources.builtin;

import java.util.Collections;
import java.util.List;

import org.jboss.seam.forge.parser.java.Method;
import org.jboss.seam.forge.parser.java.Parameter;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.resources.ClassMemberResource;

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

   public JavaMethodResource(final Resource<?> parent, final Method method)
   {
      super(parent);
      this.method = method;
   }

   @Override
   public Resource<Method> createFrom(final Method file)
   {
      throw new RuntimeException("not implemented");
   }

   public List<Resource<?>> listResources()
   {
      return Collections.emptyList();
   }

   @Override
   public Method getUnderlyingResourceObject()
   {
      return method;
   }

   @Override
   public String toString()
   {
      String params = "";
      for (Parameter param : method.getParameters())
      {
         params += param;
         if (method.getParameters().indexOf(param) < method.getParameters().size() - 1)
         {
            params += ", ";
         }
      }
      return method.getName() + "(" + params + ")" + " : "
               + (method.getReturnType() == null ? "void" : method.getReturnType());
   }
}
