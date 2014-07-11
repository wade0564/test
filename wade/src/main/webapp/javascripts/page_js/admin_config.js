$(function(){
	$("#prescan_run").button({disabled:true}).click(function(){
		var str="Are sure you to run the pre-scan immediately?";
		XUI.createXDialog("Confirm",str,["OK"],[function(){
			XUI.createXDialog("Information","Pre-scan starts!",[],[]);
			$.post("prescan/do",function(data){
				if(data==false){
					XUI.createXDialog("Information","Pre-scan is running currently.",[],[]);					
				}
			});
			_refreshPrescanInfo();
			$(this).dialog("destroy");
		}]);
	});
	$("#change_enable").button().click(function(){
		var str="Are sure you to enable the pre-scan?";
		var op="on";
		if($("#disabled").is(":checked")){
			op="off";
			str="Are sure you to disable the pre-scan?";
		}
		XUI.createXDialog("Confirm",str,["OK"],[function(){
			$.post("prescan/switch/"+op,function(data){
				_refreshPrescanInfo();
			});
			$(this).dialog("destroy");
		}]);
	});
	var _refreshPrescanInfo=function(){
		$.get("prescan/nextduetime",function(data){
			if(data==0){
				$("#prescan_date").text("Prescan not enabled");
			}
			else{
				var date=new Date().getTime()+data*1000;
				var datestr=XUtil.timestampToString(date,"-",":");
				$("#prescan_date").text(datestr);
			}
		});
		$.get("prescan/status",function(data){
			$("#prescan_status").text(data);
			if(data.toLowerCase()=="idle"){
				$("#enabled").attr("checked","checked");
				$("#prescan_run").button({disabled:false});
			}
			else if(data.toLowerCase()=="scanning"){
				$("#enabled").attr("checked","checked");
				$("#prescan_run").button({disabled:true});
			}
			else{
				$("#disabled").attr("checked","checked");
				$("#prescan_run").button({disabled:true});
			}
		});
	};
	setInterval(_refreshPrescanInfo,2000);
});