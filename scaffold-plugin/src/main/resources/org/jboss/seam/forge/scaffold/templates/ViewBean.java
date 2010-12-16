import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import @{entity.getQualifiedName()};
import org.jboss.seam.forge.persistence.PersistenceUtil;

@Named
@Stateful
@RequestScoped
public class @{entity.getName()}Bean
{
   @Inject
   private PersistenceUtil util;
   
   private @{entity.getName()} entity = new @{entity.getName()}();
   private long id = 0;

   public void load()
   {
      entity = util.findById(@{entity.getName()}.class, id);
   }

   public long getId()
   {
      return id;
   }

   public void setId(long id)
   {
      load();
      this.id = id;
   }
   
   public @{entity.getName()} get@{entity.getName()}()
   {
      return entity;
   }

   public void set@{entity.getName()}(@{entity.getName()} entity)
   {
      this.entity = entity;
   }
}