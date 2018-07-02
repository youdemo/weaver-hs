package test;


import tmc.org.HrmOrgAction;
import tmc.org.HrmDepartmentBean;
import tmc.org.HrmJobTitleBean;
import tmc.org.HrmResourceBean;
import tmc.org.HrmSubCompanyBean;
import tmc.org.ReturnInfo;
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.schedule.BaseCronJob;

public class SysnOrgAction extends BaseCronJob {

	BaseBean log = new BaseBean();

	public void execute() {
		log.writeLog("开始同步 ");
		sysnSubCompany();
		sysnDepartment();
		sysnJobtitle();
		sysnResource();
		log.writeLog("结束同步");
	}

	public void sysnSubCompany() {
		log.writeLog("开始同步分部 start sysnSubCompany ");
		RecordSetDataSource rsd = new RecordSetDataSource("EHR");
		HrmOrgAction hoa = new HrmOrgAction();
		String ORGUID = "";// 公司编码
		String b001005 = "";// 公司名称
		String b001007 = "";// 公司简称
		String b001002 = "";// 上级分部编码
		String B001715 = "";// 显示顺序
		String orgdescribe = "";// 描述
		String sql = "select ORGUID,b001005,b001007,b001002,B001715,orgdescribe from org_info_oa ";
		rsd.executeSql(sql);
		while (rsd.next()) {
			ORGUID = Util.null2String(rsd.getString("ORGUID"));
			b001005 = Util.null2String(rsd.getString("b001005"));
			b001007 = Util.null2String(rsd.getString("b001007"));
			b001002 = Util.null2String(rsd.getString("b001002"));
			B001715 = Util.null2String(rsd.getString("B001715"));
			orgdescribe = Util.null2String(rsd.getString("orgdescribe"));
			HrmSubCompanyBean hsb = new HrmSubCompanyBean();
			hsb.setSubCompanyCode(ORGUID);
			hsb.setSubCompanyName(b001005);
			hsb.setSubCompanyDesc(b001005);
			hsb.setIdOrCode(1);
			hsb.setSuperCode(b001002);
			hsb.setOrderBy(0);
			hsb.setStatus(0);
			ReturnInfo result = hoa.operSubCompany(hsb);
			if (!result.isTure()) {
				log.writeLog("分部同步失败 ORGUID:" + ORGUID + " result:"
						+ result.getRemark());
			}
		}
		log.writeLog("同步分部结束  end sysnSubCompany ");
	}

	public void sysnDepartment() {
		log.writeLog("开始同步部门 start sysnDepartment ");
		RecordSetDataSource rsd = new RecordSetDataSource("EHR");
		String ORGUID = "";// 部门编码
		String b001005 = "";// 部门名称
		String b001007 = "";// 部门简称
		String b001002 = "";// 上级部门编码
		String B001715 = "";// 显示顺序
		String superorgid = "";// 所属分部编码
		HrmOrgAction hoa = new HrmOrgAction();
		String sql = "select ORGUID,b001005,b001007,b001002,B001715,superorgid from dept_info_oa";
		rsd.executeSql(sql);
		while (rsd.next()) {
			ORGUID = Util.null2String(rsd.getString("ORGUID"));
			b001005 = Util.null2String(rsd.getString("b001005"));
			b001007 = Util.null2String(rsd.getString("b001007"));
			b001002 = Util.null2String(rsd.getString("b001002"));
			B001715 = Util.null2String(rsd.getString("B001715"));
			superorgid = Util.null2String(rsd.getString("superorgid"));
			HrmDepartmentBean hdb = new HrmDepartmentBean();
			hdb.setDepartmentcode(ORGUID);
			hdb.setDepartmentname(b001005);
			hdb.setDepartmentark(b001005);
			hdb.setComIdOrCode(1);
			hdb.setSubcompanyCode(superorgid);
			hdb.setIdOrCode(1);
			hdb.setSuperCode(b001002);
			hdb.setOrderBy(0);
			hdb.setStatus(0);
			ReturnInfo result = hoa.operDept(hdb);
			if (!result.isTure()) {
				log.writeLog("部门同步失败 ORGUID:" + ORGUID + " result:"
						+ result.getRemark());
			}
		}
		log.writeLog("同步部门结束  end sysnDepartment ");
	}

