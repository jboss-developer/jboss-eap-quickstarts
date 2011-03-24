/*
 * Copyright (C) 2008-2010, Google Inc.
 * and other copyright owners as documented in the project's IP log.
 *
 * This program and the accompanying materials are made available
 * under the terms of the Eclipse Distribution License v1.0 which
 * accompanies this distribution, is reproduced below, and is
 * available at http://www.eclipse.org/org/documents/edl-v10.php
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials provided
 *   with the distribution.
 *
 * - Neither the name of the Eclipse Foundation, Inc. nor the
 *   names of its contributors may be used to endorse or promote
 *   products derived from this software without specific prior
 *   written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.jboss.seam.forge.git;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.dircache.DirCacheCheckout;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.NotSupportedException;
import org.eclipse.jgit.errors.TransportException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RefComparator;
import org.eclipse.jgit.lib.RefUpdate;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.pgm.CLIText;
import org.eclipse.jgit.pgm.Die;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileBasedConfig;
import org.eclipse.jgit.storage.file.FileRepository;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.TagOpt;
import org.eclipse.jgit.transport.Transport;
import org.eclipse.jgit.transport.URIish;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class Clone
{
   private final String remoteName = Constants.DEFAULT_REMOTE_NAME;

   private FileRepository db;

   public void run(File projectDir, String sourceUri) throws IOException
   {
      try
      {
         final URIish uri = new URIish(sourceUri);
         File gitdir = new File(projectDir, Constants.DOT_GIT);
         db = new FileRepository(gitdir);
         db.create();
         final FileBasedConfig dstcfg = db.getConfig();
         dstcfg.setBoolean("core", null, "bare", false);
         dstcfg.save();

         saveRemote(uri);
         final FetchResult r = runFetch();
         final Ref branch = guessHEAD(r);
         doCheckout(branch);
      }
      catch (URISyntaxException e)
      {
         throw new IllegalArgumentException("Failed to parse remote repository URI", e);
      }
   }

   private void saveRemote(final URIish uri) throws URISyntaxException,
            IOException
   {
      final FileBasedConfig dstcfg = db.getConfig();
      final RemoteConfig rc = new RemoteConfig(dstcfg, remoteName);
      rc.addURI(uri);
      rc.addFetchRefSpec(new RefSpec().setForceUpdate(true)
               .setSourceDestination(Constants.R_HEADS + "*",
                        Constants.R_REMOTES + remoteName + "/*"));
      rc.update(dstcfg);
      dstcfg.save();
   }

   private FetchResult runFetch() throws NotSupportedException,
            URISyntaxException, TransportException
   {
      final Transport tn = Transport.open(db, remoteName);
      final FetchResult r;
      try
      {
         tn.setTagOpt(TagOpt.FETCH_TAGS);
         r = tn.fetch(new TextProgressMonitor(), null);
      }
      finally
      {
         tn.close();
      }
      return r;
   }

   private Ref guessHEAD(final FetchResult result)
   {
      final Ref idHEAD = result.getAdvertisedRef(Constants.HEAD);
      final List<Ref> availableRefs = new ArrayList<Ref>();
      Ref head = null;
      for (final Ref r : result.getAdvertisedRefs())
      {
         final String n = r.getName();
         if (!n.startsWith(Constants.R_HEADS))
            continue;
         availableRefs.add(r);
         if (idHEAD == null || head != null)
            continue;
         if (r.getObjectId().equals(idHEAD.getObjectId()))
            head = r;
      }
      Collections.sort(availableRefs, RefComparator.INSTANCE);
      if (idHEAD != null && head == null)
         head = idHEAD;
      return head;
   }

   private void doCheckout(final Ref branch) throws IOException
   {
      if (branch == null)
         throw new Die(CLIText.get().cannotChekoutNoHeadsAdvertisedByRemote);
      if (!Constants.HEAD.equals(branch.getName()))
      {
         RefUpdate u = db.updateRef(Constants.HEAD);
         u.disableRefLog();
         u.link(branch.getName());
      }

      final RevCommit commit = parseCommit(branch);
      final RefUpdate u = db.updateRef(Constants.HEAD);
      u.setNewObjectId(commit);
      u.forceUpdate();

      DirCache dc = db.lockDirCache();
      DirCacheCheckout co = new DirCacheCheckout(db, dc, commit.getTree());
      co.checkout();
   }

   private RevCommit parseCommit(final Ref branch)
            throws MissingObjectException, IncorrectObjectTypeException,
            IOException
   {
      final RevWalk rw = new RevWalk(db);
      final RevCommit commit;
      try
      {
         commit = rw.parseCommit(branch.getObjectId());
      }
      finally
      {
         rw.release();
      }
      return commit;
   }
}