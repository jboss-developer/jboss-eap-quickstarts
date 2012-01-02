package org.jboss.as.quickstarts.mgmt;

import java.net.InetAddress;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;

/**
 * <p>
 * This example shows you how you can use the JBoss Application Server Management API to get the
 * Status of a standalone server."
 * </p>
 * <p> The Model description via the CLI will look like this
 * <pre>
 * [standalone@localhost:9999 /] :read-attribute(name=server-state)
 * {
 *   "outcome" => "success",
 *   "result" => "running"
 * }
 * </pre>
 * </p>
 * 
 * @author Serge Pagop (spagop@redhat.com)
 * 
 */
public class Client {
	public static void main(String[] args) throws Exception {
		ModelControllerClient client = ModelControllerClient.Factory.create(
				InetAddress.getByName("localhost"), 9999);
		try {
			ModelNode op = new ModelNode();
			op.get("operation").set("read-attribute");
			op.get("name").set("server-state");
			ModelNode result = client.execute(op);
			if (result.hasDefined("outcome")
					&& "success".equals(result.get("outcome").asString())) {
				if (result.hasDefined("result")) {
					System.out.println();
					System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
					System.out.println("&&&&&& Server status: " + result.get("result"));
					System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
					System.out.println();
				}
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