package main.java.nl.tue.ieis.get.activiti;

import main.java.nl.tue.ieis.get.activiti.taskDocumentation.TaskDocumentation;

public class TaskObject {
	
	private String taskId;
	private String taskName;
	private TaskType taskType;
	private TaskStatus taskStatus;
	private String processDefinitionId;
	private String processInstanceId;
	private TaskDocumentation taskDocumentation;
	
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public String getTaskId() {
		return taskId;
	}
	public String getTaskName() {
		return taskName;
	}
	public TaskStatus getTaskStatus() {
		return taskStatus;
	}
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public void setTaskStatus(TaskStatus taskStatus) {
		this.taskStatus = taskStatus;
	}
	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}
	public TaskType getTaskType() {
		return taskType;
	}
	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}
	public TaskDocumentation getTaskDocumentation() {
		return taskDocumentation;
	}
	public void setTaskDocumentation(TaskDocumentation taskDocumentation) {
		this.taskDocumentation = taskDocumentation;
	}
	public TaskObject(String taskId, String taskName, TaskType taskType,
			TaskStatus taskStatus, String processDefinitionId,
			String processInstanceId, TaskDocumentation taskDocumentation) {
		super();
		this.taskId = taskId;
		this.taskName = taskName;
		this.taskType = taskType;
		this.taskStatus = taskStatus;
		this.processDefinitionId = processDefinitionId;
		this.processInstanceId = processInstanceId;
		this.taskDocumentation = taskDocumentation;
	}
	
	public boolean equals(Object o) {
		return ((o instanceof TaskObject) && 
				((((TaskObject) o).getProcessInstanceId()).equals(this.processInstanceId)) && 
				((((TaskObject) o).getTaskId()).equals(this.taskId)) &&
				(((TaskObject)o).getTaskStatus()).equals(this.taskStatus));
	}
	
	public int hashCode() {
        return processInstanceId.hashCode() ^ taskId.hashCode() ^ taskStatus.toString().hashCode();
    }

}
