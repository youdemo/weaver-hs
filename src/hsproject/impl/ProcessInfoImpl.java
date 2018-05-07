package hsproject.impl;

import hsproject.dao.PrjLogDao;
import hsproject.util.InsertUtil;
import hsproject.util.SysnoUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class ProcessInfoImpl {
	/**
	 * 删除阶段
	 * @param processid 阶段id
	 */
	public void deleteProcess(String processid,String userid){
		RecordSet rs = new RecordSet();
		String sql = "delete from hs_prj_process where id="+processid;
		rs.executeSql(sql);
	}
	/**
	 * 增加阶段
	 * @param pidComMap 通用字段map
	 * @param pidDefMap 自定义字段map
	 * @param prjtype 项目类型
	 * @param processType 阶段类型
	 * @param prjid 项目id
	 * @return
	 */
	public String addProcessInfo(Map<String, String> pidComMap,Map<String, String> pidDefMap,String prjtype
			,String processType,String prjid,String userid){
		RecordSet rs = new RecordSet();
		String processid="";
		String sql="";
		SysnoUtil su = new SysnoUtil();
		PrjLogDao pld = new PrjLogDao();
		pidComMap.put("prjtype", prjtype);
		pidComMap.put("processtype", processType);
		pidComMap.put("prjid", prjid);
		String nextNo = su.getTableMaxId("hs_prj_process");
		pidComMap.put("id", nextNo);
		InsertUtil iu = new InsertUtil();
		iu.insert(pidComMap, "hs_prj_process");
		sql="select id from hs_prj_process where id="+nextNo;
		rs.executeSql(sql);
		if(rs.next()){
			processid = Util.null2String(rs.getString("id"));
		}
		if(!"".equals(processid) ){
			pidDefMap.put("prjid", prjid);
			pidDefMap.put("processid", processid);
			pidDefMap.put("id", su.getTableMaxId("hs_prj_process_fielddata"));
			iu.insert(pidDefMap, "hs_prj_process_fielddata");
			pld.writePrjLog("0", "1", processid, pidComMap, pidDefMap, new HashMap<String, String>(), new HashMap<String, String>(), processType, userid);
		}
		
		return processid;
		
	}
	
	/**
	 * 更新阶段信息
	* @param pidComMap 通用字段map
	 * @param pidDefMap 自定义字段map
	 * @param prjtype 项目类型
	 * @param processType 阶段类型
	 * @param processid  阶段id
	 */
	public void editProcessInfo(Map<String, String> pidComMap,Map<String, String> pidDefMap,String prjtype
			,String processType,String processid,String userid){
		RecordSet rs = new RecordSet();
		SysnoUtil su = new SysnoUtil();
		InsertUtil iu = new InsertUtil();
		PrjLogDao pld = new PrjLogDao();
		String sql="";
		String ideId="";
		String keyword = "";
		String iscommon = "";
		String keyvalue = "";
		String prjid = "";
		Map<String, String> oldComMap = new HashMap<String, String>();
		Map<String, String> oldDefMap = new HashMap<String, String>();
		String isdone = checkProcessIsDone(processid);
		sql="select prjid from hs_prj_process where id="+processid;
		rs.executeSql(sql);
		if(rs.next()){
			prjid = Util.null2String(rs.getString("prjid"));
		}
		if(!"1".equals(isdone)){
			Map<String, String> keymap =getkeyWord(processType);
			if(keymap.size()>0){
				keyword = keymap.get("fieldname");
				iscommon = keymap.get("iscommon");
				if(!"".equals(keyword)){
					if("0".equals(iscommon)){
						keyvalue=pidComMap.get(keyword);
					}else{
						keyvalue=pidDefMap.get(keyword);
					}
					if(!"".equals(keyvalue)){
						pidComMap.put("isdone", "1");
						updatePrjStatus(prjid,processType,processid,prjtype);

					}
				}
			}
		}
		oldComMap = getOldValueMap(processid,pidComMap,"0");
		iu.updateGen(pidComMap, "hs_prj_process", "id", processid);
		sql="select id from hs_prj_process_fielddata where processid="+processid;
		rs.executeSql(sql);
		if(rs.next()){
			ideId = Util.null2String(rs.getString("id"));
		}
		if("".equals(ideId)){
			pidDefMap.put("prjid", prjid);
			pidDefMap.put("processid", processid);
			pidDefMap.put("id", su.getTableMaxId("hs_prj_process_fielddata"));
			iu.insert(pidDefMap, "hs_prj_process_fielddata");
		}else if(!"".equals(ideId) && pidDefMap.size()>0){
			oldDefMap = getOldValueMap(prjid,pidDefMap,"0");
			iu.updateGen(pidDefMap, "hs_prj_process_fielddata", "id", ideId);
		}
		pld.writePrjLog("1", "1", processid, pidComMap, pidDefMap, oldComMap, oldDefMap, processType, userid); 
	}
	/**
	 * 更新项目状态
	 * @param prjid 项目id
	 * @param processType 阶段类型
	 * @param processid 阶段id
	 * @param prjtype 项目类型
	 */
	public void updatePrjStatus(String prjid,String processType,String processid,String prjtype){
		RecordSet rs = new RecordSet();
		String id = "";
		String processname="";
		String sql="";
		String nextstatus="";
		String flag="";
		sql="select id,processname from uf_prj_process where prjtype='"+prjtype+"' and isused='1' order by dsporder asc,id asc";
		rs.executeSql(sql);
		while(rs.next()){
			id = Util.null2String(rs.getString("id"));
			processname = Util.null2String(rs.getString("processname"));

			if("1".equals(flag)){
				nextstatus = processname;
				break;
			}
			if(processType.equals(id)){
				flag="1";				
			}
		}
		if("1".equals(flag)&&"".equals(nextstatus)){
			nextstatus = "完成";
		}
		sql="update hs_projectinfo set status='"+nextstatus+"' where id="+prjid;
		rs.executeSql(sql);
	}
	/**
	 * 检查阶段是否完成
	 * @param processid 阶段id
	 * @return
	 */
	public String  checkProcessIsDone(String processid){
		RecordSet rs = new RecordSet();
		String isDone = "";
		String sql="select isdone from hs_prj_process where id="+processid;
		rs.executeSql(sql);
		if(rs.next()){
			isDone = Util.null2String(rs.getString("isdone"));
		}
		return isDone;
	}
	
	/**
	 * 获取阶段关键字
	 * @param processtype
	 * @return
	 */
	public Map<String, String> getkeyWord(String processtype){
		Map<String, String> keymap = new HashMap<String, String>();
		RecordSet rs = new RecordSet();
		String sql = "select b.fieldname,b.iscommon from uf_process_keyword a,uf_prj_porcessfield b where a.keyword = b.id and a.processtype="+processtype;
		rs.executeSql(sql);
		if(rs.next()){
			keymap.put("fieldname", Util.null2String(rs.getString("fieldname")));
			keymap.put("iscommon", Util.null2String(rs.getString("iscommon")));
			
		}
		return keymap;
	}
	/**
	 * 检查上个阶段关键字段是否填写
	 * @param prjtype
	 * @param processtype
	 * @param processid
	 * @return
	 */
	public String isPreviousKeyWordWrite(String prjtype,String processtype,String processid){
		RecordSet rs = new RecordSet();
		String sql = "";
		String id = "";
		String lastid="";
		String prjid = "";
		String fieldname = "";
		String fieldvalue = "";
		String iscommon = "";
		String isWrite = "0";
		
		sql="select prjid from hs_prj_process where id="+processid;
		rs.executeSql(sql);
		if(rs.next()){
			prjid = Util.null2String(rs.getString("prjid"));
		}
		
		sql="select id,processname from uf_prj_process where prjtype='"+prjtype+"' and isused='1' order by dsporder asc,id asc";
		rs.executeSql(sql);
		while(rs.next()){
			id = Util.null2String(rs.getString("id"));			
			
			if(processtype.equals(id)){
				break;			
			}
			
			lastid=id;
		}
		if(!"".equals(lastid)){
			sql="select b.fieldname,b.iscommon from uf_process_keyword a,uf_prj_porcessfield b  where a.keyword=b.id and a.processtype='"+lastid+"'";
			rs.executeSql(sql);
			if(rs.next()){
				fieldname = Util.null2String(rs.getString("fieldname"));
				iscommon = Util.null2String(rs.getString("iscommon"));
			}
			if("0".equals(iscommon)){
				sql="select * from hs_prj_process where prjtype="+prjtype+" and processtype="+lastid+" and prjid="+prjid;
				rs.executeSql(sql);
				if(rs.next()){
					fieldvalue = Util.null2String(rs.getString(fieldname));
				}
				if(!"".equals(fieldvalue)){
					isWrite = "1";
				}
			}else{
				sql="select b.* from hs_prj_process a,hs_prj_process_fielddata b  where  a.id=b.processid and a.prjtype="+prjtype+" and a.processtype="+lastid+" and a.prjid="+prjid;
				rs.executeSql(sql);
				if(rs.next()){
					fieldvalue = Util.null2String(rs.getString(fieldname));
				}
				if(!"".equals(fieldvalue)){
					isWrite = "1";
				}
				
			}
		}else{
			isWrite = "1";
		}
		return isWrite;
	}
	
	/**
	 * 
	 * @param processid 过程id
	 * @param type  类型 0 通用 1自定义
	 * @return
	 */
	public Map<String, String> getOldValueMap(String processid,Map<String, String> newMap,String type){
		RecordSet rs = new RecordSet();
		Map<String, String> oldMap = new HashMap<String, String>();
		String sql="";
		String fieldName = "";
		String oldValue = "";
		if("0".equals(type)){
			sql="select * from hs_prj_process where id="+processid;
		}else{
			sql="select * from hs_prj_process_fielddata where processid="+processid;
		}
		rs.executeSql(sql);
		if(rs.next()){
			Iterator<String> it = newMap.keySet().iterator();
			while(it.hasNext()){
				fieldName = Util.null2String(it.next());
				if("".equals(fieldName)){
					continue;
				}
				oldValue = Util.null2String(rs.getString(fieldName));
				oldMap.put(fieldName, oldValue);
			}
		}
		return oldMap;
	}
}
