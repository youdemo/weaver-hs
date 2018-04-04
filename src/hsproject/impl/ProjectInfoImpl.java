package hsproject.impl;

import hsproject.util.InsertUtil;
import hsproject.util.SysnoUtil;

import java.util.HashMap;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class ProjectInfoImpl {
		/**
		 * 新建项目
		 * @param pidComMap
		 * @param pidDefMap
		 * @param prjtype
		 * @return
		 */
		public String  addPorjectInfo(Map<String, String> pidComMap,Map<String, String> pidDefMap,String prjtype){
			RecordSet rs = new RecordSet();
			String prjid="";
			String sql="";
			SysnoUtil su = new SysnoUtil();
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
			
			sql="select id from hs_projectinfo where procode='"+procode+"'";
			rs.executeSql(sql);
			if(rs.next()){
				prjid = Util.null2String(rs.getString("id"));
			}
			if(!"".equals(prjid) && pidDefMap.size()>0){
				pidDefMap.put("prjid", prjid);
				pidDefMap.put("id", su.getTableMaxId("hs_project_fielddata"));
				iu.insert(pidDefMap, "hs_project_fielddata");
			}
			if(!"".equals(prjid)){
				insertPrjProcess(prjid,prjtype);
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
		public void editProjectInfo(Map<String, String> pidComMap,Map<String, String> pidDefMap,String prjid){
			RecordSet rs = new RecordSet();
			SysnoUtil su = new SysnoUtil();
			InsertUtil iu = new InsertUtil();
			String sql="";
			String ideId="";
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
			iu.updateGen(pidComMap, "hs_projectinfo", "id", prjid);
			sql="select id from hs_project_fielddata where prjid="+prjid;
			rs.executeSql(sql);
			if(rs.next()){
				ideId = Util.null2String(rs.getString("id"));
			}
			if("".equals(ideId) && pidDefMap.size()>0){
				pidDefMap.put("prjid", prjid);
				pidDefMap.put("id", su.getTableMaxId("hs_project_fielddata"));
				iu.insert(pidDefMap, "hs_project_fielddata");
			}else if(!"".equals(ideId) && pidDefMap.size()>0){
				iu.updateGen(pidDefMap, "hs_project_fielddata", "id", ideId);
			}
		}
		/**
		 * 插入项目过程
		 * @param prjid
		 * @param prjtype
		 */
		public void insertPrjProcess(String prjid,String prjtype){
			RecordSet rs = new RecordSet();
			InsertUtil iu = new InsertUtil();
			SysnoUtil su = new SysnoUtil();
			String 	processtype = "";
			String processname = "";
			String sql="select id,processname from uf_prj_process where prjtype='"+prjtype+"' and isused='1' order by dsporder asc,id asc";
			rs.executeSql(sql);
			while(rs.next()){
				processtype = Util.null2String(rs.getString("id"));
				processname = Util.null2String(rs.getString("processname"));

				Map<String, String> processMap = new HashMap<String, String>();
				processMap.put("processname", processname);
				processMap.put("prjtype", prjtype);
				processMap.put("processtype", processtype);
				processMap.put("prjid", prjid);
				processMap.put("id", su.getTableMaxId("hs_prj_process"));
				iu.insert(processMap, "hs_prj_process");
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
}
