/*
 * JBoss, by Red Hat.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.seam.forge.scaffold.plugins.meta.model;

import org.jboss.seam.forge.scaffold.plugins.meta.Renderable;

import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author Mike Brock .
 */
public class TClassType implements Renderable
{
   protected final TClassType superClass;
   protected final String name;

   protected final List<TClassType> interfaces;
   protected final List<TField> fields;
   protected final List<TConstructor> constructors;
   protected final List<TMethod> methods;

   protected int modifiers = 0;

   protected final boolean primitive;

   private TClassType(String name, TClassType superClass)
   {
      this.superClass = superClass;
      this.name = name;
      this.interfaces = new ArrayList<TClassType>();
      this.fields = new ArrayList<TField>();
      this.constructors = new ArrayList<TConstructor>();
      this.methods = new ArrayList<TMethod>();
      this.primitive = false;
   }

   private TClassType(String name, TClassType superClass, boolean primitive)
   {
      this.superClass = superClass;
      this.name = name;
      this.interfaces = new ArrayList<TClassType>();
      this.fields = new ArrayList<TField>();
      this.constructors = new ArrayList<TConstructor>();
      this.methods = new ArrayList<TMethod>();
      this.primitive = primitive;
   }

   public int getModifiers()
   {
      return modifiers;
   }

   public void setModifiers(int modifiers)
   {
      this.modifiers = modifiers;
   }

   public boolean isPrimitive()
   {
      return primitive;
   }

   public boolean isPublic()
   {
      return (this.modifiers & Modifier.PUBLIC) != 0;
   }

   public boolean isInterface()
   {
      return (this.modifiers & Modifier.INTERFACE) != 0;
   }

   public boolean isProtected()
   {
      return (this.modifiers & Modifier.PROTECTED) != 0;
   }

   public boolean isPrivate()
   {
      return (this.modifiers & Modifier.PRIVATE) != 0;
   }

   public boolean isStatic()
   {
      return (this.modifiers & Modifier.STATIC) != 0;
   }

   public boolean isFinal()
   {
      return (this.modifiers & Modifier.FINAL) != 0;
   }

   public boolean isSynchronized()
   {
      return (this.modifiers & Modifier.SYNCHRONIZED) != 0;
   }

   public boolean isVolatile()
   {
      return (this.modifiers & Modifier.VOLATILE) != 0;
   }

   public String getModifierString()
   {
      return Modifier.toString(this.modifiers);
   }

   public TClassType getSuperClass()
   {
      return superClass;
   }

   public String getName()
   {
      return name;
   }

   public String getSimpleName()
   {
      int idx = name.lastIndexOf('.');
      return idx == -1 ? name : name.substring(idx + 1);
   }

   public String getPackageName()
   {
      int idx = name.lastIndexOf('.');
      return idx == -1 ? "" : name.substring(0, idx);
   }

   public List<TField> getFields()
   {
      return fields;
   }

   public List<TConstructor> getConstructors()
   {
      return constructors;
   }

   public List<TMethod> getMethods()
   {
      return methods;
   }

   public List<TClassType> getInterfaces()
   {
      return interfaces;
   }

   public boolean isAssignableFrom(TClassType classType)
   {
      do
      {
         if (classType.getName().equals(getName()))
         {
            return true;
         }

         for (TClassType iface : getInterfaces())
         {
            if (iface.isAssignableFrom(classType))
            {
               return true;
            }
         }

      }
      while ((classType = classType.getSuperClass()) != null);

      return false;
   }

   public Set<TClassType> calculateImports()
   {
      Set<TClassType> imports = new HashSet<TClassType>();

      if (superClass != null)
      {
         imports.add(superClass);
      }

      for (TClassType iface : getInterfaces())
      {
         imports.add(iface);
      }

      for (TField fields : getFields())
      {
         imports.add(fields.getClassType());
      }

      for (TConstructor c : getConstructors())
      {
         for (TParameter p : c.getParameters())
         {
            imports.add(p.getClassType());
         }
      }

      for (TMethod method : getMethods())
      {
         imports.add(method.getReturnType());

         for (TParameter p : method.getParameters())
         {
            imports.add(p.getClassType());
         }
      }

      return imports;
   }

   public String renderImports()
   {
      StringBuilder sb = new StringBuilder();

      for (TClassType imp : calculateImports())
      {
         if (imp.isPrimitive() || imp.getName().startsWith("java.lang."))
         {
            continue;
         }

         sb.append("import ").append(imp.getName()).append(";\n");
      }

      return sb.toString();
   }


   @Override
   public String render()
   {
      StringBuilder sb = new StringBuilder();

      sb.append("package ").append(getPackageName()).append(";\n\n");
      sb.append(renderImports());
      sb.append("\n");
      sb.append(getModifierString()).append(' ').append("class").append(' ').append(getSimpleName()).append(" {\n");

      for (TField field : fields)
      {
         sb.append(field.render()).append('\n');
      }

      sb.append('\n');

      for (TConstructor constructor : constructors)
      {
         sb.append(constructor.render()).append('\n');
      }

      for (TMethod method : methods)
      {
         sb.append(method.render()).append('\n');
      }

      return RenderUtil.prettyPrint(sb.append("\n}\n").toString());
   }

   private static final Map<String, TClassType> typeCache = new HashMap<String, TClassType>();

   static
   {
      typeCache.put("java.lang.Object", new TClassType("java.lang.Object", null));
      typeCache.put("void", new TClassType("void", null));
      typeCache.put("int", new TClassType("int", null, true));
      typeCache.put("short", new TClassType("short", null, true));
      typeCache.put("long", new TClassType("long", null, true));
      typeCache.put("float", new TClassType("float", null, true));
      typeCache.put("double", new TClassType("double", null, true));
      typeCache.put("byte", new TClassType("byte", null, true));
      typeCache.put("char", new TClassType("char", null, true));
      typeCache.put("boolean", new TClassType("boolean", null, true));

   }

   public synchronized static TClassType getClass(String className, TClassType superClass)
   {
      TClassType type;
      if (!typeCache.containsKey(className))
      {
         typeCache.put(className, type = new TClassType(className, superClass));
      }
      else
      {
         type = typeCache.get(className);
      }
      return type;
   }

   public synchronized static TClassType getClass(String className)
   {
      String superClass;

      TClassType type;
      if (!typeCache.containsKey(className))
      {
         try
         {
            Class tryClass = Class.forName(className);
            if (tryClass.getSuperclass() == null)
            {
               superClass = null;
            }
            else
            {
               superClass = tryClass.getSuperclass().getName();
            }
         }
         catch (ClassNotFoundException e)
         {
            superClass = "java.lang.Object";
         }

         typeCache.put(className, type = new TClassType(className, superClass!= null ? getClass(superClass) : null));
      }
      else
      {
         type = typeCache.get(className);
      }


      return type;
   }
}
