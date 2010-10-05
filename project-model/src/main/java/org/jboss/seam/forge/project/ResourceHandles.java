package org.jboss.seam.forge.project;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;



/**
 * Declares a resource handler for specified wildcards.  For example:
 * <tt><code>
 *
 * @ResourceHandles({"*.txt", "*.text", "README"})
 * public class TextResource extends Resource {
 * ...
 * }
 * </code></tt>
 *
 * @author Mike Brock <cbrock@redhat.com>
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface ResourceHandles
{
   String[] value();
}
