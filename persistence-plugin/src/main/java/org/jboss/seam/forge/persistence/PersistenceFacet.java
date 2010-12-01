/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.seam.forge.persistence;

import org.jboss.seam.forge.parser.JavaParser;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.project.Facet;
import org.jboss.seam.forge.project.PackagingType;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.constraints.RequiresFacets;
import org.jboss.seam.forge.project.constraints.RequiresPackagingTypes;
import org.jboss.seam.forge.project.dependencies.Dependency;
import org.jboss.seam.forge.project.dependencies.DependencyBuilder;
import org.jboss.seam.forge.project.facets.DependencyFacet;
import org.jboss.seam.forge.project.facets.JavaSourceFacet;
import org.jboss.seam.forge.project.facets.ResourceFacet;
import org.jboss.shrinkwrap.descriptor.api.DescriptorImporter;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.spec.jpa.persistence.*;
import org.jboss.shrinkwrap.descriptor.impl.spec.jpa.persistence.PersistenceDescriptorImpl;
import org.jboss.shrinkwrap.descriptor.impl.spec.jpa.persistence.PersistenceModel;
import org.jboss.shrinkwrap.descriptor.spi.SchemaDescriptorProvider;

import javax.inject.Named;
import javax.persistence.Entity;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Named("persistence")
@RequiresFacets({JavaSourceFacet.class, ResourceFacet.class, DependencyFacet.class})
@RequiresPackagingTypes({PackagingType.JAR, PackagingType.WAR})
public class PersistenceFacet implements Facet
{
   private static final Dependency dep =
         DependencyBuilder.create("org.jboss.spec:jboss-javaee-6.0:1.0.0.CR1:provided:basic");

   private Project project;

   private final FilenameFilter entityFileFilter = new FilenameFilter()
   {
      @Override
      public boolean accept(final File dir, final String name)
      {
         return name.endsWith(".java");
      }
   };

   private final FileFilter directoryFilter = new FileFilter()
   {
      @Override
      public boolean accept(final File file)
      {
         return file.isDirectory();
      }
   };

   @Override
   public Project getProject()
   {
      return project;
   }

   @Override
   public void setProject(final Project project)
   {
      this.project = project;
   }

   @Override
   public Facet install()
   {
      if (!isInstalled())
      {
         DependencyFacet deps = project.getFacet(DependencyFacet.class);
         if (!deps.hasDependency(dep))
         {
            deps.addDependency(dep);
         }

         File entityRoot = getEntityPackageFile();
         if (!entityRoot.exists())
         {
            project.mkdirs(entityRoot);
            entityRoot.mkdirs();
         }

         installUtils();

         File descriptor = getConfigFile();
         if (!descriptor.exists())
         {
            PersistenceUnitDef unit = Descriptors.create(PersistenceDescriptor.class)
                  .persistenceUnit("default")
                  .description("The Seam Forge default Persistence Unit")
                  .transactionType(TransactionType.JTA)
                  .provider(ProviderType.HIBERNATE)
                  .jtaDataSource("java:/DefaultDS")
                  .includeUnlistedClasses()
                  .schemaGenerationMode(SchemaGenerationModeType.CREATE_DROP)
                  .showSql()
                  .formatSql()
                  .property("hibernate.transaction.flush_before_completion", true);

            project.writeFile(unit.exportAsString(), descriptor);
         }
      }
      project.registerFacet(this);
      return this;
   }

   private void installUtils()
   {
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      JavaClass util = JavaParser.parse(loader.getResourceAsStream("templates/PersistenceUtil.java"));
      JavaClass producer = JavaParser.parse(loader.getResourceAsStream("templates/DatasourceProducer.java"));

      JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);

      util.setPackage(java.getBasePackage() + ".persist");
      producer.setPackage(java.getBasePackage() + ".persist");

      java.saveJavaClass(producer);
      java.saveJavaClass(util);
   }

   @Override
   public boolean isInstalled()
   {
      DependencyFacet deps = project.getFacet(DependencyFacet.class);
      boolean hasDependency = deps.hasDependency(dep);
      return hasDependency && getEntityPackageFile().exists() && getConfigFile().exists();
   }

   public String getEntityPackage()
   {
      JavaSourceFacet sourceFacet = project.getFacet(JavaSourceFacet.class);
      return sourceFacet.getBasePackage() + ".domain";
   }

   public File getEntityPackageFile()
   {
      JavaSourceFacet sourceFacet = project.getFacet(JavaSourceFacet.class);
      return new File(sourceFacet.getBasePackageResource() + File.separator + "domain").getAbsoluteFile();
   }

   @SuppressWarnings("unchecked")
   public PersistenceModel getConfig()
   {
      DescriptorImporter<PersistenceDescriptor> importer = Descriptors.importAs(PersistenceDescriptor.class);
      PersistenceDescriptor descriptor = importer.from(getConfigFile());
      PersistenceModel model = ((SchemaDescriptorProvider<PersistenceModel>) descriptor).getSchemaModel();
      return model;
   }

   public void saveConfig(final PersistenceModel model)
   {
      PersistenceDescriptor descriptor = new PersistenceDescriptorImpl(model);
      String output = descriptor.exportAsString();
      project.writeFile(output, getConfigFile());
   }

   private File getConfigFile()
   {
      ResourceFacet resources = project.getFacet(ResourceFacet.class);
      return new File(resources.getResourceFolder() + File.separator + "META-INF" + File.separator + "persistence.xml");
   }

   public List<JavaClass> getAllEntities()
   {
      File packageFile = getEntityPackageFile();
      return findEntitiesInFolder(packageFile);
   }

   private List<JavaClass> findEntitiesInFolder(final File packageFile)
   {
      List<JavaClass> result = new ArrayList<JavaClass>();
      if (packageFile.exists())
      {
         for (File source : packageFile.listFiles(entityFileFilter))
         {
            try
            {
               JavaClass javaClass = JavaParser.parse(source);
               if (javaClass.hasAnnotation(Entity.class))
               {
                  result.add(javaClass);
               }
            }
            catch (FileNotFoundException e)
            {
               // Meh, oh well.
            }
         }

         for (File source : packageFile.listFiles(directoryFilter))
         {
            List<JavaClass> subResults = findEntitiesInFolder(source);
            result.addAll(subResults);
         }
      }
      return result;
   }
}
