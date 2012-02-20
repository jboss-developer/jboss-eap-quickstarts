package org.jboss.as.quickstarts.kitchensink.model;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.xml.bind.annotation.XmlAccessType.FIELD;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * A domain model object that represents a country. It is mapped as a JPA entity, and to XML using
 * JAXB.
 * 
 */
// Many IDEs warn us if we haven't added a serial id to a serializable class. Whilst you are
// prototyping and developing your application, having the serial ID generated is actually the best
// course. Generate a serial id once development is complete
@SuppressWarnings("serial")
// Map this as a JPA entity
@Entity
// Add a unique constraint on the country code, our natural key
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "code"))
// Map this object using JAXB
@XmlRootElement
// Tell JAXB to use field level annotations
@XmlAccessorType(FIELD)
public class Country implements Serializable {

   /**
    * The JPA synthetic ID
    */
   @Id
   @GeneratedValue(strategy = IDENTITY)
   // Tell JAXB to ignore the synthetic ID when generating an XML model. It's not needed
   @XmlTransient
   private Long id;

   /**
    * The name of the country
    */
   // We require the user to provide a name for the country
   @NotEmpty
   // Tell JAXB to map this as an attribute, not an element
   @XmlAttribute
   private String name;

   /**
    * The code for the country (e.g. GB, AU, US), our natural key
    */
   // We require the user to provide the country code
   @NotEmpty
   // The country code should be 2 characters long
   @Length(min = 2, max = 2)
   // Tell JAXB to map this as an attribute, not an element
   @XmlAttribute
   private String code;

   // What follows is fairly boring boilerplate code, along with generated equals and hashcode
   // methods

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getCode() {
      return code;
   }

   public void setCode(String code) {
      this.code = code;
   }

   // We use the natural key, code, for equals and hashcode
   
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((id == null) ? 0 : id.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      Country other = (Country) obj;
      if (id == null) {
         if (other.id != null)
            return false;
      } else if (!id.equals(other.id))
         return false;
      return true;
   }

}
