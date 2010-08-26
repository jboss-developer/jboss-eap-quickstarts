package org.jboss.seam.sidekick.shell.test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;

public class StringInputStream extends BufferedInputStream
{
   StringInputStream(final String data)
   {
      super(new ByteArrayInputStream(data.getBytes()));
   }

}