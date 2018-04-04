package test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import weaver.hrm.resource.ResourceComInfo;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = dateFormate.format(new Date());
		System.out.println(nowDate.substring(5,7));
		StringBuffer xml = new StringBuffer();
		xml.append("aaaa").append("\n").append("bbbb").append("vvv");
		System.out.println(xml.toString());
	}
	
	public static Map<String,String> getFieldStr(String str){
		Map<String,String> map = new HashMap<String, String>();
		int indexstart=str.indexOf("##");
		if(indexstart <0){
			map.put("fieldname", "");
			map.put("index", "-1");
			return map;
		}
		int indexend=str.substring(indexstart+2,str.length()).indexOf("##");
		if(indexend<0){
			map.put("fieldname", "");
			map.put("index", "-2");
			return map;
		}
		String fieldname=str.substring(indexstart+2,indexstart+2+indexend);
		map.put("fieldname", fieldname);
		map.put("index", indexstart+2+indexend+2+"");
		return map;
	}

}
