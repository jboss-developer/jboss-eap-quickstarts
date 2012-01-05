package org.jboss.as.quickstarts.erraihelloworld.client.local;

import org.jboss.as.quickstarts.erraihelloworld.client.shared.HelloWorldResource;
import org.jboss.errai.bus.client.api.ErrorCallback;
import org.jboss.errai.bus.client.api.Message;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.jboss.errai.ioc.client.api.Caller;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;

/**
 * Companion class to the UI declared in {@linkplain HelloWorldClient.ui.xml}.
 * Handles events and updates the DOM in response.
 *
 * @author Jonathan Fuerth <jfuerth@redhat.com>
 * @author Christian Sadilek <csadilek@redhat.com>
 */
public class HelloWorldClient {

  interface MyUiBinder extends UiBinder<Panel, HelloWorldClient> {}
  private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

  @UiField Panel root;
  @UiField Label result;
  @UiField InputElement name;
  @UiField Button sayHelloButton;

  private final Caller<HelloWorldResource> helloWorldCaller;

  HelloWorldClient(Caller<HelloWorldResource> helloWorldCaller) {
    this.helloWorldCaller = helloWorldCaller;
    uiBinder.createAndBindUi(this);
    DOM.setElementAttribute(sayHelloButton.getElement(), "name", "sayHelloButton");
  }

  /**
   * Handles a click of the button by sending an AJAX request to the
   * HelloWorldResourceImpl and then updating the {@code result} label in response.
   *
   * @param e Details of the click event. Ignored by this handler.
   */
  @UiHandler("sayHelloButton")
  public void onButtonClick(ClickEvent e) {
    helloWorldCaller.call(new RemoteCallback<String>() {

      @Override
      public void callback(String name) {
        result.setText(name);
      }
    }, errorCallback).getHelloWorldJSON(name.getValue());
  }

  /**
   * Returns the DOM fragment that this
   * @return
   */
  public Panel getElement() {
    return root;
  }

  private ErrorCallback errorCallback = new ErrorCallback() {

    @Override
    public boolean error(Message message, Throwable throwable) {
      result.setText("Sorry, can't say hello now. " + throwable.getMessage());
      return false;
    }
  };

}
