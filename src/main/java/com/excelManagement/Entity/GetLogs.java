package com.excelManagement.Entity;

public class GetLogs {
	String threadId;
	String logDate;
	String classMethod;
	String level;
	String message;
	String id;
	public String getThreadId() {
		return threadId;
	}
	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}
	
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLogDate() {
		return logDate;
	}
	public void setLogDate(String logDate) {
		this.logDate = logDate;
	}
	public String getClassMethod() {
		return classMethod;
	}
	public void setClassMethod(String classMethod) {
		this.classMethod = classMethod;
	}
	
	
	

}
