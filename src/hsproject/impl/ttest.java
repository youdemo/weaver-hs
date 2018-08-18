package hsproject.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ttest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTime = sf.format(new Date());
		System.out.println(nowTime);
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
