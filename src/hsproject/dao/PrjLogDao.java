package hsproject.dao;

import hsproject.bean.PrjLogBean;
import hsproject.bean.PrjLogDtBean;
import hsproject.util.InsertUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;

public class PrjLogDao {
	
	public void insertPrjLog(PrjLogBean plb){
		RecordSet rs = new RecordSet();
		InsertUtil iu = new InsertUtil();	
		BaseBean log = new BaseBean();
		String sql = "";
		String mainid =""; ;
		Map<String, String> map = new HashMap<String, String>();
		map.put("logtype", plb.getLogtype());
		map.put("operater", plb.getOperater());
		map.put("createdate", plb.getCreatedate());
		map.put("createtime", plb.getCreatetime());
		map.put("prjtype", plb.getPrjtype());
		map.put("prjprocess", plb.getPrjprocess());
		map.put("dataid", plb.getDataid());
		map.put("description", plb.getDescription());
		map.put("type", plb.getType());
		iu.insert(map, "uf_prj_log");
		sql= "select max(id) as id from uf_prj_log where dataid='"+ plb.getDataid()+"' and type='"+plb.getType()+"'";
		rs.executeSql(sql);
		if(rs.next()){
			mainid = Util.null2String(rs.getString("id"));
		}
		if(!"".equals(mainid)){
			List<PrjLogDtBean> dtlist=plb.getDtList();
			if(dtlist != null){
				for(PrjLogDtBean pldb:dtlist){
					 map = new HashMap<String, String>();
					 map.put("mainid", mainid);
					 map.put("fieldname", pldb.getFieldname());
					 map.put("fieldtype", pldb.getFieldtype());
					 map.put("oldvalue", pldb.getOldvalue());
					 map.put("newvalue", pldb.getNewvalue());
					 iu.insert(map, "uf_prj_log_dt1");
				}
			}
		}
				
	}
	/**
	 * 
	 * @param logType 日志类型 0 新增 1修改
	 * @param type  类型 0 项目 1阶段
	 * @param dataId 数据id 过程和项目的id
	 * @param pidComMap 通用数据数组
	 * @param pidDefMap 自定义数据数组
	 * @param oldComMap  历史通用数据数组
	 * @param oldDefMap	 历史自定义数据数组
	 * @param datatype 数据类型 项目类型或者过程类型
	 * @param userid   变更人ID
	 */
	public void writePrjLog(String logType,String type,String dataId,Map<String, String> pidComMap,Map<String, String> pidDefMap,Map<String, String> oldComMap,Map<String, String> oldDefMap,String datatype,String userid){
		PrjLogBean plb = new PrjLogBean();
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormate = new SimpleDateFormat("HH:mm");
		String nowDate = dateFormate.format(new Date());
		String nowTime = timeFormate.format(new Date());
		plb.setLogtype(logType);
		plb.setOperater(userid);
		plb.setCreatedate(nowDate);
		plb.setCreatetime(nowTime);
		if("0".equals(type)){
			plb.setPrjtype(datatype);
		}else{
			plb.setPrjprocess(datatype);
		}
		plb.setType(type);
		plb.setDataid(dataId);
		String fieldName = "";
		String newValue = "";
		String oldValue = "";
		List<PrjLogDtBean> dtList = new ArrayList<PrjLogDtBean>();
		Iterator<String> it = pidComMap.keySet().iterator();
		while(it.hasNext()){
			PrjLogDtBean pldb = new PrjLogDtBean();
			fieldName = it.next();
		    newValue=Util.null2String(pidComMap.get(fieldName));
		    oldValue=Util.null2String(oldComMap.get(fieldName));
		    if(newValue.equals(oldValue)){
		    	continue;
		    }
		    pldb.setFieldname(fieldName);
		    pldb.setFieldtype("0");
		    pldb.setNewvalue(newValue);
		    pldb.setOldvalue(oldValue);
		    dtList.add(pldb);
		}
		it = pidDefMap.keySet().iterator();
		while(it.hasNext()){
			PrjLogDtBean pldb = new PrjLogDtBean();
			fieldName = it.next();
		    newValue=Util.null2String(pidDefMap.get(fieldName));
		    oldValue=Util.null2String(oldDefMap.get(fieldName));
		    if(newValue.equals(oldValue)){
		    	continue;
		    }
		    pldb.setFieldname(fieldName);
		    pldb.setFieldtype("1");
		    pldb.setNewvalue(newValue);
		    pldb.setOldvalue(oldValue);
		    dtList.add(pldb);
		}
		plb.setDtList(dtList);
		insertPrjLog(plb);
	}
}
