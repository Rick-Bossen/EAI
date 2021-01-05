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

			//Creating a non transactional session to send/receive JMS message.
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			//Destination represents here our queue 'JCG_QUEUE' on the JMS server.
			//The queue will be created automatically on the server.
			Destination destination = session.createQueue(subject);

			// MessageConsumer is used for receiving (consuming) messages
			MessageConsumer consumer = session.createConsumer(destination);

			// Here we receive the message.
			Message message = consumer.receive();

			// We will be using TestMessage in our example. MessageProducer sent us a TextMessage
			// so we must cast to it to get access to its .getText() method.
			if (message instanceof TextMessage) {
				TextMessage textMessage = (TextMessage) message;
				System.out.println("Received message '" + textMessage.getText() + "'");
			}
			connection.close();

//			TODO maak de connection aan
//          Connection connection = ?????;
//          connection.start();
//          connection.setExceptionListener(this);
//			TODO maak de session aan
//          Session session = ?????;
//			TODO maak de destination aan
//          Destination destination = ?????;
//			TODO maak de consumer aan
//          MessageConsumer consumer = ?????;
			System.out.println("Produce, wait, consume" + selector);
//			TODO maak de Listener aan
//          consumer.?????;
		} catch (Exception e) {
			System.out.println("Caught: " + e);
			e.printStackTrace();
		}
	}

	public synchronized void onException(JMSException ex) {
		System.out.println("JMS Exception occured.  Shutting down client.");
	}
}