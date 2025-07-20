package Service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import Controllers.SprintController;
import DTOs.Response;

@Stateless
@Path("Sprint")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class SprintService {

	@EJB
	SprintController Controller;
	
	@POST
	@Path("/{sprintId}/endSprint")
	public Response endSprint(@PathParam("sprintId") int sprintId) {
		return Controller.endSprint(sprintId);
	}
	
	@GET
	@Path("/{sprintId}/generateReport")
	public String generateReport(@PathParam("sprintId") int sprintId) {
	    return Controller.generateReport(sprintId);
	}
	
}
