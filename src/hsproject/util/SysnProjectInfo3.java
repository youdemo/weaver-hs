package hsproject.util;

import java.util.HashMap;
import java.util.Map;

import hsproject.impl.AddDocShare;
import hsproject.impl.ProjectInfoImpl;
import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.Util;

public class SysnProjectInfo3 {
	public void sysnprj() {
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		SysnoUtil su = new SysnoUtil();

		InsertUtil iu = new InsertUtil();
		String name = ""; //项目名称
		String xmcode = ""; //项目编码
		String xmjlname = ""; //项目经理
		String xmgs = "";//所属公司
		String xmbm = "";//所属部门
		String sqrq = "";//申请日期
		//String xmzl = "";//项目种类
		//String firsttype  ="";//项目一级分类
		//String xmlb = ""; //项目类别  xmlbMap
		//String lxdh = "";// 联系电话
	//	String xmjlemail = "";// 项目经理邮箱
		String sfndys = "";// 是否年度预算内项目  "是":"否"
		String lxlzxs = "";// 立项论证形式
		String xmtzysje = "";// 预算金额
		String xmrjje = "";//软件金额
		String xmfyzyyjje = "";// 硬件金额
		String xmqtjf = "";// 项目其他经费
		String xmyzyje = "";// 云资源金额
		String xmysyfy = "";// 项目已使用费用
		int xmstatus = -1;
		String sql_dt = "";
		String sql = "select name,xmcode,xmjlname,xmgs,xmbm,sqrq,xmzl,firsttype,xmlb,lxdh,xmjlemail,sfndys,lxlzxs,xmtzysje,xmrjje,xmfyzyyjje,xmqtjf,xmyzyje,xmysyfy,xmstatus from wasu_projectbase ";
		rs.executeSql(sql);
		while(rs.next()) {
			name = Util.null2String(rs.getString("name"));
			xmcode = Util.null2String(rs.getString("xmcode"));
			xmjlname = Util.null2String(rs.getString("xmjlname"));
			xmgs = Util.null2String(rs.getString("xmgs"));
			xmbm = Util.null2String(rs.getString("xmbm"));
			sqrq = Util.null2String(rs.getString("sqrq"));
			sfndys = Util.null2String(rs.getString("sfndys"));
			lxlzxs = Util.null2String(rs.getString("lxlzxs"));
			xmtzysje = Util.null2String(rs.getString("xmtzysje"));
			xmrjje = Util.null2String(rs.getString("xmrjje"));
			xmfyzyyjje = Util.null2String(rs.getString("xmfyzyyjje"));
			xmqtjf = Util.null2String(rs.getString("xmqtjf"));
			xmyzyje = Util.null2String(rs.getString("xmyzyje"));
			xmysyfy = Util.null2String(rs.getString("xmysyfy"));
			xmstatus = rs.getInt("xmstatus");
			Map<String, String> map= getPrjtype(xmcode);
			String prjtype = Util.null2String(map.get("prjtype"));
			if("".equals(prjtype)) {
				continue;
			}
			String prjid=su.getTableMaxId("hs_projectinfo");
			Map<String, String> pidComMap = new HashMap<String, String>();
			pidComMap.put("id", prjid);
			pidComMap.put("prjtype",prjtype);
			pidComMap.put("name",name);
			pidComMap.put("procode",xmcode);
			pidComMap.put("manager",Util.null2String(map.get("ryid")));
			pidComMap.put("begindate",sqrq);
			pidComMap.put("enddate",map.get("jssj"));
			//pidComMap.put("enddate",name);
			
			String status="";
			if(xmstatus<=0){
				status="立项"; //未立项
          	}else if(xmstatus==1){
          		status="立项";
          	}else if(xmstatus==2){
          		status="上线"; 
          	}else if(xmstatus==3){ 
          		status="初验"; 
          	}else if(xmstatus==4){
          		status="完成";
          	}else if(xmstatus==5){
          		status="项目终止";
          	}else if(xmstatus==9){  
          		status="项目终止"; //暂不立项
          	}else if(xmstatus==7){ 
          		status="在建"; //在建
          	}else if(xmstatus==8){ 
          		status="在建";  
          	}
			pidComMap.put("status",status);
			pidComMap.put("prjbudget",xmtzysje);
			pidComMap.put("softwareamount",xmrjje);
			pidComMap.put("hardwareamount",xmfyzyyjje);
			pidComMap.put("otheramount",xmqtjf);
			pidComMap.put("cloudamount",xmyzyje);
			//pidComMap.put("prjamount",name);
			pidComMap.put("belongdepart",Util.null2String(map.get("dpid")));
			pidComMap.put("belongCompany",Util.null2String(map.get("ssgs")));
			//pidComMap.put("attach",name);
			//pidComMap.put("isdelay",name);
			//pidComMap.put("prjobject",name);
			String isinneryear ="";
			if("0".equals(sfndys)) {
				isinneryear = "21";//换
			}else {
				isinneryear = "22";//换
			}
			pidComMap.put("isinneryear",isinneryear);
			if(!"".equals(lxlzxs)) {
				pidComMap.put("argumentform",String.valueOf(Util.getIntValue(lxlzxs,1)+22));//换
			}
			//pidComMap.put("argumentform",name);
			//pidComMap.put("members",name);
			
			
			iu.insert(pidComMap, "hs_projectinfo");
			if(!"".equals(prjid)){
				ProjectInfoImpl pii = new ProjectInfoImpl();
				pii.insertPrjProcess(prjid,prjtype,"1");
			}
		}
	}
	
