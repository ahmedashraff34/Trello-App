package Controllers;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import DTOs.Response;
import Model.Board;
import Model.Lists;
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ListController {
	@PersistenceContext(unitName = "mo")
	private EntityManager em;
	
	
	public Response addList(int id,int boardId,Lists list) {
		if (!isTeamLeader(id)) {
			throw new WebApplicationException("You do not have permission to access this resource", 403);
		}
		else {
		//int boardId=list.getBoard().getBoardID();
		Board board = em.find(Board.class, boardId);
			//Board board=list.getBoard();
        if (board != null && board.getCreatorId()==id) {
        	//board.getLists().add(list);
            list.setBoard(board);
            em.persist(list); 
            return new Response(true,"List "+list.getListName()+" Added To Board "+board.getBoardName()+" Successfully!");
        }
        else {
            return new Response(false,"Board not found!");
        	}
		}
		
		
	}
	
	
	public List<Lists> getAllLists(){
		 TypedQuery<Lists> query=em.createQuery("SELECT l FROM Lists l", Lists.class);
		 return query.getResultList();
		
	}
	
	
	public Response removeList(int id,int boardId,int listId) {
		if (!isTeamLeader(id)) {
			throw new WebApplicationException("You do not have permission to access this resource", 403);
		}
		else {
		Board board = em.find(Board.class, boardId);
        if (board != null && board.getCreatorId()==id) {
        	Lists list=em.find(Lists.class, listId);
        	if(list !=null && list.getBoard()==board) {
                Query query = em.createQuery("DELETE FROM Card WHERE cardList = :list");
                query.setParameter("list", list);
                query.executeUpdate();
        		em.remove(list);
        		return new Response(true,"List "+list.getListName()+" Removed Successfully!");
        	}
        	else {
        		return new Response(false,"List not found!");
        	}
        }
        else {
            return new Response(false,"Board not found!");
        	}
		}
		
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
	

}
