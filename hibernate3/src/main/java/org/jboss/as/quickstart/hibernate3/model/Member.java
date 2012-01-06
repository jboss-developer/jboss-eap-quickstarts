package org.jboss.as.quickstart.hibernate3.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import javax.xml.bind.annotation.XmlRootElement;

// import validator annotations, those are Hiberante3, check this class source code and commented imports
// for better understanding of differences.
import org.hibernate.validator.Digits;
import org.hibernate.validator.Email;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Pattern;
import org.hibernate.validator.Size;

//NOTE: those are Hiberante v4 imports and dependencies ie. validation-api. v3 does not
//import org.hibernate.validator.constraints.Email;
//import org.hibernate.validator.constraints.NotEmpty;
//import javax.validation.constraints.Digits;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Pattern;
//import javax.validation.constraints.Size;
/*The Model uses JPA Entity as well as Hibernate Validators
 * 
 */

@Entity
@XmlRootElement
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "id"))
public class Member implements Serializable {
   /** Default value included to remove warning. Remove or modify at will. **/
   private static final long serialVersionUID = 1L;

   @Id
   private Long id;

   @NotNull
   //NOTE: @Size in v3 is used only for arrays and collection objects, we have to use @Length for string validation
   //@Size(min = 1, max = 25)
   @Length(min = 1, max = 25)
   //NOTE: v3: regex 
   //      v4: regexp
   @Pattern(regex = "[A-Za-z ]*", message = "must contain only letters and spaces")
   private String name;

   @NotNull
   @NotEmpty
   @Email
   private String email;

   @NotNull
   //NOTE: @Size in v3 is used only for arrays and collection objects, we have to use @Length for string validation
   //@Size(min = 9, max = 12, message="Size must be between 9 and 12 digits")
   @Length(min = 9, max = 12, message="Size must be between 9 and 12 digits")
   //NOTE v3: fractionalDigits
   //     v4: fraction
   //NOTE v3: integerDigits
   //     v4: integer
   @Digits(fractionalDigits = 0, integerDigits = 12,message="Not allowed digit!")
   @Column(name = "phone_number")
   private String phoneNumber;
   
   private String address;


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

   public String getAddress() {
	return address;
   }

   public void setAddress(String address) {
	this.address = address;
   }
}
