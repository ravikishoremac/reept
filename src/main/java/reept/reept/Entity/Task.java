package reept.reept.Entity;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;



public class Task {
	
	private Long id;
	private String description;
	private String department;
	private String area;
	private String sub_area;
	private String priority;
	private String person;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate start_date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate end_date;
	
	private String bottlenecks;
	private Long storypoints;
	private String status;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate completion_date;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate Request_date;
	
	private String remarks;
	
	private String manager_name;
	
	
	
	
	public LocalDate getCompletion_date() {
		return completion_date;
	}
	public void setCompletion_date(LocalDate completion_date) {
		this.completion_date = completion_date;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getSub_area() {
		return sub_area;
	}
	public void setSub_area(String sub_area) {
		this.sub_area = sub_area;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public Long getStorypoints() {
		return storypoints;
	}
	public void setStorypoints(Long storypoints) {
		this.storypoints = storypoints;
	}
	public LocalDate getStart_date() {
		return start_date;
	}
	public void setStart_date(LocalDate start_date) {
		this.start_date = start_date;
	}
	public LocalDate getEnd_date() {
		return end_date;
	}
	public void setEnd_date(LocalDate end_date) {
		this.end_date = end_date;
	}
	public String getBottlenecks() {
		return bottlenecks;
	}
	public void setBottlenecks(String bottlenecks) {
		this.bottlenecks = bottlenecks;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public LocalDate getRequest_date() {
		return Request_date;
	}
	public void setRequest_date(LocalDate request_date) {
		Request_date = request_date;
	}
	public String getManager_name() {
		return manager_name;
	}
	public void setManager_name(String manager_name) {
		this.manager_name = manager_name;
	}
	
	

}