	public void sysnJobtitle() {
		log.writeLog("开始同步岗位 start sysnJobtitle ");
		RecordSetDataSource rsd = new RecordSetDataSource("EHR");
		HrmOrgAction hoa = new HrmOrgAction();
		String POSTID = ""; // 岗位编码
		String C001700 = ""; // 岗位描述
		String C001005 = ""; // 岗位简称
		String C001010 = ""; // 所属部门编码
		String superpostid = ""; // 上级岗位
		String sql = "select POSTID,C001700,C001005,C001010,superpostid from post_info_oa";
		rsd.executeSql(sql);
		while (rsd.next()) {
			POSTID = Util.null2String(rsd.getString("POSTID"));
			C001700 = Util.null2String(rsd.getString("C001700"));
			C001005 = Util.null2String(rsd.getString("C001005"));
			C001010 = Util.null2String(rsd.getString("C001010"));
			superpostid = Util.null2String(rsd.getString("superpostid"));
			HrmJobTitleBean hjt = new HrmJobTitleBean();
			hjt.setJobtitlecode(POSTID);
			hjt.setJobtitlename(C001005);
			hjt.setJobtitlemark(C001005);
			hjt.setJobtitleremark(C001005);
			hjt.setDeptIdOrCode(1);
			hjt.setJobdepartmentCode(C001010);
			hjt.setSuperJobCode(superpostid);
			hjt.setJobactivityName(C001005);
			hjt.setJobGroupName(C001005);
			ReturnInfo result = hoa.operJobtitle(hjt);
			if (!result.isTure()) {
				log.writeLog("岗位同步失败 POSTID:" + POSTID + " result:"
						+ result.getRemark());
			}
		}
		log.writeLog("同步岗位结束 end sysnJobtitle ");
	}

	public void sysnResource() {
		log.writeLog("开始同步人员 start sysnResource ");
		RecordSetDataSource rsd = new RecordSetDataSource("EHR");
		HrmOrgAction hoa = new HrmOrgAction();
		String A001735 = ""; // 工号
		String A001001 = ""; // 姓名
		String login_name = ""; // 系统登录账号
		String A001998 = ""; // 上级工号
		String scale = ""; // 工作职级
		String A080710 = ""; // 手机号
		String a080705 = ""; // 电话
		String A001257 = ""; // 电子邮箱
		String a001077 = ""; // 身份证号码
		String sex = ""; // 性别
		String A001705 = ""; // 所属部门编码
		String A001715 = ""; // 岗位编码
		String A001011 = ""; // 生日
		String marry = ""; // 婚姻状况
		String place = ""; // 籍贯
		String diploma = ""; // 学历
		String status = ""; // 状态 在职 离职
		String A001820 = ""; // 工作地点
		String a001044 = ""; // 入职日期

		String sql = "select A001735,A001001,login_name,A001998,scale,A080710," +
				"a080705,A001257,a001077,sex,A001705,A001715,A001011,marry," +
				"place,diploma,status,A001820,a001044 from emp_info_oa";
		rsd.executeSql(sql);
		while(rsd.next()){
			A001735 = Util.null2String(rsd.getString("A001735"));
			A001001 = Util.null2String(rsd.getString("A001001"));
			login_name = Util.null2String(rsd.getString("login_name"));
			A001998 = Util.null2String(rsd.getString("A001998"));
			scale = Util.null2String(rsd.getString("scale"));
			A080710 = Util.null2String(rsd.getString("A080710"));
			a080705 = Util.null2String(rsd.getString("a080705"));
			A001257 = Util.null2String(rsd.getString("A001257"));
			a001077 = Util.null2String(rsd.getString("a001077"));
			sex = Util.null2String(rsd.getString("sex"));
			A001705 = Util.null2String(rsd.getString("A001705"));
			A001715 = Util.null2String(rsd.getString("A001715"));
			A001011 = Util.null2String(rsd.getString("A001011"));
			marry = Util.null2String(rsd.getString("marry"));
			place = Util.null2String(rsd.getString("place"));
			diploma = Util.null2String(rsd.getString("diploma"));
			status = Util.null2String(rsd.getString("status"));
			A001820 = Util.null2String(rsd.getString("A001820"));
			a001044 = Util.null2String(rsd.getString("a001044"));
			
			if("男".equals(sex)){
				sex = "0";
			}else if("女".equals(sex)){
				sex = "1";
			}else{
				sex = "";
			}
			
			if("未婚".equals(marry)){
				marry = "0";
			}else if("已婚".equals(marry)){
				marry = "1";
			}else{
				marry = "";
			}
			if("正式人员".equals(status)){
				status = "1";
			}else{
				status = "5";
			}
			HrmResourceBean hrb = new HrmResourceBean();
			hrb.setWorkcode(A001735);
			hrb.setLoginid(A001735);
			hrb.setLastname(A001001);
			hrb.setDeptIdOrCode(1);
			hrb.setDepartmentCode(A001705);
			hrb.setJobIdOrCode(1);
			if("".equals(A001715)){
				A001715 = "-111";
			}
			hrb.setJobtitleCode(A001715);
			hrb.setManagerIdOrCode(1);
			hrb.setManagerCode(A001998);
			
			hrb.setMobile(A080710);
			//hrb.setTelephone(a080705);
			//hrb.setEmail(A001257);
			hrb.setCertificatenum(a001077);
			hrb.setSex(sex);
			hrb.setBirthday(A001011);
			hrb.setMaritalstatus(marry);
			hrb.setNativeplace(place);
			hrb.setEducationlevel(diploma);
			hrb.setStatus(status);
			//hrb.setLocationid(A001820);
			hrb.setStartdate(a001044);
			ReturnInfo result = hoa.operResource(hrb);
			if (!result.isTure()) {
				log.writeLog("人员同步失败 Workcode:" + A001735 + " result:"
						+ result.getRemark());
			}
		}

		log.writeLog("同步人员结束  end sysnResource ");
	}
}
