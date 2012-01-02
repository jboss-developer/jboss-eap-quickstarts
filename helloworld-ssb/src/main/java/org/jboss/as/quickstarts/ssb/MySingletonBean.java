package org.jboss.as.quickstarts.ssb;

import javax.ejb.Singleton;

/**
 * <p>
 * </p>
 * 
 * @author Serge Pagop (spagop@redhat.com)
 * 
 */
@Singleton
public class MySingletonBean {

	private int incrementA = 1;
	private int incrementB = 1;

	public int incrementA() {
		return incrementA++;
	}

	public int incrementB() {
		return incrementB++;
	}
}
