package org.jboss.as.quickstarts.basic;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;

public class KeystorePasswordCallback implements CallbackHandler {

	private Map<String, String> passwords = new HashMap<String, String>();

	public KeystorePasswordCallback() {
		passwords.put("server", "changeit");
		passwords.put("client", "changeit");
	}

	@Override
	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {
		for (int i = 0; i < callbacks.length; i++) {
			WSPasswordCallback pc = (WSPasswordCallback) callbacks[i];

			String pass = passwords.get(pc.getIdentifier());
			if (pass != null) {
				pc.setPassword(pass);
				return;
			}
		}

	}

	public void setAliasPassword(String alias, String password) {
		passwords.put(alias, password);
	}
}
