package Model;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Stateless
@Entity
public class Board {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
		private int boardID;
		private String boardName;
		//@ManyToOne
		//@JoinColumn(name="creatorId")
		
	    private int creatorId;	//foreign key to User.id
	    //@Transient
	    @OneToMany(mappedBy = "collabBoard")
	    private static List<Collaborator> collaborators;
	    //@Transient
	    @OneToMany(mappedBy = "board")//,cascade = CascadeType.ALL
	    private static List<Lists> lists;
 
	    
	    
	//Default constructor for boards
	public Board() {
		boardID=0;
		boardName="N/A";
		creatorId=0;
		//lists=null;
	}

	  // Getter and setter for boardID
    public int getBoardID() {
        return boardID;
    }

    public void setBoardID(int boardID) {
        this.boardID = boardID;
    }

    // Getter and setter for boardName
    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    // Getter and setter for creatorId
    public int getCreatorId() {
        return creatorId;
    }
    
    public void setCreatorId(int creator) {
    	this.creatorId=creator;
    }
    ///////////////////////////////////////////////
    public List<Lists> getLists() {
        return lists;
    }

    public void setLists(List<Lists> lists) {
        Board.lists = lists;
    }
   
//    public void addToList(Board b,Lists l) {
//	l.setBoard(b);
//	b.lists.add(l);
//}

/////////////////////////////
//	public void removeFromList(Board b,Lists l,EntityManager em) {
//	b.lists.remove(l);
//	em.merge(b);
//}


    
    
}