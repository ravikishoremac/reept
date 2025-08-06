package reept.reept.Service;

import java.util.List;

import reept.reept.Entity.Employees;
import reept.reept.Entity.Task;
import reept.reept.Entity.User;




public interface ManagerService {
	
	String taskasign(Task task);
	
	List<Task> getAllTasks();
	
	List<User> getAllUsers();
	
	Task getTaskById(Long id);
	
	String update(Long id ,Task task);
	
	String delById(Long id);
	
	List<Employees> getAllEmployees();
	
	String delEmpByMail(String email);
	
	String addEmp(Employees employee);
	
	List<Task> TasksBymanagerName(String email);
	
	List<User> getUsersByManagername(String email);
	
}
