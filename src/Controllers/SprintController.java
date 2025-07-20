package Controllers;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import DTOs.Response;
import Model.Sprint;

@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class SprintController {

	@PersistenceContext(unitName = "mo")
	private EntityManager em;
	
	public Response endSprint(int sprintId) {
		//persisting first sprint
		if(sprintId == 1) {
			Sprint sprint =new Sprint();
			//sprint.setClosed(true);
			TypedQuery<String> query=em.createQuery("SELECT c.cardName FROM Card c",String.class);
			sprint.setPendingTasks(query.getResultList());	
			em.persist(sprint);
		}
		//ending old sprint
		Sprint oldSprint=em.find(Sprint.class, sprintId);
		if(oldSprint == null) {
			return new Response(false,"Invalid Sprint Id");
		}
		oldSprint.setClosed(true);
		//starting new sprint
		Sprint newSprint=new Sprint();
		TypedQuery<String> query2=em.createQuery("SELECT c.cardName FROM Card c WHERE c.done=:done",String.class);
		query2.setParameter("done", false);
		newSprint.setPendingTasks(query2.getResultList());
		em.merge(oldSprint);
		em.persist(newSprint);
		return new Response(true,"A New Sprint Has Started Successfully!");
	}
//	@GET
//	@Path("/{sprintId}/generateReport")
//	public String generateReport(@PathParam("sprintId") int sprintId) {
//	    Sprint loggedSprint = em.find(Sprint.class, sprintId);
//	    if(loggedSprint == null) {
//	    	return "[ERROR: Please Enter A Valid Sprint]";
//	    }
//	    StringBuilder log = new StringBuilder();
//	    log.append("Sprint ID: [").append(loggedSprint.getSprintId()).append("] ");
//	    log.append("Sprint Closed: [").append(loggedSprint.getClosed()).append("] ");
//	    // Assuming the list of names is accessible from loggedSprint
//	    List<String> tasks = loggedSprint.getPendingTasks();
//	    if (tasks != null && !tasks.isEmpty()) {
//	        log.append("List Of Tasks: [");
//	        for (String task : tasks) {
//	            log.append(" '").append(task).append("' ");
//	        }
//	        log.append("]");
//	    } else {
//	        log.append("No Tasks Found!");
//	    }
//
//	    return log.toString();
//	}
	
	
	public String generateReport(int sprintId) {
	    Sprint loggedSprint = em.find(Sprint.class, sprintId);
	    if(loggedSprint == null) {
	    	return "[ERROR: Please Enter A Valid Sprint]";
	    }
	    
	    String log="Sprint ID: ["+loggedSprint.getSprintId()+"] " + " Sprint Closed: ["+loggedSprint.getClosed()+"] ";
	   // log+=" Sprint Closed: ["+loggedSprint.getClosed()+"] ";
	    
	    // Assuming the list of names is accessible from loggedSprint
	    List<String> tasks = loggedSprint.getPendingTasks();
	    if (tasks != null && !tasks.isEmpty()) {
	    	log+="List Of Tasks: [";
	        
	        for (String task : tasks) {
	        	log+=" '"+task+"' ";
	           
	        }
	        log+="]";
	    } else {
	        log+="No Tasks Found!";
	    }

	    return log;
	}
	
	
	
	
}
