package reept.reept.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reept.reept.Dao.GoogleSheetsRepository;
import reept.reept.Entity.Employees;
import reept.reept.Entity.Task;
import reept.reept.Entity.User;



@Service
public class ManagerServiceImpl implements ManagerService {
	
	     @Autowired
	    private GoogleSheetsRepository googleSheetsRepository;
	     
	     @Autowired
	     private EmailService emailService;


//	     @Override
//	 	public String taskasign(Task task) {
//	 		 boolean success = googleSheetsRepository.saveTask(task);
//	 	        return success ? "task created and assigned successfully!" : "Failed to create and assign task.";
//	 	}
	     
	     @Override
	     public String taskasign(Task task) {    	 
	         boolean success = googleSheetsRepository.saveTask(task);

	         if (success) {

	        	 String subject = "New Task Assigned to You";

	        	 String body = "Dear " + task.getPerson() + ",\n\n"
	        	             + "A new task has been assigned to you. Please find the task details below:\n\n"
	        	             + "• Description : " + task.getDescription() + "\n"
	        	             + "• Priority    : " + task.getPriority() + "\n"
	        	             + "• Department  : " + task.getDepartment() + "\n"
	        	             + "• Start Date  : " + task.getStart_date() + "\n"
	        	             + "• End Date    : " + task.getEnd_date() + "\n\n"
	        	             + "Please log in to your dashboard to view more details.\n\n"
	        	             + "Best regards,\n"
	        	             + "Task Management System.\n\n"
	        	             + "Note: This is a system-generated message. Please do not reply.";


	             // Send email to employee
	             emailService.sendEmail(task.getPerson(), subject, body);
	         }

	         return success ? "Task created and assigned successfully!" : "Failed to create and assign task.";
	     }


	 	@Override
	 	public List<Task> getAllTasks() {
	 		List<Task> taksList = googleSheetsRepository.getAllTasks();
	 		return taksList;
	 	}

	 	@Override
	 	public List<User> getAllUsers() {
	 		List<User> usersList = googleSheetsRepository.getAllUsers();
	 		return usersList;
	 	}

	 	@Override
	 	public Task getTaskById(Long id) {
	 	    Optional<Task> task = googleSheetsRepository.findById(id);
	 	    return task.orElse(null); 
	 	}

//	 	@Override
//	 	public String update(Long id, Task updatedTask) {
//	 	    Optional<Task> existingTask = googleSheetsRepository.findById(id);
//	 	    if (existingTask.isEmpty()) {	        
//	 	    	return "Task with ID: " + id + "not found";
//	 	    }
//
//	 	    boolean isUpdated = googleSheetsRepository.updateById(id, updatedTask);
//	 	        return isUpdated ? "Task updated successfully." : "Failed to update task.";
//	 	}

	 	
	 	@Override
	 	public String update(Long id, Task updatedTask) {
	 	    Optional<Task> existingTask = googleSheetsRepository.findById(id);
//	 	    if (existingTask.isEmpty()) {
//	 	        return "Task with ID: " + id + " not found";
//	 	    }
	 	    
	 	   if (!existingTask.isPresent()) { // Java 8 compatible
	 	        return "Task with ID: " + id + " not found";
	 	    }

	 	    boolean isUpdated = googleSheetsRepository.updateById(id, updatedTask);

	 	    if (isUpdated) {
	 	        String subject = "Task Updated";
	 	        
	 	       String body = "Dear " + updatedTask.getPerson() + ",\n\n"
	 	              + "Your assigned task has been updated by the manager. Please find the updated details below:\n\n"
	 	              + "• Description: " + updatedTask.getDescription() + "\n"
	 	              + "• Status     : " + updatedTask.getStatus() + "\n\n"
	 	              + "Best regards,\n"
	 	              + "Task Management System.\n\n"
	 	              + "Note: This is a system-generated message. Please do not reply.";


	 	        // Notify employee of task update
	 	        emailService.sendEmail(updatedTask.getPerson(), subject, body);

	 	        return "Task updated successfully.";
	 	    }

	 	    return "Failed to update task.";
	 	}

	 	@Override
	 	public String delById(Long id) {
	 	    String result = googleSheetsRepository.deleteById(id);

	 	    if (result.toLowerCase().contains("success")) {
	 	        return "✅ Task deleted successfully.";
	 	    } else {
	 	        return "❌ Failed to delete task: " + result;
	 	    }

	 	}
	 	
	 	
	 	@Override
	 	public List<Employees> getAllEmployees() {
	 	    return googleSheetsRepository.allEmployees(); // assuming this returns List<Employees>
	 	}

	 	
	 	@Override
	 	public String delEmpByMail(String email) {
	 	    String result = googleSheetsRepository.deleteEmployeeFromBothSheets(email).toLowerCase();

	 	    boolean deletedFromUsers = result.contains("deleted from users.resustainability");
	 	    boolean deletedFromEmployees = result.contains("deleted from employees.resustainability");

	 	    if (deletedFromUsers && deletedFromEmployees) {
	 	        return "✅ Employee deleted successfully from both sheets.";
	 	    } else if (deletedFromUsers || deletedFromEmployees) {
	 	        return "⚠️ Partially deleted:\n" + result;
	 	    } else {
	 	        return "❌ Failed to delete Employee:\n" + result;
	 	    }
	 	}
	 	
	 	
	 	 @Override
	     public String addEmp(Employees employee) {
	         try {
	             Employees saved = googleSheetsRepository.saveEmployee(employee);
	             if (saved != null) {
	                 return "Employee saved successfully";
	             } else {
	                 return "Employee already exists";
	             }
	         } catch (Exception e) {
	             return "Failed to add employee";
	         }
	     }


	 	 
	 	@Override
		public List<Task> TasksBymanagerName(String email) {
			List<Task> tasks = googleSheetsRepository.TasksBymanagerName(email);
			return tasks;
		}


		@Override
		public List<User> getUsersByManagername(String email) {
			List<User> usersList = googleSheetsRepository.getUsersByManager(email);
			return usersList;
		}
}	
	

	

