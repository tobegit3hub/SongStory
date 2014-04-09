package com.aliceinwc.lyrics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lyric {
	private final long serialVersionUID = 20071125L;
	private String HOME = "/sdcard/SongStory/Music";
	private static Logger log = Logger.getLogger(Lyric.class.getName());
	private int width;
	private int height;
	private long time;
	private long tempTime;
	public List<Sentence> list = new ArrayList<Sentence>();
	private int currentIndex;
	private boolean isMoving;
	private boolean initDone;
	private transient File file;
	private boolean enabled = true;
	private long during = Integer.MAX_VALUE;
	private int offset; // ���ƫ����

	private static final Pattern pattern = Pattern
			.compile("(?<=\\[).*?(?=\\])");

	public Lyric(File file) {
		// Log.e("======file name====", "Lyric file:"+file);
		this.file = file;
		init(file);
		initDone = true;
	}

	public void setEnable(boolean b) {
		this.enabled = b;
	}

	public File getLyricFile() {

		return file;
	}

	public void adjustTime(int time) {
		if (list.size() == 1) {
			return;
		}
		offset += time;

	}

	private void init(File file) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					file), "GBK"));
			StringBuilder sb = new StringBuilder();
			String temp = null;
			while ((temp = br.readLine()) != null) {

				sb.append(temp).append("\n");
			}

			init(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void init(String content) {
		if (content == null || content.trim().equals("")) {
			list.add(new Sentence("nothing"));
			return;
		}
		try {
			BufferedReader br = new BufferedReader(new StringReader(content));
			String temp = null;
			while ((temp = br.readLine()) != null) {
				parseLine(temp.trim());
				// Log.e("**********","�ָ���");
			}

			br.close();
			Collections.sort(list, new Comparator<Sentence>() {
				@Override
				public int compare(Sentence o1, Sentence o2) {
					return (int) (o1.getFromTime() - o2.getFromTime());
				}
			});
			int size = list.size();
			for (int i = 0; i < size; i++) {
				Sentence next = null;
				if (i + 1 < size) {
					next = list.get(i + 1);
				}
				Sentence now = list.get(i);
				if (next != null) {
					now.setToTime(next.getFromTime() - 1);
				}
			}
			if (list.size() == 1) {
				list.get(0).setToTime(Integer.MAX_VALUE);
			} else {
				Sentence last = list.get(list.size() - 1);
				last.setToTime(1000 * 1000);// ��Ҫ��
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private int parseOffset(String str) {
		String[] ss = str.split("\\:");
		if (ss.length == 2) {
			if (ss[0].equalsIgnoreCase("offset")) {
				int os = Integer.parseInt(ss[1]);
				// Log.e("======the offset ==", ":"+os);
				return os;
			} else {
				return Integer.MAX_VALUE;
			}
		} else {
			return Integer.MAX_VALUE;
		}
	}

	private void parseLine(String line) {
		// TODO Auto-generated method stub
		if (line.equals("")) {
			return;
		}
		Matcher matcher = pattern.matcher(line);
		List<String> temp = new ArrayList<String>();
		int lastIndex = -1;
		int lastLength = -1;
		while (matcher.find()) {
			String s = matcher.group();
			int index = line.indexOf("[" + s + "]");
			if (lastIndex != -1 && index - lastIndex > lastLength + 2) {
				String content = line.substring(lastIndex + lastLength + 2,
						index);
				for (String str : temp) {
					long t = parseTime(str);
					if (t != -1) {
						list.add(new Sentence(content, t));
					}
				}
				temp.clear();
			}
			temp.add(s);
			lastIndex = index;
			lastLength = s.length();
		}
		if (temp.isEmpty()) {
			return;
		}
		try {
			int length = lastLength + 2 + lastIndex;
			String content = line.substring(length > line.length() ? line
					.length() : length);
			if (content.equals("") && offset == 0) {
				for (String s : temp) {
					int of = parseOffset(s);
					if (of != Integer.MAX_VALUE) {
						offset = of;
						break;
					}
				}
				return;
			}
			for (String s : temp) {
				long t = parseTime(s);
				if (t != -1) {
					list.add(new Sentence(content, t));
					// Log.e("=====lyric====", content);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private long parseTime(String time) {

		String[] ss = time.split("\\:|\\.");
		if (ss.length < 2) {
			return -1;
		} else if (ss.length == 2) {
			try {
				if (offset == 0 && ss[0].equalsIgnoreCase("offset")) {
					offset = Integer.parseInt(ss[1]);
					return -1;
				}
				int min = Integer.parseInt(ss[0]);
				int sec = Integer.parseInt(ss[1]);
				if (min < 0 || sec < 0 || sec >= 60) {
					throw new RuntimeException("���ֲ��Ϸ�");
				}
				return (min * 60 + sec) * 1000L;
			} catch (Exception e) {
				return -1;
			}
		} else if (ss.length == 3) {
			try {
				int min = Integer.parseInt(ss[0]);
				int sec = Integer.parseInt(ss[1]);
				int mm = Integer.parseInt(ss[2]);
				if (min < 0 || sec < 0 || sec > 60 || mm < 0 || mm > 99) {
					throw new RuntimeException("���ֲ��Ϸ�");
				}
				return (min * 60 + sec) * 1000L + min * 10;
			} catch (Exception e) {
				return -1;
			}
		} else {
			return -1;
		}

	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isInitDone() {
		return initDone;
	}

	int getNowSentenceIndex(long t) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).isInTime(t)) {
				return i;
			}
		}
		return -1;
	}

	public boolean canMove() {
		return list.size() > 1 && enabled;
	}

	public long getTime() {
		return tempTime;
	}

	private void checkTempTime() {
		if (tempTime < 0) {
			tempTime = 0;
		} else if (tempTime > during) {
			tempTime = during;
		}
	}

	public void stopMove() {
		isMoving = false;
	}

}
