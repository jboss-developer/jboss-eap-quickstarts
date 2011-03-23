/*
 * JBoss, by Red Hat.
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

package org.jboss.seam.forge.shell;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.jboss.seam.forge.shell.events.AcceptUserInput;
import org.jboss.seam.forge.shell.events.ReinitializeEnvironment;
import org.jboss.seam.forge.shell.events.Shutdown;
import org.jboss.seam.forge.shell.events.Startup;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * @author Mike Brock
 */
public class Bootstrap
{
   private static Thread currentShell = null;
   private static boolean restartRequested = false;
   private static File workingDir = new File("").getAbsoluteFile();

   @Inject
   private BeanManager manager;

   public static void main(final String[] args)
   {
      init();
   }

   private static void init()
   {
      do
      {
         loadPlugins();
         currentShell = new Thread(new Runnable()
         {
            @Override
            public void run()
            {
               boolean restarting = restartRequested;
               restartRequested = false;

               initLogging();
               Weld weld = new Weld();
               WeldContainer container = weld.initialize();
               BeanManager manager = container.getBeanManager();
               manager.fireEvent(new Startup(workingDir, restarting));
               manager.fireEvent(new AcceptUserInput());
               weld.shutdown();
            }
         });

         currentShell.start();
         try
         {
            currentShell.join();
         }
         catch (InterruptedException e)
         {
            throw new RuntimeException(e);
         }
      }
      while (restartRequested);
   }

   public void observeReinitialize(@Observes final ReinitializeEnvironment event, final Shell shell)
   {
      workingDir = shell.getCurrentDirectory().getUnderlyingResourceObject();
      manager.fireEvent(new Shutdown());
      restartRequested = true;
   }

   private static void initLogging()
   {
      String[] loggerNames = new String[] { "", "main", Logger.GLOBAL_LOGGER_NAME };
      for (String loggerName : loggerNames)
      {
         Logger globalLogger = Logger.getLogger(loggerName);
         Handler[] handlers = globalLogger.getHandlers();
         for (Handler handler : handlers)
         {
            handler.setLevel(Level.SEVERE);
            globalLogger.removeHandler(handler);
         }
      }
   }

   private static void loadPlugins()
   {
      try
      {
         File pluginsDir = new File(ShellImpl.FORGE_CONFIG_DIR + "/plugins/");
         if (pluginsDir.exists())
         {
            File[] files = pluginsDir.listFiles(new FilenameFilter()
            {
               @Override
               public boolean accept(final File dir, final String name)
               {
                  return name.endsWith(".jar");
               }
            });

            URL[] jars = new URL[files.length];

            for (int i = 0; i < files.length; i++)
            {
               jars[i] = files[i].toURI().toURL();
            }

            URLClassLoader classLoader = new URLClassLoader(jars, Bootstrap.class.getClassLoader());
            Thread.currentThread().setContextClassLoader(classLoader);
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
}
