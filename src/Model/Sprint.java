package Model;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Stateless
@Entity
public class Sprint {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int sprintID;
	private boolean closed;
	@ElementCollection
	private List<String> pendingTasks = new ArrayList<>();
	
	public Sprint() {
		sprintID=0;
		closed=false;
	}
	// Setter for pendingTasks
    public void setPendingTasks(List<String> pendingTasks) {
        this.pendingTasks = pendingTasks;
    }

    // Getter for pendingTasks
    public List<String> getPendingTasks() {
        return pendingTasks;
    }
	
	public int getSprintId() {
		return sprintID;
	}
	public void setSprintId(int sprintId) {
		this.sprintID=sprintId;
	}
	public boolean getClosed() {
		return closed;
	}
	public void setClosed(boolean closed) {
		this.closed = closed;
	}
	
	
	
	
	
}
