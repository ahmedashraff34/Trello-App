package Controllers;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import DTOs.Response;
import Model.Board;
import Model.Collaborator;
import Model.User;
import javax.persistence.Query;

//(2)BOARD MANAGEMENT 
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BoardController {

	@PersistenceContext(unitName = "mo")
		private EntityManager em;
		
	
	  public Response createBoard(Board board) {
		String name=board.getBoardName();
		int creatorid=board.getCreatorId();
		
		if (!isTeamLeader(creatorid)) {
			//return new Response(false,"Only team leaders can create boards");
			throw new WebApplicationException("You do not have permission to create boards", 403);
			}
		else {
			board.setBoardName(name);
			board.setCreatorId(creatorid);
			em.persist(board);
			return new Response(true,"Board "+name+" Created Succesfully!");
			}
		}
	
	
	 public List<Board> getAllBoards(int creator) {
		if (!isTeamLeader(creator)) {
			throw new WebApplicationException("You do not have permission to access this resource", 403);
		}
		else {
			TypedQuery<Board> query = em.createQuery("SELECT b FROM Board b WHERE b.creatorId = :id", Board.class);
			query.setParameter("id", creator);
			return query.getResultList();
        	}
    }
	

	public Response deleteBoard(int id, int boardId) {
	    if (!isTeamLeader(id)) {
	        throw new WebApplicationException("You do not have permission to access this resource", 403);
	    } else {
	        Board board = em.find(Board.class, boardId);
	        if (board != null && board.getCreatorId() == id) {
	        	
	        		// Remove Cards associated with Lists of this Board
                	Query query3 = em.createQuery("DELETE FROM Card WHERE cardList IN (SELECT l FROM Lists l WHERE l.board = :board)");
                	query3.setParameter("board", board);
                	query3.executeUpdate();
	                // Remove lists associated with the board
	                Query query = em.createQuery("DELETE FROM Lists WHERE board = :board");
	                query.setParameter("board", board);
	                query.executeUpdate();
	                
	                //Remove Collaborators associated with the board
	                Query query2 = em.createQuery("DELETE FROM Collaborator WHERE collabBoard = :board");
	                query2.setParameter("board",board);
	                query2.executeUpdate();
	          
	                
	                // Remove the board itself
	                em.remove(board);
	                return new Response(true, "Board " + board.getBoardName() + " Deleted Successfully!");
	            }
	        else {
	            return new Response(false, "Board not found or you do not have permission to delete it.");
	        }
	    }
	}

	
	
	public Response inviteToBoard(int leader,int collaborator,int boardId) {
		if (!isTeamLeader(leader)) {
	        throw new WebApplicationException("You do not have permission to access this resource", 403);
	    } 
		else {
	        Board board = em.find(Board.class, boardId);
	       
	        if (board != null && board.getCreatorId() == leader) {
	        	User invitedUser = em.find(User.class, collaborator);
	        	
	        	if(invitedUser != null) {	
	        		Collaborator guest=new Collaborator();
	        		guest.setCollabingUser(invitedUser);
	        		guest.setCollabBoard(board);
	        			if(!isAssigned(invitedUser)) {
	        				em.persist(guest);
	        				return new Response(true, "User " + invitedUser.getUsername() + " Was Invited To Board "+board.getBoardName()+" Successfully!");
	        			}
	        			else {
	        				return new Response(false,"User "+invitedUser.getUsername()+" Already Got Sent An Invitation to "+board.getBoardName());
	        			}
	            }
	        	else {
	        		return new Response(false,"User Not Found!");
	        	}
	        }
	        else {
	            return new Response(false, "Board Not Found In Your Created Boards!");
	        }
	    }
	}

	
	public List<Collaborator> getCollabs(){
		TypedQuery<Collaborator> query=em.createQuery("SELECT c FROM Collaborator c", Collaborator.class);
		 return query.getResultList();
	}
	
	
   	private boolean isTeamLeader(int userId) {
   		TypedQuery<Boolean> query = em.createQuery("SELECT u.leader FROM User u WHERE u.id = :userId", Boolean.class);
   		query.setParameter("userId", userId);
        try {
        	return query.getSingleResult();
        	}
        catch(NoResultException e){		//user not found
        	return false;
        }
	}
   	private boolean isAssigned(User assignee) {
   	 TypedQuery<Long> query = em.createQuery("SELECT COUNT(c.collaboratorNum) FROM Collaborator c WHERE  c.collabingUser= :assignee", Long.class);
	    query.setParameter("assignee", assignee);
	    Long count = query.getSingleResult();
	    return count > 0;
   	}
	
	

	
}