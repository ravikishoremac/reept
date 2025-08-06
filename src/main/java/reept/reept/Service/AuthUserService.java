
package reept.reept.Service;

import java.util.List;
import java.util.Optional;

import reept.reept.Entity.Employees;
import reept.reept.Entity.User;




public interface AuthUserService{
	
    String signup(User user);       
    Optional<reept.reept.Entity.LoginResponse> login(String email, String role, String password); 
    User getUserByEmail(String email);
    List<Employees> getAllemployees();
    String requestOtp(String email);
    boolean verifyOtp(String email, String otp);
    boolean resetPassword(String email, String newPassword);
    User updateUserByEmail(String email, User updatedUser);
  
}
