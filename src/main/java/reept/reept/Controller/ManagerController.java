package reept.reept.Controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reept.reept.Entity.Employees;
import reept.reept.Entity.Task;
import reept.reept.Entity.User;
import reept.reept.Service.ManagerService;


@RestController
@RequestMapping("/api/manager")
public class ManagerController {


	@Autowired
	private ManagerService taskService;

	@PostMapping("/taskasign")
	public String taskasign(@RequestBody Task task) {
		return taskService.taskasign(task);
	}

	@GetMapping("/allTasks")
	public List<Task> getAllTasks() {
		return taskService.getAllTasks();
	}

	@GetMapping("/allUsers")
	public List<User> getAllUsers() {
		return taskService.getAllUsers();
	}

    @GetMapping("/getTask/{id}")
	public Object getTaskById(@PathVariable Long id) {
	  Task task = taskService.getTaskById(id);	  
	  if (task == null) { 		  
		  return "task not found with id: "+id; 		  
		  }	  
	  return task; 	  
	  }
	 
	@PutMapping("/updateTask/{id}")
	public String updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
		return taskService.update(id, updatedTask);
	}
	
	@DeleteMapping("/delete/{id}")
	public String deleteTaskById(@PathVariable Long id) {
	    return taskService.delById(id);
	}
	
//	@GetMapping("/manager-emails")
//	public List<String> getManagerEmails() {
//	    return (taskService.getAllEmployees().stream()
//	            .filter(emp -> emp.getRole() != null && emp.getRole().equalsIgnoreCase("manager"))
//	            .map(Employees::getEmail))
//	            .toList();
//	}
	
	@GetMapping("/manager-emails")
	public List<String> getManagerEmails() {
	    return taskService.getAllEmployees().stream()
	            .filter(emp -> emp.getRole() != null && emp.getRole().equalsIgnoreCase("manager"))
	            .map(Employees::getEmail)
	            .collect(Collectors.toList());
	}

//	@DeleteMapping("/deleteUser")
//	public String deleteEmployee(@RequestParam String email) {
//	    return taskService.delEmpByMail(email);
//	}
	
//	@DeleteMapping("/deleteUser/{email}")
//	public ResponseEntity<Map<String, String>> deleteEmployee(@PathVariable String email) {
//	    String message = taskService.delEmpByMail(email);
//	    Map<String, String> response = new HashMap<>();
//	    response.put("message", message);
//	    return ResponseEntity.ok(response);
//	}

	@DeleteMapping("/deleteUser/{email}")
	public ResponseEntity<String> deleteEmployee(@PathVariable String email) {
	    String message = taskService.delEmpByMail(email); // backend does the actual delete logic
	    return ResponseEntity.ok(message); // just return plain text
	}


	 @PostMapping("/addEmployee")
	    public String addEmployee(@RequestBody Employees employee) {
	        return taskService.addEmp(employee);
	    }

	 @GetMapping("/tasks/{email}")
		public List<Task> getTasksByManagerName(@PathVariable String email) {
	        return taskService.TasksBymanagerName(email);
	    }
	 
	 @GetMapping("/employees/{email}")
	    public List<User> getEmployeesByManagerEmail(@PathVariable String email) {
	        return taskService.getUsersByManagername(email);
	    }
	
}
