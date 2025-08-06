package reept.reept.Service;

import java.util.List;

import reept.reept.Entity.Task;


public interface EmployeeService {
	
	List<Task> TasksByempName(String email);
	
	Task getTaskById(Long id);
	
	String update(Long id ,Task task);

}
