package test;

import hsproject.bean.OutDataMtBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import sun.util.logging.resources.logging;
import weaver.general.BaseBean;
import weaver.interfaces.schedule.BaseCronJob;

public class CopyOfSysnOutDataAction extends BaseCronJob{

	BaseBean log = new BaseBean();
	public void execute(){
		log.writeLog("开始外部数据同步");
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
		String min = nowTime.substring(3, 5);
		hour = String.valueOf(Integer.valueOf(hour)+1);
		if("00".equals(min)){
			min = "0";
		}else if("30".equals(min)){
			min = "1";
		}else{
			min = "";
		}
		doSysn(month,day,hour,min);
	}
	
	public void doSysn(String month,String day,String hour,String min){
		log.writeLog("kaishi month:"+month+"day:"+day+"hour:"+hour+"min:"+min);
		SysnOutDataImpl sodi = new SysnOutDataImpl();
		List<OutDataMtBean> list=sodi.getSysnMtIds(month, day, hour, min);
		log.writeLog("OutDataMtBeanlist list:"+list.size());
		for(OutDataMtBean odmb :list){
			sodi.sysnData(odmb);
			
		}
	}
}
