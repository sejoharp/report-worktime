package reporter.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table( name = "intervals" )
public class Interval {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private Date start;
	private Date stop;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getStop() {
		return stop;
	}

	public void setStop(Date stop) {
		this.stop = stop;
	}

	@Override
	public String toString() {
		return "Interval [id=" + id + ", start=" + start + ", stop=" + stop
				+ "]";
	}
}
