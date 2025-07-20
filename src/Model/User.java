package Model;


import javax.ejb.Stateless;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Stateless
@Entity
public class User {
@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String username;
//	@NotNull
//	@Column(unique=true)
	private String email;
	private String password;
	private Boolean leader;
//	@OneToMany(mappedBy="creator")
//	private List<Board> boards;
	@Transient
	@OneToOne(mappedBy="collabingUser")
	private Collaborator collabUser;
	//default constructor for User
	public User() {
		id=0;
		username="N/A";
		email="N/A";
		password="N/A";
		leader=false;
		
	}
	 // Setter method
    public void setCollabUser(Collaborator collabUser) {
        this.collabUser = collabUser;
    }

    // Getter method
    public Collaborator getCollabUser() {
        return this.collabUser;
    }
	
	 // Getter and setter for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and setter for username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter and setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and setter for password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public Boolean getLeader() {
    	return leader;
    }
    
    public void setLeader(Boolean flag) {
    	this.leader=flag;
    }

    
    
//    public boolean register(String mail,String pass) {
//    		
//      	return false;
//    }






}
	
	
	
	
	
	