	public Map<String, String> getPrjtype(String prjcode) {
		Map<String, String> map = new HashMap<String, String>();
		RecordSet rs = new RecordSet();
		String prjtype = "";
		String ssgs = "";
		String ssbm = "";
		String xmjl = "";
		String dpid = "";
		String ryid = "";
		String jssj = "";
		int count = 0;
		String sql="select b.id,a.ssgs,a.ssbm,a.xmjl,a.jssj from uf_prj_type_mid a,uf_project_type b where a.prjtype=b.protypecode and a.ssgs=b.subcompany and a.prjcode='"+prjcode+"'";
		rs.executeSql(sql);
		if(rs.next()) {
			prjtype = Util.null2String(rs.getString("id"));
			ssgs = Util.null2String(rs.getString("ssgs"));
			ssbm = Util.null2String(rs.getString("ssbm"));
			xmjl = Util.null2String(rs.getString("xmjl"));
			jssj = Util.null2String(rs.getString("jssj"));
		}
		sql="select * from hrmdepartment where subcompanyid1='"+ssgs+"' and departmentname='"+ssbm+"'";
		rs.executeSql(sql);
		if(rs.next()) {
			dpid = Util.null2String(rs.getString("id"));
		}
		sql="select count(1) as count from hrmresource where lastname='"+xmjl+"'";
		rs.executeSql(sql);
		if(rs.next()) {
			count =rs.getInt("count");
		}
		if(count == 1) {
			sql="select id from hrmresource where lastname='"+xmjl+"'";
			rs.executeSql(sql);
			if(rs.next()) {
				ryid = Util.null2String(rs.getString("id"));
			}
		}else if(count >1) {
			sql="select id from hrmresource where lastname='"+xmjl+"' and nvl(belongto,0)<=0 and subcompanyid1 in (6,623) order by status asc";
			rs.executeSql(sql);
			if(rs.next()) {
				ryid = Util.null2String(rs.getString("id"));
			}
		}
		map.put("prjtype", prjtype);
		map.put("ssgs", ssgs);
		map.put("dpid", dpid);
		map.put("ryid", ryid);
		map.put("jssj", jssj);
		return map;
	}
	
