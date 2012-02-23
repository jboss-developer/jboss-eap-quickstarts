package org.jboss.as.quickstarts.kitchensink.controller;

import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import static javax.faces.application.FacesMessage.SEVERITY_INFO;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.as.quickstarts.kitchensink.model.Country;

/**
 * Backing bean for Country entities.
 * <p>
 * This class provides CRUD functionality for all Country entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@SuppressWarnings("serial")
@Stateful
@Named @ConversationScoped
public class CountriesController implements Serializable {
   
   @Inject
   Logger log;
   
   @Inject
   private EntityManager em;
   
   @Inject
   private Conversation conversation;
   
   @Inject
   private Event<Country> countryEventSrc;
   
   private List<Country> countries;

   private Country country;
   private String code;

   @Produces @Named
	public Converter getCountryConverter() {

		return new Converter() {

			@Override
			public Object getAsObject(FacesContext context,
					UIComponent component, String value) {

				return em.find(Country.class,
						Long.valueOf(value));
			}

			@Override
			public String getAsString(FacesContext context,
					UIComponent component, Object value) {

				if (value == null) {
					return "";
				}

				return String.valueOf(((Country) value).getId());
			}
		};
	}
   
   @Produces @Named
   public Country getCountry() {
      return country;
   }
   
   public void add() throws Exception {
      if (code == null) {
         log.info("Registering " + country.getName());
         em.persist(country);
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(SEVERITY_INFO, "Added " + country.getCode() , null));
      } else {
         log.info("Updating " + country.getName());
         em.merge(country);
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(SEVERITY_INFO, "Updated " + country.getCode(), null));
         conversation.end();
      }
      init();
      countryEventSrc.fire(country);
   }
   
   @PostConstruct
   public void init() {
      this.code = null;
      this.country = new Country();
   }
   
   public String getCode() {
      return code;
   }
   
   public void setCode(String code) {
      this.code = code;
   }
   
   public void retrieve() {
      if (code != null) {
         if (conversation.isTransient()) {
            conversation.begin();
         }
         List<Country> countries = em.createQuery("select c from Country c where c.code = :code").setParameter("code", code).getResultList();
         if (countries.isEmpty()) {
            // This could happen if the user manually sets the wrong code
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(SEVERITY_ERROR, "Unable to load country with code " + code, null));
         } else if (countries.size() > 1) {
            // This should never happen, it means a db constraint went wrong
            throw new IllegalStateException("Something went very wrong, code should be unique!");
         } else {
            this.country = countries.get(0);
         }
      }
   }
	
}