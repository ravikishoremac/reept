package reept.reept.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reept.reept.Dao.GoogleSheetsRepository;
import reept.reept.Entity.Task;
import reept.reept.Entity.User;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	
	    @Autowired
	    private GoogleSheetsRepository googleSheetsRepository;
	    
	    @Autowired
	    private EmailService emailService;


		@Override
		public List<Task> TasksByempName(String email) {
			List<Task> tasks = googleSheetsRepository.TasksByempName(email);
			return tasks;
		}
		
		@Override
	 	public Task getTaskById(Long id) {
	 	    Optional<Task> task = googleSheetsRepository.findById(id);
	 	    return task.orElse(null); 
	 	}
		
//		@Override
//	 	public String update(Long id, Task updatedTask) {
//	 	    Optional<Task> existingTask = googleSheetsRepository.findById(id);
//	 	    if (existingTask.isEmpty()) {	        
//	 	    	return "Task with ID: " + id + "not found";
//	 	    }
//
//	 	    boolean isUpdated = googleSheetsRepository.updateById(id, updatedTask);
//	 	        return isUpdated ? "Task updated successfully." : "Failed to update task.";
//	 	}
//		
//		@Override
//		public String update(Long id, Task updatedTask) {
//		    Optional<Task> existingTask = googleSheetsRepository.findById(id);
////		    if (existingTask.isEmpty()) {
////		        return "Task with ID: " + id + " not found";
////		    }
//		    if (!existingTask.isPresent()) {
//		        return "Task with ID: " + id + " not found";
//		    }
//
//		    boolean isUpdated = googleSheetsRepository.updateById(id, updatedTask);
//
//		    if (isUpdated && "Completion Request".equalsIgnoreCase(updatedTask.getStatus())) {
//		        String managerEmail = "ravikishoremac1234@gmail.com"; // hardcode or fetch dynamically
//		       // String subject = "Task Updated by " + updatedTask.getPerson();
//		        String personEmail = updatedTask.getPerson(); // This is the email stored in the task
//		        User employee = googleSheetsRepository.userByemail(personEmail); // Fetch user by email
//		        String employeeName = employee != null ? employee.getName() : personEmail; // fallback to email if not found
//
//		        String subject = "Task Updated by " + employeeName;
//
//		        String body = "Dear Manager,\n\n"
//		                    + "The following task has been updated by " + updatedTask.getPerson() + ". Please review the details below:\n\n"
//		                    + "• Task ID      : " + updatedTask.getId() + "\n"
//		                    + "• Task Name    : " + updatedTask.getDescription() + "\n"
//		                    + "• Department   : " + updatedTask.getDepartment() + "\n"
//		                    + "• Priority     : " + updatedTask.getPriority() + "\n"
//		                    + "• Start Date   : " + updatedTask.getStart_date() + "\n"
//		                    + "• End Date     : " + updatedTask.getEnd_date() + "\n"
//		                    + "• Status       : " + updatedTask.getStatus().toUpperCase() + "\n\n"
//		                    + "Please log in to your dashboard to review the task.\n\n"
//		                    + "Best regards,\n"
//		                    + "Task Management System.\n\n"
//		                    + "Note: This is a system-generated message. Please do not reply.";
//
//
//		        // Notify manager
//		        emailService.sendEmail(managerEmail, subject, body);
//		    }
//
//		    return isUpdated ? "Task updated successfully." : "Failed to update task.";
//		}
//		

	
	//-----------Dynamically fetch manager Email For multiple managers--------------------
		  @Override
		 public String update(Long id, Task updatedTask) {
		      Optional<Task> existingTask = googleSheetsRepository.findById(id);

		 if (!existingTask.isPresent()) {
		          return "Task with ID: " + id + " not found";
		      }

		      boolean isUpdated = googleSheetsRepository.updateById(id, updatedTask);

		      // if (isUpdated && "Completion Request".equalsIgnoreCase(updatedTask.getStatus())) {
		      //     String personEmail = updatedTask.getPerson(); // employee email
		      //     User employee = googleSheetsRepository.userByemail(personEmail); // fetch user

		      //     if (employee != null) {
		      //        String employeeName = employee.getName();
		      //         String managerEmail = employee.getReportingto(); // dynamically get manager email

		      //         if (managerEmail != null && !managerEmail.isEmpty()) {
		      //             String subject = "Task Updated by " + employeeName;

		      //             String body = "<p>Dear Manager,</p>"
		      //                     + "<p>The following task has been updated by <span style='color:blue; font-weight: bold;'>" + updatedTask.getPerson() + "</span>. Please review the details below:</p>"
		      //                     + "<ul>"
		      //                     + "<li><strong>Task ID</strong>: " + updatedTask.getId() + "</li>"
		      //                     + "<li><strong>Task Name</strong>: " + updatedTask.getDescription() + "</li>"
		      //                     + "<li><strong>Department</strong>: " + updatedTask.getDepartment() + "</li>"
		      //                     + "<li><strong>Priority</strong>: " + updatedTask.getPriority() + "</li>"
		      //                     + "<li><strong>Start Date</strong>: " + updatedTask.getStart_date() + "</li>"
		      //                     + "<li><strong>End Date</strong>: " + updatedTask.getEnd_date() + "</li>"
		      //                     + "<li><strong>Request Date</strong>: " + updatedTask.getRequest_date() + "</li>"
		      //                     + "<li><strong>Status</strong>: " + updatedTask.getStatus().toUpperCase() + "</li>"
		      //                     + "</ul>"
		      //                     + "<p>Please login to your dashboard to review the task.</p>"
		      //                     + "<p>Best regards,<br>Task Management System.</p>"
		      //                    + "<p><i>Note: This is a system-generated message. Please do not reply.</i></p>";

		      //             emailService.sendEmail(managerEmail, subject, body);
		      //         }
		      //     }
		      // }

		      return isUpdated ? "Task updated successfully." : "Failed to update task.";
		  }

	
}
