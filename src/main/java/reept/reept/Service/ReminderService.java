package reept.reept.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reept.reept.Dao.GoogleSheetsRepository;
import reept.reept.Entity.Task;
import reept.reept.Entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReminderService {

    @Autowired
    private GoogleSheetsRepository googleSheetsRepository;

    @Autowired
    private EmailService emailService;

    public void sendDailyTaskEmailsToAllEmployees() {
        List<User> employees = googleSheetsRepository.getAllUsers();
        for (User employee : employees) {
            if ("employee".equalsIgnoreCase(employee.getRole())) {
                sendReminderAndOverdueEmailToEmployee(employee.getEmail());
            }
        }
    }

    public void sendReminderAndOverdueEmailToEmployee(String employeeEmail) {
        User employee = googleSheetsRepository.userByemail(employeeEmail);
        if (employee == null) return;

        List<Task> tasks = googleSheetsRepository.TasksByempName(employeeEmail);
        if (tasks == null || tasks.isEmpty()) return;

        LocalDate today = LocalDate.now();
        List<Task> dueTomorrow = new ArrayList<>();
        List<Task> overdueTasks = new ArrayList<>();

//        for (Task task : tasks) {
//            if (task.getEnd_date() == null || task.getStatus() == null) continue;
//
//            LocalDate endDate = task.getEnd_date(); // end_date is already LocalDate
//            String status = task.getStatus().trim();
//
//            if (!status.equalsIgnoreCase("Completed")) {
//                if (endDate.equals(today.plusDays(1))) {
//                    dueTomorrow.add(task);
//                } else if (endDate.isBefore(today)) {
//                    overdueTasks.add(task);
//                }
//            }
//        }
        
        for (Task task : tasks) {
            if (task.getEnd_date() == null || task.getStatus() == null) continue;

            LocalDate endDate = task.getEnd_date();
            String status = task.getStatus().trim();

            if (!status.equalsIgnoreCase("Completed") && !status.equalsIgnoreCase("Completion Request")) {
                if (endDate.equals(today.plusDays(1))) {
                    dueTomorrow.add(task);
                } else if (endDate.isBefore(today)) {
                    overdueTasks.add(task);
                }
            }
        }


        //Reminder before one day
        
        if (!dueTomorrow.isEmpty()) {
            StringBuilder body = new StringBuilder();
            body.append("Dear ").append(employee.getName()).append(",\n\n")
                .append("The following task(s) are due tomorrow:\n\n");

            for (Task task : dueTomorrow) {
                body.append("• Task ID      : ").append(task.getId()).append("\n")
                    .append("• Description  : ").append(task.getDescription()).append("\n")
                    .append("• Department   : ").append(task.getDepartment()).append("\n")
                    .append("• Priority     : ").append(task.getPriority()).append("\n")
                    .append("• Start Date   : ").append(task.getStart_date()).append("\n")
                    .append("• End Date     : ").append(task.getEnd_date()).append("\n")
                    .append("• Status       : ").append(task.getStatus()).append("\n\n");
            }

            body.append("Please ensure these are completed on time.\n\nBest regards,\nTask Management System");
            emailService.sendEmail(employeeEmail, "🔔 Reminder: Task(s) Due Tomorrow", body.toString());
        }

        // Alert after crossing End date
        
        if (!overdueTasks.isEmpty()) {
            StringBuilder body = new StringBuilder();
            body.append("Dear ").append(employee.getName()).append(",\n\n")
                .append("The following task(s) are now overdue:\n\n");

            for (Task task : overdueTasks) {
                body.append("• Task ID      : ").append(task.getId()).append("\n")
                    .append("• Description  : ").append(task.getDescription()).append("\n")
                    .append("• Department   : ").append(task.getDepartment()).append("\n")
                    .append("• Priority     : ").append(task.getPriority()).append("\n")
                    .append("• Start Date   : ").append(task.getStart_date()).append("\n")
                    .append("• End Date     : ").append(task.getEnd_date()).append("\n")
                    .append("• Status       : ").append(task.getStatus()).append("\n\n");
            }

            body.append("Please take immediate action.\n\nBest regards,\nTask Management System");
            emailService.sendEmail(employeeEmail, "⚠️ Overdue Task Alert", body.toString());
        }
    }
}

