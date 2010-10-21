package org.jboss.seam.forge.project.resources.builtin;

import java.util.Collections;
import java.util.List;

import org.jboss.seam.forge.parser.java.Field;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.resources.ClassMemberResource;

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

   public JavaFieldResource(final JavaResource parent, final Field field)
   {
      super(parent);
      this.field = field;
   }

   @Override
   public Resource<Field> createFrom(final Field file)
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

   @Override
   public String toString()
   {
      return field.getName();
   }
}
