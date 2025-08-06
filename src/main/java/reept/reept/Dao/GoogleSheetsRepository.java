package reept.reept.Dao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ClearValuesRequest;
import com.google.api.services.sheets.v4.model.ValueRange;

import reept.reept.Entity.Employees;
import reept.reept.Entity.Task;
import reept.reept.Entity.User;
import reept.reept.Util.GoogleSheetProperties;
import reept.reept.Util.GoogleSheetServiceUtil;

@Repository
public class GoogleSheetsRepository {
		
	 private final GoogleSheetProperties sheetProperties;
	 
	 
	 public GoogleSheetsRepository(GoogleSheetProperties sheetProperties) {
	        this.sheetProperties = sheetProperties;			
	    }
	 
	

	 /*---------- User SignUp-----------------*/
	    public boolean saveUser(User user) {
			
	    	try {
	            Sheets sheetsService = GoogleSheetServiceUtil.getSheetsService(sheetProperties.getCredentialsFirst());

	            // Read existing data to determine row count
	            ValueRange readResult = sheetsService.spreadsheets().values()
	                    .get(sheetProperties.getUserid(), "Users.Resustainability")
	                    .execute();

	            List<List<Object>> existingRows = readResult.getValues();
	            int nextId = (existingRows == null || existingRows.size() <= 1)
	                    ? 1
	                    : existingRows.size(); // Assume header is at index 0

	            user.setId((long) nextId);

	            List<Object> rowData = new ArrayList<>();
	            rowData.add(user.getId());
	            rowData.add(user.getName());
	            rowData.add(user.getEmail());
	            rowData.add(user.getMobileNumber());
	            rowData.add(user.getRole());
	            rowData.add(user.getDepartment());
	            rowData.add(user.getStatus());
	            rowData.add(user.getPassword());
	            rowData.add(user.getReportingto());

	            List<List<Object>> rows = new ArrayList<>();
	            rows.add(rowData);

	            ValueRange appendBody = new ValueRange()
                    .setValues(rows);

	            sheetsService.spreadsheets().values()
	                    .append(sheetProperties.getUserid(), "Users.Resustainability", appendBody)
	                    .setValueInputOption("RAW")
	                    .execute();

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    	
	    	return true;
	    	
    }
	    
	
	
	    /*---------- Getting All Users for Login Purpose-----------------*/
	    
	    public List<User> getAllUsers() {
	        try {
	            Sheets sheetsService = GoogleSheetServiceUtil.getSheetsService(sheetProperties.getCredentialsFirst());
	            ValueRange response = sheetsService.spreadsheets().values()
	                    .get(sheetProperties.getUserid(), "Users.Resustainability") // Make sure the range includes your data
	                    .execute();

	            List<List<Object>> values = response.getValues();
	            List<User> users = new ArrayList<>();

	            if (values == null || values.size() <= 1) {
	                return users; // empty or only header
	            }

	            for (int i = 1; i < values.size(); i++) { // skip header
	                List<Object> row = values.get(i);

	                // Skip rows that are completely empty
	                if (row == null || row.isEmpty() || row.get(0).toString().trim().isEmpty()) {
	                    continue;
	                }

	                User user = new User();
	                user.setId(row.size() > 0 ? Long.parseLong(row.get(0).toString()) : null);
	                user.setName(row.size() > 1 ? row.get(1).toString() : "");
	                user.setEmail(row.size() > 2 ? row.get(2).toString() : "");
	                user.setMobileNumber(row.size() > 3 ? row.get(3).toString() : "");
	                user.setRole(row.size() > 4 ? row.get(4).toString() : "");	                
	                user.setDepartment(row.size() > 5 ? row.get(5).toString() : "");
	                user.setStatus(row.size() > 6 ? row.get(6).toString() : "");
	                user.setPassword(row.size() > 7 ? row.get(7).toString() : "");
	                user.setReportingto(row.size() > 8 ? row.get(8).toString() : "");
	                users.add(user);
	            }

	            return users;

	        } catch (Exception e) {
	            e.printStackTrace();
	            return Collections.emptyList();
	        }
	    }

	    /*---------- Getting All Users for Manager Task assign Suggestion Purpose-----------------*/
	   
	    public List<User> getUsersByManager(String email) {
	        List<User> matchedUsers = new ArrayList<>();

	        try {
	            Sheets sheetsService = GoogleSheetServiceUtil.getSheetsService(sheetProperties.getCredentialsFirst());
	            ValueRange response = sheetsService.spreadsheets().values()
	                    .get(sheetProperties.getUserid(), "Users.Resustainability")
	                    .execute();

	            List<List<Object>> values = response.getValues();

	            if (values == null || values.size() <= 1) {
	                return matchedUsers; // no data or only header
	            }

	            for (int i = 1; i < values.size(); i++) {
	                List<Object> row = values.get(i);

	                if (row == null || row.isEmpty() || row.get(0).toString().trim().isEmpty()) {
	                    continue;
	                }

	                String reportingTo = row.size() > 8 ? row.get(8).toString().trim() : "";

	                // Match manager email
	                if (!reportingTo.equalsIgnoreCase(email)) {
	                    continue;
	                }

	                User user = new User();
	                user.setId(row.size() > 0 ? Long.parseLong(row.get(0).toString()) : null);
	                user.setName(row.size() > 1 ? row.get(1).toString() : "");
	                user.setEmail(row.size() > 2 ? row.get(2).toString() : "");
	                user.setMobileNumber(row.size() > 3 ? row.get(3).toString() : "");
	                user.setRole(row.size() > 4 ? row.get(4).toString() : "");
	                user.setDepartment(row.size() > 5 ? row.get(5).toString() : "");
	                user.setStatus(row.size() > 6 ? row.get(6).toString() : "");
	                user.setPassword(row.size() > 7 ? row.get(7).toString() : "");
	                user.setReportingto(reportingTo);

	                matchedUsers.add(user);
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return matchedUsers;
	    }


	    /*---------- Assigning Task(Used In Manager)-----------------*/
	    
//		public boolean saveTask(Task task) {
//			// TODO Auto-generated method stub
//			try {
//	            Sheets sheetsService = GoogleSheetServiceUtil.getSheetsService(sheetProperties.getCredentialsSecond());
//
//	            // Read existing data to determine row count
//	            ValueRange readResult = sheetsService.spreadsheets().values()
//	                    .get(sheetProperties.getTasksid(), "Tasks.Resustainability")
//	                    .execute();
//
//	            List<List<Object>> existingRows = readResult.getValues();
//	            int nextId = (existingRows == null || existingRows.size() <= 1)
//	                    ? 1
//	                    : existingRows.size(); // Assume header is at index 0
//
//	            task.setId((long) nextId);
//
//	            
//	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//	            List<Object> rowData = new ArrayList<>();
//	            rowData.add(task.getId() != null ? task.getId().toString() : "");
//	            rowData.add(task.getDescription() != null ? task.getDescription() : "");
//	            rowData.add(task.getDepartment() != null ? task.getDepartment() : "");
//	            rowData.add(task.getArea() != null ? task.getArea() : "");
//	            rowData.add(task.getSub_area() != null ? task.getSub_area() : "");
//	            rowData.add(task.getPriority() != null ? task.getPriority() : "");
//	            rowData.add(task.getPerson() != null ? task.getPerson() : "");
//	            rowData.add(task.getManager_name() != null ? task.getManager_name() : "");
//	            rowData.add(task.getStart_date() != null ? task.getStart_date().format(formatter) : "");
//	            rowData.add(task.getEnd_date() != null ? task.getEnd_date().format(formatter) : "");
//	            rowData.add(task.getBottlenecks() != null ? task.getBottlenecks() : "");
//	            rowData.add(task.getStorypoints() != null ? task.getStorypoints().toString() : "");
//	            rowData.add(task.getStatus() != null ? task.getStatus() : "");
//	            rowData.add(task.getRequest_date() != null ? task.getRequest_date().format(formatter) : "");
//		        rowData.add(task.getCompletion_date() != null ? task.getCompletion_date().format(formatter) : "");
//	            rowData.add(task.getRemarks() != null ? task.getRemarks() : "");
//	            //rowData.add(task.getManager_name() != null ? task.getManager_name() : "");
//
//
//	            List<List<Object>> rows = new ArrayList<>();
//	            rows.add(rowData);
//
//	            ValueRange appendBody = new ValueRange()
//	                    .setValues(rows);
//
//	            sheetsService.spreadsheets().values()
//	                    .append(sheetProperties.getTasksid(), "Tasks.Resustainability", appendBody)
//	                    .setValueInputOption("RAW")
//	                    .execute();
//
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	        }
//	    	
//	    	return true;
//	    	
//	    }
		
		
		
		public boolean saveTask(Task task) {
		    try {
		        // Validate date range before saving
		        if (task.getStart_date() != null && task.getEnd_date() != null) {
		            if (task.getStart_date().isAfter(task.getEnd_date())) {
		                System.out.println("Start date is after end date. Task not saved.");
		                return false;
		            }
		        }

		        Sheets sheetsService = GoogleSheetServiceUtil.getSheetsService(sheetProperties.getCredentialsSecond());

		        // Read existing data to determine row count
		        ValueRange readResult = sheetsService.spreadsheets().values()
		                .get(sheetProperties.getTasksid(), "Tasks.Resustainability")
		                .execute();

		        List<List<Object>> existingRows = readResult.getValues();
		        int nextId = (existingRows == null || existingRows.size() <= 1)
		                ? 1
		                : existingRows.size(); // Assume header is at index 0

		        task.setId((long) nextId);

		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		        List<Object> rowData = new ArrayList<>();
		        rowData.add(task.getId() != null ? task.getId().toString() : "");
		        rowData.add(task.getDescription() != null ? task.getDescription() : "");
		        rowData.add(task.getDepartment() != null ? task.getDepartment() : "");
		        rowData.add(task.getArea() != null ? task.getArea() : "");
		        rowData.add(task.getSub_area() != null ? task.getSub_area() : "");
		        rowData.add(task.getPriority() != null ? task.getPriority() : "");
		        rowData.add(task.getPerson() != null ? task.getPerson() : "");
		        rowData.add(task.getManager_name() != null ? task.getManager_name() : "");
		        rowData.add(task.getStart_date() != null ? task.getStart_date().format(formatter) : "");
		        rowData.add(task.getEnd_date() != null ? task.getEnd_date().format(formatter) : "");
		        rowData.add(task.getBottlenecks() != null ? task.getBottlenecks() : "");
		        rowData.add(task.getStorypoints() != null ? task.getStorypoints().toString() : "");
		        rowData.add(task.getStatus() != null ? task.getStatus() : "");
		        rowData.add(task.getRequest_date() != null ? task.getRequest_date().format(formatter) : "");
		        rowData.add(task.getCompletion_date() != null ? task.getCompletion_date().format(formatter) : "");
		        rowData.add(task.getRemarks() != null ? task.getRemarks() : "");

		        List<List<Object>> rows = new ArrayList<>();
		        rows.add(rowData);

		        ValueRange appendBody = new ValueRange().setValues(rows);

		        sheetsService.spreadsheets().values()
		                .append(sheetProperties.getTasksid(), "Tasks.Resustainability", appendBody)
		                .setValueInputOption("RAW")
		                .execute();

		        return true;

		    } catch (Exception e) {
		        e.printStackTrace();
		        return false;
		    }
		}


		/*---------- Getting All Tasks-----------------*/
		
		public List<Task> getAllTasks() {
		    List<Task> tasks = new ArrayList<>();

		    try {
		        Sheets sheetsService = GoogleSheetServiceUtil.getSheetsService(sheetProperties.getCredentialsSecond());

		        ValueRange response = sheetsService.spreadsheets().values()
		                .get(sheetProperties.getTasksid(), "Tasks.Resustainability")
		                .execute();

		        List<List<Object>> values = response.getValues();

		        // Return an empty task if the sheet is empty or has only the header
		        if (values == null || values.size() < 2) {
		            Task emptyTask = new Task();
		            emptyTask.setDescription("");
		            emptyTask.setDepartment("");
		            emptyTask.setArea("");
		            emptyTask.setSub_area("");
		            emptyTask.setPriority("");
		            emptyTask.setPerson("");
		            emptyTask.setManager_name("");
		            emptyTask.setStorypoints(0L);
		            emptyTask.setStart_date(null);
		            emptyTask.setEnd_date(null);
		            emptyTask.setBottlenecks("");
		            emptyTask.setStatus("");
		            emptyTask.setRequest_date(null);
			        emptyTask.setCompletion_date(null);
		            emptyTask.setRemarks("");
		            return Collections.singletonList(emptyTask);
		        }

		        for (int i = 1; i < values.size(); i++) {
		            List<Object> row = values.get(i);
		            Task task = new Task();

		            try {
		                task.setId(getLongSafe(row, 0));
		                task.setDescription(getStringSafe(row, 1));
		                task.setDepartment(getStringSafe(row, 2));
		                task.setArea(getStringSafe(row, 3));
		                task.setSub_area(getStringSafe(row, 4));
		                task.setPriority(getStringSafe(row, 5));
		                task.setPerson(getStringSafe(row, 6));
		                task.setManager_name(getStringSafe(row,7));
		                task.setStart_date(parseLocalDateSafe(row, 8));
		                task.setEnd_date(parseLocalDateSafe(row, 9));
		                task.setBottlenecks(getStringSafe(row, 10));
		                task.setStorypoints(getLongSafe(row, 11));
		                task.setStatus(getStringSafe(row, 12));
		                task.setRequest_date(parseLocalDateSafe(row, 13));
				        task.setCompletion_date(parseLocalDateSafe(row, 14));
		                task.setRemarks(getStringSafe(row, 15));

		                tasks.add(task);
		            } catch (Exception rowEx) {
		                System.err.println("Skipping row " + (i + 1) + " due to error: " + rowEx.getMessage());
		            }
		        }

		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return tasks;
		}
		
		/*---------- Find Task Using Id-----------------*/
		
		public Optional<Task> findById(Long id) {
		    try {
		        Sheets sheetsService = GoogleSheetServiceUtil.getSheetsService(sheetProperties.getCredentialsSecond());

		        ValueRange response = sheetsService.spreadsheets().values()
		                .get(sheetProperties.getTasksid(), "Tasks.Resustainability")
		                .execute();

		        List<List<Object>> values = response.getValues();

		        if (values == null || values.size() < 2) {
		            return Optional.empty(); // No data or only header
		        }

		        for (int i = 1; i < values.size(); i++) {
		            List<Object> row = values.get(i);

		            try {
		                Long rowId = getLongSafe(row, 0); // use safe parse
		                if (rowId != null && rowId.equals(id)) {
		                    Task task = new Task();
		                    task.setId(rowId);
		                    task.setDescription(getStringSafe(row, 1));
		                    task.setDepartment(getStringSafe(row, 2));
		                    task.setArea(getStringSafe(row, 3));
		                    task.setSub_area(getStringSafe(row, 4));
		                    task.setPriority(getStringSafe(row, 5));
		                    task.setPerson(getStringSafe(row, 6));
		                    task.setManager_name(getStringSafe(row, 7));
		                    task.setStart_date(parseLocalDateSafe(row, 8));
		                    task.setEnd_date(parseLocalDateSafe(row, 9));
		                    task.setBottlenecks(getStringSafe(row, 10));
		                    task.setStorypoints(getLongSafe(row, 11));
		                    task.setStatus(getStringSafe(row, 12));
		                    task.setRequest_date(parseLocalDateSafe(row, 13));
				            task.setCompletion_date(parseLocalDateSafe(row, 14));
		                    task.setRemarks(getStringSafe(row, 15));

		                    return Optional.of(task);
		                }
		            } catch (Exception ex) {
		                System.err.println("Skipping row " + (i + 1) + ": " + ex.getMessage());
		            }
		        }

		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return Optional.empty();
		}

		
		/*---------- Update Task Using Id-----------------*/

//		public boolean updateById(Long id, Task updatedTask) {
//		    try {
//		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//		        Sheets sheetsService = GoogleSheetServiceUtil.getSheetsService(sheetProperties.getCredentialsSecond());
//
//		        ValueRange response = sheetsService.spreadsheets().values()
//		                .get(sheetProperties.getTasksid(), "Tasks.Resustainability")
//		                .execute();
//
//		        List<List<Object>> values = response.getValues();
//
//		        if (values == null || values.size() < 2) {
//		            return false;
//		        }
//
//		        for (int i = 1; i < values.size(); i++) {
//		            List<Object> row = values.get(i);
//		            if (row.size() > 0 && row.get(0) != null) {
//		                Long rowId = Long.parseLong(row.get(0).toString());
//		                if (rowId.equals(id)) {
//		                    List<Object> updatedRow = Arrays.asList(
//		                            updatedTask.getId(),
//		                            updatedTask.getDescription(),
//		                            updatedTask.getDepartment(),
//		                            updatedTask.getArea(),
//		                            updatedTask.getSub_area(),
//		                            updatedTask.getPriority(),
//		                            updatedTask.getPerson(),
//		                            updatedTask.getManager_name(),
//		                            updatedTask.getStart_date() != null ? updatedTask.getStart_date().format(formatter) : "",
//		                            updatedTask.getEnd_date() != null ? updatedTask.getEnd_date().format(formatter) : "",
//		                            updatedTask.getBottlenecks(),
//		                            updatedTask.getStorypoints() == null ? "":updatedTask.getStorypoints(),
//		                            updatedTask.getStatus(),
//		                            updatedTask.getRequest_date() != null ? updatedTask.getRequest_date().format(formatter) : "",
//					                updatedTask.getCompletion_date() != null ? updatedTask.getCompletion_date().format(formatter) : "",
//		                            updatedTask.getRemarks()
//		                    );
//
//		                    ValueRange body = new ValueRange().setValues(Collections.singletonList(updatedRow));
//		                    String range = "Tasks.Resustainability!A" + (i + 1) + ":P" + (i + 1); // Adjust to include column L (remarks)
//
//		                    sheetsService.spreadsheets().values()
//		                            .update(sheetProperties.getTasksid(), range, body)
//		                            .setValueInputOption("RAW")
//		                            .execute();
//
//		                    return true;
//		                }
//		            }
//		        }
//
//		    } catch (Exception e) {
//		        e.printStackTrace();
//		    }
//
//		    return false;
//		}
		
		public boolean updateById(Long id, Task updatedTask) {
		    try {
		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		        Sheets sheetsService = GoogleSheetServiceUtil.getSheetsService(sheetProperties.getCredentialsSecond());

		        ValueRange response = sheetsService.spreadsheets().values()
		                .get(sheetProperties.getTasksid(), "Tasks.Resustainability")
		                .execute();

		        List<List<Object>> values = response.getValues();

		        if (values == null || values.size() < 2) {
		            return false;
		        }

		        for (int i = 1; i < values.size(); i++) {
		            List<Object> row = values.get(i);
		            if (row.size() > 0 && row.get(0) != null) {
		                Long rowId = Long.parseLong(row.get(0).toString());
		                if (rowId.equals(id)) {

		                    List<Object> updatedRow = Arrays.asList(
		                            updatedTask.getId() != null ? updatedTask.getId() : "",
		                            updatedTask.getDescription() != null ? updatedTask.getDescription() : "",
		                            updatedTask.getDepartment() != null ? updatedTask.getDepartment() : "",
		                            updatedTask.getArea() != null ? updatedTask.getArea() : "",
		                            updatedTask.getSub_area() != null ? updatedTask.getSub_area() : "",
		                            updatedTask.getPriority() != null ? updatedTask.getPriority() : "",
		                            updatedTask.getPerson() != null ? updatedTask.getPerson() : "",		                           
		                            row.size() > 7 ? row.get(7).toString() : "",
		                            updatedTask.getStart_date() != null ? updatedTask.getStart_date().format(formatter) : "",
		                            updatedTask.getEnd_date() != null ? updatedTask.getEnd_date().format(formatter) : "",
		                            updatedTask.getBottlenecks() != null ? updatedTask.getBottlenecks() : "",
		                            updatedTask.getStorypoints() != null ? updatedTask.getStorypoints() : "",
		                            updatedTask.getStatus() != null ? updatedTask.getStatus() : "",
		                            updatedTask.getRequest_date() != null ? updatedTask.getRequest_date().format(formatter) : "",
		                            updatedTask.getCompletion_date() != null ? updatedTask.getCompletion_date().format(formatter) : "",
		                            updatedTask.getRemarks() != null ? updatedTask.getRemarks() : ""
		                    );

		                    // Match A to P columns (16 columns total)
		                    String range = "Tasks.Resustainability!A" + (i + 1) + ":P" + (i + 1);
		                    ValueRange body = new ValueRange().setValues(Collections.singletonList(updatedRow));

		                    sheetsService.spreadsheets().values()
		                            .update(sheetProperties.getTasksid(), range, body)
		                            .setValueInputOption("RAW")
		                            .execute();

		                    return true;
		                }
		            }
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    return false;
		}


		
		/*---------- List of Tasks using Email(Used In Employee)-----------------*/
		
		public List<Task> TasksByempName(String email) {
		    List<Task> employeeTasks = new ArrayList<>();

		    try {
		        Sheets sheetsService = GoogleSheetServiceUtil.getSheetsService(sheetProperties.getCredentialsSecond());

		        ValueRange response = sheetsService.spreadsheets().values()
		                .get(sheetProperties.getTasksid(), "Tasks.Resustainability")
		                .execute();

		        List<List<Object>> values = response.getValues();

		        if (values == null || values.size() < 2) {
		            return Collections.emptyList(); // Only header or no data
		        }

		        for (int i = 1; i < values.size(); i++) {
		            List<Object> row = values.get(i);

		            try {
		                String personInRow = getStringSafe(row, 6); // Person column
		                if (!personInRow.equalsIgnoreCase(email)) continue;	                
		                Task task = new Task();
		                task.setId(getLongSafe(row, 0));
		                task.setDescription(getStringSafe(row, 1));
		                task.setDepartment(getStringSafe(row, 2));
		                task.setArea(getStringSafe(row, 3));
		                task.setSub_area(getStringSafe(row, 4));
		                task.setPriority(getStringSafe(row, 5));
		                task.setPerson(personInRow);
		                task.setManager_name(getStringSafe(row,7));
		                task.setStart_date(parseLocalDateSafe(row, 8));
		                task.setEnd_date(parseLocalDateSafe(row, 9));
		                task.setBottlenecks(getStringSafe(row, 10));
		                task.setStorypoints(getLongSafe(row, 11));
		                task.setStatus(getStringSafe(row, 12));
		                task.setRequest_date(parseLocalDateSafe(row, 13));
				        task.setCompletion_date(parseLocalDateSafe(row, 14));				        
		                task.setRemarks(getStringSafe(row, 15));
		                employeeTasks.add(task);
		            } catch (Exception rowEx) {
		                System.err.println("Skipping row " + (i + 1) + " due to error: " + rowEx.getMessage());
		            }
		        }

		    } catch (Exception e) {
		        e.printStackTrace(); // Preferably log this
		    }

		    return employeeTasks;
		}
		
		/*---------- List of Tasks using Email(Used In Manager)-----------------*/
		
		public List<Task> TasksBymanagerName(String email) {
		    List<Task> managerTasks = new ArrayList<>();

		    try {
		        Sheets sheetsService = GoogleSheetServiceUtil.getSheetsService(sheetProperties.getCredentialsSecond());

		        ValueRange response = sheetsService.spreadsheets().values()
		                .get(sheetProperties.getTasksid(), "Tasks.Resustainability")
		                .execute();

		        List<List<Object>> values = response.getValues();

		        if (values == null || values.size() < 2) {
		            return Collections.emptyList(); // Only header or no data
		        }

		        for (int i = 1; i < values.size(); i++) {
		            List<Object> row = values.get(i);

		            try {
		                String manager_nameInRow = getStringSafe(row, 7); 
		                if (!manager_nameInRow.equalsIgnoreCase(email)) continue;	                
		                Task task = new Task();
		                task.setId(getLongSafe(row, 0));
		                task.setDescription(getStringSafe(row, 1));
		                task.setDepartment(getStringSafe(row, 2));
		                task.setArea(getStringSafe(row, 3));
		                task.setSub_area(getStringSafe(row, 4));
		                task.setPriority(getStringSafe(row, 5));
		                task.setPerson(getStringSafe(row, 6));	
		                task.setManager_name(manager_nameInRow);
		                task.setStart_date(parseLocalDateSafe(row, 8));
		                task.setEnd_date(parseLocalDateSafe(row, 9));
		                task.setBottlenecks(getStringSafe(row, 10));
		                task.setStorypoints(getLongSafe(row, 11));
		                task.setStatus(getStringSafe(row, 12));
		                task.setRequest_date(parseLocalDateSafe(row, 13));
				        task.setCompletion_date(parseLocalDateSafe(row, 14));				        
		                task.setRemarks(getStringSafe(row, 15));
		                //task.setManager_name(manager_nameInRow);
		                managerTasks.add(task);
		            } catch (Exception rowEx) {
		                System.err.println("Skipping row " + (i + 1) + " due to error: " + rowEx.getMessage());
		            }
		        }

		    } catch (Exception e) {
		        e.printStackTrace(); // Preferably log this
		    }

		    return managerTasks;
		}

		 /*---------------Employee Using Email----------------*/
		
		 public User userByemail(String email) {
		        try {
		            Sheets sheetsService = GoogleSheetServiceUtil.getSheetsService(sheetProperties.getCredentialsFirst());

		            ValueRange response = sheetsService.spreadsheets().values()
		                    .get(sheetProperties.getUserid(), "Users.Resustainability") // assuming "Users" is the sheet name
		                    .execute();

		            List<List<Object>> values = response.getValues();

		            if (values == null || values.size() < 2) {
		                return null; // No data or only header
		            }

		            for (int i = 1; i < values.size(); i++) {
		                List<Object> row = values.get(i);
		                String rowEmail = getStringSafe(row, 2); // Email is at index 2

		                if (rowEmail != null && rowEmail.equalsIgnoreCase(email)) {
		                    User user = new User();
		                    user.setId(getLongSafe(row, 0));              
		                    user.setName(getStringSafe(row, 1));           
		                    user.setEmail(rowEmail);                       
		                    user.setMobileNumber(getObjectSafe(row, 3));   
		                    user.setRole(getStringSafe(row, 4));           
		                    user.setDepartment(getStringSafe(row, 5));      
		                    user.setStatus(getStringSafe(row, 6));         
		                    user.setPassword(getStringSafe(row, 7)); 
		                    user.setReportingto(getStringSafe(row, 8));
		                    return user;
		                }
		            }
		        } catch (Exception e) {
		            e.printStackTrace(); // Preferably log this
		        }

		        return null;
		    }

		    

		    private Object getObjectSafe(List<Object> row, int index) {
		        if (row.size() > index) {
		            return row.get(index);
		        }
		        return null;
		    }
		    
		   
		    /*---------------To Get All Employees----------------*/
		    
		   public List<Employees> allEmployees() {
		        int retries = 3;
		        for (int i = 0; i < retries; i++) {
		            try {
		                Sheets sheetsService = GoogleSheetServiceUtil.getSheetsService(sheetProperties.getCredentialsThird());
		                ValueRange response = sheetsService.spreadsheets().values()
		                    .get(sheetProperties.getEmpid(), "Employees.Resustainability")
		                    .execute();

		                List<List<Object>> values = response.getValues();
		                List<Employees> employees = new ArrayList<>();

		                if (values == null || values.size() <= 1) {
		                    return employees; // Only header or no data
		                }

		                for (int j = 1; j < values.size(); j++) { // Skip header
		                    List<Object> row = values.get(j);
		                    if (row.size() < 2 || row.get(1).toString().trim().isEmpty()) continue;

		                    Employees emp = new Employees();
		                    emp.setId(row.size() > 0 ? Long.parseLong(row.get(0).toString()) : null);
		                    emp.setEmail(row.get(1).toString());
		                    emp.setRole(row.size() > 2 ? row.get(2).toString() : null);
		                    emp.setDepartment(row.size() > 3 ? row.get(3).toString() : null);  // New line added

		                    employees.add(emp);
		                }

		                return employees;

		            } catch (com.google.api.client.googleapis.json.GoogleJsonResponseException e) {
		                if (e.getStatusCode() == 503 && i < retries - 1) {
		                    System.out.println("Google Sheets API 503 error. Retrying... (" + (i + 1) + ")");
		                    try {
		                        Thread.sleep(2000);
		                    } catch (InterruptedException ignored) {}
		                } else {
		                    e.printStackTrace();
		                    return Collections.emptyList();
		                }
		            } catch (Exception e) {
		                e.printStackTrace();
		                return Collections.emptyList();
		            }
		        }
		        return Collections.emptyList();
		    }
		   

		   /*---------------To Reset/Update Password----------------*/
		   
		    public boolean updateUserPassword(String email, String newPassword) {
		        try {
		            Sheets sheetsService = GoogleSheetServiceUtil.getSheetsService(sheetProperties.getCredentialsFirst());
		            if (sheetsService == null) {
		                System.err.println("Sheets service is null. Check credentials.");
		                return false;
		            }

		            ValueRange response = sheetsService.spreadsheets().values()
		                    .get(sheetProperties.getUserid(), "Users.Resustainability")
		                    .execute();

		            List<List<Object>> values = response.getValues();
		            if (values == null || values.size() <= 1) {
		                System.err.println("No data found in Users sheet.");
		                return false;
		            }

		            int rowIndex = -1;
		            for (int i = 1; i < values.size(); i++) {
		                List<Object> row = values.get(i);
		                if (row.size() > 2 && row.get(2).toString().equalsIgnoreCase(email.trim())) {
		                    rowIndex = i + 1; // Google Sheets rows are 1-based
		                    break;
		                }
		            }

		            if (rowIndex == -1) {
		                System.err.println("User not found for email: " + email);
		                return false; // User not found
		            }

		            // Update the password column (index 7)
		            List<List<Object>> updateData = new ArrayList<>();
		            List<Object> rowData = new ArrayList<>();
		            rowData.add(newPassword);
		            updateData.add(rowData);

		            ValueRange updateBody = new ValueRange().setValues(updateData);
		            sheetsService.spreadsheets().values()
		                    .update(sheetProperties.getUserid(), "Users.Resustainability!H" + rowIndex, updateBody)
		                    .setValueInputOption("RAW")
		                    .execute();

		            System.out.println("Password updated successfully for email: " + email);
		            return true;

		        } catch (Exception e) {
		            System.err.println("Error updating password for email " + email + ": " + e.getMessage());
		            e.printStackTrace();
		            return false;
		        }
		    }
		
		    
		    /*---------------To Delete Task By Manager----------------*/
		    
               public String deleteById(Long id) {
		        try {
		            Sheets sheetsService = GoogleSheetServiceUtil.getSheetsService(sheetProperties.getCredentialsSecond());
		            String spreadsheetId = sheetProperties.getTasksid();
		            String sheetName = "Tasks.Resustainability";

		            // Step 1: Fetch existing rows (excluding header)
		            ValueRange response = sheetsService.spreadsheets().values()
		                    .get(spreadsheetId, sheetName + "!A2:Z")
		                    .execute();

		            List<List<Object>> rows = response.getValues();

		            if (rows == null || rows.isEmpty()) {
		                return "No tasks found.";
		            }

		            // Step 2: Identify and remove the row with matching task ID
		            boolean deleted = false;
		            for (int i = 0; i < rows.size(); i++) {
		                List<Object> row = rows.get(i);
		                if (!row.isEmpty()) {
		                    try {
		                        Long taskId = Long.parseLong(row.get(0).toString());
		                        if (taskId.equals(id)) {
		                            rows.remove(i);
		                            deleted = true;
		                            break;
		                        }
		                    } catch (NumberFormatException ignored) {
		                    }
		                }
		            }

		            if (!deleted) {
		                return "Task with ID " + id + " not found.";
		            }

		            // Step 3: Reassign IDs (column A) starting from 1
		            for (int i = 0; i < rows.size(); i++) {
		                if (!rows.get(i).isEmpty()) {
		                    rows.get(i).set(0, String.valueOf(i + 1)); // ID in column A
		                }
		            }

		            // Step 4: Clear existing data (excluding header row)
		            sheetsService.spreadsheets().values()
		                    .clear(spreadsheetId, sheetName + "!A2:Z", new ClearValuesRequest())
		                    .execute();

		            // Step 5: Write back the updated rows with reassigned IDs
		            ValueRange updatedBody = new ValueRange().setValues(rows);
		            sheetsService.spreadsheets().values()
		                    .update(spreadsheetId, sheetName + "!A2", updatedBody)
		                    .setValueInputOption("RAW")
		                    .execute();

		            return "Task deleted successfully.";
		        } catch (Exception e) {
		            e.printStackTrace();
		            return "Error while deleting task: " + e.getMessage();
		        }
		    }
               
               
               /*---------------To Update Profile----------------*/
               
               public Optional<User> updateUserByEmail(String email, User updatedUser) {
   		        try {
   		            Sheets sheetsService = GoogleSheetServiceUtil.getSheetsService(sheetProperties.getCredentialsFirst());

   		            ValueRange response = sheetsService.spreadsheets().values()
   		                    .get(sheetProperties.getUserid(), "Users.Resustainability!A2:I")
   		                    .execute();

   		            List<List<Object>> values = response.getValues();
   		            if (values == null || values.isEmpty()) {
   		                return Optional.empty();
   		            }

   		            int rowIndex = -1;
   		            List<Object> rowToUpdate = null;

   		            for (int i = 0; i < values.size(); i++) {
   		                List<Object> row = values.get(i);
   		                if (row.size() > 2 && row.get(2).toString().equalsIgnoreCase(email)) {
   		                    rowIndex = i + 2; // Adjust for header and 0-based index
   		                    rowToUpdate = row;
   		                    break;
   		                }
   		            }

   		            if (rowIndex == -1 || rowToUpdate == null) {
   		                return Optional.empty();
   		            }

   		            // Preserve old values or override with new ones
   		            String id = rowToUpdate.size() > 0 ? rowToUpdate.get(0).toString() : "";
   		            String name = updatedUser.getName() != null ? updatedUser.getName() : (rowToUpdate.size() > 1 ? rowToUpdate.get(1).toString() : "");
   		            String emailFinal = email;
   		            String mobile = updatedUser.getMobileNumber() != null ? updatedUser.getMobileNumber().toString() : (rowToUpdate.size() > 3 ? rowToUpdate.get(3).toString() : "");
   		            String role = updatedUser.getRole() != null ? updatedUser.getRole() : (rowToUpdate.size() > 4 ? rowToUpdate.get(4).toString() : "");
   		            String department = updatedUser.getDepartment() != null ? updatedUser.getDepartment() : (rowToUpdate.size() > 5 ? rowToUpdate.get(5).toString() : "");
   		            String status = rowToUpdate.size() > 6 ? rowToUpdate.get(6).toString() : "";
   		            String password = rowToUpdate.size() > 7 ? rowToUpdate.get(7).toString() : "";
   		            String reportingTo = rowToUpdate.size() > 8 ? rowToUpdate.get(8).toString() : "";

//   		            List<Object> newRow = List.of(
//   		                    id, name, emailFinal, mobile, role, department, status, password, reportingTo
//   		            );
//
//   		            ValueRange body = new ValueRange().setValues(List.of(newRow));
//   		            sheetsService.spreadsheets().values()
//   		                    .update(sheetProperties.getUserid(), "Users!A" + rowIndex + ":I" + rowIndex, body)
//   		                    .setValueInputOption("RAW")
//   		                    .execute();
   		            
   		            //for tomcat 8.5
   		         List<Object> newRow = Arrays.asList(
   		                id, name, emailFinal, mobile, role, department, status, password, reportingTo
   		        );

   		        ValueRange body = new ValueRange().setValues(Arrays.asList(newRow));
   		        sheetsService.spreadsheets().values()
   		                .update(sheetProperties.getUserid(), "Users.Resustainability!A" + rowIndex + ":I" + rowIndex, body)
   		                .setValueInputOption("RAW")
   		                .execute();

   		            // Return updated user
   		            User user = new User();
   		            user.setId(Long.parseLong(id));
   		            user.setName(name);
   		            user.setEmail(emailFinal);
   		            user.setMobileNumber(mobile); // stored as Object
   		            user.setRole(role);
   		            user.setDepartment(department);
   		            user.setStatus(status);
   		            user.setPassword(password);
   		            user.setReportingto(reportingTo);

   		            return Optional.of(user);

   		        } catch (Exception e) {
   		            e.printStackTrace();
   		            return Optional.empty();
   		        }
   		    }
   		 
               
               /*---------------To Delete Employee Permanently By Manager----------------*/
               
      		 public String deleteEmployeeFromBothSheets(String email) {
      			    StringBuilder result = new StringBuilder();

      			    try {
      			        // USERS sheet - email in column C => index 2
      			        Sheets sheetsUsers = GoogleSheetServiceUtil.getSheetsService(sheetProperties.getCredentialsFirst());
      			        String usersSheetId = sheetProperties.getUserid();
      			        result.append(
      			            deleteFromSheetByEmailAndReassignSerials(
      			                sheetsUsers, usersSheetId, "Users.Resustainability", email, 2
      			            )
      			        ).append("\n");

      			        // EMPLOYEES sheet - email in column B => index 1
      			        Sheets sheetsEmployees = GoogleSheetServiceUtil.getSheetsService(sheetProperties.getCredentialsThird());
      			        String employeesSheetId = sheetProperties.getEmpid();
      			        result.append(
      			            deleteFromSheetByEmailAndReassignSerials(
      			                sheetsEmployees, employeesSheetId, "Employees.Resustainability", email, 1
      			            )
      			        );

      			        return result.toString().trim();

      			    } catch (Exception e) {
      			        e.printStackTrace();
      			        return "Error deleting employee: " + e.getMessage();
      			    }
      			}
      		 
      		 
      		
      		 /*---------------Support Method To Delete Employee Permanently By Manager----------------*/
      		 
      		private String deleteFromSheetByEmailAndReassignSerials(
    		        Sheets sheetsService,
    		        String spreadsheetId,
    		        String sheetName,
    		        String email,
    		        int emailColumnIndex
    		) {
    		    try {
    		        ValueRange response = sheetsService.spreadsheets().values()
    		                .get(spreadsheetId, sheetName + "!A2:Z")
    		                .execute();

    		        List<List<Object>> rows = response.getValues();
    		        if (rows == null || rows.isEmpty()) {
    		            return "No data found in " + sheetName;
    		        }

    		        boolean deleted = false;
    		        for (int i = 0; i < rows.size(); i++) {
    		            List<Object> row = rows.get(i);
    		            if (row.size() > emailColumnIndex && email.equalsIgnoreCase(row.get(emailColumnIndex).toString())) {
    		                rows.remove(i);
    		                deleted = true;
    		                break;
    		            }
    		        }

    		        if (!deleted) {
    		            return "No user with email " + email + " found in " + sheetName;
    		        }

    		        for (int i = 0; i < rows.size(); i++) {
    		            if (!rows.get(i).isEmpty()) {
    		                rows.get(i).set(0, String.valueOf(i + 1)); // Reset serial number
    		            }
    		        }

    		        sheetsService.spreadsheets().values()
    		                .clear(spreadsheetId, sheetName + "!A2:Z", new ClearValuesRequest())
    		                .execute();

    		        ValueRange updatedBody = new ValueRange().setValues(rows);
    		        sheetsService.spreadsheets().values()
    		                .update(spreadsheetId, sheetName + "!A2", updatedBody)
    		                .setValueInputOption("RAW")
    		                .execute();

    		        return "Deleted from " + sheetName;

    		    } catch (Exception e) {
    		        e.printStackTrace();
    		        return "Error deleting from " + sheetName + ": " + e.getMessage();
    		    }
    		}

    		
      		/*---------------To Add Employee By Manager Who can Register To Application----------------*/
      		
      		 public Employees saveEmployee(Employees employee) {
      	        try {
      	            Sheets sheetsService = GoogleSheetServiceUtil.getSheetsService(sheetProperties.getCredentialsThird());
      	            String spreadsheetId = sheetProperties.getEmpid();
      	            String sheetName = "Employees.Resustainability";

      	            // Step 1: Read existing rows
      	            ValueRange response = sheetsService.spreadsheets().values()
      	                    .get(spreadsheetId, sheetName + "!A2:D")
      	                    .execute();

      	            List<List<Object>> rows = response.getValues();

      	            // Step 2: Check for duplicate email in column B (index 1)
      	            if (rows != null) {
      	                for (List<Object> row : rows) {
      	                    if (row.size() > 1 && employee.getEmail().equalsIgnoreCase(row.get(1).toString())) {
      	                        return null; // duplicate found
      	                    }
      	                }
      	            }

      	            int newId = (rows == null) ? 1 : rows.size() + 1;

      	            // Step 3: Prepare row to append
      	          List<Object> newRow = new ArrayList<>();
      	        newRow.add(String.valueOf(newId));
      	        newRow.add(employee.getEmail());
      	        newRow.add(capitalizeFirstLetter(employee.getRole()));
      	        newRow.add(employee.getDepartment().toUpperCase());

      	            List<List<Object>> data = new ArrayList<>();
      	            data.add(newRow);

      	            ValueRange body = new ValueRange().setValues(data);
      	            sheetsService.spreadsheets().values()
      	                .append(spreadsheetId, sheetName + "!A:D", body)
      	                .setValueInputOption("RAW")
      	                .execute();

      	            employee.setId((long) newId);
      	            return employee;

      	        } catch (Exception e) {
      	            e.printStackTrace();
      	            return null;
      	        }
      	    }
      	
      		 
      		private String capitalizeFirstLetter(String input) {
      		    if (input == null || input.isEmpty()) return input;
      		    return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
      		}

	
		/*---------- Utility Methods-----------------*/
		
		private String getStringSafe(List<Object> row, int index) {
		    return row.size() > index ? row.get(index).toString().trim() : "";
		}

		private Long getLongSafe(List<Object> row, int index) {
		    try {
		        return row.size() > index ? Long.parseLong(row.get(index).toString().trim()) : null;
		    } catch (NumberFormatException e) {
		        return null;
		    }
		}


		private LocalDate parseLocalDateSafe(List<Object> row, int index) {
		    try {
		        if (row.size() > index && row.get(index) != null && !row.get(index).toString().isEmpty()) {
		            return LocalDate.parse(row.get(index).toString());
		        }
		    } catch (Exception e) {
		        System.err.println("Date parse error at column " + index + ": " + e.getMessage());
		    }
		    return null;
		}
		
		

}