package hsproject.impl;

import hsproject.util.InsertUtil;
import hsproject.util.ValueTransMethod;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class PrjReportImpl {
	/**
	 * 新建项目报表字段配置
	 * 
	 * @param prjtype
	 * @param userid
	 * @param showMap
	 * @param queryMap
	 * @param reportname
	 * @return
	 */
	public String addPrjConfig(String prjtype, String userid,
			Map<String, String> showMap, Map<String, String> queryMap,
			String reportname) {
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormate = new SimpleDateFormat("HH:mm");
		String nowDate = dateFormate.format(new Date());
		String nowTime = timeFormate.format(new Date());
		RecordSet rs = new RecordSet();
		InsertUtil iu = new InsertUtil();
		String mainid = "";
		String sql = "";
		Map<String, String> map = new HashMap<String, String>();
		map.put("prjtype", prjtype);
		map.put("createrperson", userid);
		map.put("createdate", nowDate);
		map.put("createtime", nowTime);
		map.put("reportname", reportname);
		iu.insert(map, "uf_prj_report_conf");
		sql = "select max(id) as mainid from uf_prj_report_conf where prjtype='"
				+ prjtype + "'";
		rs.executeSql(sql);
		if (rs.next()) {
			mainid = Util.null2String(rs.getString("mainid"));
		}
		if ("".equals(mainid)) {
			return "";
		}
		Iterator<String> show = showMap.keySet().iterator();
		while (show.hasNext()) {
			String fieldname = show.next();
			String fieldvalue = showMap.get(fieldname);
			String dsporder=fieldvalue.substring(fieldvalue.lastIndexOf("_")+1);
			fieldvalue =fieldvalue.substring(0,fieldvalue.lastIndexOf("_"));
			String iscommon = "0";
			map = new HashMap<String, String>();
			if (fieldname.indexOf("def_") >= 0) {
				iscommon = "1";
				fieldname = fieldname.replace("def_", "");
			}
			map.put("fieldid", fieldvalue);
			map.put("fieldname", fieldname);
			map.put("iscommon", iscommon);
			map.put("ishow", "0");
			map.put("isquery", "1");
			map.put("dsporder", dsporder);
			map.put("mainid", mainid);
			iu.insert(map, "uf_prj_report_conf_dt1");
		}

		Iterator<String> query = queryMap.keySet().iterator();
		while (query.hasNext()) {
			String fieldname = query.next();
			String fieldvalue = queryMap.get(fieldname);
			String dsporder=fieldvalue.substring(fieldvalue.lastIndexOf("_")+1);
			fieldvalue =fieldvalue.substring(0,fieldvalue.lastIndexOf("_"));
			String iscommon = "0";
			map = new HashMap<String, String>();
			if (fieldname.indexOf("def_") >= 0) {
				iscommon = "1";
				fieldname = fieldname.replace("def_", "");
			}
			String dtid = checkIsexist(mainid, fieldvalue);
			if (!"-1".equals(dtid)) {
				map.put("isquery", "0");
				iu.updateGen(map, "uf_prj_report_conf_dt1", "id", dtid);
			} else {
				map.put("fieldid", fieldvalue);
				map.put("fieldname", fieldname);
				map.put("iscommon", iscommon);
				map.put("ishow", "1");
				map.put("isquery", "0");
				map.put("dsporder", dsporder);
				map.put("mainid", mainid);
				iu.insert(map, "uf_prj_report_conf_dt1");
			}
		}
		sql = "update uf_prj_report_conf set reporturl='/hsproject/project/report/hs-show-prj-report-Url.jsp?reportid="
				+ mainid + "' where id=" + mainid;
		rs.executeSql(sql);

		return mainid;
	}

	/**
	 * 编辑项目报表数据
	 * 
	 * @param reportid
	 * @param showMap
	 * @param queryMap
	 * @param reportname
	 */
	public void editPrjConfig(String reportid, Map<String, String> showMap,
			Map<String, String> queryMap, String reportname) {

		RecordSet rs = new RecordSet();
		InsertUtil iu = new InsertUtil();
		String sql = "";
		Map<String, String> map = new HashMap<String, String>();
		if ("".equals(reportid)) {
			return;
		}
		map.put("reportname", reportname);
		iu.updateGen(map, "uf_prj_report_conf", "id", reportid);

		sql = "delete from uf_prj_report_conf_dt1 where mainid=" + reportid;
		rs.executeSql(sql);

		Iterator<String> show = showMap.keySet().iterator();
		while (show.hasNext()) {
			String fieldname = show.next();
			String fieldvalue = showMap.get(fieldname);
			String dsporder=fieldvalue.substring(fieldvalue.lastIndexOf("_")+1);
			fieldvalue =fieldvalue.substring(0,fieldvalue.lastIndexOf("_"));
			String iscommon = "0";
			map = new HashMap<String, String>();
			if (fieldname.indexOf("def_") >= 0) {
				iscommon = "1";
				fieldname = fieldname.replace("def_", "");
			}
			map.put("fieldid", fieldvalue);
			map.put("fieldname", fieldname);
			map.put("iscommon", iscommon);
			map.put("ishow", "0");
			map.put("isquery", "1");
			map.put("mainid", reportid);
			map.put("dsporder", dsporder);
			iu.insert(map, "uf_prj_report_conf_dt1");
		}

		Iterator<String> query = queryMap.keySet().iterator();
		while (query.hasNext()) {
			String fieldname = query.next();
			String fieldvalue = queryMap.get(fieldname);
			String dsporder=fieldvalue.substring(fieldvalue.lastIndexOf("_")+1);
			fieldvalue =fieldvalue.substring(0,fieldvalue.lastIndexOf("_"));
			String iscommon = "0";
			map = new HashMap<String, String>();
			if (fieldname.indexOf("def_") >= 0) {
				iscommon = "1";
				fieldname = fieldname.replace("def_", "");
			}
			String dtid = checkIsexist(reportid, fieldvalue);
			if (!"-1".equals(dtid)) {
				map.put("isquery", "0");
				iu.updateGen(map, "uf_prj_report_conf_dt1", "id", dtid);
			} else {
				map.put("fieldid", fieldvalue);
				map.put("fieldname", fieldname);
				map.put("iscommon", iscommon);
				map.put("ishow", "1");
				map.put("isquery", "0");
				map.put("mainid", reportid);
				map.put("dsporder", dsporder);
				iu.insert(map, "uf_prj_report_conf_dt1");
			}
		}
	}

	/**
	 * 判断该字段在表中是否存在
	 * 
	 * @param mainid
	 * @param fieldvalue
	 * @return
	 */
	public String checkIsexist(String mainid, String fieldvalue) {
		RecordSet rs = new RecordSet();
		String dtid = "-1";
		String sql = "select id from uf_prj_report_conf_dt1 where mainid="
				+ mainid + " and fieldid='" + fieldvalue + "'";
		rs.executeSql(sql);
		if (rs.next()) {
			dtid = Util.null2String(rs.getString("id"));
		} else {
			dtid = "-1";
		}
		return dtid;
	}

	public String checkIsexistAll(String mainid, String fieldvalue) {
		RecordSet rs = new RecordSet();
		String dtid = "-1";
		String sql = "select id from uf_prj_all_reportmt_dt1 where mainid="
				+ mainid + " and fieldid='" + fieldvalue + "'";
		rs.executeSql(sql);
		if (rs.next()) {
			dtid = Util.null2String(rs.getString("id"));
		} else {
			dtid = "-1";
		}
		return dtid;
	}

	public void editAllPrjConfig(String reportid, Map<String, String> showMap,
			Map<String, String> queryMap, String reportname,
			List<Map<String, String>> dtlist) {

		RecordSet rs = new RecordSet();
		InsertUtil iu = new InsertUtil();
		String sql = "";
		Map<String, String> map = new HashMap<String, String>();
		if ("".equals(reportid)) {
			return;
		}
		map.put("reportname", reportname);
		iu.updateGen(map, "uf_prj_all_reportmt", "id", reportid);

		sql = "delete from uf_prj_all_reportmt_dt1 where mainid=" + reportid;
		rs.executeSql(sql);

		Iterator<String> show = showMap.keySet().iterator();
		while (show.hasNext()) {
			String fieldname = show.next();
			String fieldvalue = showMap.get(fieldname);
			String dsporder=fieldvalue.substring(fieldvalue.lastIndexOf("_")+1);
			fieldvalue =fieldvalue.substring(0,fieldvalue.lastIndexOf("_"));
			String iscommon = "0";
			fieldname = fieldname.replace("prj_", "");
			map = new HashMap<String, String>();
			if (fieldname.indexOf("def_") >= 0) {
				iscommon = "1";
				fieldname = fieldname.replace("def_", "");
			}
			map.put("fieldid", fieldvalue);
			map.put("fieldname", fieldname);
			map.put("iscommon", iscommon);
			map.put("ishow", "0");
			map.put("isquery", "1");
			map.put("mainid", reportid);
			map.put("dsporder", dsporder);
			map.put("fieldbelong", "0");
			iu.insert(map, "uf_prj_all_reportmt_dt1");
		}

		Iterator<String> query = queryMap.keySet().iterator();
		while (query.hasNext()) {
			String fieldname = query.next();
			String fieldvalue = queryMap.get(fieldname);
			String dsporder=fieldvalue.substring(fieldvalue.lastIndexOf("_")+1);
			fieldvalue =fieldvalue.substring(0,fieldvalue.lastIndexOf("_"));
			String iscommon = "0";
			fieldname = fieldname.replace("prj_", "");
			map = new HashMap<String, String>();
			if (fieldname.indexOf("def_") >= 0) {
				iscommon = "1";
				fieldname = fieldname.replace("def_", "");
			}
			String dtid = checkIsexistAll(reportid, fieldvalue);
			if (!"-1".equals(dtid)) {
				map.put("isquery", "0");
				iu.updateGen(map, "uf_prj_all_reportmt_dt1", "id", dtid);
			} else {
				map.put("fieldid", fieldvalue);
				map.put("fieldname", fieldname);
				map.put("iscommon", iscommon);
				map.put("ishow", "1");
				map.put("isquery", "0");
				map.put("mainid", reportid);
				map.put("dsporder", dsporder);
				map.put("fieldbelong", "0");
				iu.insert(map, "uf_prj_all_reportmt_dt1");
			}
		}

		for (Map<String, String> jdmap : dtlist) {
			String processid_mt = jdmap.get("processid_mt");
			Iterator<String> dtshow = jdmap.keySet().iterator();
			while (dtshow.hasNext()) {
				String fieldname = dtshow.next();
				String fieldvalue = jdmap.get(fieldname);
				if("processid_mt".equals(fieldname)){
					continue;
				}
				String dsporder=fieldvalue.substring(fieldvalue.lastIndexOf("_")+1);
				fieldvalue =fieldvalue.substring(0,fieldvalue.lastIndexOf("_"));
				String iscommon = "0";
				fieldname = fieldname.replace(processid_mt + "_", "");
				map = new HashMap<String, String>();
				if (fieldname.indexOf("def_") >= 0) {
					iscommon = "1";
					fieldname = fieldname.replace("def_", "");
				}
				map.put("fieldid", fieldvalue);
				map.put("fieldname", fieldname);
				map.put("iscommon", iscommon);
				map.put("ishow", "0");
				map.put("isquery", "1");
				map.put("mainid", reportid);
				map.put("fieldbelong", "1");
				map.put("dsporder", dsporder);
				map.put("processtype", processid_mt);
				iu.insert(map, "uf_prj_all_reportmt_dt1");
			}

		}
	}

	public String addAllPrjConfig(String prjtype, String userid,
			Map<String, String> showMap, Map<String, String> queryMap,
			String reportname, List<Map<String, String>> dtlist) {
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormate = new SimpleDateFormat("HH:mm");
		String nowDate = dateFormate.format(new Date());
		String nowTime = timeFormate.format(new Date());
		RecordSet rs = new RecordSet();
		InsertUtil iu = new InsertUtil();
		String mainid = "";
		String sql = "";
		Map<String, String> map = new HashMap<String, String>();
		map.put("prjtype", prjtype);
		map.put("createrperson", userid);
		map.put("createdate", nowDate);
		map.put("createtime", nowTime);
		map.put("reportname", reportname);
		iu.insert(map, "uf_prj_all_reportmt");
		sql = "select max(id) as mainid from uf_prj_all_reportmt where prjtype='"
				+ prjtype + "'";
		rs.executeSql(sql);
		if (rs.next()) {
			mainid = Util.null2String(rs.getString("mainid"));
		}
		if ("".equals(mainid)) {
			return "";
		}
		Iterator<String> show = showMap.keySet().iterator();
		while (show.hasNext()) {
			String fieldname = show.next();
			String fieldvalue = showMap.get(fieldname);
			String dsporder=fieldvalue.substring(fieldvalue.lastIndexOf("_")+1);
			fieldvalue =fieldvalue.substring(0,fieldvalue.lastIndexOf("_"));
			String iscommon = "0";
			fieldname = fieldname.replace("prj_", "");
			map = new HashMap<String, String>();
			if (fieldname.indexOf("def_") >= 0) {
				iscommon = "1";
				fieldname = fieldname.replace("def_", "");
			}
			map.put("fieldid", fieldvalue);
			map.put("fieldname", fieldname);
			map.put("iscommon", iscommon);
			map.put("ishow", "0");
			map.put("isquery", "1");
			map.put("mainid", mainid);
			map.put("dsporder", dsporder);
			map.put("fieldbelong", "0");
			iu.insert(map, "uf_prj_all_reportmt_dt1");
		}

		Iterator<String> query = queryMap.keySet().iterator();
		while (query.hasNext()) {
			String fieldname = query.next();
			String fieldvalue = queryMap.get(fieldname);
			String dsporder=fieldvalue.substring(fieldvalue.lastIndexOf("_")+1);
			fieldvalue =fieldvalue.substring(0,fieldvalue.lastIndexOf("_"));
			String iscommon = "0";
			fieldname = fieldname.replace("prj_", "");
			map = new HashMap<String, String>();
			if (fieldname.indexOf("def_") >= 0) {
				iscommon = "1";
				fieldname = fieldname.replace("def_", "");
			}
			String dtid = checkIsexistAll(mainid, fieldvalue);
			if (!"-1".equals(dtid)) {
				map.put("isquery", "0");
				iu.updateGen(map, "uf_prj_all_reportmt_dt1", "id", dtid);
			} else {
				map.put("fieldid", fieldvalue);
				map.put("fieldname", fieldname);
				map.put("iscommon", iscommon);
				map.put("ishow", "1");
				map.put("isquery", "0");
				map.put("mainid", mainid);
				map.put("dsporder", dsporder);
				map.put("fieldbelong", "0");
				iu.insert(map, "uf_prj_all_reportmt_dt1");
			}
		}
		for (Map<String, String> jdmap : dtlist) {
			String processid_mt = jdmap.get("processid_mt");
			Iterator<String> dtshow = jdmap.keySet().iterator();
			while (dtshow.hasNext()) {
				String fieldname = dtshow.next();
				String fieldvalue = jdmap.get(fieldname);
				if("processid_mt".equals(fieldname)){
					continue;
				}
			
				String dsporder=fieldvalue.substring(fieldvalue.lastIndexOf("_")+1);
				fieldvalue =fieldvalue.substring(0,fieldvalue.lastIndexOf("_"));
				String iscommon = "0";
				fieldname = fieldname.replace(processid_mt + "_", "");
				map = new HashMap<String, String>();
				if (fieldname.indexOf("def_") >= 0) {
					iscommon = "1";
					fieldname = fieldname.replace("def_", "");
				}
				map.put("fieldid", fieldvalue);
				map.put("fieldname", fieldname);
				map.put("iscommon", iscommon);
				map.put("ishow", "0");
				map.put("isquery", "1");
				map.put("mainid", mainid);
				map.put("fieldbelong", "1");
				map.put("dsporder", dsporder);
				map.put("processtype", processid_mt);
				iu.insert(map, "uf_prj_all_reportmt_dt1");
			}

		}
		sql = "update uf_prj_all_reportmt set reporturl='/hsproject/project/report/hs-show-prj-all-report-Url.jsp?reportid="
				+ mainid + "' where id=" + mainid;
		rs.executeSql(sql);

		return mainid;
	}

	/**
	 * 报表数据显示转换
	 * 
	 * @param value
	 * @param fieldid
	 * @return
	 */
	public String prjReportTransMethod(String value, String fieldid) {
		ValueTransMethod vtm = new ValueTransMethod();
		String result = vtm.doTrans(fieldid, value, "0");
		return result;
	}

	public String processReportTransMethod(String value, String fieldid) {
		// BaseBean log = new BaseBean();
		ValueTransMethod vtm = new ValueTransMethod();
		String result = vtm.doTrans(fieldid, value, "1");
		return result;
	}
	 public  String removeHtmlTag(String content) {
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
