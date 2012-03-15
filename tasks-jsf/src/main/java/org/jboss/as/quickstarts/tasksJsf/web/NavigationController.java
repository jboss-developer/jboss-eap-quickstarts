package org.jboss.as.quickstarts.tasksJsf.web;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Redirects user to another view when he requests page without sufficient permissions.
 *
 * @author Lukas Fryc
 *
 */
@Named
@RequestScoped
public class NavigationController {

    @Inject
    FacesContext facesContext;

    @Inject
    AuthController auth;

    /**
     * Checks whether user is logged in - redirects to tasks page when he is logged in
     */
    public void redirectLoggedUsers(ComponentSystemEvent event) {
        if (auth.isLogged()) {
            ConfigurableNavigationHandler navigationHandler = (ConfigurableNavigationHandler) facesContext.getApplication()
                    .getNavigationHandler();
            navigationHandler.performNavigation("tasks?faces-redirect=true");
        }
    }

    /**
     * Checks whether user is not logged in - redirects to index (login) page when he is not loged in
     */
    public void redirectUnloggedUsers(ComponentSystemEvent event) {
        if (!auth.isLogged()) {
            ConfigurableNavigationHandler navigationHandler = (ConfigurableNavigationHandler) facesContext.getApplication()
                    .getNavigationHandler();
            navigationHandler.performNavigation("index?faces-redirect=true");
        }
    }
}
