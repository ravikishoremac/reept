package reept.reept.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ViewController {

	
	@GetMapping("/ept/home/manager")
	  public String forwardManager() {
	    return "forward:/manager.html";
	  }

	  @GetMapping("/ept/home/employee")
	  public String forwardEmployee() {
	    return "forward:/employee.html";
	  }
	  
	  @GetMapping("/")
	    public String login() {
	        return "forward:/login.html"; 
	    }

	    @GetMapping("/ept/signup")
	    public String signup() {
	        return "forward:/signup.html";
	    }
	    
	    @GetMapping("/ept/emptasklist")
	    public String emptasklist() {
	    	return "forward:/empTaskList.html";
	    }
	    @GetMapping("/ept/taskListView")
	    public String showTaskListView() {
	        return "forward:/taskListView.html";
	    }
	    
	    @GetMapping("/ept/profile")
	    public String profile() {
			return "forward:/profile.html";
	    	
	    }
	    
	    @GetMapping("/ept/Pdashboard")
	    public String perormaceDashboard() {
	    	return "forward:/performanceDashboard.html";
	    }
	    
	    @GetMapping("/ept/storypoints")
	    public String storyPoints() {
	    	
	    	return "forward:/storypoints.html";
	    	
	    }
	    
	    @GetMapping("/ept/emplsDetails")
	    public String EmployeesDetails() {
	    	return "forward:/empDetails.html";
	    	
	    }
	    
	    @GetMapping("/ept/empscore")
	    public String Allempscore() {
	    	return "forward:/Allempscore.html";
	    	
	    }

}

