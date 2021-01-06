package infoborden;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ListenerStarter implements Runnable, ExceptionListener {
	private String selector = "";
	private Infobord infobord;
	private Berichten berichten;

	public ListenerStarter() {
	}

	public ListenerStarter(String selector, Infobord infobord, Berichten berichten) {
		this.selector = selector;
		this.infobord = infobord;
		this.berichten = berichten;
	}

	public void run() {
		try {
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
					ActiveMQConnection.DEFAULT_BROKER_URL);
			Connection connection = connectionFactory.createConnection();
			connection.start();

			Session session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue(selector);
			MessageConsumer consumer = session.createConsumer(destination);
			consumer.setMessageListener(new QueueListener(selector, infobord, berichten));
			System.out.println("Produce, wait, consume" + selector);
		} catch (Exception e) {
			System.out.println("Caught: " + e);
			e.printStackTrace();
		}
	}

	public synchronized void onException(JMSException ex) {
		System.out.println("JMS Exception occured.  Shutting down client.");
	}
}