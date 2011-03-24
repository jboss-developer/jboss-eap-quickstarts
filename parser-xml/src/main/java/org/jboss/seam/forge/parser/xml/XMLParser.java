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
package org.jboss.seam.forge.parser.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jboss.shrinkwrap.descriptor.api.DescriptorExportException;
import org.jboss.shrinkwrap.descriptor.api.DescriptorImportException;
import org.jboss.shrinkwrap.descriptor.api.Node;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 */
public class XMLParser
{
   public static InputStream toXMLInputStream(Node node)
   {
      return new ByteArrayInputStream(toXMLByteArray(node));
   }

   public static String toXMLString(Node node)
   {
      return new String(toXMLByteArray(node));
   }

   public static byte[] toXMLByteArray(Node node)
   {
      try
      {
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         factory.setNamespaceAware(true);
         DocumentBuilder builder = factory.newDocumentBuilder();
         Document root = builder.newDocument();

         writeRecursive(root, node);

         Transformer transformer = TransformerFactory.newInstance().newTransformer();
         transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
         transformer.setOutputProperty(OutputKeys.INDENT, "yes");

         ByteArrayOutputStream stream = new ByteArrayOutputStream();
         StreamResult result = new StreamResult(stream);
         transformer.transform(new DOMSource(root), result);

         return stream.toByteArray();
      }
      catch (Exception e)
      {
         throw new DescriptorExportException("Could not export Node strcuture to XML", e);
      }
   }

   public static Node parse(byte[] xml)
   {
      return parse(new ByteArrayInputStream(xml));
   }

   public static Node parse(String xml)
   {
      return parse(xml.getBytes());
   }

   public static Node parse(InputStream stream) throws DescriptorImportException
   {
      try
      {
         if (stream.available() == 0)
         {
            return null;
         }

         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         factory.setNamespaceAware(true);
         DocumentBuilder builder = factory.newDocumentBuilder();
         Document doc = builder.parse(stream);

         Node root = new Node(doc.getDocumentElement().getNodeName());
         readRecursive(root, doc.getDocumentElement());
         return root;

      }
      catch (Exception e)
      {
         throw new DescriptorImportException("Could not import XML from stream", e);
      }
   }

   private static void readRecursive(Node target, org.w3c.dom.Node source)
   {
      readAttributes(target, source);
      NodeList sourceChildren = source.getChildNodes();
      if (sourceChildren != null)
      {
         for (int i = 0; i < sourceChildren.getLength(); i++)
         {
            org.w3c.dom.Node child = sourceChildren.item(i);
            if (child.getNodeType() != org.w3c.dom.Node.TEXT_NODE)
            {
               Node newTarget = target.create(child.getNodeName());
               if (onlyTextChildren(child))
               {
                  newTarget.text(child.getTextContent());
                  readAttributes(newTarget, child);
               }
               else
               {
                  readRecursive(newTarget, child);
               }
            }
         }
      }
   }

   private static void writeRecursive(org.w3c.dom.Node target, Node source)
   {
      Document owned = target.getOwnerDocument();
      if (owned == null)
      {
         owned = (Document) target;
      }
      org.w3c.dom.Node targetChild = null;
      if (source.text() != null)
      {
         targetChild = owned.createElement(source.name());
         targetChild.appendChild(owned.createTextNode(source.text()));
      }
      else
      {
         targetChild = owned.createElement(source.name());
      }

      target.appendChild(targetChild);

      for (Map.Entry<String, String> attribute : source.attributes().entrySet())
      {
         Attr attr = owned.createAttribute(attribute.getKey());
         attr.setValue(attribute.getValue());

         targetChild.getAttributes().setNamedItem(attr);
      }
      for (Node sourceChild : source.children())
      {
         writeRecursive(targetChild, sourceChild);
      }
   }

   private static void readAttributes(Node target, org.w3c.dom.Node source)
   {
      NamedNodeMap attributes = source.getAttributes();
      if (attributes != null)
      {
         for (int i = 0; i < attributes.getLength(); i++)
         {
            org.w3c.dom.Node attribute = attributes.item(i);
            target.attribute(attribute.getNodeName(), attribute.getNodeValue());
         }
      }
   }

   private static boolean onlyTextChildren(org.w3c.dom.Node source)
   {
      NodeList children = source.getChildNodes();
      for (int i = 0; i < children.getLength(); i++)
      {
         org.w3c.dom.Node child = children.item(i);
         if (child.getNodeType() != org.w3c.dom.Node.TEXT_NODE)
         {
            return false;
         }
      }
      return true;
   }
}