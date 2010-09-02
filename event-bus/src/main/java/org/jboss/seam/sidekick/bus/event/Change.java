package org.jboss.seam.sidekick.bus.event;

/**
 *
 * @author Mike Brock <cbrock@redhat.com>
 */
public abstract class Change<E>
{
   protected final Delta deltaType;
   protected final E changeType;
   protected final Payload<?> payload;

   public Change(Delta deltaType, E changeType, Payload<?> payload)
   {
      this.deltaType = deltaType;
      this.changeType = changeType;
      this.payload = payload;
   }

   public Delta getDeltaType()
   {
      return deltaType;
   }

   public E getChangeType()
   {
      return changeType;
   }

   public Payload<?> getPayload()
   {
      return payload;
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (!(o instanceof Change)) return false;

      Change change = (Change) o;

      if (changeType != null ? !changeType.equals(change.changeType) : change.changeType != null) return false;
      if (deltaType != change.deltaType) return false;
      if (payload != null ? !payload.equals(change.payload) : change.payload != null) return false;

      return true;
   }

   @Override
   public int hashCode()
   {
      int result = deltaType != null ? deltaType.hashCode() : 0;
      result = 31 * result + (changeType != null ? changeType.hashCode() : 0);
      result = 31 * result + (payload != null ? payload.hashCode() : 0);
      return result;
   }
}
