package org.jboss.as.quickstarts.mgmt;

import java.net.InetAddress;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;

/**
 * <p>
 * This example shows you how you can use the JBoss Application Server
 * Management API to shutdown a running standalone server."
 * </p>
 * <p>
 * The Model description via the CLI will look like this
 * 
 * <pre>
 * [standalone@localhost:9999 /] :shutdown
 * {
 *   "outcome" => "success"
 * }
 * </pre>
 * 
 * </p>
 * 
 * @author Serge Pagop (spagop@redhat.com)
 * 
 */
public class Client {

	public static void main(String[] args) throws Exception {
		ModelControllerClient client = ModelControllerClient.Factory.create(
				InetAddress.getByName("localhost"), 9999,
				DemoAuthentication.getCallbackHandler());
		try {
			ModelNode op = new ModelNode();
			op.get("operation").set("shutdown");
			ModelNode result = client.execute(op);
			if (result.hasDefined("outcome")
					&& "success".equals(result.get("outcome").asString())) {
				System.out.println();
				System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
				System.out.println("Server Shutdown status: " + result.get("outcome"));
				System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
				System.out.println();
				
			} else if (result.hasDefined("failure-description")) {
				throw new RuntimeException(result.get("failure-description")
						.toString());
			} else {
				throw new RuntimeException(
						"Operation not successful; outcome = "
								+ result.get("outcome"));
			}
		} finally {
			client.close();
		}
	}
}