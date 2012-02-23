package org.jboss.as.quickstarts.kitchensink.model;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.xml.bind.annotation.XmlAccessType.FIELD;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * The main domain model object in the application. It is mapped as a JPA entity, and to XML using
 * JAXB.
 * 
 */
// Many IDEs warn us if we haven't added a serial id to a serializable class. Whilst you are
// prototyping and developing your application, having the serial ID generated is actually the best
// course. Generate a serial id once development is complete
@SuppressWarnings("serial")
// Map this as a JPA entity
@Entity
// Add a unique constraint on email, our natural key
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
// Map this object using JAXB
@XmlRootElement
// Tell JAXB to use field level annotations
@XmlAccessorType(FIELD)
public class Member implements Serializable {

   /**
    * The JPA synthetic ID
    */
   @Id
   @GeneratedValue(strategy = IDENTITY)
   // Tell JAXB to ignore the synthetic ID when generating an XML model. It's not needed
   @XmlTransient
   private long id;

   /**
    * The name of member
    */
   // A name must be provided
   @NotEmpty
   // The name can be a maximum of five characters
   @Length(max = 25)
   // The name can only contain letters and spaces
   @Pattern(regexp = "[A-Za-z ]*", message = "must contain only letters and spaces")
   // Tell JAXB to map this as an attribute, not an element
   @XmlAttribute
   private String name;

   /**
    * The email of the member, our natural key
    */
   // The email must be provided
   @NotEmpty
   // We use the Hibernate Validator Email constraint to check the email is valid
   @Email
   // Tell JAXB to map this as an attribute, not an element
   @XmlAttribute
   private String email;

   /**
    * The phone number of the member
    */
   // The phone number must be provided
   @NotEmpty
   // The phone number can be between 9 and 12 digits
   @Length(min = 9, max = 12)
   // The phone number string should represent a whole number, with a maximum of 12 digits
   @Digits(fraction = 0, integer = 12)
   // Tell JAX to map this as an attribute, not an element
   @XmlAttribute
   private String phoneNumber;

   /**
    * The address of the member
    */
   // The address must be provided
   // We model the address as an embeddable entity, allowing reuse
   // We do want JAXB to map this as an element, not an attribute, so we omit @XmlAttribute to get
   // the default behavior
   @NotNull
   private Address address = new Address();

   // What follows is fairly boring boilerplate code, along with generated equals and hashcode
   // methods

   public long getId() {
      return id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getPhoneNumber() {
      return phoneNumber;
   }

   public void setPhoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
   }

   public Address getAddress() {
      return address;
   }

   public void setAddress(Address address) {
      this.address = address;
   }

   // We use the natural key, email, for the hashcode and equals implementation

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((email == null) ? 0 : email.hashCode());
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
      Member other = (Member) obj;
      if (email == null) {
         if (other.email != null)
            return false;
      } else if (!email.equals(other.email))
         return false;
      return true;
   }

}