package reept.reept.Entity;

public class User {
		  
	  private Long id;
	  private String name;
	  private String email; 
	  private Object mobileNumber;
	  private String role;
	  private String department;
	  private String status;
	  private String password;
	  private String reportingto;
	  
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public User(){
	  
	  }
	  
	  
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Object getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(Object mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getDepartment() {
		return department;
	}


	public void setDepartment(String department) {
		this.department = department;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public String getReportingto() {
		return reportingto;
	}


	public void setReportingto(String reportingto) {
		this.reportingto = reportingto;
	}

}
