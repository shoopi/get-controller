package main.java.nl.tue.ieis.get.simulation;

import java.util.ArrayList;
import java.util.List;

import main.java.de.ptv.intermodal.IMPoint;

import org.joda.time.DateTime;
import org.joda.time.Period;

public class SimulateTime {

	FindDistance fd = new FindDistance();
	
	public DateTime simulateFinishTime(IMPoint a, IMPoint b, int averageSpeed, DateTime start) {
		DateTime finishTime = new DateTime();

		@SuppressWarnings("static-access")
		double distance = fd.distance(a.getLatitude(), a.getLongitude(), b.getLatitude(), b.getLongitude(), 'K');

		
		double hourDuration = (double)(distance/ (double)averageSpeed);
		int seconds = (int)Math.ceil(hourDuration * 60 * 60);
		
		finishTime = start.plusSeconds(seconds);
		
		return finishTime;
	}
	
	public List<DateTime> changeDateTimeToCurrent(List<DateTime> inputs, DateTime now) {
		DateTime initial = inputs.get(0);
		Period period = new Period(initial, now);
		List<DateTime> output = new ArrayList<DateTime>();
		for(DateTime dt : inputs) {
			DateTime temp = dt.plus(period);
			output.add(temp);
		}
		return output;
	}
	/*
	public static void main (String[] args) {
		Point a = new Point(50.5725212097168, 8.54597282409668);
		Point b = new Point(49.0219726, 8.4628152);
		DateTime start = new DateTime();
		int averageSpeed = 100;
		DateTime finish = simulateFinishTime(a, b, averageSpeed, start);
		System.out.println("Start time is: " + start + "\n Finish Time is: " + finish);
	}
	*/
	/*
	public int calculatePassedPercentage(ExecutedPoint a, PlannedPoint b, int averageSpeed) {
		double distance = fd.distance(a.getPoint().getX(), a.getPoint().getY(), b.getPoint().getX(), b.getPoint().getY(), 'K');
		Duration dur = new Duration(b.getPlannedTime(),a.getArrivedDateTime());
		double hourDur = dur.getStandardHours();
		return (Integer) null;
	}
	*/
}
 