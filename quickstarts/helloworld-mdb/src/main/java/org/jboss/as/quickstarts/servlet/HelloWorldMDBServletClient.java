package org.jboss.as.quickstarts.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * A simple servlet 3 as client that sends several messages to a queue.
 * </p>
 * 
 * <p>
 * The servlet is registered and mapped to /HelloWorldMDBServletClient using the {@linkplain WebServlet
 * @HttpServlet}.
 * </p>
 * 
 * @author Serge Pagop (spagop@redhat.com)
 * 
 */
@WebServlet("/HelloWorldMDBServletClient")
public class HelloWorldMDBServletClient extends HttpServlet {

	private static final long serialVersionUID = -8314035702649252239L;

	private static final Logger LOGGER = Logger
			.getLogger(HelloWorldMDBServletClient.class.toString());

	private static final int MSG_COUNT = 5;

	@Resource(mappedName = "java:/ConnectionFactory")
	private static ConnectionFactory connectionFactory;

	@Resource(mappedName = "java:/queue/test")
	private static Queue queue;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Connection connection = null;
		try {
			connection = connectionFactory.createConnection();
			Session session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			MessageProducer messageProducer = session.createProducer(queue);
			connection.start();

			TextMessage message = session.createTextMessage();

			for (int i = 0; i < MSG_COUNT; i++) {
				message.setText("This is message " + (i + 1));
				LOGGER.info("Sending message: " + message.getText());
				messageProducer.send(message);
			}

		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

}
