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

import Controllers.BoardController;
import DTOs.Response;
import Model.Board;
import Model.Collaborator;

@Stateless
@Path("/Board")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BoardService {

	@EJB
	BoardController Controller;
	
	@POST
	@Path("create")
	  public Response createBoard(Board board) {
		return Controller.createBoard(board);
		}
	
	@GET
	@Path("/{id}/boards") //including id in the URL to only show the boards that this Team Leader has access to
	 public List<Board> getAllBoards(@PathParam("id") int creator) {
		return Controller.getAllBoards(creator);
    }
	
	@DELETE
	@Path("/{id}/{boardId}/delete")
	public Response deleteBoard(@PathParam("id") int id, @PathParam("boardId") int boardId) {
	    return Controller.deleteBoard(id, boardId);
	}
	
	@POST 
	@Path("/{id}/{guestId}/{boardId}/invite")
	public Response inviteToBoard(@PathParam("id") int leader,@PathParam("guestId") int collaborator,@PathParam("boardId") int boardId) {
		return Controller.inviteToBoard(leader, collaborator, boardId);
	}

	@GET
	@Path("collab")
	public List<Collaborator> getCollabs(){
		return Controller.getCollabs();
	}
}
