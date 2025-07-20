package messaging;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Stateless
public class Event implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int eventID;
    private static final long serialVersionUID = 1L;
	private String messageId;
    private String eventName;
    private String taskUpdate;
    private int eventCard;
    // Constructors, getters, and setters
    
    
    public Event() {}
    public Event(String messageId, String eventName,String taskUpdate,int eventCard) {
        this.messageId = messageId;
        this.eventName = eventName;
        this.taskUpdate = taskUpdate;
        this.eventCard = eventCard;
    }

    // Getters and setters for each property
    public int getEventCard() {
    	return eventCard;
    }
    
    public void setEventCard(int eventCard) {
    	this.eventCard = eventCard;
    }
    public String getTaskUpdate() {
    	return taskUpdate;
    }
    
    public void setTaskUpdate(String taskUpdate) {
    	this.taskUpdate = taskUpdate;
    }
    
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    
    @Override
    public String toString() {
        return 
                "messageID:['" + messageId+"'] " +
                ", Event: ['" + eventName + "'] " +
                ", Task Updates: ['" +  taskUpdate +"'] "
                ;
    }
}

