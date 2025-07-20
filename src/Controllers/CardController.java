package Controllers;

import java.util.List;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import DTOs.Response;
import Model.Board;
import Model.Card;
import Model.Collaborator;
import Model.Lists;
import Model.User;
import messaging.JMSClient;
import messaging.Event;


@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CardController {

	@PersistenceContext(unitName = "mo")
	private EntityManager em;
	
	@Inject
	JMSClient jmsUtil;
	
	//Add Card id:lazem yb2a collaborator, lma collaborator by create card w lsa m7d4 3mlha assign l7d byt7t howa as el assignee

	public Response addCard(int id,int boardId,int listId,Card card) {
		
		if(!isCollaborator(id)) {
			throw new WebApplicationException("You do not have permission to access this resource", 403);
		}
		else {
			Board board = em.find(Board.class, boardId);
			Collaborator cola=getCollaborator(id);
			
	        if (board != null && cola.getCollabBoard() == board) {
	        	Lists list=em.find(Lists.class, listId);
	        	///////////////////////////
	        	if(list !=null && list.getBoard() == board) {
	        		String edit="["+cola.getCollabingUser().getUsername()+"]: "+card.getComments();
	        		card.setComments(edit);
	        		card.setCardList(list);
	        		card.setAssignee(cola);
	        		card.gatherCommenters(id);
	        		em.persist(card);
	        		return new Response(true,"Card '"+card.getCardName()+"' Was Created Successfully By "+cola.getCollabingUser().getUsername());
	        	}
	        	///////////////////////////
	        	else {
	        		return new Response(false,"List not found!");
	        	}
	        }
	        
	        else {
	            return new Response(false,"Board not found!");
	        	}
		}
			
	}
	//move cards between lists.
	
	public Response moveCard(int id,int boardId,int oldListId,int newListId,int cardId ) {
		if(!isCollaborator(id)) {
			throw new WebApplicationException("You do not have permission to access this resource", 403);
		}
		else {
			Board board = em.find(Board.class, boardId);
			Collaborator cola=getCollaborator(id);
			
	        if (board != null && cola.getCollabBoard() == board) {
	        	///////////////////////////
	        	Lists list1=em.find(Lists.class, oldListId);
	        	Lists list2=em.find(Lists.class, newListId);
	        	
	        	if(list1 !=null && list2 !=null && list1.getBoard()== board && list2.getBoard()==board) {
	        		Card card = em.find(Card.class, cardId);
	        		if(card != null && card.getCardList()==list1) {
	        			
	        			if(list2.getListName().equals("Done")) {
	        				card.setDone(true);
	        			}
	        			Query query= em.createQuery("UPDATE Card c SET c.cardList = :newList WHERE c.cardID IN :cardId");
		            	 query.setParameter("newList", list2);
	                     query.setParameter("cardId", cardId);
	                     query.executeUpdate();
	                     em.merge(list1);
	                     em.merge(list2);
	                     em.merge(card);
	                     //JMS?
	                     Event event = new Event();
	                     event.setEventCard(cardId);
	                     event.setMessageId(UUID.randomUUID().toString());
	                     event.setEventName("Moving Cards Between Lists");
	                     event.setTaskUpdate("Card '" + card.getCardName() + "' has been moved from list "+list1.getListName() +" to list "+ list2.getListName());
	                     em.persist(event);
	                     jmsUtil.sendMessage(event);
	                     //
	                     return new Response(true,"Card '"+card.getCardName()+"' Was Moved Successfully To List '"+list2.getListName()+"' By "+cola.getCollabingUser().getUsername());
	        		}
	        		else {
	        			return new Response(false,"Card Not Found In List '"+list1.getListName()+"' !");
	        		}
	        	}
	        	///////////////////////////
	        	else {
	        		return new Response(false,"A List Wasn't Found!");
	        	}
	        }
	        
	        else {
	            return new Response(false,"Board not found!");
	        	}
		}
	}
	//assign cards (to themselves or others)

	public Response assignCard(int id,int boardId,int listId,int assignedId,int cardId) {
		if(!isCollaborator(id)) {
			throw new WebApplicationException("You do not have permission to access this resource", 403);
		}
		else if(!isCollaborator(assignedId)){
			
			return new Response(false,"User You Want To Assign This Card To, Isn't A Collaborator!");
		}
		else {
			Board board = em.find(Board.class, boardId);
			Collaborator cola=getCollaborator(id);
			Collaborator assignedCola=getCollaborator(assignedId);
	        if (board != null && cola.getCollabBoard() == board && assignedCola.getCollabBoard() == board) {
	        	Lists list=em.find(Lists.class, listId);
	        	
	        	if(list !=null && list.getBoard() == board) {
	        		Card card=em.find(Card.class, cardId);
	        		if(card != null && card.getCardList()==list){
	        			
	        			Query query= em.createQuery("UPDATE Card c SET c.assignee = :assignedCola WHERE c.cardID IN :cardId");
		            	 query.setParameter("assignedCola", assignedCola);
	                     query.setParameter("cardId", cardId);
	                     query.executeUpdate();
	                     em.merge(card);	
	        	
	                   //JMS?
	                     Event event = new Event();
	                     event.setEventCard(cardId);
	                     event.setMessageId(UUID.randomUUID().toString());
	                     event.setEventName("Assigning Cards");
	                     event.setTaskUpdate("Card '"+card.getCardName()+"'Is Now Assigned To "+assignedCola.getCollabingUser().getUsername());
	                     em.persist(event);
	                     jmsUtil.sendMessage(event);
	                     //
	        			return new Response(true,"Card '"+card.getCardName()+"' Was Successfully Assigned To "+assignedCola.getCollabingUser().getUsername());
	        		}
	        		else {
	        			return new Response(false,"Card Not Found In List '"+list.getListName()+"' !");
	        		}
	        		
	        	}
	        	///////////////////////////
	        	else {
	        		return new Response(false,"List not found!");
	        	}
	        }
	        
	        else {
	            return new Response(false,"Board not found!");
	        	}
		}
		
	}
	
	//Add Description/Comments on cards
	//append comments aw stack it fo2 b3dha w replace l description
	public Response updateCardDetails(int id,int boardId,int listId,int cardId,Card newCard) {
		
		if(!isCollaborator(id)) {
			throw new WebApplicationException("You do not have permission to access this resource", 403);
		}
		else {
			
			Board board = em.find(Board.class, boardId);
			Collaborator cola=getCollaborator(id);
			
	        if (board != null && cola.getCollabBoard() == board) {
	        	Lists list=em.find(Lists.class, listId);
	        	///////////////////////////
	        	if(list !=null && list.getBoard() == board) {
	        		Card card=em.find(Card.class, cardId);
	        		if(card != null && card.getCardList()==list) {
	        			 String comm=card.getComments()+", ["+cola.getCollabingUser().getUsername()+"]: "+newCard.getComments();
	        			 String script=newCard.getDescription();
	        			 //update values of Card
	        			if(newCard.getComments() !="N/A") { 
	        			 card.setComments(comm);
	        			 
	        			 card.gatherCommenters(id);
	        			 }
	        			if(script !="N/A") { 
	        			 card.setDescription(script);
	        			 }
	        			 em.merge(card);
	        			//JMS?
	                     Event event = new Event();
	                     event.setEventCard(cardId);
	                     event.setMessageId(UUID.randomUUID().toString());
	                     event.setEventName("Adding Description/Comments On A Card");
	                     event.setTaskUpdate("Card '"+card.getCardName()+"' Details Got Updated By "+cola.getCollabingUser().getUsername());
	                     em.persist(event);
	                     jmsUtil.sendMessage(event);
	                     //
	                     return new Response(true,"Card '"+card.getCardName()+"' Details Was Updated Successfully!");
	        		}
	        		else {
	        			return new Response(false,"Card Not Found In List '"+list.getListName()+"' !");
	        		}
	        	}
	        	///////////////////////////
	        	else {
	        		return new Response(false,"List not found!");
	        	}
	        }
	        
	        else {
	            return new Response(false,"Board not found!");
	        	}
		}
			
	}
	

	
	public List<Event> getEvents(int id,int cardId) {
	    try {
	        // Query to fetch events associated with the specified user ID
	        List<Event> events = em.createQuery("SELECT e FROM Event e WHERE e.eventCard =: cardId", Event.class)
	        		.setParameter("cardId", cardId)
	                .getResultList();
	        
	        
	        Card card = em.find(Card.class, cardId);

	        if(card.getAssignee().getCollabingUser().getId()== id || card.allCommenters(id)) {
	 
	        	return events;
	        }
	        return null;
	        
	    }catch (Exception e) {
		        e.printStackTrace();
		        // Handle any exceptions
		        return null;
		        }
	}
	
	
	
	public List<Card> getCards(){
		TypedQuery<Card> query=em.createQuery("SELECT c FROM Card c", Card.class);
		 return query.getResultList();
	}
	
	private boolean isCollaborator(int id) {
		User collaber = em.find(User.class, id);
	   	 TypedQuery<Long> query = em.createQuery("SELECT COUNT(c.collaboratorNum) FROM Collaborator c WHERE  c.collabingUser= :collaber", Long.class);
		    query.setParameter("collaber", collaber);
		    Long count = query.getSingleResult();
		    return count > 0;
	   	}
	
	private Collaborator getCollaborator(int id) {
		User collaber = em.find(User.class, id);
	   	 TypedQuery<Collaborator> query = em.createQuery("SELECT c FROM Collaborator c WHERE  c.collabingUser= :collaber", Collaborator.class);
		    query.setParameter("collaber", collaber);
		    Collaborator cola = query.getSingleResult();
		    return cola;
	   	}
	
	
	
}
