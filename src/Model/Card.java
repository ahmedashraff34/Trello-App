package Model;
import javax.ejb.Stateless;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import java.util.ArrayList;
import java.util.List;

@Stateless
@Entity
public class Card {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int cardID;
    private String cardName;
    private String description;
    private String comments;
    private Boolean done;
    @ManyToOne
    @JoinColumn(name="cardList")
    private Lists cardList;
    @ManyToOne
    @JoinColumn(name="assignee")
    private Collaborator assignee;

    // New field for commenters
    
    //@ElementCollection
    @Transient
    private List<Integer> commenters= new ArrayList<>();

    public Card() {
        cardID=0;
        cardName="N/A";
        description="N/A";
        comments="N/A";
        done=false;
        cardList=null;
        assignee=null;
        // Initialize the commenters list
        commenters = null;
    }
    public boolean allCommenters(int id) {
    	return commenters.contains(id);
    }
 public void gatherCommenters(int id) {
	  if (commenters == null) {
	        commenters = new ArrayList<>();
	    }
    	if(!commenters.contains(id)) {
    		commenters.add(id);
    	}
    }
    // Getters and setters for all fields
   
    public int getCardID() {
        return cardID;
    }

    public void setCardID(int cardID) {
        this.cardID = cardID;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Lists getCardList() {
        return cardList;
    }

    public void setCardList(Lists cardList) {
        this.cardList = cardList;
    }

    public Collaborator getAssignee() {
        return assignee;
    }

    public void setAssignee(Collaborator assignee) {
        this.assignee = assignee;
    }

    public List<Integer> getCommenters() {
        return commenters;
    }

    public void setCommenters(List<Integer> commentors) {
        this.commenters = commentors;
    }
	public Boolean getDone() {
		return done;
	}
	public void setDone(Boolean done) {
		this.done = done;
	}
}
