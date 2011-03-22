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
package org.jboss.seam.forge.git;

import java.io.IOException;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.FetchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.CanceledException;
import org.eclipse.jgit.api.errors.DetachedHeadException;
import org.eclipse.jgit.api.errors.InvalidConfigurationException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.RefSpec;
import org.jboss.seam.forge.resources.DirectoryResource;

/**
 * Convenience tools for interacting with the Git version control system.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public abstract class GitUtils
{
   public static Git clone(DirectoryResource dir, String repoUri) throws IOException
   {
      new Clone().run(dir.getUnderlyingResourceObject(), repoUri);
      return git(dir);
   }

   public static Git git(DirectoryResource dir) throws IOException
   {
      RepositoryBuilder db = new RepositoryBuilder().findGitDir(dir.getUnderlyingResourceObject());
      return new Git(db.build());
   }

   public static Ref checkout(Git git, String remote, boolean createBranch, SetupUpstreamMode mode, boolean force)
            throws JGitInternalException,
            RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException
   {
      CheckoutCommand checkout = git.checkout();
      checkout.setCreateBranch(createBranch);
      checkout.setName(remote);
      checkout.setForce(force);
      checkout.setUpstreamMode(mode);
      return checkout.call();
   }

   public static FetchResult fetch(Git git, String remote, String refSpec, int timeout, boolean fsck, boolean dryRun,
            boolean thin,
            boolean prune) throws JGitInternalException, InvalidRemoteException
   {
      FetchCommand fetch = git.fetch();
      fetch.setCheckFetchedObjects(fsck);
      fetch.setRemoveDeletedRefs(prune);
      if (refSpec != null)
         fetch.setRefSpecs(new RefSpec(refSpec));
      if (timeout >= 0)
         fetch.setTimeout(timeout);
      fetch.setDryRun(dryRun);
      fetch.setRemote(remote);
      fetch.setThin(thin);
      fetch.setProgressMonitor(new TextProgressMonitor());

      FetchResult result = fetch.call();
      return result;
   }

   public static PullResult pull(Git git, int timeout) throws WrongRepositoryStateException,
            InvalidConfigurationException, DetachedHeadException, InvalidRemoteException, CanceledException
   {
      PullCommand pull = git.pull();
      if (timeout >= 0)
         pull.setTimeout(timeout);
      pull.setProgressMonitor(new TextProgressMonitor());

      PullResult result = pull.call();
      return result;
   }
}
