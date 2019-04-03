package hsproject.impl;

import hsproject.bean.PurchaseMoneyMtBean;
import hsproject.bean.UsedMoneyOutDataMtBean;
import hsproject.dao.ProjectInfoDao;
import hsproject.util.InsertUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;
import weaver.general.Util;

public class SysnPurchaseMoneyImpl {
	BaseBean log = new BaseBean();

	/**
	 * 获取这次需要同步的配置idS
	 * 
	 * @param month
	 * @param day
	 * @param hour
	 * @param min
	 * @return
	 */
	public List<PurchaseMoneyMtBean> getSysnMtIds(String month, String day,
			String hour) {
		String sql = "";
		String id = "";
		String datasource = "";
		String prjtype = "";
		RecordSet rs = new RecordSet();
		BaseBean log = new BaseBean();
		List<PurchaseMoneyMtBean> list = new ArrayList<PurchaseMoneyMtBean>();
		if ("".equals(month) || "".equals(day) || "".equals(hour)) {
			return list;
		}

		sql = "select *  from uf_prj_syspur_mt where (month='" + month
				+ "' or month='0') and (day='" + day
				+ "' or day='0') and (hour='" + hour
				+ "' or hour='0') and isused='1' ";

		// log.writeLog("getSysnMtIds sql"+sql);
		rs.executeSql(sql);
		while (rs.next()) {
			id = Util.null2String(rs.getString("id"));
			datasource = Util.null2String(rs.getString("datasource"));
			if (!checkExists("datasourcesetting", "id", datasource)) {
				log.writeLog("该同步配置数据源错误 id:" + id + " datasource:"
						+ datasource);
				continue;
			}
			

			PurchaseMoneyMtBean odmb = new PurchaseMoneyMtBean();
			odmb.setId(id);
			odmb.setMark(Util.null2String(rs.getString("mark")));
			odmb.setDatasource(datasource);
			odmb.setMapsql(Util.null2String(rs.getString("mapsql")));
			odmb.setDescription(Util.null2String(rs.getString("description")));
			odmb.setIsused(Util.null2String(rs.getString("isused")));
			odmb.setMonth(Util.null2String(rs.getString("month")));
			odmb.setDay(Util.null2String(rs.getString("day")));
			odmb.setHour(Util.null2String(rs.getString("hour")));
			list.add(odmb);
		}
		return list;
	}

