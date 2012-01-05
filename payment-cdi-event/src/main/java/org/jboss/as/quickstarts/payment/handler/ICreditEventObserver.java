package org.jboss.as.quickstarts.payment.handler;

import javax.enterprise.event.Observes;

import org.jboss.as.quickstarts.payment.events.PaymentEvent;
import org.jboss.as.quickstarts.payment.qualifiers.Credit;

public interface ICreditEventObserver {
	
	public void onCreditPaymentEvent(@Observes @Credit PaymentEvent event);

}
