package test;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import weaver.docs.docs.DocComInfo;
import weaver.general.Util;
import weaver.hrm.resource.ResourceComInfo;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map<String, String> map = new HashMap<String, String>();
		System.out.println(Util.null2String(map.get("123")));
		DecimalFormat decimalFormat=new DecimalFormat(".00");
		String p=decimalFormat.format(1.1);
		System.out.println(p);
		
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
