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
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RepositoryBuilder;
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
      return db(dir);
   }

   public static Git db(DirectoryResource dir) throws IOException
   {
      RepositoryBuilder db = new RepositoryBuilder().findGitDir(dir.getUnderlyingResourceObject());
      return new Git(db.build());
   }

   public static void checkout(Git git, String remote, boolean force) throws JGitInternalException,
            RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException
   {
      CheckoutCommand command = git.checkout();
      command.setCreateBranch(true);
      command.setName(remote);
      command.setForce(force);
      Ref call = command.call();
   }
}
