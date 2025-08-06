
package reept.reept.Controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reept.reept.Entity.Employees;
import reept.reept.Entity.LoginRequest;
import reept.reept.Entity.LoginResponse;
import reept.reept.Entity.User;
import reept.reept.Service.AuthUserService;




@RestController
@RequestMapping("/api/users")
public class AuthController {
    @Autowired
    private AuthUserService userService;
    

    @PostMapping("/signup")
    public String signup(@RequestBody User user) {
        return userService.signup(user);
    }

    @GetMapping("/by-email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<LoginResponse> response = userService.login(
            loginRequest.getEmail(),
            loginRequest.getRole(),
            loginRequest.getPassword()
        );

        if (response.isPresent()) {
            return ResponseEntity.ok(response.get());
        } else {
            return ResponseEntity.status(401).body("Invalid email, role, or password");
        }
    }
   
     

    @GetMapping("/employees")
    public List<Employees> getAllemployees() {
        return userService.getAllemployees();
    }

    @PostMapping("/request-otp")
    public ResponseEntity<?> requestOtp(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        if (!email.endsWith("@resustainability.com")) {
            return ResponseEntity.status(400).body("Please use a valid company email");
        }
        String result = userService.requestOtp(email);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String otp = loginRequest.getPassword(); // Using password field to pass OTP
        if (userService.verifyOtp(email, otp)) {
            return ResponseEntity.ok("OTP verified successfully");
        } else {
            return ResponseEntity.status(401).body("Invalid OTP");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String newPassword = loginRequest.getPassword();
        if (newPassword.length() < 8) {
            return ResponseEntity.status(400).body("Password must be at least 8 characters long");
        }
        if (userService.resetPassword(email, newPassword)) {
            return ResponseEntity.ok("Password reset successful");
        } else {
            return ResponseEntity.status(400).body("Failed to reset password");
        }
    }
    

    @PutMapping("/update-by-email/{email}")
    public ResponseEntity<?> updateUserByEmail(@PathVariable String email, @RequestBody User updatedUser) {
        User user = userService.updateUserByEmail(email, updatedUser);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(404).body("User not found with email: " + email);
        }
    }

   
}


