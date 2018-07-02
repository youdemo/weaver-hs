package hsproject.impl;

import hsproject.dao.PrjLogDao;
import hsproject.util.InsertUtil;
import hsproject.util.SysnoUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import weaver.conn.ConnStatement;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;

public class ProjectInfoImpl {
		/**
		 * 新建项目
		 * @param pidComMap
		 * @param pidDefMap
		 * @param prjtype
		 * @return
		 */
		public String  addPorjectInfo(Map<String, String> pidComMap,Map<String, String> pidDefMap,String prjtype,String userid){
			RecordSet rs = new RecordSet();
			String prjid="";
			String sql="";
			SysnoUtil su = new SysnoUtil();
			PrjLogDao pld = new PrjLogDao();
			String procode=pidComMap.get("procode");
			String belongdepart = pidComMap.get("belongdepart");
			String belongCompany = "";
			if(!"".equals(belongdepart)){
				sql="select subcompanyid1 from HrmDepartment where id="+belongdepart;
				rs.executeSql(sql);
				if(rs.next()){
					belongCompany = Util.null2String(rs.getString("subcompanyid1"));
				}
				pidComMap.put("belongCompany", belongCompany);
			}
			pidComMap.put("prjtype", prjtype);
			//pidComMap.put("procode", procode);
			pidComMap.put("id", su.getTableMaxId("hs_projectinfo"));
			pidComMap.put("status", getPrjStatus(prjtype));
			InsertUtil iu = new InsertUtil();
			iu.insert(pidComMap, "hs_projectinfo");
			ConnStatement cs = new ConnStatement();
			sql="select max(id) as id from hs_projectinfo where procode=?";
			 try {
					cs.setStatementSql(sql);
					cs.setString(1,procode);						
					cs.executeQuery();	
					if(cs.next()){
						prjid=Util.null2String(cs.getString("id"));
					}
					cs.close();
				} catch (Exception e) {
					cs.close();
					e.printStackTrace();
				}				

			if(!"".equals(prjid) ){
				pidDefMap.put("prjid", prjid);
				pidDefMap.put("id", su.getTableMaxId("hs_project_fielddata"));
				iu.insert(pidDefMap, "hs_project_fielddata");
				pld.writePrjLog("0", "0", prjid, pidComMap, pidDefMap, new HashMap<String, String>(), new HashMap<String, String>(), prjtype, userid);
			}
			if(!"".equals(prjid)){
				insertPrjProcess(prjid,prjtype,userid);
			}
			return prjid;
		}
		/**
		 * 获取项目状态
		 * @param prjtype
		 * @return
		 */
		public String getPrjStatus(String prjtype){
			RecordSet rs = new RecordSet();
			String status = "";
			String sql="select id,processname from uf_prj_process where prjtype='"+prjtype+"' and isused='1' order by dsporder asc,id asc";
			rs.executeSql(sql);
			if(rs.next()){
				status = Util.null2String(rs.getString("processname"));
			}
			return status;
		}
		/**
		 * 更新项目信息
		 * @param pidComMap
		 * @param pidDefMap
		 * @param prjid
		 */
		public void editProjectInfo(Map<String, String> pidComMap,Map<String, String> pidDefMap,String prjid,String userid,String editType){
			BaseBean log = new BaseBean();
			RecordSet rs = new RecordSet();
			SysnoUtil su = new SysnoUtil();
			InsertUtil iu = new InsertUtil();
			PrjLogDao pld = new PrjLogDao();
			String sql="";
			String ideId="";
			String prjtype = "";
			Map<String, String> oldComMap = new HashMap<String, String>();
			Map<String, String> oldDefMap = new HashMap<String, String>();
			oldComMap = getOldValueMap(prjid,pidComMap,"0");
			iu.updateGen(pidComMap, "hs_projectinfo", "id", prjid);
			if("1".equals(editType)){//部门对接人编辑
				String oldManager = Util.null2String(oldComMap.get("manager"));
				String newManager = Util.null2String(pidComMap.get("manager"));
				if(!oldManager.equals(newManager)){
					SendEmail se = new SendEmail();
					try {
						se.SendEmailByChangePerson(prjid,userid,oldManager,newManager);
					} catch (Exception e) {
						log.writeLog("发送变更提醒邮件失败 prjid");
						log.writeLog(e);
					}
				}
			}
			String belongdepart = "";

			String belongCompany = "";
			sql="select belongdepart,prjtype from hs_projectinfo where id="+prjid;
			rs.executeSql(sql);
			if(rs.next()){
				belongdepart = Util.null2String(rs.getString("belongdepart"));
				prjtype = Util.null2String(rs.getString("prjtype"));
			}
			if(!"".equals(belongdepart)){
				sql="select subcompanyid1 from HrmDepartment where id="+belongdepart;
				rs.executeSql(sql);
				if(rs.next()){
					belongCompany = Util.null2String(rs.getString("subcompanyid1"));
				}
				 
			}else{
				belongCompany = "";
			}
			sql="update hs_projectinfo set belongCompany='"+belongCompany+"' where id="+prjid;
			rs.executeSql(sql);
			
			sql="select id from hs_project_fielddata where prjid="+prjid;
			rs.executeSql(sql);
			if(rs.next()){
				ideId = Util.null2String(rs.getString("id"));
			}
			if("".equals(ideId) ){
				pidDefMap.put("prjid", prjid);
				pidDefMap.put("id", su.getTableMaxId("hs_project_fielddata"));
				iu.insert(pidDefMap, "hs_project_fielddata");
			}else if(!"".equals(ideId) && pidDefMap.size()>0){
				oldDefMap = getOldValueMap(prjid,pidDefMap,"0");
				iu.updateGen(pidDefMap, "hs_project_fielddata", "id", ideId);
			}
			pld.writePrjLog("1", "0", prjid, pidComMap, pidDefMap, oldComMap, oldDefMap, prjtype, userid);
		}
		/**
		 * 插入项目过程
		 * @param prjid
		 * @param prjtype 
		 */
		public void insertPrjProcess(String prjid,String prjtype,String userid){
			RecordSet rs = new RecordSet();
			InsertUtil iu = new InsertUtil();
			SysnoUtil su = new SysnoUtil();
			PrjLogDao pld = new PrjLogDao();
			String 	processtype = "";
			String processname = "";
			String sql="select id,processname from uf_prj_process where prjtype='"+prjtype+"' and isused='1' order by dsporder asc,id asc";
			rs.executeSql(sql);
			while(rs.next()){
				processtype = Util.null2String(rs.getString("id"));
				processname = Util.null2String(rs.getString("processname"));
				String processid=su.getTableMaxId("hs_prj_process");
				Map<String, String> processMap = new HashMap<String, String>();
				processMap.put("processname", processname);
				processMap.put("prjtype", prjtype);
				processMap.put("processtype", processtype);
				processMap.put("prjid", prjid);
				processMap.put("id",processid);
				iu.insert(processMap, "hs_prj_process");
				
				Map<String, String> pidDefMap = new HashMap<String, String>();
				pidDefMap.put("prjid", prjid);
				pidDefMap.put("processid", processid);
				pidDefMap.put("id", su.getTableMaxId("hs_prj_process_fielddata"));
				iu.insert(pidDefMap, "hs_prj_process_fielddata");
				pld.writePrjLog("0", "1", processid, processMap, pidDefMap, new HashMap<String, String>(), new HashMap<String, String>(), processtype, userid);
			}
		}
		
		/**
		 * 获取下个项目编号
		 * @param prjtype
		 * @param indexno
		 */
		public String getPrjNo(String prjtype,int indexno){
			RecordSet rs = new RecordSet();
			SysnoUtil su = new SysnoUtil();
			String proTypeCode = "";
			String seqNo = "";
			String sql="select protypecode from uf_project_type where id="+prjtype;
			rs.executeSql(sql);
			if(rs.next()){
				proTypeCode = Util.null2String(rs.getString("protypecode"));
			}
			seqNo=su.getNum(proTypeCode, "hs_projectinfo", indexno);
			return seqNo;
		}
		/**
		 * 
		 * @param prjid 项目id
		 * @param type  类型 0 通用 1自定义
		 * @return
		 */
		public Map<String, String> getOldValueMap(String prjid,Map<String, String> newMap,String type){
			RecordSet rs = new RecordSet();
			Map<String, String> oldMap = new HashMap<String, String>();
			String sql="";
			String fieldName = "";
			String oldValue = "";
			if("0".equals(type)){
				sql="select * from hs_projectinfo where id="+prjid;
			}else{
				sql="select * from hs_project_fielddata where prjid="+prjid;
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
