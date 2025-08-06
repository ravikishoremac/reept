package reept.reept.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reept.reept.Entity.Task;
import reept.reept.Service.EmployeeService;


@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	@GetMapping("/tasks/{email}")
	public List<Task> getTasksByEmployeeName(@PathVariable String email) {
        return employeeService.TasksByempName(email);
    }
	
	 @GetMapping("/getTask/{id}")
		public Object getTaskById(@PathVariable Long id) {
		  Task task = employeeService.getTaskById(id);	  
		  if (task == null) { 		  
			  return "task not found with id: "+id; 		  
			  }	  
		  return task; 	  
		  }
	 
	 @PutMapping("/updateTask/{id}")
		public String updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
			return employeeService.update(id, updatedTask);
		}
		 
}
