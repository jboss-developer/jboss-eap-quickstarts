package org.jboss.as.quickstarts.erraihelloworld.client.local;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.jboss.as.quickstarts.erraihelloworld.client.shared.HelloWorldResource;
import org.jboss.errai.ioc.client.api.Caller;
import org.jboss.errai.ioc.client.api.EntryPoint;

import com.google.gwt.user.client.ui.RootPanel;

/**
 * When the script is loaded in the web browser, the generated GWT code creates
 * an instance of this class, then calls its {@link #onModuleLoad()} method. From
 * there, we create the UI and add it to the DOM.
 *
 * @author Jonathan Fuerth <jfuerth@redhat.com>
 * @author Christian Sadilek <csadilek@redhat.com>
 */
@EntryPoint
public class HelloWorldApp {

  @Inject
  private Caller<HelloWorldResource> helloWorldCaller;

  @PostConstruct
  public void init() {
    RootPanel.get().add(new HelloWorldClient(helloWorldCaller).getElement());
  }

}
