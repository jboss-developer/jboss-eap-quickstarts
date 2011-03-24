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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
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
import org.jboss.seam.forge.project.dependencies.Dependency;
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

   @SuppressWarnings("unchecked")
   public static List<PluginRef> findPlugin(String repoUrl, String searchString, PipeOut out) throws Exception
   {
      DefaultHttpClient client = new DefaultHttpClient();
      HttpGet httpGet = new HttpGet(repoUrl);

      out.print("Connecting to remote repository [" + repoUrl + "]... ");
      HttpResponse httpResponse = client.execute(httpGet);

      switch (httpResponse.getStatusLine().getStatusCode())
      {
      case 200:
         out.println("connected!");
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

         Map<String, String> map = (Map<String, String>) o;
         String name = map.get(PROP_NAME);

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

      Dependency artifact = ref.getArtifact();
      String groupId = artifact.getGroupId();
      String artifactId = artifact.getArtifactId();
      String version = artifact.getVersion();

      String[] id = artifact.toIdentifier().split(":");
      if (id.length != 3)
      {
         throw new RuntimeException("malformed artifact identifier " +
                  "(format should be: <maven.group>:<maven.artifact>:<maven.version>) encountered: "
                  + artifact);
      }

      String packageLocation = Packages.toFileSyntax(groupId);
      String homeRepo = ref.getHomeRepo();

      if (!homeRepo.endsWith("/"))
      {
         homeRepo += "/";
      }

      String baseUrl = homeRepo + packageLocation + "/" + artifactId + "/" + version;
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

            String snapshotVersion = n.getFirstChild().getTextContent();

            // plugin definition points to a snapshot.
            out.println("good! (maven snapshot found): " + snapshotVersion);

            String fileName = artifactId + "-" + snapshotVersion + ".jar";

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
         String requestUrl = baseUrl + "/" + version + ".pom";
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

   public static File saveFile(String fileName, InputStream stream) throws IOException
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
