package hsproject.impl;

import hsproject.bean.OutDataMtBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import weaver.general.BaseBean;
import weaver.interfaces.schedule.BaseCronJob;

public class SysnCptBusOutDataAction extends BaseCronJob{

	BaseBean log = new BaseBean();
	public void execute(){
		log.writeLog("开始固定资产同步");
		sysnData();
		log.writeLog("外部数据同步结束");
	}
	public void sysnData(){
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormate = new SimpleDateFormat("HH:mm");
		String nowDate = dateFormate.format(new Date());
		String nowTime = timeFormate.format(new Date());
		String month = nowDate.substring(5,7).replaceAll("^(0+)", "");
		String day = nowDate.substring(8,10).replaceAll("^(0+)", "");
		String hour = nowTime.substring(0, 2).replaceAll("^(0+)", "");
		//String min = nowTime.substring(3, 5);
		hour = String.valueOf(Integer.valueOf(hour)+1);
		//if("00".equals(min)){
		//	min = "0";
		//}else if("30".equals(min)){
		//	min = "1";
		//}else{
		//	min = "";
		//}
		doSysn(month,day,hour,nowDate,nowTime);
	}
	
	public void doSysn(String month,String day,String hour,String nowDate,String nowTime){
		log.writeLog("kaishi month:"+month+"day:"+day+"hour:"+hour);
		SysnCptBusOutDataImpl sodi = new SysnCptBusOutDataImpl();
		List<OutDataMtBean> list=sodi.getSysnMtIds(month, day, hour);
		log.writeLog("OutDataMtBeanlist list:"+list.size());
		for(OutDataMtBean odmb :list){
			sodi.sysnData(odmb,nowDate,nowTime);
			
		}
	}
}
