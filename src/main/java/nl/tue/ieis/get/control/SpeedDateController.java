package main.java.nl.tue.ieis.get.control;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Slider;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Timer;

public class SpeedDateController extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;
	
	@Wire	private Hlayout calSpeed;
	@Wire	private Timebox planTimeBox, curTimeBox;
	@Wire	private Timer 	currentTimeTimer;
	
	/*
	@Wire
	private Datebox calendar;
	@Wire
	private Datebox calendarExecution;
	*/
	@Wire
	private Slider speedSlider;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		/*
		DateTime plannedDateTime = new DateTime(calendar.getValue());
		MapServiceImpl.plannedStart = plannedDateTime;
		
		DateTime execDateTime = new DateTime(calendarExecution.getValue());
		MapServiceImpl.execStart = execDateTime;
		*/
		int speed = speedSlider.getCurpos();
		MapServiceImpl.speed = speed;
		
		DateTime initDate = new DateTime();
		planTimeBox.setValue(initDate.plusHours(-1).toDate());
		MapServiceImpl.plannedStart = convertTimetoDateTime(planTimeBox.getValue());
		
		curTimeBox.setValue(initDate.toDate());
		MapServiceImpl.currentTime = convertTimetoDateTime(curTimeBox.getValue());
	}
	/*
	@Listen("onChange = #calendarExecution")
	public void changeExecutionDate() {
		DateTime dateTime = new DateTime(calendar.getValue());
		showNotify("Date Time is changed" , calendarExecution);
		MapServiceImpl.execStart = dateTime;
	}
	
	@Listen("onChange = #calendar")
	public void changePlanDate() {
		DateTime dateTime = new DateTime(calendar.getValue());
		showNotify("Date Time is changed" , calendar);
		MapServiceImpl.plannedStart = dateTime;
	}
	*/
	@Listen("onScroll = #speedSlider")
	public void changeSpeedSlider() {
		int speed = speedSlider.getCurpos();
		MapServiceImpl.speed = speed;
		showNotify("Average speed is " + speed + " Km/h", speedSlider);
	}
	
	@Listen("onChange = #planTimeBox")
	public void planTimeChange() {
		MapServiceImpl.plannedStart = convertTimetoDateTime(planTimeBox.getValue());
		showNotify("Change Time Notification" , planTimeBox);
	}
	
	@Listen("onChange = #curTimeBox")
	public void currentTimeChange() {
		MapServiceImpl.currentTime = convertTimetoDateTime(curTimeBox.getValue());
		showNotify("Change Time Notification" , curTimeBox);
	}
	
	@Listen("onTimer = #currentTimeTimer")
	public void runCurrentTimer() {
		curTimeBox.setValue(new DateTime().plusHours(1).toDate());
		MapServiceImpl.currentTime = convertTimetoDateTime(curTimeBox.getValue());
	}
	
	 private void showNotify(String msg, Component ref) {
	        Clients.showNotification(msg, "info", ref, "end_center", 1000);
	 }
	 
	 private DateTime convertTimetoDateTime(Date time) {
		 DateTime initDate = new DateTime();
		 MutableDateTime planTime = new MutableDateTime(time);
		 planTime.setDate(initDate.getYear(), initDate.getMonthOfYear(), initDate.getDayOfMonth());
		 return new DateTime(planTime);
	 }
	 
	
	/*
	@Wire
	private Selectbox speedSelectbox;
	
	private ListModel<String> speedList = new ListModelList<String>();
	
	public ListModel<String> getSpeedList() {
		return speedList;
	}
	
	public SpeedDateController() {
		((ListModelList<String>) speedList).add("1X");
		((ListModelList<String>) speedList).add("4X");
		((ListModelList<String>) speedList).add("8X");
		((ListModelList<String>) speedList).add("16X");
		((ListModelList<String>) speedList).add("32X");
		((ListModelList<String>) speedList).add("64X");
		//Default
		((ListModelList<String>)speedList).addToSelection("1X");
	}
	
	@Listen("onSelect = #speedSelectbox")
    public void changeSpeed() {
        Set<String> speeds = ((ListModelList<String>)speedList).getSelection();
        String speedStr = speeds.iterator().next();
        int speed = Integer.parseInt(speedStr.substring(0, (speedStr.length()-1)));
        //setSpeed(type);
        showNotify("Changed to: " + speed + " X", speedSelectbox);
        System.out.println(speed);
    }
	*/
}