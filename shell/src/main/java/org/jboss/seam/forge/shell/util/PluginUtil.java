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

package org.jboss.seam.forge.shell.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jboss.seam.forge.shell.plugins.PipeOut;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.yaml.snakeyaml.Yaml;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Mike Brock .
 */
public class PluginUtil
{
   public static List<PluginRef> findPlugin(String repoUrl, String searchString, PipeOut out) throws Exception
   {
      DefaultHttpClient client = new DefaultHttpClient();
      HttpGet httpGet = new HttpGet(repoUrl);

      out.print("Connecting to remote repository ... ");
      HttpResponse httpResponse = client.execute(httpGet);

      switch (httpResponse.getStatusLine().getStatusCode())
      {
      case 200:
         out.println("found!");
         break;

      case 404:
         out.println("failed! (plugin index not found: " + repoUrl + ")");
         return Collections.emptyList();

      default:
         out.println("failed! (server returned status code: " + httpResponse.getStatusLine().getStatusCode());
         return Collections.emptyList();
      }


      Pattern pattern = Pattern.compile(GeneralUtils.pathspecToRegEx("*" + searchString + "*"));

      List<PluginRef> pluginList = new ArrayList<PluginRef>();

      Yaml yaml = new Yaml();
      for (Object o : yaml.loadAll(httpResponse.getEntity().getContent()))
      {
         if (o == null)
         {
            continue;
         }

         Map map = (Map) o;
         String name = (String) map.get("name");

         if (pattern.matcher(name).matches())
         {
            pluginList.add(bindToPuginRef(map));
         }
      }

      return pluginList;
   }

   public static File downloadPlugin(final PluginRef ref, final PipeOut out, final String targetPath) throws Exception
   {
      DefaultHttpClient client = new DefaultHttpClient();

      String[] artifactParts = ref.getArtifact().split(":");

      if (artifactParts.length != 3)
      {
         throw new RuntimeException("malformed artifact identifier " +
                  "(format should be: <maven.group>:<maven.artifact>:<maven.version>) encountered: "
                  + ref.getArtifact());
      }

      String packageLocation = artifactParts[0].replaceAll("\\.", "/");
      String baseUrl;
      if (ref.getHomeRepo().endsWith("/"))
      {
         baseUrl = ref.getHomeRepo() + packageLocation + "/" + artifactParts[1] + "/" + artifactParts[2];
      }
      else
      {
         baseUrl = ref.getHomeRepo() + "/" + packageLocation + "/" + artifactParts[1] + "/" + artifactParts[2];
      }

      HttpGet httpGetManifest = new HttpGet(baseUrl + "/maven-metadata.xml");
      out.print("Retrieving artifact manifest ... ");

      HttpResponse response = client.execute(httpGetManifest);
      switch (response.getStatusLine().getStatusCode())
      {
      case 200:
         out.println("done.");

         Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                  .parse(response.getEntity().getContent());

         XPath xpath = XPathFactory.newInstance().newXPath();
         XPathExpression checkSnapshotExpr = xpath.compile("//versioning/snapshot");
         XPathExpression findJar = xpath.compile("//snapshotVersion[extension='jar']/value");

         NodeList list = (NodeList) checkSnapshotExpr.evaluate(document, XPathConstants.NODESET);

         out.print("Reading manifest ... ");

         if (list.getLength() != 0)
         {

            Node n = (Node) findJar.evaluate(document, XPathConstants.NODE);

            if (n == null)
            {
               out.println("failed: could not determine where to find jar file.");
               return null;
            }

            String version = n.getFirstChild().getTextContent();

            // plugin definition points to a snapshot.
            out.println("good! (maven snapshot found): " + version);

            String fileName = artifactParts[1] + "-" + version + ".jar";

            HttpGet jarGet = new HttpGet(baseUrl + "/" + fileName);

            out.print("Downloading: " + baseUrl + "/" + fileName + " ... ");
            response = client.execute(jarGet);

            try
            {
               File file = saveFile(targetPath + "/" + fileName, response.getEntity().getContent());
               out.println("done.");
               return file;
            }
            catch (IOException e)
            {
               out.println("failed to download: " + e.getMessage());
               return null;
            }



            // do download of snapshot.
         }
         else
         {
            out.println("error! (maven snapshot not found)");
            return null;
         }

      case 404:
         String requestUrl = baseUrl + "/" + artifactParts[2] + ".pom";
         httpGetManifest = new HttpGet(requestUrl);
         response = client.execute(httpGetManifest);

         if (response.getStatusLine().getStatusCode() != 200)
         {
            printError(response.getStatusLine().getStatusCode(), requestUrl, out);
            return null;
         }
         else
         {

            // download regular POM here.

         }
         break;
      default:
         out.println("failed! (server returned status code: " + response.getStatusLine().getStatusCode());
         return null;
      }


      return null;

   }

   private static void printError(int status, String requestUrl, PipeOut out)
   {
      switch (status)
      {
      case 404:
         out.println("failed! (file not found in repository: " + requestUrl + ")");
         break;
      default:
         out.println("failed! (server returned status code: " + status);
      }
   }

   private static File saveFile(String fileName, InputStream stream) throws IOException
   {
      File file = new File(fileName);
      new File(fileName.substring(0, fileName.lastIndexOf('/'))).mkdirs();

      file.createNewFile();

      OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));

      byte[] buf = new byte[127];
      int read;
      while ((read = stream.read(buf)) != -1)
      {
         outputStream.write(buf, 0, read);
      }

      outputStream.flush();
      outputStream.close();

      return file;
   }


   public static void loadPluginJar(File file) throws Exception
   {
      ClassLoader cl = Thread.currentThread().getContextClassLoader();
      if (cl == null)
      {
         cl = PluginUtil.class.getClassLoader();
      }

      URLClassLoader classLoader = new URLClassLoader(new URL[] { file.toURI().toURL() }, cl);

      Thread.currentThread().setContextClassLoader(classLoader);
   }

   private static PluginRef bindToPuginRef(Map map)
   {
      return new PluginRef((String) map.get("name"), (String) map.get("author"),
            (String) map.get("description"), (String) map.get("artifact"), (String) map.get("homerepo"));
   }
}
