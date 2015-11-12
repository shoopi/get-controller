package main.java.nl.tue.ieis.get.event.type.activitiLog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "processSpecId",
    "processInstanceId",
    "timestamp",
    "userId",
    "taskId",
    "taskState"
})
@XmlRootElement(name = "activityLog")
public class ActivityLog {

    @XmlElement(required = true)
    protected String processSpecId;
    @XmlElement(required = true)
    protected String processInstanceId;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected String timestamp;
    @XmlElement(required = true)
    protected String userId;
    @XmlElement(required = true)
    protected String taskId;
    @XmlElement(required = true)
    protected String taskState;

    public String getProcessSpecId() {
        return processSpecId;
    }

    public void setProcessSpecId(String value) {
        this.processSpecId = value;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String value) {
        this.processInstanceId = value;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String string) {
        this.timestamp = string;
    }

    public String getUserId() {
        return userId;
    }

    
    public void setUserId(String value) {
        this.userId = value;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String value) {
        this.taskId = value;
    }

    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String value) {
        this.taskState = value;
    }

}
