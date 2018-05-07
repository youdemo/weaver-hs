package hsproject.impl;

import hsproject.bean.OutDataMtBean;
import hsproject.bean.ProcessFieldBean;
import hsproject.bean.ProjectFieldBean;
import hsproject.dao.OutDataLogDao;
import hsproject.dao.ProcessFieldDao;
import hsproject.dao.ProcessInfoDao;
import hsproject.dao.ProjectFieldDao;
import hsproject.dao.ProjectInfoDao;
import hsproject.util.InsertUtil;
import hsproject.util.SysnoUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dc.engine.core.f;

import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;
import weaver.general.Util;


public class SysnOutDataImpl {
	BaseBean log = new BaseBean();
	/**
	 * 获取这次需要同步的配置idS
	 * @param month
	 * @param day
	 * @param hour
	 * @param min
	 * @return
	 */
	public List<OutDataMtBean> getSysnMtIds(String month,String day,String hour){
		String sql = "";
		String id = "";
		String datasource = "";
		String type = "";
		String prjfield = "";
		String processfield = "";
		String prjtype = "";
		String processtype = "";
		RecordSet rs = new RecordSet();
		BaseBean log = new BaseBean();
		List<OutDataMtBean> list = new ArrayList<OutDataMtBean>();
		if("".equals(month) || "".equals(day) || "".equals(hour) ){
			return list;
		}
		
		sql = "select *  from uf_prj_outdata_mt where (month='"+month+"' or month='0') and (day='"+day+"' or day='0') and (hour='"+hour+"' or hour='0') and isused='1' ";
		
		//log.writeLog("getSysnMtIds sql"+sql);
		rs.executeSql(sql);
		while(rs.next()){
			id = Util.null2String(rs.getString("id"));
			datasource = Util.null2String(rs.getString("datasource"));
			type = Util.null2String(rs.getString("type"));
			prjfield = Util.null2String(rs.getString("prjfield"));
			processfield = Util.null2String(rs.getString("processfield"));
			prjtype = Util.null2String(rs.getString("prjtype"));
			processtype = Util.null2String(rs.getString("processtype"));
			if(!checkExists("datasourcesetting","id",datasource)){
				log.writeLog("该同步配置数据源错误 id:"+id+" datasource:"+datasource);
				continue;
			}
			if("0".equals(type)){
				if(!checkExists("uf_project_type","id",prjtype)){
					log.writeLog("该同步配置错误 id:"+id+" prjtype:"+prjtype);
					continue;
				}
				if(!checkExists("uf_project_field","id",prjfield)){
					log.writeLog("该同步配置错误 id:"+id+" prjfield:"+prjfield);
					continue;
				}
			}else if("1".equals(type)){
				if(!checkExists("uf_prj_process","id",processtype)){
					log.writeLog("该同步配置错误 id:"+id+" processtype:"+processtype);
					continue;
				}
				if(!checkExists("uf_prj_porcessfield","id",processfield)){
					log.writeLog("该同步配置错误 id:"+id+" processfield:"+processfield);
					continue;
				}
			}else{
				continue;
			}
			OutDataMtBean odmb = new OutDataMtBean();
			odmb.setId(id);
			odmb.setMark(Util.null2String(rs.getString("mark")));
			odmb.setProcesstype(processtype);
			odmb.setPrjtype(prjtype);
			odmb.setPrjfield(prjfield);
			odmb.setDatasource(datasource);
			odmb.setProcessfield(processfield);
			odmb.setType(type);
			odmb.setMapfield(Util.null2String(rs.getString("mapfield")));
			odmb.setMapsql(Util.null2String(rs.getString("mapsql")));
			odmb.setDescription(Util.null2String(rs.getString("description")));
			odmb.setIsused(Util.null2String(rs.getString("isused")));
			odmb.setMonth(Util.null2String(rs.getString("month")));
			odmb.setDay(Util.null2String(rs.getString("day")));
			odmb.setHour(Util.null2String(rs.getString("hour")));
			odmb.setHalfhour(Util.null2String(rs.getString("halfhour")));
			list.add(odmb);
		}
		return list;
	}
	/**
	 * 检验值是否存在
	 * @param tablename
	 * @param key
	 * @param keyValue
	 * @return
	 */
	public boolean checkExists(String tablename,String key,String keyValue){
		RecordSet rs = new RecordSet();
		String sql = "";
		int count = 0;
		sql = "select count(1) as count from "+tablename+" where "+key+"='"+keyValue+"'";
		//log.writeLog("check sql:"+sql);
		rs.executeSql(sql);
		if(rs.next()){
			count = rs.getInt("count");
		}
		if(count>0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 同步数据
	 * @param odmb
	 * @param nowDate
	 * @param nowTime
	 */
	public void sysnData(OutDataMtBean odmb,String nowDate,String nowTime){
		OutDataLogDao odld = new OutDataLogDao();
		String dataSourceFlag = "";
		RecordSet rs =new RecordSet();
		String sql="";
		String fieldname = "";
		String fieldvalue = "";
		String sysnValue = "";
		String processtype = odmb.getProcesstype();
		String prjfield = odmb.getPrjfield();
		String datasource = odmb.getDatasource();
		String processfield = odmb.getProcessfield();
		String type =odmb.getType();
		String mapfield = odmb.getMapfield();
		String prjtype = odmb.getPrjtype();
		String mapsql = odmb.getMapsql();		
		ProjectFieldBean pfb= new ProjectFieldDao().getPrjFieldBean(prjfield);
		ProcessFieldBean psfb = new ProcessFieldDao().getProcessFieldBean(processfield);
		dataSourceFlag = getDataSourceFlag(datasource);
		if("".equals(dataSourceFlag)){
			odld.writeLog(odmb.getId(),type,"", "", "", "", "", "", "", "", nowDate, nowTime, "数据源标识找不到", "1");
			return;
		}
		Map<String,String> checkMap=checkMapSql(mapsql,type,prjtype,processtype,mapfield);
		String checkResult = checkMap.get("result");
		String fieldNames = checkMap.get("fieldNames");
		//log.writeLog("checkResult:"+checkResult);
		//log.writeLog("fieldNames:"+fieldNames);
		if("-1".equals(checkResult)){
			odld.writeLog(odmb.getId(),type,"", "", "", "", "", "", "", "", nowDate, nowTime, "同步sql检查失败", "1");
			return;
		}

		String prjid = "";
		 if("0".equals(type)){
			sql="select * from hs_projectinfo where prjtype='"+prjtype+"'";
			//log.writeLog(sql);
			rs.executeSql(sql);
			while(rs.next()){
				fieldname = "";
				fieldvalue = "";
				sysnValue = "";
				prjid = Util.null2String(rs.getString("id"));
				//log.writeLog("prjid:"+prjid);
				ProjectInfoDao pid = new ProjectInfoDao();
				Map<String, String> pidComMap=pid.getProjetCommonData(prjid);
				Map<String, String> pidDefMap=pid.getProjetDefineData(prjid);
				String fieldArr[]=fieldNames.split(",");
				for(int i=0;i<fieldArr.length;i++){
					fieldname = Util.null2String(fieldArr[i]);
					if("".equals(fieldname)){
						continue;
					}
					if(fieldname.indexOf("def_")>=0){
						fieldvalue = pidDefMap.get(fieldname.replace("def_", ""));
					}else{
						fieldvalue = pidComMap.get(fieldname);
					}
					fieldname="##"+fieldname+"##";
					mapsql = mapsql.replace(fieldname, fieldvalue);
					//log.writeLog("mapsql:"+mapsql);
				}

				Map<String,String> sysnmap=getSysnValue(dataSourceFlag,mapsql,mapfield);
				if("-1".equals(sysnmap.get("result"))){
					odld.writeLog(odmb.getId(),"0",pfb.getIscommon(), prjid, "", pfb.getFieldname(), "", "", mapsql, "", nowDate, nowTime, "执行同步sql获取值失败", "1");
					return;
				}
				sysnValue = sysnmap.get("value");
				//log.writeLog("sysnmap sysnValue:"+sysnValue);
				updateProjectData(prjid,prjfield,sysnValue,odmb.getId(),mapsql,nowDate,nowTime);
			}
		}else if("1".equals(type)){
			String processid = "";
			sql="select * from hs_prj_process where prjtype='"+prjtype+"' and processtype='"+processtype+"'";
			rs.executeSql(sql);
			while(rs.next()){
				fieldname = "";
				fieldvalue = "";
				sysnValue = "";
				prjid = Util.null2String(rs.getString("prjid"));
				processid = Util.null2String(rs.getString("id"));
				//log.writeLog("processid"+processid);
				ProcessInfoDao pid = new ProcessInfoDao();
				Map<String, String> pidComMap=pid.getProcessCommonData(processid);
				Map<String, String> pidDefMap=pid.getProcessDefineData(processid);
				String fieldArr[]=fieldNames.split(",");
				for(int i=0;i<fieldArr.length;i++){
					fieldname = Util.null2String(fieldArr[i]);
					if("".equals(fieldname)){
						continue;
					}
					if(fieldname.indexOf("def_")>=0){
						fieldvalue = pidDefMap.get(fieldname.replace("def_", ""));
					}else{
						fieldvalue = pidComMap.get(fieldname);
						//log.writeLog("fieldname"+fieldname+" fieldvalue"+fieldvalue);
					}
					fieldname="##"+fieldname+"##";
					mapsql = mapsql.replace(fieldname, fieldvalue);
					//log.writeLog("mapsql:"+mapsql);
				}
				Map<String,String> sysnmap=getSysnValue(dataSourceFlag,mapsql,mapfield);
				if("-1".equals(sysnmap.get("result"))){
					//log.writeLog("sysnmap aaa");
					odld.writeLog(odmb.getId(),"1",psfb.getIscommon(), prjid, processid, psfb.getFieldname(), "", "", mapsql, "", nowDate, nowTime, "执行同步sql获取值失败", "1");
					continue;
				}
				sysnValue = sysnmap.get("value");
				//log.writeLog("sysnmap sysnValue:"+sysnValue);
				updateProcessData(prjid,processid,processfield,sysnValue,odmb.getId(),mapsql,nowDate,nowTime);
			}
		}
		
	}
	/**
	 * 更新项目数据
	 * @param prjid
	 * @param prjfield
	 * @param sysnvalue
	 */
	public void updateProjectData(String prjid,String prjfield,String sysnvalue,String mtid,String mapsql,String nowDate,String nowTime){
		InsertUtil iu = new InsertUtil();
		OutDataLogDao odld = new OutDataLogDao();
		RecordSet rs = new RecordSet();
		String sql = "";
		String fieldname = "";
		String iscommon = "";
		String oldvalue="";
		boolean result=true;
		sql = "select fieldname,iscommon from uf_project_field where id="+prjfield;
		//log.writeLog("sql:"+sql);
		rs.executeSql(sql);
		if(rs.next()){
			fieldname = Util.null2String(rs.getString("fieldname"));
			iscommon = Util.null2String(rs.getString("iscommon"));
		}
		if("0".equals(iscommon)){
			sql="select * from hs_projectinfo where id="+prjid;
			//log.writeLog("sql:"+sql);
			rs.executeSql(sql);
			if(rs.next()){
				oldvalue = Util.null2String(rs.getString(fieldname));
				Map<String, String> map = new HashMap<String, String>();
				map.put(fieldname, sysnvalue);
				result=iu.updateGen(map, "hs_projectinfo", "id", prjid);
				sql="update hs_projectinfo set "+fieldname+"='"+sysnvalue+"' where id="+prjid;
				//log.writeLog("sysn sql:"+sql);
				//result = rs.executeSql(sql);
				if(result){
					odld.writeLog(mtid,"0","0", prjid, "", fieldname, oldvalue, sysnvalue, mapsql, sql, nowDate, nowTime, "同步成功", "0");
				}else{ 
					odld.writeLog(mtid,"0","0", prjid, "", fieldname, oldvalue, sysnvalue, mapsql, sql, nowDate, nowTime, "更新sql执行失败", "1");	
				}
			}
		}else if("1".equals(iscommon)){
			sql="select * from hs_project_fielddata where prjid="+prjid;
			//log.writeLog("sql:"+sql);
			rs.executeSql(sql);
			if(rs.next()){
				oldvalue = Util.null2String(rs.getString(fieldname));
				Map<String, String> map = new HashMap<String, String>();
				map.put(fieldname, sysnvalue);
				result=iu.updateGen(map, "hs_project_fielddata", "prjid", prjid);
				sql="update hs_project_fielddata set "+fieldname+"='"+sysnvalue+"' where prjid="+prjid;
				//log.writeLog("sysn sql:"+sql);
				//result = rs.executeSql(sql);
				if(result){
					odld.writeLog(mtid,"0","1", prjid, "", fieldname, oldvalue, sysnvalue, mapsql, sql, nowDate, nowTime, "同步成功", "0");
				}else{
					odld.writeLog(mtid,"0","1", prjid, "", fieldname, oldvalue, sysnvalue, mapsql, sql, nowDate, nowTime, "更新sql执行失败", "1");	
				}
			}else{
				SysnoUtil su = new SysnoUtil();
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", su.getTableMaxId("hs_project_fielddata"));
				map.put("prjid", prjid);
				map.put(fieldname, sysnvalue);
				result=iu.insert(map, "hs_project_fielddata");
				//result=iu.updateGen(map, "hs_project_fielddata", "prjid", prjid);
				sql="insert into hs_project_fielddata(id,prjid,"+fieldname+") values("+su.getTableMaxId("hs_project_fielddata")+",'"+prjid+"','"+sysnvalue+"')";
				//log.writeLog("sysn sql:"+sql);
				//result = rs.executeSql(sql);
				if(result){
					odld.writeLog(mtid,"0","1", prjid, "", fieldname, oldvalue, sysnvalue, mapsql, sql, nowDate, nowTime, "同步成功", "0");
				}else{
					odld.writeLog(mtid,"0","1", prjid, "", fieldname, oldvalue, sysnvalue, mapsql, sql, nowDate, nowTime, "更新sql执行失败", "1");	
				}
			}
			
		}
		
	}
	/**
	 * 更新阶段数据
	 * @param prjid
	 * @param processid
	 * @param processfield
	 * @param sysnvalue
	 */
	public void updateProcessData(String prjid,String processid,String processfield,String sysnvalue,String mtid,String mapsql,String nowDate,String nowTime){
		InsertUtil iu = new InsertUtil();
		OutDataLogDao odld = new OutDataLogDao();
		RecordSet rs = new RecordSet();
		ProcessInfoImpl pii = new ProcessInfoImpl();
		String sql = "";
		String fieldname = "";
		String iscommon = "";
		String oldvalue="";
		String keyword = "";
		String key_iscommon = "";
		String processtype = "";
		String isdone = "";
		String needupdate = "";
		String prjtype = "";
		boolean result = true;
		sql = "select fieldname,iscommon,processtype from uf_prj_porcessfield where id="+processfield;
		//log.writeLog("sql:"+sql);
		rs.executeSql(sql);
		if(rs.next()){
			fieldname = Util.null2String(rs.getString("fieldname"));
			iscommon = Util.null2String(rs.getString("iscommon"));
			processtype = Util.null2String(rs.getString("processtype"));
		}
		sql="select isdone,prjtype from hs_prj_process where id="+processid;
		rs.executeSql(sql);
		if(rs.next()){
			isdone = Util.null2String(rs.getString("isdone"));
			prjtype = Util.null2String(rs.getString("prjtype"));
		}
		if(!"1".equals(isdone)){
			Map<String, String> keymap =pii.getkeyWord(processtype);
			if(keymap.size()>0){
				keyword = keymap.get("fieldname");
				key_iscommon = keymap.get("iscommon");
				//log.writeLog("keyword "+keyword+"key_iscommon "+key_iscommon+" fieldname"+fieldname+" iscommon"+iscommon+" sysnvalue"+sysnvalue);
				if(keyword.equals(fieldname) && iscommon.equals(key_iscommon)){
					if(!"".equals(sysnvalue)){
						needupdate = "1";
					}
				}
			}
		}
		//log.writeLog("needupdate"+needupdate);
		if("0".equals(iscommon)){
			sql="select * from hs_prj_process where id="+processid;
			//log.writeLog("sql:"+sql);
			rs.executeSql(sql);
			if(rs.next()){
				oldvalue = Util.null2String(rs.getString(fieldname));
				Map<String, String> map = new HashMap<String, String>();
				map.put(fieldname, sysnvalue);
				result=iu.updateGen(map, "hs_prj_process", "id", processid);
				sql="update hs_prj_process set "+fieldname+"='"+sysnvalue+"' where id="+processid;
				//log.writeLog("sysn sql:"+sql);
				//result = rs.executeSql(sql);
				if(result){
					if("1".equals(needupdate)){
						map = new HashMap<String, String>();
						map.put("isdone", "1");
						iu.updateGen(map, "hs_prj_process", "id", processid);
						pii.updatePrjStatus(prjid,processtype,processid,prjtype);
					}
					odld.writeLog(mtid,"1","0", prjid, processfield, fieldname, oldvalue, sysnvalue, mapsql, sql, nowDate, nowTime, "同步成功", "0");
				}else{
					odld.writeLog(mtid,"1","0", prjid, processfield, fieldname, oldvalue, sysnvalue, mapsql, sql, nowDate, nowTime, "更新sql执行失败", "1");	
				}
			}
		}else if("1".equals(iscommon)){
			sql="select * from hs_prj_process_fielddata where processid="+processid;
			//log.writeLog("sql:"+sql);
			rs.executeSql(sql);
			if(rs.next()){
				oldvalue = Util.null2String(rs.getString(fieldname));
				Map<String, String> map = new HashMap<String, String>();
				map.put(fieldname, sysnvalue);
				result=iu.updateGen(map, "hs_prj_process_fielddata", "processid", processid);
				sql="update hs_prj_process_fielddata set "+fieldname+"='"+sysnvalue+"' where processid="+processid;
				//log.writeLog("sysn sql:"+sql);
				//result = rs.executeSql(sql);
				if(result){
					if("1".equals(needupdate)){
						map = new HashMap<String, String>();
						map.put("isdone", "1");
						iu.updateGen(map, "hs_prj_process", "id", processid);
						pii.updatePrjStatus(prjid,processtype,processid,prjtype);
					}
					odld.writeLog(mtid,"1","1", prjid, processfield, fieldname, oldvalue, sysnvalue, mapsql, sql, nowDate, nowTime, "同步成功", "0");
				}else{
					odld.writeLog(mtid,"1","1", prjid, processfield, fieldname, oldvalue, sysnvalue, mapsql, sql, nowDate, nowTime, "更新sql执行失败", "1");	
				}
			}else{
				SysnoUtil su = new SysnoUtil();
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", su.getTableMaxId("hs_prj_process_fielddata"));
				map.put("prjid", prjid);
				map.put("processid", processid);
				map.put(fieldname, sysnvalue);
				result=iu.insert(map, "hs_prj_process_fielddata");
				sql="insert into hs_prj_process_fielddata(id,prjid,processid,"+fieldname+") values("+su.getTableMaxId("hs_prj_process_fielddata")+",'"+prjid+"','"+processid+"','"+sysnvalue+"')";
				//log.writeLog("sysn sql:"+sql);
				//result = rs.executeSql(sql);
				if(result){
					odld.writeLog(mtid,"1","1", prjid, processfield, fieldname, oldvalue, sysnvalue, mapsql, sql, nowDate, nowTime, "同步成功", "0");
				}else{
					odld.writeLog(mtid,"1","1", prjid, processfield, fieldname, oldvalue, sysnvalue, mapsql, sql, nowDate, nowTime, "更新sql执行失败", "1");	
				}
			}
			
		}
		
	}
	/**
	 * 获取其他系统同步过来的值
	 * @param dataSourceFlag
	 * @param mapsql
	 * @param mapfield
	 * @return
	 */
	public Map<String,String> getSysnValue(String dataSourceFlag,String mapsql,String mapfield ){
		Map<String,String> map = new HashMap<String,String >();
		RecordSetDataSource rsd = new RecordSetDataSource(dataSourceFlag);
		String sql=mapsql;
		String value="";
		//log.writeLog("getSysnValue"+sql);
		if(rsd.executeSql(sql)){
			//log.writeLog("getSysnValue true");
			if(rsd.next()){
				value = Util.null2String(rsd.getString(mapfield));
			}
		}else{
			//log.writeLog("getSysnValue false");
			map.put("value", "");
			map.put("result", "-1");
			return map;
		}
		map.put("value",value);
		map.put("result", "1");
		return map;
	}
	
	/**
	 * 获取数据源标识
	 * @param sourceid
	 * @return
	 */
	public String getDataSourceFlag(String sourceid){
		String datasourcename = "";
		RecordSet rs =new RecordSet();
		String sql = "select datasourcename from datasourcesetting where id="+sourceid;
		rs.executeSql(sql);
		if(rs.next()){
			datasourcename = Util.null2String(rs.getString("datasourcename"));
		}
		return datasourcename;
	}
	
	/**
	 * 检查sql条件字段是否正确
	 * @param mapSql
	 * @param type
	 * @param prjtype
	 * @param processtype
	 * @param mapfield
	 * @return
	 */
	public Map<String,String> checkMapSql(String mapSql,String type,String prjtype,String processtype,String mapfield){
		String tranSql = mapSql;
		boolean flag = true;
		boolean checkResult = true;
		String falg2="";
		String fieldname = "";
		String fieldNames = "";
		String str = "";
		int index = 0;
		Map<String,String> resultMap = new HashMap<String,String >();
		str = mapSql;
		if(tranSql.indexOf(mapfield)<0){
			resultMap.put("fieldNames", fieldNames);
			resultMap.put("result", "-1");
			return resultMap;
		}
		while(flag){
			str = str.substring(index,str.length());
			Map<String,String> map = getFieldStr(str);
			fieldname = map.get("fieldname");
			if(!"".equals(fieldname)){
				fieldNames = fieldNames +falg2+fieldname;
				falg2 = ",";
			}
			index = Integer.valueOf(map.get("index"));
			//log.writeLog("index"+index);
			if(index == -1){
				flag =false;
			}else if(index == -2){
				checkResult = false;
				flag =false;
			}else{
				if(fieldname.indexOf("def_")>=0){
					if(!checkFieldExists(type,"1",prjtype,processtype,fieldname.replace("def_", ""))){
						checkResult = false;
						flag =false;
					}
				}else{
					if(!checkFieldExists(type,"0",prjtype,processtype,fieldname)){
						checkResult = false;
						flag =false;
					}
				}
			}
			  
		}
		if(!checkResult){
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
	 * @param str
	 * @return
	 */
	public  Map<String,String> getFieldStr(String str){
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
	/**
	 * 检查字段是否存在
	 * @param type
	 * @param isdef 0 通用 1自定义
	 * @param prjtype
	 * @param processtype
	 * @param fieldname
	 * @return
	 */
	public boolean checkFieldExists(String type,String isdef,String prjtype,String processtype,String fieldname ){
	     RecordSet rs = new RecordSet();
	     String sql = "";
	     int count = 0;
	     if("0".equals(type)){
	    	 sql="select count(1) as count from uf_project_field where prjtype='"+prjtype+"' and fieldname='"+fieldname+"' and iscommon='"+isdef+"'";
	    	 //log.writeLog("sql"+sql);
	    	 rs.executeSql(sql);
	    	 if(rs.next()){
	    		 count = rs.getInt("count");
	    	 }
	     }else{
	    	 sql="select count(1) as count from uf_prj_porcessfield where projecttype='"+prjtype+"' and processtype='"+processtype+"' and fieldname='"+fieldname+"' and iscommon='"+isdef+"'";
	    	// log.writeLog("sql"+sql);
	    	 rs.executeSql(sql);
	    	 if(rs.next()){
	    		 count = rs.getInt("count");    		 
	    	 }
	     }
	     if(count >0){
	    	 return true;
	     }else{
	    	 return false;
	     }
	}
}
