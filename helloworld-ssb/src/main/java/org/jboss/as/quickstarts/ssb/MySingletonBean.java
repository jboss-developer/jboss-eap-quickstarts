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

	private int consumeA = 1;
	private int consumeB = 1;

	public int consumeA() {
		return consumeA++;
	}

	public int consumeB() {
		return consumeB++;
	}
}
