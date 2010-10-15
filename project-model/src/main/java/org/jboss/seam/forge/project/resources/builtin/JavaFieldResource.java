package org.jboss.seam.forge.project.resources.builtin;

import org.jboss.seam.forge.parser.java.Field;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.resources.ClassMemberResource;
import org.jboss.seam.forge.project.services.ResourceFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collections;
import java.util.List;

/**
 * @author Mike Brock <cbrock@redhat.com>
 */
public class JavaFieldResource extends ClassMemberResource<Field>
{
   private Field field;

   public JavaFieldResource()
   {
      super(null);
   }

   public JavaFieldResource(JavaResource parent, Field field)
   {
      super(parent);
      this.field = field;
   }

   @Override
   public Resource<Field> createFrom(Field file)
   {
      throw new RuntimeException("not implemented");
   }

   @Override
   public List<Resource<?>> listResources()
   {
      return Collections.emptyList();
   }

   @Override
   public Field getUnderlyingResourceObject()
   {
      return field;
   }
}
