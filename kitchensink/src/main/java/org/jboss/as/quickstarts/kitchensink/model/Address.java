package org.jboss.as.quickstarts.kitchensink.model;

import static javax.xml.bind.annotation.XmlAccessType.FIELD;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * A reusable representation of an address. It is mapped as a JPA embeddable entity, which models it
 * as part of the owning entity. It is also mapped as an anonymous type in JAXB.
 * 
 */
// Many IDEs warn us if we haven't added a serial id to a serializable class. Whilst you are
// prototyping and developing your application, having the serial ID generated is actually the best
// course. Generate a serial id once development is complete
@SuppressWarnings("serial")
// Map this class as a JPA embeddable entity. This means no id property is needed
@Embeddable
// Map this as an anonymous type for JAXB
@XmlType(name = "")
// Tell JAXB to use field level annotations
@XmlAccessorType(FIELD)
public class Address implements Serializable {

   /**
    * The first line of the address
    */
   // Require the user to provide the first line of the address
   @NotEmpty
   // Tell JAXB to ignore the synthetic ID when generating an XML model. It's not needed
   @XmlAttribute
   private String address1;

   /**
    * The second line of the address
    */
   // This line is not required, some addresses are quite short!
   // Tell JAXB to ignore the synthetic ID when generating an XML model. It's not needed
   @XmlAttribute
   private String address2;

   /**
    * The city
    */
   // Require the user to provide the city
   @NotEmpty
   // Tell JAXB to ignore the synthetic ID when generating an XML model. It's not needed
   @XmlAttribute
   private String city;

   /**
    * The region, state, county or province
    */
   // This line is not required, some cities are a region!
   // Tell JAXB to ignore the synthetic ID when generating an XML model. It's not needed
   @XmlAttribute
   private String region;

   /**
    * The postal (or "zip") code
    */
   // Require the user to provide a postal code
   @NotEmpty
   // Tell JAXB to ignore the synthetic ID when generating an XML model. It's not needed
   @XmlAttribute
   private String postalCode;

   /**
    * The country
    */
   // Countries are an open set, so modeled as an entity, not an enum (it would be silly to require
   // the application to be recompiled if a new country was created!)
   // We use a uni-directional many-to-one relationship (there is no need for a country to know the
   // associated addresses, that wouldn't scale well!)
   @ManyToOne
   // We require a country to be set
   @NotNull
   private Country country;

   public String getAddress1() {
      return address1;
   }

   // What follows is fairly boring boilerplate code, along with generated equals and hashcode
   // methods

   public void setAddress1(String address1) {
      this.address1 = address1;
   }

   public String getAddress2() {
      return address2;
   }

   public void setAddress2(String address2) {
      this.address2 = address2;
   }

   public String getCity() {
      return city;
   }

   public void setCity(String city) {
      this.city = city;
   }

   public String getRegion() {
      return region;
   }

   public void setRegion(String region) {
      this.region = region;
   }

   public String getPostalCode() {
      return postalCode;
   }

   public void setPostalCode(String postalCode) {
      this.postalCode = postalCode;
   }

   public Country getCountry() {
      return country;
   }

   public void setCountry(Country country) {
      this.country = country;
   }

   // An address doesn't have a good natural identifier, other than the value of all fields, so we
   // have to use all fields in hashCode and equals

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((address1 == null) ? 0 : address1.hashCode());
      result = prime * result + ((address2 == null) ? 0 : address2.hashCode());
      result = prime * result + ((city == null) ? 0 : city.hashCode());
      result = prime * result + ((country == null) ? 0 : country.hashCode());
      result = prime * result + ((postalCode == null) ? 0 : postalCode.hashCode());
      result = prime * result + ((region == null) ? 0 : region.hashCode());
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
      Address other = (Address) obj;
      if (address1 == null) {
         if (other.address1 != null)
            return false;
      } else if (!address1.equals(other.address1))
         return false;
      if (address2 == null) {
         if (other.address2 != null)
            return false;
      } else if (!address2.equals(other.address2))
         return false;
      if (city == null) {
         if (other.city != null)
            return false;
      } else if (!city.equals(other.city))
         return false;
      if (country == null) {
         if (other.country != null)
            return false;
      } else if (!country.equals(other.country))
         return false;
      if (postalCode == null) {
         if (other.postalCode != null)
            return false;
      } else if (!postalCode.equals(other.postalCode))
         return false;
      if (region == null) {
         if (other.region != null)
            return false;
      } else if (!region.equals(other.region))
         return false;
      return true;
   }

   public String toSimpleString() {
      StringBuilder builder = new StringBuilder().append(address1);
      if (address2 != null) {
         builder.append(", ").append(address2);
      }
      builder.append(", ").append(city);
      if (region != null) {
         builder.append(", ").append(region);
      }
      builder.append(", ").append(postalCode).append(", ").append(country.getName()).append(".");
      return builder.toString();
   }

}
