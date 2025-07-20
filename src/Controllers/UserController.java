package Controllers;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import DTOs.Response;
import Model.User;

//(1)USER MANAGEMENT 
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {
	
@PersistenceContext(unitName = "mo")
	private EntityManager em;
	

public Response register(User user){
	String mail=user.getEmail();	
	if (!isEmailRegistered(mail)) { //kan mmkn a use: em.contains(user); ???->grbt mrdytsh (y3ny eh btshofo fil context wla la2 tab??
		em.persist(user);
		return new Response(true, "User registered successfully");
		}
	else {
		return new Response(false, "Email already registered");
		}
}

public Response login(User user) {
	String mail=user.getEmail();
	String pass=user.getPassword();
	try {
			TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email AND u.password = :password", User.class);
			query.setParameter("email", mail);
			query.setParameter("password", pass);
			user = query.getSingleResult();
			return new Response(true, "Login successful");
		} 
	catch (NoResultException e) {
			return new Response(false, "Invalid credentials");
}
}


public Response updateProfile(int id,User newUser) {
	String name=newUser.getUsername();
	String mail=newUser.getEmail();
	String pass=newUser.getPassword();
	Boolean lead=newUser.getLeader();
	User user=em.find(User.class, id);
	  if (user != null ) {
		  if(mail.equals(user.getEmail())||!isEmailRegistered(mail)) {
          	user.setUsername(name);
          	user.setEmail(mail);
          	user.setPassword(pass);
          	user.setLeader(lead);
          	
          	em.merge(user); //msh lmfrood merge ba update ah l data bs lw el entity f detached state???
          	return new Response(true, "Profile updated successfully");
          	}
		  	else {
			 return new Response(false,"Email already in use! Try another one");
		  	}
      } else {
          return new Response(false, "User not found");
      }
	
}
	
	
	
	 
	
//boolean method to detect while registeration if the email is already linked to another account in the database while trying to POST
	private boolean isEmailRegistered(String email) {
	    TypedQuery<Long> query = em.createQuery(
	            "SELECT COUNT(u.id) FROM User u WHERE u.email = :email", Long.class);
	    query.setParameter("email", email);
	    Long count = query.getSingleResult();
	    return count > 0;
	}

}
