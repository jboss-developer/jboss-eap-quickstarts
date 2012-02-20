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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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
   public List<Country> getCountrys() {
      return countries;
   }

   public void onCountryListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Country member) {
      retrieveAllCountrysOrderedByName();
   }

   @PostConstruct
   public void retrieveAllCountrysOrderedByName() {
      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery<Country> criteria = cb.createQuery(Country.class);
      Root<Country> member = criteria.from(Country.class);
      // Swap criteria statements if you would like to try out type-safe criteria queries, a new
      // feature in JPA 2.0
      // criteria.select(member).orderBy(cb.asc(member.get(Country_.name)));
      criteria.select(member).orderBy(cb.asc(member.get("name")));
      countries = em.createQuery(criteria).getResultList();
   }
}
