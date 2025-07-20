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
import javax.persistence.Transient;

@Stateless
@Entity
public class Lists {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int listID;
	private String listName;
	@ManyToOne
	@JoinColumn(name="board")
	private Board board;
	
	@Transient
	@OneToMany(mappedBy = "cardList")
    private static List<Card> cards;
	public Lists() {
		listID=0;
		listName="N/A";
		board=null;
	}
	public Lists(String name, Board board) {
        this.listName = name;
        this.board = board;
    }

    // Getters and setters

    public int getListID() {
        return listID;
    }

    public void setListID(int id) {
        this.listID = id;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String name) {
        this.listName = name;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
	
}
