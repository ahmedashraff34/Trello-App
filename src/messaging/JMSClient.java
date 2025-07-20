package messaging;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;



@Startup
@Singleton
public class JMSClient{

    @Resource(mappedName = "java:/jms/queue/MyTrelloQueue")
    private Queue myTrelloQueue;

    @Inject
    JMSContext context;

    public void sendMessage(Event event) {
        JMSProducer producer = context.createProducer();
		ObjectMessage message = context.createObjectMessage(event);
		producer.send(myTrelloQueue, message);
		System.out.println("[New Event Sent] ");
    }
    }