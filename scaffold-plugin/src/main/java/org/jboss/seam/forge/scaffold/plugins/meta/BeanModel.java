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

package org.jboss.seam.forge.scaffold.plugins.meta;

import org.jboss.seam.forge.scaffold.plugins.meta.model.*;

import java.lang.reflect.Modifier;
import java.util.List;

/**
 * @author Mike Brock .
 */
public class BeanModel
{
   private TClassType classType;

   public BeanModel(String beanName)
   {
      this.classType = TClassType.getClass(beanName);
      this.classType.setModifiers(Modifier.PUBLIC);
   }

   public List<TField> getFields()
   {
      return classType.getFields();
   }

   public List<TConstructor> getConstructors()
   {
      return classType.getConstructors();
   }

   public List<TClassType> getInterface()
   {
      return classType.getInterfaces();
   }

   public void addBeanField(String name, Class type)
   {
      addBeanField(name, TClassType.getClass(type.getName()));
   }

   public void addBeanField(String name, String type)
   {
      addBeanField(name, TClassType.getClass(type));
   }

   public void addBeanField(String name, TClassType type)
   {
      classType.getFields().add(new TField(classType, name, type, Modifier.PRIVATE | Modifier.FINAL));

      TMethod getterMethod = new TMethod(classType, RenderUtil.getGetterName(type, name), type, Modifier.PUBLIC);
      getterMethod.setMethodContents("return this." + name + ";\n");
      classType.getMethods().add(getterMethod);

      TMethod setterMethod = new TMethod(classType, RenderUtil.getSetterName(name), type, Modifier.PUBLIC);
      setterMethod.addParameter(name, type, Modifier.FINAL);
      setterMethod.setMethodContents("this." + name + " = " + name + ";\n");
      classType.getMethods().add(setterMethod);
   }

   public String render() {
      return classType.render();
   }


   public static void main(String[] args)
   {
      BeanModel bm = new BeanModel("org.foo.Bar");
      bm.addBeanField("age", "int");
      bm.addBeanField("name", String.class);
      bm.addBeanField("list", List.class);

      System.out.println(bm.render());
   }
}