	public void sysprocess() {
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		String sql = "";
		String sql_dt = "";
		String prjid = "";
		String procode = "";
		String begindate = "";
		String status = "";
		String xmprjid = "";
		sql="select id,procode,begindate,status from hs_projectinfo";
		rs.executeSql(sql);
		while(rs.next()) {
			prjid = Util.null2String(rs.getString("id"));
			procode = Util.null2String(rs.getString("procode"));
			begindate = Util.null2String(rs.getString("begindate"));
			status = Util.null2String(rs.getString("status"));
			sql_dt = "select id from wasu_projectbase where xmcode='"+procode+"' and sqrq='"+begindate+"'";
			rs_dt.executeSql(sql_dt);
			if(rs_dt.next()) {
				xmprjid = Util.null2String(rs_dt.getString("id"));
			}
			if("".equals(xmprjid)) {
				continue;
			}
			updateProcess(prjid,xmprjid,status);
		}
	}
	
	public void updateProcess(String prjid,String xmprjid,String status) {
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		String sql = "";
		String sql_dt = "";
		String stonedate = "";
		String stonename = "";
		String isover = "";
		sql="select * from wasu_projstone where projid="+xmprjid;
		rs.executeSql(sql);
		while(rs.next()) {
			stonedate = Util.null2String(rs.getString("stonedate"));
			stonename = Util.null2String(rs.getString("stonename"));
			isover = Util.null2String(rs.getString("isover"));
			if("1".equals(isover)){
				sql_dt = "update hs_prj_process set isdone='1',enddate='"+stonedate+"',status='完成',iscomplete='1' where processname='"+stonename+"' and prjid="+prjid;//换
			}else {
				sql_dt = "update hs_prj_process set enddate='"+stonedate+"' where processname='"+stonename+"' and prjid="+prjid;
			}
			rs_dt.executeSql(sql_dt);
		}
		sql = "select processname from uf_prj_proc_status where statusname='"+status+"' order by id desc";
		rs.executeSql(sql);
		if(rs.next()){
			status = Util.null2String(rs.getString("processname"));
		}
		if(!"".equals(status)) {			
			sql="select a.id,b.processname from hs_prj_process a,uf_prj_process b where a.processtype=b.id and b.isused='1' and a.prjid="+prjid+"  order by b.dsporder asc,b.id asc";
			rs.executeSql(sql);
			while(rs.next()) {
				if(status.equals(Util.null2String(rs.getString("processname")))) {
					break;
				}
				sql_dt = "update hs_prj_process set isdone='1',status='完成',iscomplete='1' where id="+Util.null2String(rs.getString("id"));//换
				rs_dt.executeSql(sql_dt);
			}
			sql="update hs_prj_process set status='进行中' where processname='"+status+"' and prjid="+prjid;
			rs.executeSql(sql);
		}
	}
	
	public void sysndoc() {
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		String sql = "";
		String sql_dt = "";
		String prjid = "";
		String procode = "";
		String begindate = "";
		String xmprjid = "";
		sql="select id,procode,begindate from hs_projectinfo";
		rs.executeSql(sql);
		while(rs.next()) {
			prjid = Util.null2String(rs.getString("id"));
			procode = Util.null2String(rs.getString("procode"));
			begindate = Util.null2String(rs.getString("begindate"));
			sql_dt = "select id from wasu_projectbase where xmcode='"+procode+"' and sqrq='"+begindate+"'";
			rs_dt.executeSql(sql_dt);
			if(rs_dt.next()) {
				xmprjid = Util.null2String(rs_dt.getString("id"));
			}
			if("".equals(xmprjid)) {
				continue;
			}
			updatefile(prjid,xmprjid);
		}
	}
	
