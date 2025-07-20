package Service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import Controllers.UserController;
import DTOs.Response;
import Model.User;

@Stateless
@Path("/User")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserService {

	@EJB
	UserController Controller;
	
	@POST
	@Path("register")
	public Response register(User user){		
		return Controller.register(user);
	}
	
	@POST
	@Path("login")
	public Response login(User user) {
		return Controller.login(user);
	}
	
	
	@PUT
	@Path("/{id}/update")
	public Response updateProfile(@PathParam("id")int id,User newUser) {
		return Controller.updateProfile(id, newUser);
	}
	
	
		
	
}
