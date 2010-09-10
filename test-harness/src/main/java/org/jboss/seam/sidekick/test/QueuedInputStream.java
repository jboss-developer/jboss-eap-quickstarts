package org.jboss.seam.sidekick.test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;

public class QueuedInputStream extends InputStream
{
   BufferedInputStream current;
   private final Queue<String> inputQueue;

   QueuedInputStream(final Queue<String> inputQueue)
   {
      super();
      this.inputQueue = inputQueue;
   }

   @Override
   public synchronized int available() throws IOException
   {
      requireCurrent();
      return current.available();
   }

   @Override
   public void close() throws IOException
   {
      requireCurrent();
      current.close();
   }

   @Override
   public synchronized void mark(int readlimit)
   {
      requireCurrent();
      current.mark(readlimit);
   }

   @Override
   public boolean markSupported()
   {
      requireCurrent();
      return current.markSupported();
   }

   @Override
   public synchronized int read() throws IOException
   {
      requireCurrent();
      return current.read();
   }

   @Override
   public synchronized int read(byte[] b, int off, int len) throws IOException
   {
      requireCurrent();
      return current.read(b, off, len);
   }

   @Override
   public synchronized void reset() throws IOException
   {
      current.reset();
   }

   @Override
   public synchronized long skip(long n) throws IOException
   {
      requireCurrent();
      return current.skip(n);
   }

   /*
    * Utilities
    */
   synchronized private void requireCurrent()
   {
      try
      {
         if (((current == null) || (current.available() <= 0)) && !inputQueue.isEmpty())
         {
            byte[] bytes = inputQueue.remove().getBytes();
            current = new BufferedInputStream(new ByteArrayInputStream(bytes));
         }
         else
         {
            new BufferedInputStream(new ByteArrayInputStream(new byte[] {}));
         }
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
   }
}