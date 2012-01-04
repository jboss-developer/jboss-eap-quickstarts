package org.jboss.as.quickstarts.hibernate3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.jboss.as.quickstarts.hibernate3.data.GameHistory;

/**
 * <p>
 * {@link Game} contains all the business logic for the application, and also serves as the
 * controller for the JSF view.
 * </p>
 * <p>
 * It contains properties for the <code>number</code> to be guessed, the current <code>guess</code>,
 * the <code>smallest</code> and <code>biggest</code> numbers guessed so far (as this is a
 * higher/lower game we can prevent them entering numbers that they should know are wrong), and the
 * number of <code>remainingGuesses</code>.
 * </p>
 * <p>
 * The {@link #check()} method, and {@link #reset()} methods provide the business logic whilst the
 * {@link #validateNumberRange(FacesContext, UIComponent, Object)} method provides feedback to the
 * user.
 * </p>
 * <p>
 *  {@link Game} is upgraded version of class from 'numberguess' example. It introduces JPA/Hibernate code to
 *  track game progress. The {@link #check()} and {@link #getHistory()} method perform operation DB to update and fetch game history.
 * </p>
 * 
 * @author Pete Muir
 * @author baranowb
 */
@Named
@SessionScoped
public class Game implements Serializable {

   private static final long serialVersionUID = 991300443278089016L;

   /**
    * The number that the user needs to guess
    */
   private int number;

   /**
    * The users latest guess
    */
   private int guess;

   /**
    * The smallest number guessed so far (so we can track the valid guess range).
    */
   private int smallest;

   /**
    * The largest number guessed so far
    */
   private int biggest;

   /**
    * The number of guesses remaining
    */
   private int remainingGuesses;

   /**
    * The maximum number we should ask them to guess
    */
   @Inject
   @MaxNumber
   private int maxNumber;

   /**
    * The random number to guess
    */
   @Inject
   @Random
   Instance<Integer> randomNumber;

   @PersistenceContext
   EntityManager entityManager;
   
   @Resource
   UserTransaction userTransaction;
   
   public Game() {
   }

   public int getNumber() {
      return number;
   }

   public int getGuess() {
      return guess;
   }

   public void setGuess(int guess) {
      this.guess = guess;
   }

   public int getSmallest() {
      return smallest;
   }

   public int getBiggest() {
      return biggest;
   }

   public int getRemainingGuesses() {
      return remainingGuesses;
   }

   /**
    * Check whether the current guess is correct, and update the biggest/smallest guesses as needed.
    * Give feedback to the user if they are correct.
    */
   public void check() throws Exception{
      if (guess > number) {
         biggest = guess - 1;
      } else if (guess < number) {
         smallest = guess + 1;
      } else if (guess == number) {
         
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Correct!"));
      }
      remainingGuesses--;
      GameHistory gameHistory = new GameHistory( number, guess, smallest, biggest, remainingGuesses);
      // start transaction
      
      boolean startedTransaction = false;
      try{
          userTransaction.begin();
          startedTransaction = true;
          
          entityManager.joinTransaction();
          entityManager.persist(gameHistory);
      }finally
      {
          if(startedTransaction)
              userTransaction.commit();
      }
      
   }

    public List<GameHistory> getHistory() throws Exception {

       Query query = entityManager.createQuery("FROM GameHistory as gameHistory ORDER BY gameHistory.time");
       List untypedResultList = query.getResultList();
       ArrayList<GameHistory> result = new ArrayList<GameHistory>(untypedResultList.size());
       for (Object o : untypedResultList) {
            result.add((GameHistory) o);
        }

        return result;

    }

   /**
    * Reset the game, by putting all values back to their defaults, and getting a new random number.
    * We also call this method when the user starts playing for the first time using
    * {@linkplain PostConstruct @PostConstruct} to set the initial values.
    */
   @PostConstruct
   public void reset() {
      this.smallest = 0;
      this.guess = 0;
      this.remainingGuesses = 10;
      this.biggest = maxNumber;
      this.number = randomNumber.get();
   }

   /**
    * A JSF validation method which checks whether the guess is valid. It might not be valid because
    * there are no guesses left, or because the guess is not in range.
    * 
    */
   public void validateNumberRange(FacesContext context, UIComponent toValidate, Object value) {
      if (remainingGuesses <= 0) {
         FacesMessage message = new FacesMessage("No guesses left!");
         context.addMessage(toValidate.getClientId(context), message);
         ((UIInput) toValidate).setValid(false);
         return;
      }
      int input = (Integer) value;

      if (input < smallest || input > biggest) {
         ((UIInput) toValidate).setValid(false);

         FacesMessage message = new FacesMessage("Invalid guess");
         context.addMessage(toValidate.getClientId(context), message);
      }
   }
}
