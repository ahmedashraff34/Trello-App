package Service;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import Controllers.CardController;
import DTOs.Response;
import Model.Card;
import messaging.Event;


@Stateless
@Path("Card")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class CardService {

	@EJB
	CardController Controller;
	
	@POST
	@Path("/{id}/{boardId}/{listId}/addCard")
	public Response addCard(@PathParam("id") int id,@PathParam("boardId") int boardId,@PathParam("listId") int listId,Card card) {
		return Controller.addCard(id, boardId, listId, card);
	}
	
	@PUT
	@Path("/{id}/{boardId}/{oldListId}/{newListId}/{cardId}/moveCard")
	public Response moveCard(@PathParam("id") int id,@PathParam("boardId") int boardId,@PathParam("oldListId") int oldListId,@PathParam("newListId") int newListId,@PathParam("cardId") int cardId ) {
		return Controller.moveCard(id, boardId, oldListId, newListId, cardId);
	}
	
	@PUT
	@Path("/{id}/{boardId}/{listId}/{cardId}/{assignedId}/assignCard") //	@Path("/{id}/{boardId}/{listId}/addCard")
	public Response assignCard(@PathParam("id") int id,@PathParam("boardId") int boardId,@PathParam("listId") int listId,@PathParam("assignedId") int assignedId,@PathParam("cardId") int cardId) {
		return Controller.assignCard(id, boardId, listId, assignedId, cardId);
	}
	
	@PUT
	@Path("/{id}/{boardId}/{listId}/{cardId}/updateDetails")
	public Response updateCardDetails(@PathParam("id") int id,@PathParam("boardId") int boardId,@PathParam("listId") int listId,@PathParam("cardId") int cardId,Card newCard) {
		return Controller.updateCardDetails(id, boardId, listId, cardId, newCard);	
	}
	
	@GET
	@Path("/{id}/{cardId}/retrieve")
	public List<Event> getEvents(@PathParam("id") int id,@PathParam("cardId") int cardId) {
	    return Controller.getEvents(id, cardId);
	}
	
	@GET
	@Path("getCards")
	public List<Card> getCards(){
		return Controller.getCards();
	}
	
	
	

	
}
