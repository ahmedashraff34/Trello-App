package Service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import Controllers.ListController;
import DTOs.Response;
import Model.Lists;

@Stateless
@Path("/List")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class ListService {

	@EJB
	ListController Controller;
	
	@POST
	@Path("/{id}/{boardId}/add")
	public Response addList(@PathParam("id") int id,@PathParam("boardId") int boardId,Lists list) {
		return Controller.addList(id, boardId, list);
	}
	
	@GET
	@Path("lists")
	public List<Lists> getAllLists(){
		 return Controller.getAllLists();
	}
	
	@DELETE
	@Path("/{id}/{boardId}/{listId}/remove")
	public Response removeList(@PathParam("id") int id,@PathParam("boardId") int boardId,@PathParam("listId") int listId) {
		return Controller.removeList(id, boardId, listId);
	}
	
	
}
