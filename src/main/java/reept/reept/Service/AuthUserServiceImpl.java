
package reept.reept.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import reept.reept.Dao.GoogleSheetsRepository;
import reept.reept.Entity.Employees;
import reept.reept.Entity.LoginResponse;
import reept.reept.Entity.User;





@Service
public class AuthUserServiceImpl implements AuthUserService {
    @Autowired
    private GoogleSheetsRepository googleSheetsRepository;

    @Autowired
    private EmailService emailService;
         

    private final ConcurrentHashMap<String, String> otpStore = new ConcurrentHashMap<>();
    private final Random random = new Random();
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

//    @Override
//    public String signup(User user) {
//        boolean success = googleSheetsRepository.saveUser(user);
//        return success ? "User signed up successfully!" : "Failed to sign up user.";
//    }
    
//    @Override
//    public String signup(User user) {
//        // Hash the password before saving
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        boolean success = googleSheetsRepository.saveUser(user);
//        return success ? "User signed up successfully!" : "Failed to sign up user.";
//    }
    
    @Override
    public String signup(User user) {
        // Check if user with the given email already exists
        User existingUser = getUserByEmail(user.getEmail());
        if (existingUser != null) {
            return "Email already registered. Please use a different email or log in.";
        }

        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        boolean success = googleSheetsRepository.saveUser(user);
        return success ? "User signed up successfully!" : "Failed to sign up user.";
    }
    

    @Override
    public Optional<LoginResponse> login(String email, String role, String password) {
        List<User> users = googleSheetsRepository.getAllUsers();

        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email.trim()) &&
                user.getRole().equalsIgnoreCase(role.trim()) &&
               // user.getPassword().equals(password)) {
                passwordEncoder.matches(password, user.getPassword())) {

                String userRole = user.getRole().toLowerCase();
                String redirectUrl = role.equals("manager") ? "/ept/home/manager" : "/ept/home/employee";

                return Optional.of(new LoginResponse("Login successful", redirectUrl, userRole));
            }
        }

        return Optional.empty();
    }
    
  

    @Override
    public User getUserByEmail(String email) {
        return googleSheetsRepository.userByemail(email);
    }

    @Override
    public List<Employees> getAllemployees() {
        return googleSheetsRepository.allEmployees();
    }

    @Override
    public String requestOtp(String email) {
        User user = googleSheetsRepository.userByemail(email);
        if (user == null) {
            return "User not found";
        }

        // Generate 4-digit OTP
        String otp = String.format("%04d", random.nextInt(10000));
        otpStore.put(email, otp);

        // Send OTP via email
        try {
            emailService.sendOtpEmail(email, otp);
            return "OTP sent to your email";
        } catch (Exception e) {
            System.err.println("Error sending OTP to " + email + ": " + e.getMessage());
            e.printStackTrace();
            return "Failed to send OTP";
        }
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        String storedOtp = otpStore.get(email);
        if (storedOtp != null && storedOtp.equals(otp)) {
            return true;
        }
        return false;
    }

//    @Override
//    public boolean resetPassword(String email, String newPassword) {
//        return googleSheetsRepository.updateUserPassword(email, newPassword);
//    }
    
    @Override
    public boolean resetPassword(String email, String newPassword) {
        // Hash the new password before updating
        String hashedPassword = passwordEncoder.encode(newPassword);
        return googleSheetsRepository.updateUserPassword(email, hashedPassword);
    }


    @Override
    public User updateUserByEmail(String email, User updatedUser) {
        // First fetch existing user
        User existingUser = getUserByEmail(email);
        if (existingUser == null) {
            return null; // User not found
        }
        return googleSheetsRepository.updateUserByEmail(email, updatedUser).orElse(null);
    }

}


