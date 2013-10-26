package reporter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.joda.time.DateTime;
import reporter.model.Interval;

/**
 * Hello world!
 * 
 */
public class App {
	public static void main(String[] args) {
		Configuration configuration = new Configuration();
		configuration.addAnnotatedClass(Interval.class);
		configuration.configure();
		ServiceRegistry sr = new ServiceRegistryBuilder().applySettings(
				configuration.getProperties()).buildServiceRegistry();
		SessionFactory sf = configuration.buildSessionFactory(sr);
		Session session = sf.openSession();
		Query query = session
				.createQuery("from Interval where start between '2013-07-01' and '2013-09-30'");
		List<Interval> intervals = query.list();
		session.close();
		createFile(intervals);
	}

	public static String generateTable(List<WorkedDay> workdays) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<html><head></head><body><table border='1'>");
		stringBuilder.append("<tr>");
		stringBuilder.append("<th>datum</th>");
		stringBuilder.append("<th>gearbeitete Zeit</th>");
		stringBuilder.append("<th>zu wenig gearbeitet</th>");
		stringBuilder.append("<th>zu viel gearbeitet</th>");
		stringBuilder.append("</tr>");
		for (WorkedDay workday : workdays) {
			stringBuilder.append("<tr>");

			stringBuilder.append("<td>");
			stringBuilder.append(workday.getDayAsString());
			stringBuilder.append("</td>");

			stringBuilder.append("<td>");
			stringBuilder.append(workday.getWorkedTimeAsSeconds());
			stringBuilder.append("</td>");

			stringBuilder.append("<td>");
			stringBuilder.append(workday.getUnderTimeAsSeconds());
			stringBuilder.append("</td>");

			stringBuilder.append("<td>");
			stringBuilder.append(workday.getOverTimeAsSeconds());
			stringBuilder.append("</td>");

			stringBuilder.append("</tr>");
		}
		stringBuilder.append("</table></body></html>");
		return stringBuilder.toString();
	}

	public static void createFile(List<Interval> intervals) {
		Path path = Paths.get("result.html");
		try (BufferedWriter writer = Files.newBufferedWriter(path,
				StandardCharsets.UTF_8)) {
			writer.write(generateTable(calculateWorkdays(intervals)));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	public static List<WorkedDay> calculateWorkdays(List<Interval> intervals) {
		List<WorkedDay> workdays = new ArrayList<>();
		DateTime firstDay = new DateTime(intervals.get(0).getStart())
				.withTimeAtStartOfDay();
		WorkedDay workday = new WorkedDay(firstDay);
		for (Interval interval : intervals) {
			DateTime day = new DateTime(interval.getStart())
					.withTimeAtStartOfDay();
			if (!workday.getDay().isEqual(day)) {
				workday.calculateDiff();
				workdays.add(workday);

				workday = new WorkedDay(day);
			}
			workday.addWorkedTime(interval);
		}
		workday.calculateDiff();
		workdays.add(workday);
		return workdays;
	}
}
