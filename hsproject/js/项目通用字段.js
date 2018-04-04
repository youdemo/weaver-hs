<!-- script代码，如果需要引用js文件，请使用与HTML中相同的方式。 -->
<script type="text/javascript">
var wblxid="#wblxid";//文本类型id
var wbcdid="#wbcdid";//文本长度id
var fdswsid="#fdswsid";//浮点数位数id
var llanid="#llanid";//浏览按钮id
var xzkid="#xzkid";//选择框id

var zdlx="field15870";//字段类型
var wblx="field15871";//文本类型
var wbcd="field15873";//文本长度
var fdsws="field15874";//浮点数的位数
var llan="field15872";//浏览按钮
var xzk="field15875";//选择框

var isMust="#field15878";//必填
var isEdit="#field15920";//编辑
var isReadonly="#field15921";//只读

var zdsfcz = "#field15931";//字段是否存在
jQuery(document).ready(function(){
	showhide();
	showhide2();
	addremovecheck();
	 jQuery("#"+zdlx).bind("change",function(){
      showhide();
	  addremovecheck();
     });	
    jQuery("#"+wblx).bind("change",function(){
      showhide2();
	  addremovecheck();
     });	 
	 checkCustomize = function () {
		 var checknum=0;
		 var zdsfcz_val = jQuery(zdsfcz).val();
		 if(Number(zdsfcz_val)>Number("0")){
			window.top.Dialog.alert("该字段已存在，请检查");
			return false;
		 }
		 if(jQuery(isMust).is(':checked')==true){
			 checknum=checknum+1;
		 }
		 if(jQuery(isEdit).is(':checked')==true){
			 checknum=checknum+1;
		 }
		 if(jQuery(isReadonly).is(':checked')==true){
			 checknum=checknum+1;
		 }
		 if(checknum==0){
			 window.top.Dialog.alert("必填、编辑、只读必须选择一项");
			return false;
		 }
		  if(checknum >1){
			 window.top.Dialog.alert("必填、编辑、只读只能选择一项");
			return false;
		 }
		return true;
	}
		
})
function addremovecheck(){
	var zdlx_val = jQuery("#"+zdlx).val();
	var wblx_val=jQuery("#"+wblx).val();
	if(zdlx_val==""||zdlx_val=="4"){
		removeaddcheck(wblx,"1","0");
		removeaddcheck(wbcd,"1","0");
		removeaddcheck(fdsws,"1","0");
		removeaddcheck(llan,"1","0");
		removeaddcheck(xzk,"1","1");
		
	}
	if(zdlx_val=="0"){
		if(wblx_val=="0"){
			removeaddcheck(wbcd,"0","0");
			removeaddcheck(fdsws,"1","0");
		}else if(wblx_val=="2"){
			removeaddcheck(wbcd,"1","0");
		    removeaddcheck(fdsws,"0","0");
		}else{
			removeaddcheck(fdsws,"1","0");
			removeaddcheck(wbcd,"1","0");
		}
		removeaddcheck(wblx,"0","0");
		
		removeaddcheck(llan,"1","0");
		removeaddcheck(xzk,"1","1");
		
	}
	if(zdlx_val=="1"){
		removeaddcheck(wblx,"1","0");
		removeaddcheck(wbcd,"1","0");
		removeaddcheck(fdsws,"1","0");
		removeaddcheck(llan,"0","0");
		removeaddcheck(xzk,"1","1");
		
	}
	if(zdlx_val=="3"){
		removeaddcheck(wblx,"1","0");
		removeaddcheck(wbcd,"1","0");
		removeaddcheck(fdsws,"1","0");
		removeaddcheck(llan,"1","0");
		removeaddcheck(xzk,"0","1");
		
	}
}
function showhide(){
	var zdlx_val=jQuery("#"+zdlx).val();
	if(zdlx_val==""||zdlx_val=="4"){
	  jQuery(wblxid).hide();	 
      jQuery(wbcdid).hide();
      jQuery(fdswsid).hide();
      jQuery(llanid).hide();
	  jQuery(xzkid).hide();
		
	}
	if(zdlx_val=="0"){
		jQuery(wblxid).show();	 
      jQuery(wbcdid).hide();
      jQuery(fdswsid).hide();
      jQuery(llanid).hide();
	  jQuery(xzkid).hide();
		showhide2();
	}
	if(zdlx_val=="1"){
	  jQuery(wblxid).hide();	 
      jQuery(wbcdid).hide();
      jQuery(fdswsid).hide();
      jQuery(llanid).show();
	  jQuery(xzkid).hide();
		
	}
	if(zdlx_val=="3"){
		jQuery(wblxid).hide();	 
      jQuery(wbcdid).hide();
      jQuery(fdswsid).hide();
      jQuery(llanid).hide();
	  jQuery(xzkid).show();
		
	}
	
}
function showhide2(){
	var zdlx_val = jQuery("#"+zdlx).val();
	var wblx_val=jQuery("#"+wblx).val();
	if (zdlx_val!="0"){
		return;
	}
	if(wblx_val==""||wblx_val=="1"){	 
      jQuery(wbcdid).hide();
      jQuery(fdswsid).hide();
		
	}
	if(wblx_val=="0"){	 
      jQuery(wbcdid).show();
      jQuery(fdswsid).hide();
		
	}
	if(wblx_val=="2"){	 
      jQuery(wbcdid).hide();
      jQuery(fdswsid).show();
		
	}
	
	
}
//type 0 增加必填，1 移除必填，flag 0 文本选择框 1 浏览框
function removeaddcheck(fieldid,type,flag){
	var fieldval = jQuery("#"+fieldid).val();
	if(type=="0"){
		if(fieldval==''){
		 	if(flag=='0'){ 
     	 	 jQuery("#"+fieldid+"span").html("<img align='absmiddle' src='/images/BacoError_wev8.gif'>");
     	 	}else{
     	 	 jQuery("#"+fieldid+"spanimg").html("<img align='absmiddle' src='/images/BacoError_wev8.gif'>");
     	 	} 	  
     	 	 	 
     	}
     	 
     	    var needcheck = document.all("needcheck");
                if(needcheck.value.indexOf(","+fieldid)<0){
                  if(needcheck.value!='') needcheck.value+=",";
                   needcheck.value+=fieldid;
                }
		if(flag=='0'){ 
			jQuery("#"+fieldid).bind("change",function(){
				var fieldval2= jQuery("#"+fieldid).val();
				if(fieldval2==''){
			
				 jQuery("#"+fieldid+"span").html("<img align='absmiddle' src='/images/BacoError_wev8.gif'>");
				}else{
				 jQuery("#"+fieldid+"span").html("");
				} 	  
     	 	 	 
				 
			})
		}else{
			jQuery("#"+fieldid).bindPropertyChange(function(){
				var fieldval2= jQuery("#"+fieldid).val();
				if(fieldval2==''){
				 jQuery("#"+fieldid+"spanimg").html("<img align='absmiddle' src='/images/BacoError_wev8.gif'>");
				}else{
				 jQuery("#"+fieldid+"spanimg").html("");
				} 	  
			})
		}
     }else{
     	if(flag=='0'){ 
     	  jQuery("#"+fieldid+"span").html("");
     	}else{
     	 jQuery("#"+fieldid+"spanimg").html("");
        }
     	var parastr = document.all('needcheck').value;
        parastr = parastr.replace(","+fieldid,"");
        document.all('needcheck').value=parastr;
     }
}
</script>