	/**
	 * 检验值是否存在
	 * 
	 * @param tablename
	 * @param key
	 * @param keyValue
	 * @return
	 */
	public boolean checkExists(String tablename, String key, String keyValue) {
		RecordSet rs = new RecordSet();
		String sql = "";
		int count = 0;
		sql = "select count(1) as count from " + tablename + " where " + key
				+ "='" + keyValue + "'";
		// log.writeLog("check sql:"+sql);
		rs.executeSql(sql);
		if (rs.next()) {
			count = rs.getInt("count");
		}
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 同步数据
	 * 
	 * @param odmb
	 * @param nowDate
	 * @param nowTime
	 */
	public void sysnData(PurchaseMoneyMtBean odmb, String nowDate,
			String nowTime) {
		String dataSourceFlag = "";
		RecordSet rs = new RecordSet();
		String sql = "";
		String fieldname = "";
		String fieldvalue = "";
		String datasource = odmb.getDatasource();
		String transql = odmb.getMapsql();
		dataSourceFlag = getDataSourceFlag(datasource);
		if ("".equals(dataSourceFlag)) {
			return;
		}
		Map<String, String> checkMap = checkMapSql(transql);
		String checkResult = checkMap.get("result");
		String fieldNames = checkMap.get("fieldNames");
		// log.writeLog("checkResult:"+checkResult);
		// log.writeLog("fieldNames:"+fieldNames);
		if ("-1".equals(checkResult)) {
			return;
		}

		String prjid = "";
		String procode = "";
		sql = "select * from hs_projectinfo ";
		// log.writeLog(sql);
		rs.executeSql(sql);
		while (rs.next()) {
			String mapsql=transql;
			fieldname = "";
			fieldvalue = "";
			prjid = Util.null2String(rs.getString("id"));
			procode = Util.null2String(rs.getString("procode"));
			// log.writeLog("prjid:"+prjid);
			ProjectInfoDao pid = new ProjectInfoDao();
			Map<String, String> pidComMap = pid.getProjetCommonData(prjid);
			Map<String, String> pidDefMap = pid.getProjetDefineData(prjid);
			String fieldArr[] = fieldNames.split(",");
			for (int i = 0; i < fieldArr.length; i++) {
				fieldname = Util.null2String(fieldArr[i]);
				if ("".equals(fieldname)) {
					continue;
				}
				if (fieldname.indexOf("def_") >= 0) {
					fieldvalue = pidDefMap.get(fieldname.replace("def_", ""));
				} else {
					fieldvalue = pidComMap.get(fieldname);
				}
				fieldname = "##" + fieldname + "##";
				mapsql = mapsql.replace(fieldname, fieldvalue);
				
			}
			updateOrInsertInfo(dataSourceFlag,mapsql,prjid);
		    //log.writeLog("mapsql:"+mapsql);
		   
		}

	}

	
	public void updateOrInsertInfo(String dataSourceFlag,
			String mapsql,String prjid) {
		RecordSet rs = new RecordSet();
		RecordSetDataSource rsd = new RecordSetDataSource(dataSourceFlag);
		String sql = mapsql;
		String sql_dt = "";
		String Wzmc = "";
		String ggxh = "";
		String dj = "";
		String sl = "";
		String zj = "";
		String gysname = "";
		String htbh = "";
		String sgbh = "";
		String sgid = "";
		String billid = "";
		rsd.executeSql(sql);
		if(rsd.next()){
			sql_dt = "update uf_prj_purchaselist set issys='1' where prjid="+prjid;
		
			rs.executeSql(sql_dt);
		}
		rsd.executeSql(sql);
		while(rsd.next()){
			Wzmc = Util.null2String(rsd.getString("Wzmc")).replace("'","'||chr(39)||'").replace("&","'||chr(38)||'");
			ggxh = Util.null2String(rsd.getString("ggxh")).replace("'","'||chr(39)||'").replace("&","'||chr(38)||'");
			dj = Util.null2String(rsd.getString("dj")).replace("'","'||chr(39)||'").replace("&","'||chr(38)||'");
			sl = Util.null2String(rsd.getString("sl")).replace("'","'||chr(39)||'").replace("&","'||chr(38)||'");
			zj = Util.null2String(rsd.getString("zj")).replace("'","'||chr(39)||'").replace("&","'||chr(38)||'");
			gysname = Util.null2String(rsd.getString("gysname")).replace("'","'||chr(39)||'").replace("&","'||chr(38)||'");
			htbh = Util.null2String(rsd.getString("htbh")).replace("'","'||chr(39)||'").replace("&","'||chr(38)||'");
			sgbh = Util.null2String(rsd.getString("sgbh")).replace("'","'||chr(39)||'").replace("&","'||chr(38)||'");
			sgid = Util.null2String(rsd.getString("sgid")).replace("'","'||chr(39)||'").replace("&","'||chr(38)||'");
			sql_dt = "select id from uf_prj_purchaselist where prjid="+prjid+" and Wzmc='"+Wzmc+"' and ggxh='"+ggxh+"' and dj='"+dj+"' and sl='"+sl+"' and zj='"+zj+"' and gysname='"+gysname+"' and htbh='"+htbh+"' and sgbh='"+sgbh+"' and sgid='"+sgid+"'";
			if("916".equals(prjid)) {
				log.writeLog("aaa "+sql_dt);
			}
			rs.executeSql(sql_dt);
			if(rs.next()){
				billid = Util.null2String(rs.getString("id"));
			}
			if("".equals(billid)){
				sql_dt = "insert into uf_prj_purchaselist(prjid,Wzmc,ggxh,dj,sl,zj,gysname,htbh,issys,sgbh,sgid) values('"+prjid+"','"+Wzmc+"','"+ggxh+"','"+dj+"','"+sl+"','"+zj+"','"+gysname+"','"+htbh+"','0','"+sgbh+"','"+sgid+"')";
				rs.executeSql(sql_dt);
			}else{
				sql_dt = "update uf_prj_purchaselist set issys='"+0+"' where id="+billid;
				rs.executeSql(sql_dt);
			}
		}
		sql_dt = "delete from uf_prj_purchaselist where issys='1' and prjid="+prjid;
		rs.executeSql(sql_dt);
		
		
	}

	/**
	 * 获取数据源标识
	 * 
	 * @param sourceid
	 * @return
	 */
	public String getDataSourceFlag(String sourceid) {
		String datasourcename = "";
		RecordSet rs = new RecordSet();
		String sql = "select datasourcename from datasourcesetting where id="
				+ sourceid;
		rs.executeSql(sql);
		if (rs.next()) {
			datasourcename = Util.null2String(rs.getString("datasourcename"));
		}
		return datasourcename;
	}

	/**
	 * 检查sql条件字段是否正确
	 * 
	 * @param mapSql
	 * @param prjtype
	 * @return
	 */
	public Map<String, String> checkMapSql(String mapSql) {
		String tranSql = mapSql;
		boolean flag = true;
		boolean checkResult = true;
		String falg2 = "";
		String fieldname = "";
		String fieldNames = "";
		String str = "";
		int index = 0;
		Map<String, String> resultMap = new HashMap<String, String>();
		str = mapSql;
		if (tranSql.indexOf("Wzmc") < 0 || tranSql.indexOf("ggxh") < 0|| tranSql.indexOf("dj") < 0|| tranSql.indexOf("sl") < 0|| tranSql.indexOf("zj") < 0|| tranSql.indexOf("gysname") < 0|| tranSql.indexOf("htbh") < 0) {
			resultMap.put("fieldNames", fieldNames);
			resultMap.put("result", "-1");
			return resultMap;
		}
		while (flag) {
			str = str.substring(index, str.length());
			Map<String, String> map = getFieldStr(str);
			fieldname = map.get("fieldname");
			if (!"".equals(fieldname)) {
				fieldNames = fieldNames + falg2 + fieldname;
				falg2 = ",";
			}
			index = Integer.valueOf(map.get("index"));
			// log.writeLog("index"+index);
			if (index == -1) {
				flag = false;
			} else if (index == -2) {
				checkResult = false;
				flag = false;
			} else {
				if (fieldname.indexOf("def_") >= 0) {
					if (!checkFieldExists("1",
							fieldname.replace("def_", ""))) {
						checkResult = false;
						flag = false;
					}
				} else {
					if (!checkFieldExists("0",
							fieldname)) {
						checkResult = false;
						flag = false;
					}
				}
			}

		}
		if (!checkResult) {
			resultMap.put("fieldNames", fieldNames);
			resultMap.put("result", "-1");
			return resultMap;
		}
		resultMap.put("fieldNames", fieldNames);
		resultMap.put("result", "1");
		return resultMap;
	}

	/**
	 * 获取sql中where条件的字段
	 * 
	 * @param str
	 * @return
	 */
	public Map<String, String> getFieldStr(String str) {
		Map<String, String> map = new HashMap<String, String>();
		int indexstart = str.indexOf("##");
		if (indexstart < 0) {
			map.put("fieldname", "");
			map.put("index", "-1");
			return map;
		}
		int indexend = str.substring(indexstart + 2, str.length())
				.indexOf("##");
		if (indexend < 0) {
			map.put("fieldname", "");
			map.put("index", "-2");
			return map;
		}
		String fieldname = str.substring(indexstart + 2, indexstart + 2
				+ indexend);
		map.put("fieldname", fieldname);
		map.put("index", indexstart + 2 + indexend + 2 + "");
		return map;
	}

	/**
	 * 检查字段是否存在
	 * 
	 * @param isdef
	 *            0 通用 1自定义
	 * @param prjtype
	 * @param fieldname
	 * @return
	 */
	public boolean checkFieldExists(String isdef,
			String fieldname) {
		RecordSet rs = new RecordSet();
		String sql = "";
		int count = 0;
		sql = "select count(1) as count from uf_project_field where  fieldname='" + fieldname
				+ "' and iscommon='" + isdef + "'";
		// log.writeLog("sql"+sql);
		rs.executeSql(sql);
		if (rs.next()) {
			count = rs.getInt("count");
		}

		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}
}
