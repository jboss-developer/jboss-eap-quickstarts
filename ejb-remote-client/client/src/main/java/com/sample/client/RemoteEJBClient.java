package com.sample.client;

import javax.naming.*;

import com.sample.ejb.SampleBeanRemote;
import com.sample.ejb.SampleBeanRemoteImpl;

import java.util.*;


public class RemoteEJBClient {

	public static void main(String[] args) throws Exception {
		testRemoteEJB();

	}

	private static void testRemoteEJB() throws NamingException {

		final SampleBeanRemote ejb = lookupRemoteEJB();
		String s = ejb.echo("Frank");
		System.out.println(s);
	}

	private static SampleBeanRemote lookupRemoteEJB() throws NamingException {
		final Hashtable jndiProperties = new Hashtable();
		jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");

		final Context context = new InitialContext(jndiProperties);


		final String appName = "";
		final String moduleName = "jboss-as-ejb-remote-app";
		final String distinctName = "";
		final String beanName = SampleBeanRemoteImpl.class.getSimpleName();

		final String viewClassName = SampleBeanRemote.class.getName();
		System.out.println("Looking EJB via JNDI ");
		System.out.println("ejb:" + appName + "/" + moduleName + "/" + distinctName + "/" + beanName + "!" + viewClassName);

		return (SampleBeanRemote) context.lookup("ejb:" + appName + "/" + moduleName + "/" + distinctName + "/" + beanName + "!" + viewClassName);


	}

}
