/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.wshelloworld;

import org.jboss.as.quickstarts.wshelloworld.HelloWorldService;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import org.apache.cxf.configuration.jsse.SSLUtils;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.configuration.security.ProxyAuthorizationPolicy;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.cxf.transports.http.configuration.ProxyServerType;

/**
 * A Client stub to the HelloWorld JAX-WS Web Service.
 * 
 * @author lnewson@redhat.com
 * @author glamperi 
 * added changes to name to allow Client security usage - only acceptance of the server cert at this point
 */
public class ClientWS implements HelloWorldService {
    private HelloWorldService helloWorldService;

    /**
     * Default constructor
     * 
     * @param url The URL to the Hello World WSDL endpoint.
     */
    public ClientWS(final URL wsdlUrl) {
        QName serviceName = new QName("http://www.jboss.org/jbossas/quickstarts/wshelloworld/HelloWorld", "HelloWorldService");
 
        
        Service service = Service.create(wsdlUrl, serviceName);
        
        helloWorldService = service.getPort(HelloWorldService.class);
        
        Client client = ClientProxy.getClient(helloWorldService);
        HTTPConduit conduit = (HTTPConduit)client.getConduit();
       
        TLSClientParameters tcp = new TLSClientParameters();  
        tcp.setTrustManagers( new TrustManager[]{ new TrustAllX509TrustManager() } );  
        conduit.setTlsClientParameters( tcp );
        AuthorizationPolicy auth = conduit.getAuthorization();  
        if ( null == auth ) 
        {
            auth = new AuthorizationPolicy();  

        } 
        
        auth.setUserName( "local" );  
        auth.setPassword( "local" );  

        assert (helloWorldService != null);
    }
    
    /**
     * Default constructor
     * 
     * @param url The URL to the Hello World WSDL endpoint.
     * @throws MalformedURLException if the WSDL url is malformed.
     */
    public ClientWS(final String url) throws MalformedURLException {

        this(new URL(url));
    }

    /**
     * Gets the Web Service to say hello
     */
    @Override
    public String sayHello() {
        return helloWorldService.sayHello();
    }

    /**
     * Gets the Web Service to say hello to someone
     */
    @Override
    public String sayHelloToName(final String name) {
        return helloWorldService.sayHelloToName(name);
    }

    /**
     * Gets the Web Service to say hello to a group of people
     */
    @Override
    public String sayHelloToNames(final List<String> names) {
        return helloWorldService.sayHelloToNames(names);
    }
    
    public static class TrustAllX509TrustManager implements X509TrustManager
    {
    /** Empty array of certificate authority certificates. */
        private static final X509Certificate[] acceptedIssuers = new X509Certificate[]{ };

  
    /**
     * Return an empty array of certificate authority certificates which are trusted for authenticating peers.
     *
     * @return a empty array of issuer certificates.
     */
        public X509Certificate[] getAcceptedIssuers()
        {
            return ( acceptedIssuers );
        }

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            // TODO Auto-generated method stub
        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            // TODO Auto-generated method stub
        }
    }
}
