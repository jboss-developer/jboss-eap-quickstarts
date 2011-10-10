package org.jboss.as.quickstarts.login;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The user entity. As user is a reserved word in some databases we change the table name in the database
 */
@Entity
@Table(name = "person")
@XmlRootElement
public class User {
   @Id
   private String username;
   private String name;
   private String password;

   public String getUsername() {
      return username;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getName() {
      return name;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getPassword() {
      return password;
   }

   @Override
   public String toString() {
      return "User (username = " + username + ", name = " + name + ")";
   }

}
