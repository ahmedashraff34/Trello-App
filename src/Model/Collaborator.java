package Model;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Stateless
@Entity
public class Collaborator {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	int collaboratorNum;
	@OneToOne
	@JoinColumn(name="collabingUser")
	User collabingUser;
	
	@ManyToOne
	@JoinColumn(name="collabBoard")
	Board collabBoard;
	
	@Transient
	@OneToMany(mappedBy = "assignee")
    private static List<Card> assignedCards;
	
	
	
	
	//int UserNum;
	//int BoardNum;
	//Default Constructor
	public Collaborator(){
		collaboratorNum=0;
		collabingUser=null;
		collabBoard=null;
		//UserNum=0;
		//BoardNum=0;	
	}
		//Setter methods
		public void setCollaboratorNum(int collaboratorNum) {
	        this.collaboratorNum = collaboratorNum;
	    }

//	    public void setUserNum(int userNum) {
//	        this.UserNum = userNum;
//	    }
//
//	    public void setBoardNum(int boardNum) {
//	        this.BoardNum = boardNum;
//	    }
		public void setCollabingUser(User collabingUser) {
	        this.collabingUser = collabingUser;
	    }

	    public void setCollabBoard(Board collabBoard) {
	        this.collabBoard = collabBoard;
	    }

	    // Getter methods
	    public User getCollabingUser() {
	        return this.collabingUser;
	    }

	    public Board getCollabBoard() {
	        return this.collabBoard;
	    }
	    // Getter methods
	    public int getCollaboratorNum() {
	        return this.collaboratorNum;
	    }

//	    public int getUserNum() {
//	        return this.UserNum;
//	    }
//
//	    public int getBoardNum() {
//	        return this.BoardNum;
//	    }
}
