package hsproject.impl;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class AddDocShare {
	
	public void addShareByType(String departid,String prjtype){
		RecordSet rs = new RecordSet();
		String docids = "";
		if("".equals(departid) || "".equals(prjtype)){
			return;
		}
		String sql = "select processattach from hs_prj_process where prjtype='"+prjtype+"' and processattach is not null";
		rs.executeSql(sql);
		while(rs.next()){
			docids = Util.null2String(rs.getString("processattach"));
			if(!"".equals(docids)){
				addDocShareDepart(departid,docids);
			}
		}
		sql = "select attach from hs_projectinfo where prjtype="+prjtype;
		rs.executeSql(sql);
		while(rs.next()){
			docids = Util.null2String(rs.getString("attach"));
			if(!"".equals(docids)){
				addDocShareDepart(departid,docids);
			}
		}
		
		sql = "select a.attach from uf_prj_check_doc a, hs_projectinfo b where a.prjid=b.id and b.prjtype="+prjtype;
		rs.executeSql(sql);
		while(rs.next()){
			docids = Util.null2String(rs.getString("attach"));
			if(!"".equals(docids)){
				addDocShareDepart(departid,docids);
			}
		}
	}
	
	/**
	 * 变更项目信息时调用
	 * @param prjid
	 */
	public void addShareByPrj(String prjid){
		RecordSet rs = new RecordSet();
		String docids = "";
		String sql = "select processattach from hs_prj_process where prjid="+prjid+" and processattach is not null";
		rs.executeSql(sql);
		while(rs.next()){
			docids = Util.null2String(rs.getString("processattach"));
			if(!"".equals(docids)){
				addShare(prjid,docids);
			}
		}
		sql = "select attach from hs_projectinfo where id="+prjid;
		rs.executeSql(sql);
		while(rs.next()){
			docids = Util.null2String(rs.getString("attach"));
			if(!"".equals(docids)){
				addShare(prjid,docids);
			}
		}
		
		sql = "select attach from uf_prj_check_doc where prjid="+prjid;
		rs.executeSql(sql);
		while(rs.next()){
			docids = Util.null2String(rs.getString("attach"));
			if(!"".equals(docids)){
				addShare(prjid,docids);
			}
		}
	}
	/**
	 * 阶段新增文件添加共享
	 * @param prjid
	 * @param docids
	 */
	public void addShare(String prjid,String docids){		
		RecordSet rs = new RecordSet();
		String manager = "";//项目经理
		String members = "";//项目成员
		String prjtype = "";//项目类型
		String belongdepart = "";//所属部门
		String bmdjr = "";//部门对接人
		String bmz = "";//部门总
		String glbm = "";//管理部门
		if("".equals(Util.null2String(prjid)) || "".equals(Util.null2String(docids))){
			return;
		}
		String sql = "select manager,members,prjtype,belongdepart from hs_projectinfo where id="+prjid;
		rs.executeSql(sql);
		if(rs.next()){
			manager = Util.null2String(rs.getString("manager"));
			members = Util.null2String(rs.getString("members"));
			prjtype = Util.null2String(rs.getString("prjtype"));
			belongdepart = Util.null2String(rs.getString("belongdepart"));
		}
		sql="select person,general from uf_prj_departperson where department='"+belongdepart+"'";
		rs.executeSql(sql);
		if(rs.next()){
			bmdjr = Util.null2String(rs.getString("person"));
			bmz = Util.null2String(rs.getString("general"));
		}
		sql="select department from uf_project_type  where id="+prjtype;
		rs.executeSql(sql);
		if(rs.next()){
			glbm = Util.null2String(rs.getString("department"));
		}
		if(!"".equals(manager)){
			addDocSharePerson(manager,docids);
		}
		if(!"".equals(members)){
			String memberAttr[] = members.split(",");
			for(String member:memberAttr){
				addDocSharePerson(member,docids);
			}
			
		}
		if(!"".equals(bmz)){
			addDocSharePerson(bmz,docids);
		}
		if(!"".equals(bmdjr)){
			addDocSharePerson(bmdjr,docids);
		}
		if(!"".equals(glbm)){
			addDocShareDepart(glbm,docids);
		}
	}
	
	public void addDocSharePerson(String ryid,String docids){
		RecordSet rs = new RecordSet();
		String sql = "";
		if("".equals(Util.null2String(docids))||"".equals(Util.null2String(ryid))){
			return;
		}
		String docAttr[]=docids.split(",");
		for(String docid:docAttr){
			if("".equals(docid)){
				continue;
			}
			int count = 0;
			sql="select count(1) as count from docshare where docid="+docid+" and sharetype=1 and userid="+ryid;
		    rs.executeSql(sql);
			if(rs.next()){
				count = rs.getInt("count");
			}
			if(count == 0){
				sql="insert into docshare(docid,sharetype,seclevel,rolelevel,sharelevel,userid,downloadlevel,seclevelmax) values("+docid+",1,0,1,1,"+ryid+",1,0)";
				rs.executeSql(sql);
				sql="insert into shareinnerdoc(sourceid,type,content,seclevel,sharelevel,srcfrom,opuser,sharesource,downloadlevel,seclevelmax) values("+docid+",1,"+ryid+",0,1,1,"+ryid+",0,1,255)";
				rs.executeSql(sql);
			}
		}
		
	}
	
	public void addDocShareDepart(String departId,String docids){
		RecordSet rs = new RecordSet();
		String sql = "";
		if("".equals(Util.null2String(docids))||"".equals(Util.null2String(departId))){
			return;
		}
		String docAttr[]=docids.split(",");
		for(String docid:docAttr){
			if("".equals(docid)){
				continue;
			}
			int count = 0;
			sql="select count(1) as count from docshare where docid="+docid+" and sharetype=3 and departmentid="+departId;
		    rs.executeSql(sql);
			if(rs.next()){
				count = rs.getInt("count");
			}
			if(count == 0){
				sql="insert into docshare(docid,sharetype,seclevel,rolelevel,sharelevel,userid,departmentid,downloadlevel,includesub,seclevelmax) values("+docid+",3,10,0,1,0,"+departId+",1,1,100)";
				rs.executeSql(sql);
				sql="insert into shareinnerdoc(sourceid,type,content,seclevel,sharelevel,srcfrom,opuser,sharesource,downloadlevel,seclevelmax) values("+docid+",3,"+departId+",10,1,3,"+departId+",2,1,100)";
				rs.executeSql(sql);
			}
		}
		
	}

}
