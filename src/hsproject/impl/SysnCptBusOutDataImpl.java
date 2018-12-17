package hsproject.impl;

import hsproject.bean.CptBusOutDataMtBean;
import hsproject.bean.CptBusOutDataMtDtBean;
import hsproject.bean.ProjectFieldBean;
import hsproject.dao.ProjectFieldDao;
import hsproject.dao.ProjectInfoDao;
import hsproject.util.InsertUtil;
import hsproject.util.SysnoUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;
import weaver.general.Util;

public class SysnCptBusOutDataImpl {
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
	public List<CptBusOutDataMtBean> getSysnMtIds(String month, String day,
			String hour) {
		String sql = "";
		String id = "";
		String datasource = "";
		String type = "";
		String prjtype = "";

		String sysfiled = "";
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		String sql_dt = "";
		BaseBean log = new BaseBean();
		List<CptBusOutDataMtBean> list = new ArrayList<CptBusOutDataMtBean>();
		if ("".equals(month) || "".equals(day) || "".equals(hour)) {
			return list;
		}

		sql = "select *  from uf_prj_syscptbus_mt where (month='" + month
				+ "' or month='0') and (day='" + day
				+ "' or day='0') and (hour='" + hour
				+ "' or hour='0') and isused='1' ";

		// log.writeLog("getSysnMtIds sql"+sql);
		rs.executeSql(sql);
		while (rs.next()) {
			id = Util.null2String(rs.getString("id"));
			datasource = Util.null2String(rs.getString("datasource"));
			type = Util.null2String(rs.getString("type"));
			prjtype = Util.null2String(rs.getString("prjtype"));
			if (!checkExists("datasourcesetting", "id", datasource)) {
				log.writeLog("该同步配置数据源错误 id:" + id + " datasource:"
						+ datasource);
				continue;
			}
			if (!checkExists("uf_project_type", "id", prjtype)) {
				log.writeLog("该同步配置错误 id:" + id + " prjtype:" + prjtype);
				continue;
			}
			CptBusOutDataMtBean odmb = new CptBusOutDataMtBean();
			List<CptBusOutDataMtDtBean> dtList = new ArrayList<CptBusOutDataMtDtBean>();
			odmb.setId(id);
			odmb.setMark(Util.null2String(rs.getString("mark")));
			odmb.setPrjtype(prjtype);
			odmb.setDatasource(datasource);
			odmb.setType(type);
			odmb.setMapsql(Util.null2String(rs.getString("mapsql")));
			odmb.setDescription(Util.null2String(rs.getString("description")));
			odmb.setIsused(Util.null2String(rs.getString("isused")));
			odmb.setMonth(Util.null2String(rs.getString("month")));
			odmb.setDay(Util.null2String(rs.getString("day")));
			odmb.setHour(Util.null2String(rs.getString("hour")));
			odmb.setHalfhour(Util.null2String(rs.getString("halfhour")));
			int count = 0;
			sql_dt = "select count(1) as count from uf_prj_syscptbus_mt_dt1 where mainid="
					+ id;
			rs_dt.executeSql(sql_dt);
			if (rs_dt.next()) {
				count = rs_dt.getInt("count");
			}
			if (count <= 0) {
				log.writeLog("该同步明细数量为空 id:" + id);
				continue;
			}
			sql_dt = "select * from uf_prj_syscptbus_mt_dt1 where mainid=" + id;
			rs_dt.executeSql(sql_dt);
			while (rs_dt.next()) {
				CptBusOutDataMtDtBean cbo = new CptBusOutDataMtDtBean();
				cbo.setId(rs_dt.getString("id"));
				cbo.setMainid(rs_dt.getString("mainid"));
				cbo.setMapfield(rs_dt.getString("mapfield"));
				cbo.setSysfiled(rs_dt.getString("sysfiled"));
				dtList.add(cbo);
			}
			odmb.setDtList(dtList);
			// if("0".equals(type)){
			//
			// if(!checkExists("uf_project_field","id",prjfield)){
			// log.writeLog("该同步配置错误 id:"+id+" prjfield:"+prjfield);
			// continue;
			// }
			// }else if("1".equals(type)){
			// if(!checkExists("uf_prj_porcessfield","id",processfield)){
			// log.writeLog("该同步配置错误 id:"+id+" processfield:"+processfield);
			// continue;
			// }
			// }else{
			// continue;
			// }

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
	public void sysnData(CptBusOutDataMtBean odmb, String nowDate,
			String nowTime) {
		String dataSourceFlag = "";
		RecordSet rs = new RecordSet();
		String sql = "";
		String fieldname = "";
		String fieldvalue = "";
		String sysnValue = "";
		String datasource = odmb.getDatasource();
		String type = odmb.getType();
		String prjtype = odmb.getPrjtype();
		String transql = odmb.getMapsql();
		dataSourceFlag = getDataSourceFlag(datasource);
		if ("".equals(dataSourceFlag)) {
			log.writeLog("数据源标识找不到 datasource:" + datasource);
			return;
		}
		Map<String, String> checkMap = checkMapSql(transql, type, prjtype);
		String checkResult = checkMap.get("result");
		String fieldNames = checkMap.get("fieldNames");
		// log.writeLog("checkResult:"+checkResult);
		// log.writeLog("fieldNames:"+fieldNames);
		if ("-1".equals(checkResult)) {
			log.writeLog("同步sql检查失败 transql:" + transql);
			return;
		}

		String prjid = "";
		sql = "select * from hs_projectinfo where prjtype='" + prjtype + "'";
		// log.writeLog(sql);
		rs.executeSql(sql);
		while (rs.next()) {
			String mapsql = transql;
			fieldname = "";
			fieldvalue = "";
			sysnValue = "";
			prjid = Util.null2String(rs.getString("id"));
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
			// log.writeLog("mapsql:"+mapsql);
			if ("0".equals(type)) {
				updateCptInfo(dataSourceFlag, mapsql, prjid, odmb);
			} else {
				updateBusInfo(dataSourceFlag, mapsql, prjid, odmb);
			}

		}

	}
	
	/**
	 * 更新项目固定资产数据
	 * @param dataSourceFlag
	 * @param mapsql
	 * @param prjid
	 * @param odmb
	 */
	public void updateCptInfo(String dataSourceFlag, String mapsql,
			String prjid, CptBusOutDataMtBean odmb) {
		RecordSet rs = new RecordSet();
		RecordSetDataSource rsd = new RecordSetDataSource(dataSourceFlag);
		SysnoUtil su = new SysnoUtil();
		InsertUtil iu = new InsertUtil();
		String sql = mapsql;
		String sql_dt = "";
		//log.writeLog("sql:"+sql);
		rsd.executeSql(sql);
		if(rsd.next()){
			sql_dt = "update hs_prj_cptinfo set issys='1' where prjid="+prjid;
		
			rs.executeSql(sql_dt);
		}
		List<CptBusOutDataMtDtBean> list = odmb.getDtList();
		rsd.executeSql(sql);
		while(rsd.next()){
			Map<String, String> map = new HashMap<String,String>();
			for(CptBusOutDataMtDtBean dbo:list){
				String fieldName=getSysFieldName(dbo.getSysfiled());
				if(!"".equals(fieldName)){
					String value = "";
					value = Util.null2String(rsd.getString(dbo.getMapfield()));
					map.put(fieldName, value.replace("'","'||chr(39)||'").replace("&","'||chr(38)||'"));
				}
				
			}

			String whereSql=" prjid='"+prjid+"' ";
			Iterator<String> it = map.keySet().iterator();
			while (it.hasNext()) {
				String key=it.next();
				String value=map.get(key);
				whereSql += "and "+key+"='"+value+"'";
			}
			String cptid="";
			sql_dt="select id from hs_prj_cptinfo where "+whereSql;
			//log.writeLog("sql_dt:"+sql_dt);
			rs.executeSql(sql_dt);
			if(rs.next()){
				cptid = Util.null2String(rs.getString("id"));
			}
			if(!"".equals(cptid)){
				sql_dt = "update hs_prj_cptinfo set issys='0' where id="+cptid;
				rs.executeSql(sql_dt);
				continue;
			}else{
				String nextNo = su.getTableMaxId("hs_prj_cptinfo");
				map.put("id", nextNo);
				map.put("prjid", prjid);
				map.put("issys", "0");
				iu.insert(map, "hs_prj_cptinfo");
			}
		}
		sql_dt="delete from hs_prj_cptinfo where issys='1' and prjid="+prjid;
		rs.executeSql(sql_dt);
	}
	/**
	 * 同步商务信息
	 * @param dataSourceFlag
	 * @param mapsql
	 * @param prjid
	 * @param odmb
	 */
	public void updateBusInfo(String dataSourceFlag, String mapsql,
			String prjid, CptBusOutDataMtBean odmb) {
		RecordSet rs = new RecordSet();
		RecordSetDataSource rsd = new RecordSetDataSource(dataSourceFlag);
		SysnoUtil su = new SysnoUtil();
		InsertUtil iu = new InsertUtil();
		String sql = mapsql;
		String sql_dt = "";
		String purchaseamount = "";
		//log.writeLog("sql:"+sql);
		rsd.executeSql(sql);
		if(rsd.next()){
			sql_dt = "update hs_prj_businfo set issys='1' where prjid="+prjid;
		
			rs.executeSql(sql_dt);
		}
		List<CptBusOutDataMtDtBean> list = odmb.getDtList();
		rsd.executeSql(sql);
		while(rsd.next()){
			Map<String, String> map = new HashMap<String,String>();
			for(CptBusOutDataMtDtBean dbo:list){
				String fieldName=getSysFieldName(dbo.getSysfiled());
				if(!"".equals(fieldName)){
					String value = "";
					value = Util.null2String(rsd.getString(dbo.getMapfield()));
					map.put(fieldName, value.replace("'","'||chr(39)||'").replace("&","'||chr(38)||'"));
				}
				
			}

			String whereSql=" prjid='"+prjid+"' ";
			Iterator<String> it = map.keySet().iterator();
			while (it.hasNext()) {
				String key=it.next();
				String value=map.get(key);
				whereSql += "and "+key+"='"+value+"'";
			}
			String cptid = "";
			sql_dt = "select id from hs_prj_businfo where "+whereSql;
			rs.executeSql(sql_dt);
			if(rs.next()){
				cptid = Util.null2String(rs.getString("id"));
			}
			if(!"".equals(cptid)){
				sql_dt = "update hs_prj_businfo set issys='0' where id="+cptid;
				rs.executeSql(sql_dt);
				continue;
			}else{
				String nextNo = su.getTableMaxId("hs_prj_businfo");
				map.put("id", nextNo);
				map.put("prjid", prjid);
				map.put("issys", "0");
				iu.insert(map, "hs_prj_businfo");
			}
		}
		sql_dt = "delete from hs_prj_businfo where issys='1' and prjid="+prjid;
		rs.executeSql(sql_dt);
		sql_dt = "select nvl(sum(nvl(applyamount,0)),0) as purchaseamount from hs_prj_businfo where prjid="+prjid;
		rs.executeSql(sql_dt);
		if(rs.next()) {
			purchaseamount = Util.null2String(rs.getString("purchaseamount"));
		}
		sql_dt = "update hs_projectinfo set purchaseamount='"+purchaseamount+"' where id="+prjid;
		//log.writeLog("bus update sql:"+sql_dt);
		rs.executeSql(sql_dt);
	}
	
	public String getSysFieldName(String id){
		RecordSet rs = new RecordSet();
		String fieldName = "";
		String sql = "select fieldname from uf_prj_cptbus_field where id="+id;
		rs.executeSql(sql);
		if(rs.next()){
			fieldName = Util.null2String(rs.getString("fieldname"));
		}
		return fieldName;
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
	 * @param type
	 * @param prjtype
	 * @param processtype
	 * @param mapfield
	 * @return
	 */
	public Map<String, String> checkMapSql(String mapSql, String type,
			String prjtype) {
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
					if (!checkFieldExists("1", prjtype,
							fieldname.replace("def_", ""))) {
						checkResult = false;
						flag = false;
					}
				} else {
					if (!checkFieldExists("0", prjtype, fieldname)) {
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
	public boolean checkFieldExists(String isdef, String prjtype,
			String fieldname) {
		RecordSet rs = new RecordSet();
		String sql = "";
		int count = 0;
		sql = "select count(1) as count from uf_project_field where prjtype='"
				+ prjtype + "' and fieldname='" + fieldname
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
