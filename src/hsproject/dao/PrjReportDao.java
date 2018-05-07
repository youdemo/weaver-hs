package hsproject.dao;

import hsproject.bean.PrjAllReportBean;
import hsproject.bean.PrjAllReportDtBean;
import hsproject.bean.PrjReportBean;
import hsproject.bean.PrjReportDtBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class PrjReportDao {
	/**
	 * 获取报表配置数据
	 * 
	 * @param reportid
	 * @return
	 */
	public PrjReportBean getPrjReportBean(String reportid) {
		RecordSet rs = new RecordSet();
		PrjReportBean prb = new PrjReportBean();
		String mainid = "";
		if ("".equals(reportid)) {
			return prb;
		}
		String sql = "select * from uf_prj_report_conf where id=" + reportid;
		rs.executeSql(sql);
		if (rs.next()) {
			mainid = Util.null2String(rs.getString("id"));
			prb.setId(mainid);
			prb.setPrjtype(Util.null2String(rs.getString("prjtype")));
			prb.setCreatedate(Util.null2String(rs.getString("createdate")));
			prb.setCreatetime(Util.null2String(rs.getString("createtime")));
			prb.setCreaterperson(Util.null2String(rs.getString("createrperson")));
			prb.setReportname(Util.null2String(rs.getString("reportname")));
			prb.setReporturl(Util.null2String(rs.getString("reporturl")));
		}
		if (!"".equals(mainid)) {
			List<PrjReportDtBean> dtList = new ArrayList<PrjReportDtBean>();
			sql = "select a.*,b.showname from uf_prj_report_conf_dt1 a,uf_project_field b where a.fieldid=b.id  and a.mainid="
					+ mainid + " order by a.dsporder asc,b.dsporder asc,b.id asc";
			rs.executeSql(sql);
			while (rs.next()) {
				PrjReportDtBean pdb = new PrjReportDtBean();
				pdb.setFieldid(Util.null2String(rs.getString("fieldid")));
				pdb.setFieldname(Util.null2String(rs.getString("fieldname")));
				pdb.setId(Util.null2String(rs.getString("id")));
				pdb.setIscommon(Util.null2String(rs.getString("iscommon")));
				pdb.setIshow(Util.null2String(rs.getString("ishow")));
				pdb.setIsquery(Util.null2String(rs.getString("isquery")));
				pdb.setShowname(Util.null2String(rs.getString("showname")));
				pdb.setMainid(mainid);
				pdb.setDsporder(Util.null2String(rs.getString("dsporder")));
				dtList.add(pdb);
			}
			prb.setDtList(dtList);
		}
		return prb;
	}

	/**
	 * 获取报表名称
	 * 
	 * @param reportid
	 * @return
	 */
	public String getReportName(String reportid) {
		RecordSet rs = new RecordSet();
		String reportname = "";
		String sql = "select reportname from uf_prj_report_conf where id="
				+ reportid;
		rs.executeSql(sql);
		if (rs.next()) {
			reportname = Util.null2String(rs.getString("reportname"));
		}
		return reportname;

	}

	/**
	 * 获取报表显示字段
	 * 
	 * @param reportid
	 * @return
	 */
	public Map<String, String> getReportShowMap(String reportid) {
		RecordSet rs = new RecordSet();

		Map<String, String> reportShowMap = new HashMap<String, String>();
		String mainid = "";
		String fieldname = "";
		String iscommon = "";
		String sql = "select id from uf_prj_report_conf where id=" + reportid;
		rs.executeSql(sql);
		if (rs.next()) {
			mainid = Util.null2String(rs.getString("id"));
		}
		sql = "select * from uf_prj_report_conf_dt1 where mainid=" + mainid
				+ " and ishow='0'";
		rs.executeSql(sql);
		while (rs.next()) {
			fieldname = Util.null2String(rs.getString("fieldname"));
			iscommon = Util.null2String(rs.getString("iscommon"));
			if (!"0".equals(iscommon)) {
				fieldname = "def_" + fieldname;
			}
			reportShowMap.put(fieldname, "0");

		}
		return reportShowMap;
	}
	
	public Map<String, String> getReportDsporderMap(String reportid) {
		RecordSet rs = new RecordSet();

		Map<String, String> reportDsporderMap = new HashMap<String, String>();
		String mainid = "";
		String fieldname = "";
		String iscommon = "";
		String dsporder = "";
		String sql = "select id from uf_prj_report_conf where id=" + reportid;
		rs.executeSql(sql);
		if (rs.next()) {
			mainid = Util.null2String(rs.getString("id"));
		}
		sql = "select * from uf_prj_report_conf_dt1 where mainid=" + mainid;
		rs.executeSql(sql);
		while (rs.next()) {
			fieldname = Util.null2String(rs.getString("fieldname"));
			iscommon = Util.null2String(rs.getString("iscommon"));
			dsporder = Util.null2String(rs.getString("dsporder"));
			if (!"0".equals(iscommon)) {
				fieldname = "def_" + fieldname;
			}
			reportDsporderMap.put(fieldname, dsporder);

		}
		return reportDsporderMap;
	}

	/**
	 * 获取报表查询字段
	 * 
	 * @param reportid
	 * @return
	 */
	public Map<String, String> getReportQueryMap(String reportid) {
		RecordSet rs = new RecordSet();

		Map<String, String> reportqueryMap = new HashMap<String, String>();
		String mainid = "";
		String fieldname = "";
		String iscommon = "";
		String sql = "select id from uf_prj_report_conf where id=" + reportid;
		rs.executeSql(sql);
		if (rs.next()) {
			mainid = Util.null2String(rs.getString("id"));
		}
		sql = "select * from uf_prj_report_conf_dt1 where mainid=" + mainid
				+ " and isquery='0'";
		rs.executeSql(sql);
		while (rs.next()) {
			fieldname = Util.null2String(rs.getString("fieldname"));
			iscommon = Util.null2String(rs.getString("iscommon"));
			if (!"0".equals(iscommon)) {
				fieldname = "def_" + fieldname;
			}
			reportqueryMap.put(fieldname, "0");

		}
		return reportqueryMap;
	}

	public PrjAllReportBean getPrjAllReportBean(String reportid) {
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		PrjAllReportBean prb = new PrjAllReportBean();
		String mainid = "";
		String processtype = "";
		String sql_dt = "";
		if ("".equals(reportid)) {
			return prb;
		}
		String sql = "select * from uf_prj_all_reportmt where id=" + reportid;
		rs.executeSql(sql);
		if (rs.next()) {
			mainid = Util.null2String(rs.getString("id"));
			prb.setId(mainid);
			prb.setPrjtype(Util.null2String(rs.getString("prjtype")));
			prb.setCreatedate(Util.null2String(rs.getString("createdate")));
			prb.setCreatetime(Util.null2String(rs.getString("createtime")));
			prb.setCreaterperson(Util.null2String(rs.getString("createrperson")));
			prb.setReportname(Util.null2String(rs.getString("reportname")));
			prb.setReporturl(Util.null2String(rs.getString("reporturl")));
		}
		if (!"".equals(mainid)) {
			Map<String, List<PrjAllReportDtBean>> dtmap = new HashMap<String, List<PrjAllReportDtBean>>();

			List<PrjAllReportDtBean> prjlist = new ArrayList<PrjAllReportDtBean>();
			sql = "select a.*,b.showname from uf_prj_all_reportmt_dt1 a,uf_project_field b where a.fieldid=b.id  and a.mainid="
					+ mainid
					+ " and fieldbelong='0' order by a.dsporder asc,b.dsporder asc,b.id asc";
			rs.executeSql(sql);
			while (rs.next()) {
				PrjAllReportDtBean pdb = new PrjAllReportDtBean();
				pdb.setFieldid(Util.null2String(rs.getString("fieldid")));
				pdb.setFieldname(Util.null2String(rs.getString("fieldname")));
				pdb.setId(Util.null2String(rs.getString("id")));
				pdb.setIscommon(Util.null2String(rs.getString("iscommon")));
				pdb.setIshow(Util.null2String(rs.getString("ishow")));
				pdb.setIsquery(Util.null2String(rs.getString("isquery")));
				pdb.setShowname(Util.null2String(rs.getString("showname")));
				pdb.setMainid(mainid);
				pdb.setFieldbelong(Util.null2String(rs.getString("fieldbelong")));
				pdb.setProcesstype(Util.null2String(rs.getString("processtype")));
				pdb.setDsporder(Util.null2String(rs.getString("dsporder")));
				prjlist.add(pdb);
			}
			dtmap.put("prj", prjlist);
			sql = "select distinct(processtype) as processtype  from uf_prj_all_reportmt_dt1 where mainid="
					+ mainid + " and fieldbelong='1'";
			rs.executeSql(sql);
			while (rs.next()) {
				processtype = Util.null2String(rs.getString("processtype"));
				if ("".equals(processtype)) {
					continue;
				}
				List<PrjAllReportDtBean> processList = new ArrayList<PrjAllReportDtBean>();
				sql_dt = "select a.*,b.showname from uf_prj_all_reportmt_dt1 a,uf_prj_porcessfield b where a.fieldid=b.id  and a.mainid="
						+ mainid
						+ " and fieldbelong='1' and a.processtype='"
						+ processtype + "'  order by a.dsporder asc,b.dsporder asc,b.id asc";
				rs_dt.executeSql(sql_dt);
				while (rs_dt.next()) {
					PrjAllReportDtBean pdb = new PrjAllReportDtBean();
					pdb.setFieldid(Util.null2String(rs_dt.getString("fieldid")));
					pdb.setFieldname(Util.null2String(rs_dt
							.getString("fieldname")));
					pdb.setId(Util.null2String(rs_dt.getString("id")));
					pdb.setIscommon(Util.null2String(rs_dt
							.getString("iscommon")));
					pdb.setIshow(Util.null2String(rs_dt.getString("ishow")));
					pdb.setIsquery(Util.null2String(rs_dt.getString("isquery")));
					pdb.setShowname(Util.null2String(rs_dt
							.getString("showname")));
					pdb.setMainid(mainid);
					pdb.setFieldbelong(Util.null2String(rs_dt
							.getString("fieldbelong")));
					pdb.setProcesstype(Util.null2String(rs_dt
							.getString("processtype")));
					pdb.setDsporder(Util.null2String(rs_dt.getString("dsporder")));
					processList.add(pdb);
				}
				dtmap.put(processtype, processList);
			}
			prb.setDtMap(dtmap);
		}

		return prb;
	}

	public String getAllReportName(String reportid) {
		RecordSet rs = new RecordSet();
		String reportname = "";
		String sql = "select reportname from uf_prj_all_reportmt where id="
				+ reportid;
		rs.executeSql(sql);
		if (rs.next()) {
			reportname = Util.null2String(rs.getString("reportname"));
		}
		return reportname;

	}

	public Map<String, String> getAllReportShowMap(String reportid, String type) {
		RecordSet rs = new RecordSet();

		Map<String, String> reportShowMap = new HashMap<String, String>();
		String mainid = "";
		String fieldname = "";
		String iscommon = "";
		String sql = "select id from uf_prj_all_reportmt where id=" + reportid;
		rs.executeSql(sql);
		if (rs.next()) {
			mainid = Util.null2String(rs.getString("id"));
		}
		if ("prj".equals(type)) {
			sql = "select * from uf_prj_all_reportmt_dt1 where mainid="
					+ mainid + " and ishow='0' and fieldbelong='0'";
		} else {
			sql = "select * from uf_prj_all_reportmt_dt1 where mainid="
					+ mainid
					+ " and ishow='0' and fieldbelong='1' and processtype='"
					+ type + "'";
		}
		rs.executeSql(sql);
		while (rs.next()) {
			fieldname = Util.null2String(rs.getString("fieldname"));
			iscommon = Util.null2String(rs.getString("iscommon"));
			if (!"0".equals(iscommon)) {
				fieldname = "def_" + fieldname;
			}
			reportShowMap.put(fieldname, "0");

		}
		return reportShowMap;
	}

	public Map<String, String> getAllReportQueryMap(String reportid) {
		RecordSet rs = new RecordSet();

		Map<String, String> reportqueryMap = new HashMap<String, String>();
		String mainid = "";
		String fieldname = "";
		String iscommon = "";
		String sql = "select id from uf_prj_all_reportmt where id=" + reportid;
		rs.executeSql(sql);
		if (rs.next()) {
			mainid = Util.null2String(rs.getString("id"));
		}
		sql = "select * from uf_prj_all_reportmt_dt1 where mainid=" + mainid
				+ " and isquery='0' and fieldbelong='0'";
		rs.executeSql(sql);
		while (rs.next()) {
			fieldname = Util.null2String(rs.getString("fieldname"));
			iscommon = Util.null2String(rs.getString("iscommon"));
			if (!"0".equals(iscommon)) {
				fieldname = "def_" + fieldname;
			}
			reportqueryMap.put(fieldname, "0");

		}
		return reportqueryMap;
	}
	
	public Map<String, String> getAllReportDsporderMap(String reportid, String type) {
		RecordSet rs = new RecordSet();

		Map<String, String> reportDsporderMap = new HashMap<String, String>();
		String mainid = "";
		String fieldname = "";
		String iscommon = "";
		String dsporder = "";
		String sql = "select id from uf_prj_all_reportmt where id=" + reportid;
		rs.executeSql(sql);
		if (rs.next()) {
			mainid = Util.null2String(rs.getString("id"));
		}
		if ("prj".equals(type)) {
			sql = "select * from uf_prj_all_reportmt_dt1 where mainid="
					+ mainid + " and ishow='0' and fieldbelong='0'";
		}else {
			sql = "select * from uf_prj_all_reportmt_dt1 where mainid="
					+ mainid
					+ " and ishow='0' and fieldbelong='1' and processtype='"
					+ type + "'";
		}
		rs.executeSql(sql);
		while (rs.next()) {
			fieldname = Util.null2String(rs.getString("fieldname"));
			iscommon = Util.null2String(rs.getString("iscommon"));
			dsporder = Util.null2String(rs.getString("dsporder"));
			if (!"0".equals(iscommon)) {
				fieldname = "def_" + fieldname;
			}
			reportDsporderMap.put(fieldname, dsporder);

		}
		return reportDsporderMap;
	}
}
