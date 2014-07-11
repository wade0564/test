scan_bug_results=[];

$(function(){	
	var scanPage=new ScanPage();
	scanPage.initPageUI();
	scanPage.initEventCallbacks();
	_initDomainSelect("#domain_select,.domain");
	_initStateSelect("#lifecyle_select,.lifecycle");
	_initImpactSelect("#impact_select,.impact");
	
	// update page
	$.ajax({
		type : "post",
		url : "user/history/page",
		data: "scan_symptom_data.html"
	});
});

function ScanPage(){
	//class members to act as 'global variables'
	this.loading_div=null;
	this.scan_bug_ids=[];
	this.genInfo=null;
	this.cancelScanRequest = false;
	this.scan_bug_uids=[];
//	this.scan_bug_results=[];
}

//In Prototype, here goes all the functions.('_xxxx' function is private)
ScanPage.prototype={
	initPageUI:function(){
		$("#scan_symptom,#skip_all").button();
		$("#copy_all,#export_system_bugs").button({disabled:true});
		$("#software_tree_ok_btn").button();
		$("#hardware_tree_ok_btn").button();
		
		$("#pager_setting input[type='text']").val(config.page_config.items_per_page);
		XUtil.pageXTable("#pagination","#bug_table tr");
		
		XUtil.preloadImages("images/showMore.png","images/showLess.png");
		
		$(".symptom_op,.symptom_op+span,.scope_input,.symptom_unit,.delete_symptom,.symptom_cmp").css("visibility", "hidden");
		
		$("#bug_table .copy_btn").attr("title",config.page_config.slideup_tooltip);
		
		this._initDatePicker();
		this._initProductSelect();
//		this._initComponentSelect();
		this._initDomainSelect();
		this._initBugSelect();
		this._initSnAutocomplete();
		this._bindClickBugDescriptionEvent();
		this._initNoDateCheckbox();
		$(document).on("focus","#dialog .hardware_tree_input:visible,#dialog .software_tree_input:visible",function(){
			softwareTree.setIsEditable(false);
			hardwareTree.setIsEditable(false);
		});
		
		$(document)
		.on(
				"click",
				"img.add_symptom",
				function() {
					$("#dialog table tr:last select,#dialog table tr:last .scope_input,#dialog table tr:last span").css("visibility",
							"visible");
					var old_row = $(this).parent("td").parent("tr");
					var new_row = old_row.clone();
					new_row.find(".symptom_id").val("").removeClass("ui-autocomplete-input").removeAttr("autocomplete");
					new_row.insertAfter(old_row);
					_initSymptomAndBugAutocomplete();
					XUtil.enableUIElement(old_row.find(".symptom_op,.symptom_op+span"), old_row.find(".scope_input"), old_row
							.find(".symptom_unit"), old_row.find(".delete_symptom"), old_row.find(".symptom_cmp"));
					XUtil.enableUIElement(new_row.find(".delete_symptom"));
					new_row.find("input").val("");
					new_row.find("select.symptom_op").val("AND").change();
					new_row.find("img.left_parenthesis, img.right_parenthesis").attr("data-status", "true").click();
					$("#dialog table tr:last select,#dialog table tr:last .scope_input,#dialog table tr:last span").css("visibility",
							"hidden");
				});
	},
	
	_initNoDateCheckbox:function(){
		$("#no_date_check").change(function(){
			if($(this).is(":checked")){
				$("#date_from_input").attr("disabled","disabled").val("2005-01-01");
				$("#date_to_input").attr("disabled","disabled");
			}
			else{
				$("#date_to_input,#date_from_input").removeAttr("disabled","disabled");
			}
		});
	},
	
	_bindClickBugDescriptionEvent:function(){
		$(document).on("click",".bug_description",function(evt){
			_getBugByBugId($(this).attr('data-bugId'));
			evt.stopPropagation();
			return false;
		});
	},
	_initDatePicker:function(){
		$("#date_from_input").val(XUtil.timestampToString(new Date().getTime()-1000*60*60*24*182));
		$("#date_to_input").val(XUtil.timestampToString(new Date().getTime()));
		$("#date_to_input,#date_from_input").datepicker({dateFormat:"yy-mm-dd",maxDate:"+0d",changeYear:true});
	},
	
	_initProductSelect:function(){
		var select_html=$("div.xui_title_bar:eq(0) select").html();
		$.get("signature/product", function(data) {
			for ( var i = 0; i < data.length; i++) {
				select_html+="<option value='" + data[i].id + "'>" + data[i].name+"</option>";
			}
			$("div.xui_title_bar:eq(0) select").html(select_html);
		});
	},
	
	_initComponentSelect:function(){
		var select_html=$(".component_select").html();
		$.get("signature/component", function(data) {
			for ( var i = 0; i < data.length; i++) {
				select_html+="<option value='" + data[i].id + "'>" + data[i].name+"</option>";
			}
			$(".component_select").html(select_html);
		});
	},
	
	_initDomainSelect:function(){
		var select_html=$(".domain_select").html();
		$.get("signature/domain", function(data) {
			for ( var i = 0; i < data.length; i++) {
				select_html+="<option value='" + data[i].id + "'>" + data[i].name+"</option>";
			}
			$(".domain_select").html(select_html);
		});
		$(document).on("change",".domain_select",function(){
			if($(this).val() == -1){
				$(".component_select").find("option[value!='-1']").remove();
				return;
			}
			$.get("signature/component/"+$(this).val(),function(data){
				$(".component_select").find("option[value!='-1']").remove();
				data.each(function(v,k){
					$(".component_select").append("<option value='"+v.id+"'>"+v.name+"</option>");
				});
			});
		});
	},
	
	_initBugSelect:function(){
		this.loading_div = XUI.createXLoadingDiv();
		var context=this;
		$.get("signature/state/active/ids",function(data){
			$(".bug_select option:gt(0)").remove();
			for(var i=0;i<data.length;i++){
				$(".bug_select").append("<option value='"+data[i].id+"'>"+data[i].bugzillaId+"</option>");
			}
			context.loading_div.hideLoadingDiv();
		});
	},
	
	_initSnAutocomplete:function(){
		
		$("#serial_number_input").autocomplete({"source": function(request, response) {
			var currentValue=request.term;
			if(currentValue==""){
				currentValue="-1";
			}
			
			$.get("scan/validSn/"+currentValue,function(data){
				response(data);
			});
		}});	
	},
	
	initEventCallbacks:function(){
		var context = this;
		$("#show_all").change(function(){
			if($(this).is(":checked")){
				var size=$("#bug_table tr:gt(1)").length;
				XUtil.pageXTable("#pagination","#bug_table tr",size);		
			}
			else{
				XUtil.pageXTable("#pagination","#bug_table tr");		
			}
		});
		$("#scan_symptom").click($.proxy(this._scan_symptom_data,this));
		$("#pager_setting input[type='text']").bind("keydown blur",function(evt){
			if(evt.type=="blur" || evt.which==13){
				var validator=XUI.createXValidator("#pager_setting input[type='text']", "\\d+");
				if(validator.validate()){
					var num_per_page=parseInt($(this).val());
					config.page_config.items_per_page=num_per_page;
					XUtil.pageXTable("#pagination", "#bug_table tr");
					$("#show_all").prop("checked",false);
				}
				else{
					$(this).val("");
				}
			}
		});
		$('#skip_all').click($.proxy(this._cancel_scan,this));
		$("#copy_all").click(function(){
			context._copyAllResults();
		});
		
		$(document).on("click","#bug_table .bug_id",function(evt) {
			evt.stopPropagation();
		});
		
		$(document).on("click","#bug_table .remediation",function(evt) {
			evt.stopPropagation();
		});
		
		$(document).on("click","#bug_table .bug_ga",function(evt) {
			evt.stopPropagation();
		});
		
		$(document).on("click","#bug_table .bug_no_ga",function(evt) {
			evt.stopPropagation();
		});
		
		$(document).on("click","#bug_table tr .copy_btn",function(evt) {
			var jTargetTr = $($(this).parent('td').parent('tr'));
			if($(this).hasClass('more')){
				$(this).removeClass('more');
				jTargetTr.next('.bug_info_row').slideDown('fast');
				$(this).attr("title",config.page_config.slideup_tooltip);
			}else{
				$(this).addClass('more');
				jTargetTr.next('.bug_info_row').slideUp('fast');
				$(this).attr("title",config.page_config.slidedown_tooltip);
			}
			evt.stopPropagation();
		});
		
		$(document).on("click","#bug_table tr:gt(1)",function() {
			$(this).find(".copy_btn").removeClass("more").addClass("less");
			$(this).next('.bug_info_row').slideDown('fast');
			$(this).find(".copy_btn").attr("title",config.page_config.slideup_tooltip);
		});
		
		// sort functions for scan result
		$("#bug_table th:not(:last)").click(function() {
//			_cacheLastPage();
			if ($(this).hasClass("desc")) {
				$("#bug_table th").removeClass("desc");
				$(this).addClass("asc");
				var field = $(this).data("field");
				context._sortBugScanResults(field, "asc");
			} else if ($(this).hasClass("asc")) {
				$("#bug_table th").removeClass("asc");
				$(this).addClass("desc");
				var field = $(this).data("field");
				context._sortBugScanResults(field, "desc");
			} else {
				$("#bug_table th").removeClass("desc").removeClass("asc");
				$(this).addClass("asc");
				var field = $(this).data("field");
				context._sortBugScanResults(field, "asc");
			}
		});
	},
	_cancel_scan:function(){
		var context=this;
		var yesFun = function(){
			var dialog = this;
			$(dialog).dialog('close');
			context.cancelScanRequest = true;
			context._scan_symptom_completed();
			$.ajax({
				url:'scan/cancel',
				type:'post',
				success:function(data){
					console.log("scan task is cancelled");
				}
			});
		};
		var noFun = function(){
			$(this).dialog('close');
		};
		var buttonsArray = [{text:'YES',click:yesFun},{text:'NO',click:noFun}];
		$('<div>Do you want to cancel all scan requests?<div>').dialog({buttons:buttonsArray});
	},
	_scan_symptom_data:function(){		
		if(this._validateScanFields()==true){
			this.cancelScanRequest = false;
			var scanParam={};
			scanParam.product=$(".xui_title_bar select").val();
			scanParam.fromDate=$("#date_from_input").val();
			scanParam.toDate=$("#date_to_input").val();
			scanParam.domain=$(".domain_select").val();
			scanParam.component=$(".component_select").val();
			scanParam.bugId=$(".bug_select").val();
			scanParam.caseNumber="";
			scanParam.serialNumber=$("#serial_number_input").val();
			scanParam.bugzillaId=$(".bug_select option:selected").text();
			this.scan_bug_ids=[];
			this.scan_bug_uids=[];
			scan_bug_results=[];
			$("#scan_symptom").button({disabled:true});
			$("#scan_loading").show();
//			var bug_id=parseInt($(".bug_select").val());
//			if(bug_id==-1){
//				var options=$(".bug_select option:gt(0)");
//				for(var i=0;i<options.length;i++){
//					this.scan_bug_ids.push(parseInt($(options[i]).text()));
//				}
//			}
//			else{
//				this.scan_bug_ids.push(bug_id);
//			}
			var context=this;
			$.ajax({
				type : "post",
				url : "scan/getBugIdsWithParams",
				contentType : "application/json",
				data : JSON.stringify(scanParam),
				success : function(data,status,xhr) {
					$("#scan_loading").hide();
					if(xhr.getResponseHeader("ddos_ver")!="null"){
					    $("#software_span").show().text("Software: "+xhr.getResponseHeader("ddos_ver")+" since "+xhr.getResponseHeader("ddos_date"));						
					}
					for(var i in data){
						context.scan_bug_ids.push(i);
					}
					if(context.scan_bug_ids.length>0){
						context.genInfo=[];
						$("#export_system_bugs").show();
						$("#skip_all").show();
						$("#progress_bar").show().progressbar({value:0,max:context.scan_bug_ids.length});
						$("#progress_bar span").text("0%");
						$("#bug_table tr:gt(1)").remove();
						context._scan_symptom_by_id(0,scanParam);	
					}
					else{
//						XUI.createXDialog("Info",config.page_config.no_bug_selected,[],[]);
						var num=0;
						var text=config.page_config.scan_bugs_success;
						XUI.createXDialog("Information",text.format({num:num}),[],[]);
						$("#bug_table tr:gt(1)").remove();
						$("#scan_symptom").button({disabled:false});
					}
				},
				error:function(xhr){
					$("#scan_loading").hide();
					$("#scan_symptom").button({disabled:false});
					if(!XUtil.checkSession(xhr)){
						XUI.createXDialog("Error",xhr.getResponseHeader("msg"),[],[]);
					}
				}
			});
		}
		else{
			XUI.createXDialog("Error",config.page_config.fill_fields_error,[],[]);
		}
	},
	
	_scan_symptom_by_id:function(index,scanParam){
		if(this.cancelScanRequest)return;
		var bugId=this.scan_bug_ids[index];
		var context=this;
		$.ajax({
			type : "post",
			url : "scan/bugId/"+bugId,
			contentType : "application/json",
			data : JSON.stringify(scanParam),
			success : function(bug) {
				this.scan_bug_uids=[];
				if(context.cancelScanRequest)return;
				if(bug.bugId!=undefined && bug!=null){
					$("#bug_table tr:eq(1)").clone().appendTo($("#bug_table"));
					$("#bug_table tr:last .bug_id").text(bug.bugzillaId).attr("href",bug.link);
					var description = bug.description;
					$("#bug_table tr:last .bug_description").attr({"title":description,"href":"manage_bug_def.html?bugID="+bug.bugId,'data-bugId':bug.bugId}).text(description.substr(0, 45)+"......");
					var gaList=bug.fixedGA;
					var options="";
					for(var i=0;i<gaList.length;i++){
						options+="<option>"+gaList[i].version+"</option>";
					}
					$("#bug_table tr:last .bug_ga").html(options);
					var nonGaList=bug.fixedNonGA;
					options="";
					for(var i=0;i<nonGaList.length;i++){
						options+="<option>"+nonGaList[i].version+"</option>";
					}
					$("#bug_table tr:last .bug_no_ga").html(options);
					$("#bug_table tr:last .remediation").text(bug.remediation.kbaId).attr({"href":bug.remediation.url,"title":bug.remediation.url});
					$("#bug_table tr:last .bug_dates").text(bug.matchedTimeStamp[0].bugLogs[0].substr(0,10));
					var copy = new emc.prometheus.symptonScan.copyLogic(bug);
					var result=copy.renderOneBug(bug);
					var jTargetTr = $("#bug_table tr:last");
					jTargetTr.show();
					context._copyCurrentResult(result,jTargetTr);
					context.genInfo.push(bug);
					context.scan_bug_uids.push(bug.taskId);
					$("#export_system_bugs").unbind('click').click(function(){
						var date = new Date();
						monitorExportProgress("scan/exportsystembugs/uid/"+context.scan_bug_uids.join()+"/System "+scanParam.serialNumber+" Bugs ["+date.getFullYear()+"-"+pad((date.getMonth()+1),2)+"-"+pad(date.getDate(),2)+"].xls",
												null, null);
					});
					$("#show_all").change();	
					scan_bug_results.push(bug);
				}
				context._scan_symptom_inprogress(index,scanParam);
			},
			error:function(){
				if(this.cancelScanRequest)return;
//				if(arguments[0].statusText=="timeout"){
//					context._scan_symptom_inprogress(index,scanParam);
//				}
				else{
					context._scan_symptom_completed();
					XUI.createXDialog("Error",config.page_config.scan_bugs_failed,[],[]);
				}
			}
		});
	},
	
	_scan_symptom_inprogress:function(index,scanParam){
		var value=$("#progress_bar").progressbar("value");
		value++;
		$("#progress_bar").progressbar("value",value);
		$("#progress_bar span").text(parseInt(value*100/this.scan_bug_ids.length)+"%");
		if(index==this.scan_bug_ids.length-1){
			this._scan_symptom_completed();
			var num=$("#bug_table tr:not(.bug_info_row)").length-2;
			var text=config.page_config.scan_bugs_success;
			XUI.createXDialog("Information",text.format({num:num}),[],[]);
		}
		else{
			if(this.cancelScanRequest)return;
			this._scan_symptom_by_id(++index,scanParam);
		}
	},
	
	_scan_symptom_completed:function(){
		var context=this;
		$("#copy_all,#scan_symptom,#export_system_bugs").button({disabled:false});
		$("#progress_bar,#skip_all").hide();
		$("#software_span").text($("#software_span").text().replace("since","on"));
//		var copy_btns=$("#bug_table .copy_btn:visible");
//		for(var i=0;i<copy_btns.length;i++){
//			$(copy_btns[i]).parent("td").parent("tr").addClass("more");
//			$(copy_btns[i]).click();
//		}
	},
	
	_validateScanFields:function(){
		var validatorManager = XUI.createXValidatorManager();
		validatorManager.addValidator(XUI.createXValidator("#date_from_input,#date_to_input", "^\\d{4}\-\\d{2}\-\\d{2}$"));
		validatorManager.addValidator(XUI.createXValidator("#serial_number_input", "\\w+"));
		return validatorManager.validate();
	},
	
	_copyCurrentResult:function(result,preRow){
		var html="<tr class='bug_info_row'><td colspan='7'><iframe style='height:300px;width:100%;overflow-x:hidden;border:0;' class='copy' src='scan_results.html'/></td></tr>";
		$(html).insertAfter(preRow);
		$("iframe.copy:last").load(function(){
			$(this).contents().find("body").html(result);
		});
	},
	
	_copyAllResults:function(){
		var copy_order=$("#copy_order").val();
		var copy = new emc.prometheus.symptonScan.copyLogic(this.genInfo);
		var result="";
		if(copy_order=="0"){
			//copy by id
			result=copy.renderAllById();
		}
		else{
			//copy by date
			result=copy.renderBugsByDate();
		}
		var html="<iframe style='height:480px;width:650px;overflow-x:hidden;' class='copyAll' src='scan_results.html'/>";
		XUI.createXDialog("Scan Results",html,[],[],true);
		$("iframe.copyAll").load(function(){
			$(this).contents().find("body").html(result);
		});
	},
	
	_sortBugScanResults:function(field, order){
			if(field=="bugzillaId"){
				if(order=="asc")
					scan_bug_results.sort(function(bug1, bug2){
						return parseInt(bug1.bugzillaId)-parseInt(bug2.bugzillaId);
					});
				else{
					scan_bug_results.sort(function(bug1, bug2){
						return parseInt(bug2.bugzillaId)-parseInt(bug1.bugzillaId);
					});
				}
			}
			else if(field="description"){
				if(order=="asc")
					scan_bug_results.sort(function(bug1, bug2){
						return bug1.description-bug2.description;
					});
				else{
					scan_bug_results.sort(function(bug1, bug2){
						return bug2.description-bug1.description;
					});
				}
			}
			else if(field="remediation.kbaId"){
				if(order=="asc")
					scan_bug_results.sort(function(bug1, bug2){
						return bug1.remediation.kbaId-bug2.remediation.kbaId;
					});
				else{
					scan_bug_results.sort(function(bug1, bug2){
						return bug2.remediation.kbaId-bug1.remediation.kbaId;
					});
				}
			}
			$("#bug_table tr:gt(1)").remove();
			for(var i=0;i<scan_bug_results.length;i++){
				this._renderBugResults(scan_bug_results[i],i);
			}
			
	},
	
	_renderBugResults:function(bug,index){
		var context = this;
		$("#bug_table tr:eq(1)").clone().appendTo($("#bug_table"));
		$("#bug_table tr:last .bug_id").text(bug.bugzillaId).attr("href",bug.link);
		var description = bug.description;
		$("#bug_table tr:last .bug_description").attr({"title":description,"href":"manage_bug_def.html?bugID="+bug.bugId,'data-bugId':bug.bugId}).text(description.substr(0, 45)+"......");
		var gaList=bug.fixedGA;
		var options="";
		for(var i=0;i<gaList.length;i++){
			options+="<option>"+gaList[i].version+"</option>";
		}
		$("#bug_table tr:last .bug_ga").html(options);
		var nonGaList=bug.fixedNonGA;
		options="";
		for(var i=0;i<nonGaList.length;i++){
			options+="<option>"+nonGaList[i].version+"</option>";
		}
		$("#bug_table tr:last .bug_no_ga").html(options);
		$("#bug_table tr:last .remediation").text(bug.remediation.kbaId).attr({"href":bug.remediation.url,"title":bug.remediation.url});;
		$("#bug_table tr:last .bug_dates").text(bug.matchedTimeStamp[0].bugLogs[0].substr(0,10));
		var copy = new emc.prometheus.symptonScan.copyLogic(bug);
		var result=copy.renderOneBug(bug);
		var jTargetTr = $("#bug_table tr:last");
		jTargetTr.show();
		context._copyCurrentResult(result,jTargetTr);
		context.genInfo.push(bug);
		context.scan_bug_uids.push(bug.taskId);
		$("#export_system_bugs").click(function(){
			var date = new Date();
			$.fileDownload("scan/exportsystembugs/uid/"+context.scan_bug_uids.join()+"/System "+$("#serial_number_input").val()+ " Bugs ["+date.getFullYear()+"-"+pad((date.getMonth()+1),2)+"-"+pad(date.getDate(),2)+"].xls");
		});
		$("#show_all").change();
	}
};