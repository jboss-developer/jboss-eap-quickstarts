package org.jboss.as.quickstarts.kitchensink.data;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.as.quickstarts.kitchensink.model.Country;

@RequestScoped
public class CountryListProducer {
   @Inject
   private EntityManager em;

   private List<Country> countries;

   // @Named provides access the return value via the EL variable name "countries" in the UI (e.g.,
   // Facelets or JSP view)
   @Produces
   @Named
   public List<Country> getCountries() {
      return countries;
   }

   public void onCountryListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Country member) {
      retrieveAllCountrysOrderedByName();
   }

   @PostConstruct
   public void retrieveAllCountrysOrderedByName() {
      countries = em.createQuery("select c from Country c order by c.name").getResultList();
   }
}
