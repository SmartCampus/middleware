package fr.unice.smart_campus.middleware;


public class TimeRange {

	long first;
	long second;

	public TimeRange (long first) {
		this.first = first;
		this.second = 0L;
	}

	public TimeRange (long first, long second) {
		this.first = first;
		this.second = second;
	}

	public long getFirst () {
		return first;
	}

	public long getSecond () {
		return second;
	}
}
