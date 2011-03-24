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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jboss.seam.forge.resources.DirectoryResource;
import org.jboss.seam.forge.resources.FileResource;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.PipeOut;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.yaml.snakeyaml.Yaml;

/**
 * @author Mike Brock .
 */
public class PluginUtil
{
   private static final String PROP_GIT_REPOSITORY = "gitrepo";
   private static final String PROP_HOME_MAVEN_REPO = "homerepo";
   private static final String PROP_ARTIFACT = "artifact";
   private static final String PROP_DESCRIPTION = "description";
   private static final String PROP_AUTHOR = "author";
   private static final String PROP_NAME = "name";
   private static final Object PROP_GIT_REF = "gitref";

   private static String getDefaultRepo(Shell shell)
   {
      String defaultRepo = (String) shell.getProperty("DEFFAULT_PLUGIN_REPO");
      if (defaultRepo == null)
      {
         throw new RuntimeException("no default repository set: (to set, type: set DEFAULT_PLUGIN_REPO <repository>)");
      }
      return defaultRepo;
   }

   @SuppressWarnings("unchecked")
   public static List<PluginRef> findPlugin(Shell sh, String searchString, PipeOut out) throws Exception
   {
      String defaultRepo = getDefaultRepo(sh);
      HttpGet httpGet = new HttpGet(defaultRepo);

      out.print("Connecting to remote repository [" + defaultRepo + "]... ");
      HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);

      switch (httpResponse.getStatusLine().getStatusCode())
      {
      case 200:
         out.println("connected!");
         break;

      case 404:
         out.println("failed! (plugin index not found: " + defaultRepo + ")");
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

         Map<String, String> map = (Map<String, String>) o;
         String name = map.get(PROP_NAME);

         if (pattern.matcher(name).matches())
         {
            pluginList.add(bindToPuginRef(map));
         }
      }

      return pluginList;
   }

   public static void downloadFromURL(final PipeOut out, final URL url, final FileResource<?> resource)
            throws IOException
   {

      HttpGet httpGetManifest = new HttpGet(url.toExternalForm());
      out.print("Retrieving artifact ... ");

      HttpResponse response = new DefaultHttpClient().execute(httpGetManifest);
      switch (response.getStatusLine().getStatusCode())
      {
      case 200:
         out.println("done.");
         try
         {
            resource.setContents(response.getEntity().getContent());
            out.println("done.");
         }
         catch (IOException e)
         {
            out.println("failed to download: " + e.getMessage());
         }

      default:
         out.println("failed! (server returned status code: " + response.getStatusLine().getStatusCode());
      }
   }

   public static FileResource<?> downloadFromIndexRef(final PluginRef ref, final PipeOut out,
            final DirectoryResource pluginDir) throws Exception
   {
      DefaultHttpClient client = new DefaultHttpClient();

      String[] artifactParts = ref.getArtifact().toIdentifier().split(":");

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

      HttpResponse response = new DefaultHttpClient().execute(httpGetManifest);
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

            response = new DefaultHttpClient().execute(jarGet);

            try
            {
               FileResource<?> target = (FileResource<?>) pluginDir.getChild(fileName);
               if (!target.exists() && target.createNewFile())
               {
                  target.setContents(response.getEntity().getContent());
                  out.println("done.");
                  return target;
               }
               else
               {
                  throw new IllegalStateException("Could not create target file [" + target.getFullyQualifiedName()
                           + "]");
               }
            }
            catch (IOException e)
            {
               out.println("failed to download: " + e.getMessage());
               return null;
            }
         }
         else
         {
            out.println("error! (maven snapshot not found)");
            return null;
         }

      case 404:
         String requestUrl = baseUrl + "/" + artifactParts[2] + ".pom";
         httpGetManifest = new HttpGet(requestUrl);
         response = new DefaultHttpClient().execute(httpGetManifest);

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

   private static PluginRef bindToPuginRef(Map<String, String> map)
   {
      return new PluginRef(map.get(PROP_NAME),
               map.get(PROP_AUTHOR),
               map.get(PROP_DESCRIPTION),
               map.get(PROP_ARTIFACT),
               map.get(PROP_HOME_MAVEN_REPO),
               map.get(PROP_GIT_REPOSITORY),
               map.get(PROP_GIT_REF));
   }
}
