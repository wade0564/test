$(function(){

	initPageUI();
	initEventCallbacks();
	
	_initSnAutocomplete();
});

function initPageUI(){
	$("#search_sub_status").button();
	$("#filter_type").multiselect({
		selectedText: function(numChecked, numTotal, checkedInputs){   
			if(numChecked==1){
				return checkedInputs.map(function(element){ return element.title; });
			}else if(numChecked==2){
				return "&nbsp;&nbsp;&nbsp;ALL";
			}
	    }   
	});
	$("#date").val(XUtil.timestampToString(new Date().getTime()-1000*60*60*24*30));
	$("#date").datepicker({dateFormat:"yy-mm-dd",maxDate:"+0d",changeYear:true});
};

function initEventCallbacks(){
	
	$("#search_sub_status").click($.proxy(this._search_sub_status,this));
	
	$("body").on("click","#sub_table>tbody>tr",function(evt){
		if($(this).next(".subContent_info").text().trim()!=""){
			if($(this).attr("expanded")=="false"){
				$(this).next(".subContent_info").show();
				$(this).attr("expanded","true");
				if($("#sub_table>tbody>tr>td:nth-child(4)").text().trim()=="sub"){
					
				}
				$(this).attr("title","Click to collapse the row.");
				
			}else{
				$(this).next(".subContent_info").hide();
				$(this).attr("expanded","false");
				if($("#sub_table>tbody>tr>td:nth-child(4)").text().trim()=="sub"){
					
				}
				$(this).attr("title","Click to expand the row to show detailed infomation.");
			}
		}
	});
	
	$("#show_all").change(function() {
		if ($(this).is(":checked")) {
			var size = $("#sub_table tr.sub_info").length;
			XUtil.pageXTable("#pagination", "#sub_table tr.sub_info", size);
			$("#sub_table tr.subContent_info").hide();
		} else {
			XUtil.pageXTable("#pagination", "#sub_table tr.sub_info");
			$("#sub_table tr.subContent_info").hide();
		}
	});
	
	$("#pager_setting input[type='text']").bind("keydown blur", function(evt) {
		if (evt.type == "blur" || evt.which == 13) {
			var validator = XUI.createXValidator("#pager_setting input[type='text']", "\\d+");
			if (validator.validate()) {
				var num_per_page = parseInt($(this).val());
				config.page_config.items_per_page = num_per_page;
				XUtil.pageXTable("#pagination", "#sub_table tr.sub_info");
				$("#show_all").prop("checked", false);
			} else {
				$(this).val("");
			}
		}
	});
};


function _search_sub_status(){
		
		if(this._validateSubStatusFields()==true){
			$("#search_sub_status").button({disabled:true});
			$("#search_loading").css("visibility","visible");
			var sn=$("#serial_number_input").val();
			var type=$("#filter_type").multiselect("getChecked").map(function(){return this.value;}).get().join(",");
			var ts=$("#date").val();
			$("#sub_table tr:gt(2)").remove();
			var dataToSend = { type: type, ts: ts };
			var context = this;
			var loading_div = XUI.createXLoadingDiv();
			$.ajax({
				type : "get",
				url : "sub/get/"+sn,
				contentType : "application/json",
				data : dataToSend,
				dataType : 'json',
				success : function(data,status) {
					loading_div.hideLoadingDiv();
					context._showSubStatus(data.info);
					$("#search_sub_status").button({disabled:false});
				},
				error: function(xhr){
					loading_div.hideLoadingDiv();
					$("#search_sub_status").button({disabled:false});
					if(!XUtil.checkSession(xhr)){
						XUI.createXDialog("Error",xhr.getResponseHeader("msg"),[],[]);
					}
				}
			});
			
			
		}
};


function _validateSubStatusFields(){
	var validatorManager = XUI.createXValidatorManager();
	validatorManager.addValidator(XUI.createXValidator("#serial_number_input", "\\w+"));
	return validatorManager.validate();
};



function _showSubStatus(data){
	$("#sub_table_body").empty();
	if(data==null || data.length == 0){
		$("<tr><td colspan='5'><div style='font-size:medium'>No related SUB File infomation.</td></div>").appendTo($("#sub_table_body"));
		return;
	}
//	data.each(function(v,k){
//		v.timestamp = XUtil.timestampToString(v.ts,"-",":");;
//	});
	
	$("#subToAddTemplate").tmpl(data).appendTo("#sub_table_body");
	$('.xui_table > tbody > tr.sub_info:odd').css("background","#999");
	$('.xui_table > tbody > tr.sub_info:even').css("background","#BBB");
	$('.xui_table > tbody > tr.subContent_info').css("background","#CCC");
	$("#show_all").change();
	$("#search_sub_status").button({disabled:false});
	
}


function _initSnAutocomplete (){
	$("#serial_number_input").autocomplete({"source":[]});	
	$("#serial_number_input").keyup(function(){
		var currentValue=$(this).val();
		if(currentValue==""){
			currentValue="-1";
		}
		$.get("scan/validSn/"+currentValue,function(data){
			$("#serial_number_input").autocomplete({"source":data});				
		});
	});
}

function stringHandler(str){
	if(str==null || str==""){
		return "UNKNOWN";
	}else if(str.length>100){
		var span= str.slice(0,6)+"..."+str.slice(-89);
		return  '<span title="'+str+'">'+span+'</span>';
	}else{
		return str;
	}
}

function _splitLocation(subContents) {
	var html = "";
	var existedDirectory = [];

	for (var k = 0; k < subContents.length; k++) {
		var location = subContents[k].path;
		var status = subContents[k].status;
		if(status=="PARSE_SUCCESS"){
			status = "Success";
		}else if(status=="PARSE_FAILED"){
			status = "Failed";
		}
		var index = location.indexOf("ddr/var");
		location = location.substring(index);
		var directory = location.split("/");

		var space = "";
		for (var i = 0; i < directory.length; i++) {
			var isExisted = false;
			// check if already printed the directory tree
			for (var j = 0; j < existedDirectory.length; j++) {
				if (existedDirectory[j] == directory[i]) {
					isExisted = true;
				}
			}
			// if not printed, print
			if (!isExisted) {
				if (i != directory.length - 1) {
					html += "<div class=\"subInfo\">";
					html += "<span class=\"subLocation\">" + space + "|--" + directory[i] + "</span>";
				} else {
					html += "<div class=\"dot\">";
					html += "<span class=\"subLocation\">" + space + "|--" + directory[i] + "</span>";
					html += "<span class=\"subStatus\">" + status + "</span>";
				}
				html += "</div>";
				existedDirectory.push(directory[i]);
			}
			space += "|&nbsp&nbsp";
		}
	}
	return html;
};

function _getStatus(s){
	var status;
	if(s=="PARSE_SUCCESS")
		status='SUCCESS';
	else if(s=='PARSE_FAILED')
		status='FAILED';
	else {
		status=s.replace(/_/g," ");
	}
	return status;
}



