package hsproject.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.org.apache.bcel.internal.generic.NEW;

public class ttest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormate = new SimpleDateFormat("HH:mm");
		System.out.println(sf.format(new Date()));
		String nowDate = dateFormate.format(new Date());
		String nowTime = timeFormate.format(new Date());
		String month = nowDate.substring(5,7).replaceAll("^(0+)", "");
		String day = nowDate.substring(8,10).replaceAll("^(0+)", "");
		String hour = nowTime.substring(0, 2).replaceAll("^(0+)", "");
		if("".equals(hour)) {
			hour = "0";
		}
		System.out.println("2018-09-12".compareTo("2018-09-13"));
		String phoneString = "ZAA-A20180119";
		Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(phoneString);
        String all = matcher.replaceAll("");
        System.out.println(all);
	}
	 public static String removeHtmlTag(String content) {
			Pattern p = Pattern.compile("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>");
			Matcher m = p.matcher(content);
			if (m.find()) {
				content = content
						.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>", "$2");
				content = removeHtmlTag(content);
			}
			return content;
		}
}
