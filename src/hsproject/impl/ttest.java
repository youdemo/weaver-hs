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
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:MM");
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormate = new SimpleDateFormat("HH:mm");
		
		String nowDate = dateFormate.format(new Date());
		String nowTime = timeFormate.format(new Date());
		String month = nowDate.substring(5,7).replaceAll("^(0+)", "");
		String day = nowDate.substring(8,10).replaceAll("^(0+)", "");
		String hour = nowTime.substring(0, 2).replaceAll("^(0+)", "");
		if("".equals(hour)) {
			hour = "0";
		}
		hour = String.valueOf(Integer.valueOf(hour)+1);
		ConvertPDFTools aa = new ConvertPDFTools();
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
