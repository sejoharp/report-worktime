package reporter;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class WorkedDay {
	private DateTime day;
	private long workedTime;
	private long undertime;
	private long overtime;

	public WorkedDay(DateTime day) {
		this.day = day;
	}

	public DateTime getDay() {
		return day;
	}

	public void addWorkedTime(Date startDate, Date stopDate) {
		DateTime start = new DateTime(startDate);
		DateTime stop = new DateTime(stopDate);
		Seconds duration = Seconds.secondsBetween(start, stop);
		workedTime += duration.getSeconds();
	}

	public void setDay(DateTime day) {
		this.day = day;
	}

	public long getWorkedTime() {
		return workedTime;
	}

	public void setWorkedTime(long workedTime) {
		this.workedTime = workedTime;
	}

	public long getUndertime() {
		return undertime;
	}

	public void setUndertime(long undertime) {
		this.undertime = undertime;
	}

	public long getOvertime() {
		return overtime;
	}

	public void setOvertime(long overtime) {
		this.overtime = overtime;
	}

	public String getDayAsString() {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("dd.MM.yyyy");
		return fmt.print(day);
	}

	public String getWorkedTimeAsSeconds() {
		return createTimeString(workedTime);
	}

	public String getOverTimeAsSeconds() {
		return createTimeString(overtime);
	}

	public String getUnderTimeAsSeconds() {
		return createTimeString(undertime);
	}

	public String createTimeString(long time) {
		if (time == 0)
			return "";
		else
			return getHours(time) + getMinutes(time);
	}

	public String getHours(long duration) {
		long hours = duration / 3600;
		return hours == 0 ? "" : hours + " Stunden ";
	}

	public String getMinutes(long duration) {
		long minutes = Math.round((duration % 3600) / 60.0);
		return minutes == 0 ? "" : minutes + " Minuten";
	}

	public void calculateDiff() {
		long diff = workedTime - 28800;
		if (diff < 0)
			undertime = diff * -1;
		else
			overtime = diff;
	}
}
