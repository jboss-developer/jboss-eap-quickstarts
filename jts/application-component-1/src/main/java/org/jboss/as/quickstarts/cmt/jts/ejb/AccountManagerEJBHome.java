package org.jboss.as.quickstarts.cmt.jts.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBHome;

public interface AccountManagerEJBHome extends EJBHome {

    public AccountManagerEJB create() throws RemoteException;

}
