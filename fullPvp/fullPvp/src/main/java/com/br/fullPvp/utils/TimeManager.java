package com.br.fullPvp.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;

public class TimeManager {

	@Getter
	static TimeManager instance = new TimeManager();
	
	static long timeDiff = 0L;
	public static long getTimeDiff() {
		return timeDiff;
	}

	public String getTime(long expire) {
		String string = formatDifference((expire - System.currentTimeMillis()) / 1000L);
		if (string == null || string.isEmpty()) {
			string = "0 segundo";
		}
		return string;
	}

	public String formatDifference(long time) {
		if (time == 0L) {
			return "";
		}
		long day = TimeUnit.SECONDS.toDays(time);
		long hours = TimeUnit.SECONDS.toHours(time) - day * 24L;
		long minutes = TimeUnit.SECONDS.toMinutes(time) - TimeUnit.SECONDS.toHours(time) * 60L;
		long seconds = TimeUnit.SECONDS.toSeconds(time) - TimeUnit.SECONDS.toMinutes(time) * 60L;

		StringBuilder sb = new StringBuilder();
		if (day > 0L) {
			sb.append(day).append(" ").append("dias").append(" ");
		}
		if (hours > 0L) {
			sb.append(hours).append(" ").append("horas").append(" ");
		}
		if (minutes > 0L) {
			sb.append(minutes).append(" ").append("minutos").append(" ");
		}
		if (seconds > 0L) {
			sb.append(seconds).append(" ").append("segundos");
		}
		String diff = sb.toString();

		return diff.isEmpty() ? "0 " + "segundo" : diff;
	}

	public Long getTime(String string) {
		try {
			return parseDateDiff(string, true) + TimeUnit.SECONDS.toMillis(1);
		} catch (Exception e) {
		}
		return null;
	}

	public long parseDateDiff(String time, boolean future) throws Exception {
		Pattern timePattern = Pattern.compile(
				"(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?(?:([0-9]+)\\s*(?:s[a-z]*)?)?",
				2);
		Matcher m = timePattern.matcher(time);
		int years = 0;
		int months = 0;
		int weeks = 0;
		int days = 0;
		int hours = 0;
		int minutes = 0;
		int seconds = 0;
		boolean found = false;
		while (m.find()) {
			if ((m.group() != null) && (!m.group().isEmpty())) {
				for (int i = 0; i < m.groupCount(); i++) {
					if ((m.group(i) != null) && (!m.group(i).isEmpty())) {
						found = true;
						break;
					}
				}
				if (found) {
					if ((m.group(1) != null) && (!m.group(1).isEmpty())) {
						years = Integer.parseInt(m.group(1));
					}
					if ((m.group(2) != null) && (!m.group(2).isEmpty())) {
						months = Integer.parseInt(m.group(2));
					}
					if ((m.group(3) != null) && (!m.group(3).isEmpty())) {
						weeks = Integer.parseInt(m.group(3));
					}
					if ((m.group(4) != null) && (!m.group(4).isEmpty())) {
						days = Integer.parseInt(m.group(4));
					}
					if ((m.group(5) != null) && (!m.group(5).isEmpty())) {
						hours = Integer.parseInt(m.group(5));
					}
					if ((m.group(6) != null) && (!m.group(6).isEmpty())) {
						minutes = Integer.parseInt(m.group(6));
					}
					if ((m.group(7) == null) || (m.group(7).isEmpty())) {
						break;
					}
					seconds = Integer.parseInt(m.group(7));

					break;
				}
			}
		}
		if (!found) {
			throw new Exception("Illegal Date");
		}
		if (years > 30) {
			throw new Exception("Illegal Date");
		}
		Calendar c = new GregorianCalendar();
		if (years > 0) {
			c.add(1, years * (future ? 1 : -1));
		}
		if (months > 0) {
			c.add(2, months * (future ? 1 : -1));
		}
		if (weeks > 0) {
			c.add(3, weeks * (future ? 1 : -1));
		}
		if (days > 0) {
			c.add(5, days * (future ? 1 : -1));
		}
		if (hours > 0) {
			c.add(11, hours * (future ? 1 : -1));
		}
		if (minutes > 0) {
			c.add(12, minutes * (future ? 1 : -1));
		}
		if (seconds > 0) {
			c.add(13, seconds * (future ? 1 : -1));
		}
		return c.getTimeInMillis();
	}
}