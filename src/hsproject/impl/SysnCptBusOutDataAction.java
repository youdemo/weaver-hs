package hsproject.impl;

import hsproject.bean.CptBusOutDataMtBean;
import hsproject.bean.OutDataMtBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import weaver.general.BaseBean;
import weaver.interfaces.schedule.BaseCronJob;

public class SysnCptBusOutDataAction extends BaseCronJob{

	BaseBean log = new BaseBean();
	public void execute(){
		log.writeLog("开始固定资产/商务数据同步");
		sysnData();
		log.writeLog("固定资产/商务数据同步结束");
	}
	public void sysnData(){
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormate = new SimpleDateFormat("HH:mm");
		String nowDate = dateFormate.format(new Date());
		String nowTime = timeFormate.format(new Date());
		String month = nowDate.substring(5,7).replaceAll("^(0+)", "");
		String day = nowDate.substring(8,10).replaceAll("^(0+)", "");
		String hour = nowTime.substring(0, 2).replaceAll("^(0+)", "");
		if("".equals(hour)) {
			hour = "0";
		}
		hour = String.valueOf(Integer.valueOf(hour)+1);
		doSysn(month,day,hour,nowDate,nowTime);
	}
	
	public void doSysn(String month,String day,String hour,String nowDate,String nowTime){
		log.writeLog("SysnCptBusOutDataActionkaishi month:"+month+"day:"+day+"hour:"+hour);
		SysnCptBusOutDataImpl sodi = new SysnCptBusOutDataImpl();
		List<CptBusOutDataMtBean> list=sodi.getSysnMtIds(month, day, hour);
		log.writeLog("OutDataMtBeanlist list:"+list.size());
		for(CptBusOutDataMtBean odmb :list){
			sodi.sysnData(odmb,nowDate,nowTime);
			
		}
	}
}
