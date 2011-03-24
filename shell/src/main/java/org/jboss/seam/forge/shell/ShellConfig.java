/*
 * JBoss, Home of Professional Open Source
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
package org.jboss.seam.forge.shell;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class ShellConfig
{

   public void loadConfig(Shell shell)
   {
      File configDir = new File((String) shell.getProperty(ShellImpl.PROP_FORGE_CONFIG_DIR));

      if (!configDir.exists())
      {
         if (!configDir.mkdirs())
         {
            System.err.println("could not create config directory: " + configDir.getAbsolutePath());
            return;
         }
      }

      File historyFile = new File(configDir.getPath() + "/" + ShellImpl.FORGE_COMMAND_HISTORY_FILE);

      try
      {
         if (!historyFile.exists())
         {
            if (!historyFile.createNewFile())
            {
               System.err.println("could not create config file: " + historyFile.getAbsolutePath());
            }

         }
      }
      catch (IOException e)
      {
         throw new RuntimeException("could not create config file: " + historyFile.getAbsolutePath());
      }

      List<String> history = new ArrayList<String>();
      try
      {
         BufferedReader reader = new BufferedReader(new FileReader(historyFile));

         String line;
         while ((line = reader.readLine()) != null)
         {
            history.add(line);
         }

         reader.close();

         shell.setHistory(history);
      }
      catch (IOException e)
      {
         throw new RuntimeException("error loading file: " + historyFile.getAbsolutePath());
      }

      File configFile = new File(configDir.getPath() + "/" + ShellImpl.FORGE_CONFIG_FILE);

      if (!configFile.exists())
      {
         createDefaultConfigFile(configFile);
      }

      try
      {
         /**
          * Load the config file script.
          */
         shell.execute(configFile);
      }
      catch (IOException e)
      {
         e.printStackTrace();
         throw new RuntimeException("error loading file: " + configFile.getAbsolutePath());
      }

      try
      {
         shell.setHistoryOutputStream(new BufferedOutputStream(new FileOutputStream(historyFile, true)));
      }
      catch (FileNotFoundException e)
      {
         throw new RuntimeException("error setting shell history output stream to file: "
                  + historyFile.getAbsolutePath());
      }

   }

   private void createDefaultConfigFile(File configFile)
   {
      try
      {
         /**
          * Create a default config file.
          */

         configFile.createNewFile();
         OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(configFile));
         String defaultConfig = getDefaultConfig();
         for (int i = 0; i < defaultConfig.length(); i++)
         {
            outputStream.write(defaultConfig.charAt(i));
         }
         outputStream.flush();
         outputStream.close();

      }
      catch (IOException e)
      {
         e.printStackTrace();
         throw new RuntimeException("error creating default config file: " + configFile.getAbsolutePath());
      }
   }

   private String getDefaultConfig()
   {
      return "@/* Automatically generated config file */;\n"
               +
               "if (!$NO_MOTD) { "
               +
               "   echo \"   ____                          _____                    \";\n"
               +
               "   echo \"  / ___|  ___  __ _ _ __ ___    |  ___|__  _ __ __ _  ___ \";\n"
               +
               "   echo \"  \\\\___ \\\\ / _ \\\\/ _` | '_ ` _ \\\\   | |_ / _ \\\\| '__/ _` |/ _ \\\\  \\c{yellow}\\\\\\\\\\c\";\n"
               +
               "   echo \"   ___) |  __/ (_| | | | | | |  |  _| (_) | | | (_| |  __/  \\c{yellow}//\\c\";\n" +
               "   echo \"  |____/ \\\\___|\\\\__,_|_| |_| |_|  |_|  \\\\___/|_|  \\\\__, |\\\\___| \";\n" +
               "   echo \"                                                |___/      \";\n\n" +
               "}\n" +
               "\n" +
               "if ($OS_NAME.startsWith(\"Windows\")) {\n" +
               "    echo \"  Windows? Really? Okay...\\n\"\n" +
               "}\n" +
               "\n" +
               "set " + ShellImpl.PROP_PROMPT + " \"" + ShellImpl.DEFAULT_PROMPT + "\";\n" +
               "set " + ShellImpl.PROP_PROMPT_NO_PROJ + " \"" + ShellImpl.DEFAULT_PROMPT_NO_PROJ + "\";\n" +
               "set " + ShellImpl.PROP_DEFAULT_PLUGIN_REPO + " \"" + ShellImpl.DEFAULT_PLUGIN_REPO + "\";\n" +
               "set " + ShellImpl.PROP_IGNORE_EOF + " " + ShellImpl.DEFAULT_IGNORE_EOF + ";\n";

   }
}
