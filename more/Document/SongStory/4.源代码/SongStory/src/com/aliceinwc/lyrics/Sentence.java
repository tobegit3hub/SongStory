package com.aliceinwc.lyrics;

import java.io.Serializable;

public class Sentence implements Serializable {

	private static final long serialVersionUID = 20071125L;
	private long fromTime;
	private long toTime;
	private String content;
	private final static long DISAPPEAR_TIME = 1000L;

	public Sentence(String content, long fromTime, long toTime) {
		this.content = content;
		this.fromTime = fromTime;
		this.toTime = toTime;
	}

	public Sentence(String content, long fromTime) {
		this(content, fromTime, 0);
	}

	public Sentence(String content) {
		this(content, 0, 0);
	}

	public long getFromTime() {
		return fromTime;
	}

	public void setFromTime(long fromTime) {
		this.fromTime = fromTime;
	}

	public long getToTime() {
		return toTime;
	}

	public void setToTime(long toTime) {
		this.toTime = toTime;
	}

	public boolean isInTime(long time) {
		return time >= fromTime && time <= toTime;
	}

	public String getContent() {
		return content;
	}

	public long getDuring() {
		return toTime - fromTime;
	}

	@Override
	public String toString() {
		return "{" + fromTime + "(" + content + ")" + toTime + "}";
	}

}
