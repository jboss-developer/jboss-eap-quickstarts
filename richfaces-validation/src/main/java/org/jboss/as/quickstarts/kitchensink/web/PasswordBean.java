package org.jboss.as.quickstarts.kitchensink.web;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * <p>
 * PasswordBean holds the current passwords.
 * </p>
 * 
 * <p>
 * This class also contains password confirmation logic which can be reused by JSR-303: Bean Validation
 * </p>
 * 
 * 
 * @author Lukas Fryc
 */
@Named
@SessionScoped
public class PasswordBean implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    /**
     * Defined constraints on password
     */
    @NotNull
    @NotEmpty
    @Size(min = 6)
    private String password;

    /**
     * Defined constraints on password confirmation
     */
    @NotNull
    @NotEmpty
    @Size(min = 6)
    private String passwordConfirmation;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    /**
     * <p>
     * Defines method for validation of passwords' equality.
     * </p>
     * 
     * <p>
     * By annotation method with {@link AssertTrue}, we instructs Bean Validation implementation to check that the result of its
     * invocation is true; otherwise, it fails validation process.
     * </p>
     * 
     * <p>
     * By using validation group, we specify that only selected validations should be proceeded.
     * </p>
     */
    @AssertTrue(message = "Password and confirmation needs to be same", groups = { PasswordConfirmation.class })
    public boolean isPasswordEquals() {
        return password.equals(passwordConfirmation);
    }

    /**
     * Exposes the validation group for password confirmation to the view layer.
     */
    public Class<?>[] getPasswordConfirmationGroups() {
        return new Class[] { PasswordConfirmation.class };
    }

    /**
     * This method is invoked once the user confirms the registration process.
     * 
     * It should setup password hash to member entity.
     */
    public void savePasswordHash() {
        // once a password pass validation, we can save the hash
        // e.g. newMember.setPasswordHash("...");
    }

    /**
     * The validation group for password confirmation
     */
    public interface PasswordConfirmation {
    }
}
