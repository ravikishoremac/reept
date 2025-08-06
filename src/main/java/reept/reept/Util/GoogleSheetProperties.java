package reept.reept.Util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "google.sheet")
public class GoogleSheetProperties {
	
    private String userid;
    private String userrange;
    private String tasksid;
    private String tasksrange;
    private String credentialsFirst;
    private String credentialsSecond;
    private String credentialsThird;
    private String empid;
    private String emprange;
    
	public String getEmprange() {
		return emprange;
	}
	public void setEmprange(String emprange) {
		this.emprange = emprange;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUserrange() {
		return userrange;
	}
	public void setUserrange(String userrange) {
		this.userrange = userrange;
	}
	public String getTasksid() {
		return tasksid;
	}
	public void setTasksid(String tasksid) {
		this.tasksid = tasksid;
	}
	public String getTasksrange() {
		return tasksrange;
	}
	public void setTasksrange(String tasksrange) {
		this.tasksrange = tasksrange;
	}
	public String getCredentialsFirst() {
		return credentialsFirst;
	}
	public void setCredentialsFirst(String credentialsFirst) {
		this.credentialsFirst = credentialsFirst;
	}
	public String getCredentialsSecond() {
		return credentialsSecond;
	}
	public void setCredentialsSecond(String credentialsSecond) {
		this.credentialsSecond = credentialsSecond;
	}
	public String getEmpid() {
		return empid;
	}
	public void setEmpid(String empid) {
		this.empid = empid;
	}
	public String getCredentialsThird() {
		return credentialsThird;
	}
	public void setCredentialsThird(String credentialsThird) {
		this.credentialsThird = credentialsThird;
	}
	
	
	
	

  
}

