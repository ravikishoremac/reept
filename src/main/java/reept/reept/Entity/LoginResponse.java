package reept.reept.Entity;

public class LoginResponse {
	
	private String message;
    private String redirectUrl;
    private String role;
 
    public LoginResponse(String message, String redirectUrl, String role) {
		super();
		this.message = message;
		this.redirectUrl = redirectUrl;
		this.role = role;		
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getRedirectUrl() {
		return redirectUrl;
	}
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

   
   
	  
		
		  
		 
	  

}
