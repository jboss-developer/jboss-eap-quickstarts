package org.jboss.as.quickstarts.managedbeans;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

import org.jboss.as.quickstarts.ssb.MySingletonBean;
import java.io.Serializable;

@ManagedBean
@SessionScoped
public class MyManagedBean implements Serializable {

	private static final long serialVersionUID = -4401892785145993999L;

	@EJB
	private MySingletonBean mySingletonBean;

	private int aCounter;
	private int bCounter;

	public MyManagedBean() {
		this.aCounter = 0;
		this.bCounter = 0;
	}

	public int getaCounter() {
		aCounter = mySingletonBean.incrementA();
		return aCounter;
	}

	public void setaCounter(int aCounter) {
		this.aCounter = aCounter;
	}

	public int getbCounter() {
		bCounter = mySingletonBean.incrementB();
		return bCounter;
	}

	public void setbCounter(int bCounter) {
		this.bCounter = bCounter;
	}
}