	public void updatefile(String prjid,String xmprjid) {
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		ModeRightInfo dri = new ModeRightInfo();
		AddDocShare ads = new AddDocShare();
		String prjattach = "";
		String tableName2 = "uf_prj_check_doc";
		String modeId = "";
		String name = "";
		String type = "";
		String checktype = "";
		String attach = "";
		String cjr = "";
		String cjrq = "";
		String flag = "";
		String sql_dt = "";
		String sql="select fileid from wasu_projfiles where projid="+xmprjid;
		rs.executeSql(sql);
		while(rs.next()) {
			prjattach = prjattach+flag+Util.null2String(rs_dt.getString("fileid"));
			flag = ",";
		}
		sql="update hs_projectinfo set attach='"+prjattach+"' where id="+prjid;
		rs.executeSql(sql);
		ads.addShare(prjid, prjattach);
		sql = "select b.id from workflow_bill a,modeinfo b where a.id=b.formid and  a.tablename='"
				+ tableName2 + "'";
		rs.executeSql(sql);
		if (rs.next()) {
			modeId = Util.null2String(rs.getString("id"));
		}
		sql="select creater,createdate,taskname,tasklb,qtfj,fj1,fj2,fj3,fj4,fj5,fj6 from wasu_projtask where projid="+xmprjid;
		rs.executeSql(sql);
		while(rs.next()){
			flag = "";
			attach = "";
			name = Util.null2String(rs.getString("taskname"));
			
			if(!"".equals(Util.null2String(rs.getString("qtfj")))) {
				attach = Util.null2String(rs.getString("qtfj"));
				flag=",";
			}
			if(!"".equals(Util.null2String(rs.getString("fj1")))) {
				attach = attach+flag+Util.null2String(rs.getString("fj1"));
				flag=",";
			}
			if(!"".equals(Util.null2String(rs.getString("fj2")))) {
				attach = attach+flag+Util.null2String(rs.getString("fj2"));
				flag=",";
			}
			if(!"".equals(Util.null2String(rs.getString("fj3")))) {
				attach = attach+flag+Util.null2String(rs.getString("fj3"));
				flag=",";
			}
			if(!"".equals(Util.null2String(rs.getString("fj4")))) {
				attach = attach+flag+Util.null2String(rs.getString("fj4"));
				flag=",";
			}
			if(!"".equals(Util.null2String(rs.getString("fj5")))) {
				attach = attach+flag+Util.null2String(rs.getString("fj5"));
				flag=",";
			}
			if(!"".equals(Util.null2String(rs.getString("fj6")))) {
				attach = attach+flag+Util.null2String(rs.getString("fj6"));
				flag=",";
			}
			if("".equals(attach)) {
				continue;
			}
			cjr = Util.null2String(rs.getString("creater"));
			cjrq = Util.null2String(rs.getString("createdate"));
			String wjlb = Util.null2String(rs.getString("tasklb"));
			if("1".equals(wjlb)) {
				type = "0";
				checktype = "0";
			}else if("2".equals(wjlb)){
				type = "0";
				checktype = "1";
			}else if("3".equals(wjlb)){
				type = "1";
				checktype = "2";
			}else if("4".equals(wjlb)){
				type = "1";
				checktype = "3";
			}else if("5".equals(wjlb)){
				type = "1";
				checktype = "4";
			}else if("6".equals(wjlb)){
				type = "1";
				checktype = "5";
			}else if("21".equals(wjlb)){
				type = "0";
				checktype = "0";
			}else if("22".equals(wjlb)){
				type = "0";
				checktype = "1";
			}else if("23".equals(wjlb)){
				type = "1";
				checktype = "2";
			}else if("24".equals(wjlb)){
				type = "1";
				checktype = "3";
			}else if("25".equals(wjlb)){
				type = "1";
				checktype = "4";
			}else if("26".equals(wjlb)){
				type = "1";
				checktype = "5";
			}else if("27".equals(wjlb)){
				type = "1";
				checktype = "6";
			}else{
				type = "1";
				checktype = "5";
			}
			sql_dt="insert into uf_prj_check_doc(formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,name,type,checktype,attach,cjr,cjrq,prjid ) values("+modeId+",1,0,to_char(sysdate,'yyyy-mm-dd'),'"+name+"','"+type+"','"+checktype+"','"+attach+"','"+cjr+"','"+cjrq+"','"+prjid+"')";
			rs_dt.executeSql(sql_dt);
			
			String billid="";
			sql_dt="select id from uf_prj_check_doc where prjid='"+prjid+"' and attach='"+attach+"' order by id desc";
			rs_dt.executeSql(sql_dt);
			if(rs_dt.next()) {
				billid = Util.null2String(rs_dt.getString("id"));
				dri.editModeDataShare(Integer.valueOf("1"), Integer.valueOf(modeId),
						Integer.valueOf(billid));
			}
			ads.addShare(prjid, attach);
		}
		
		
	}
	
	
}
