var symIds;
var cache=XUI.createXSessionStorage();
var isIE=/*@cc_on!@*/false; //shitty IE
var totalcount;
var totalper;

$(function(){
	_initUI();
	_initEventCallbacks();
	_confirmSaveBeforeUnload();
	_initNameSelection();
	_initMapSelection();
	
	// update page
	$.ajax({
		type : "post",
		url : "user/history/page",
		data: "manage_symptom_def.html"
	});
});

function _confirmSaveBeforeUnload(){
	window.onbeforeunload=function(){
		if($(".save.enable").length>0){
			return config.page_config.confirm_leave_page;
		}
	};
	window.onunload=function(){
		//when refreshing or leaving this page, session cache should be clear
		cache.clear();
	};
}

function _initUI(){
	//buttons
	$("#search_symptom,#search_comparison").button();
	$("#export_symptom_definitions").button();
	//hover text
	$("#add_symptom").attr("title",config.page_config.add_symptom_def);
	$("#filter_symptom").attr("title",config.page_config.search_symptom_tooltip);
	$(".slide_up").attr("title",config.page_config.slideup_tooltip);
	$(".substitute").attr("title",config.page_config.substitute_tooltip);
	$("#symptom_table>tbody>tr:gt(1)").attr("title",config.page_config.slidedown_tooltip);
	$(".add_substitute").attr("title",config.page_config.add_substitute);
	$(".delete_substitute").attr("title",config.page_config.delete_substitute);
	$(".sortable").attr("title",config.page_config.sortable_column_hint);
	$("#symptom_table .sortable:last").attr("title",config.page_config.sortable_operations_column_hint);
	//default text
	$("#pager_setting input[type='text']").val(config.page_config.items_per_page);
	$(".name_select").val(config.page_config.select_symptom_data);
	$(".var_select").val(config.page_config.select_variable);
	$(".mapping,.var_calculation").val(config.page_config.select_mapping);
	//at least shows one row of substitution table
	$(".third_inner_row table tr:eq(1)").clone(true).insertAfter($(".third_inner_row table tr:eq(1)")).show();	
	//multiselect
	var	loading_div = XUI.createXLoadingDiv();
	$.get("symptoms/symptomids",function(data){
		var id_options="";
		for(var i=0;i<data.length;i++){
			id_options+="<option value='"+data[i]+"'>&nbsp;&nbsp;&nbsp;"+data[i]+"</option>";
		}
		$("#filter_id").html(id_options).multiselect().multiselect("uncheckAll");
		loading_div.hideLoadingDiv();
		//parse URL parameters
		var ids=XUtil.getURLParameter("symptoms");
		if(ids!=null){
			var arr=ids.split(",");
			var dedupeList=new Array();
			var widget=$("#filter_id").multiselect("widget");
			for(var i=0;i<arr.length;i++){
				var isExisted = false;
				for(var d=0;d<dedupeList.length;d++){
					if(arr[i]==dedupeList[d]){
						isExisted=true;
						break;
					} 
				}
				if(!isExisted){
					widget.find(":checkbox[value='"+arr[i]+"']").click();
					dedupeList.push(arr[i]);
				}
			}
			$("#filter_symptom").click();
		}
		_getAllPatternSymptoms("id","desc");				
	});
	_getAllComparisonSymptoms("id", "desc");
	//set margin
	_alignFieldsInCell();
}

//set field's width in order to align them in the cell
function _alignFieldsInCell(){
	var margin=10;
	var setMargin=function(selector,container){
		$(selector).outerWidth($(container).width()-margin);		
	};
	//set symptom margins
	setMargin("#symptom_table .min_input","#symptom_table .cell_min");
	setMargin("#symptom_table .max_input","#symptom_table .cell_max");
	setMargin("#symptom_table .pattern_input","#symptom_table .cell_pattern");
	setMargin("#symptom_table .name_select",".cell_data");
	setMargin("#symptom_table .description_input","#symptom_table>tbody>tr>th:eq(1)");
	var width=($("#symptom_table .cell_scope").width()-3*margin/2)/2;
	$("#symptom_table .scope_input,#symptom_table .scope_select").outerWidth(width);
	$("#symptom_table .cell_pattern").outerWidth($("#symptom_table .first_inner_row").width()-$("#symptom_table .cell_data").outerWidth()-1);
	$("#comparison_table .description_input").outerWidth(90);
	$("#comparison_table textarea.description_input").outerHeight(60);
	$("#comparison_table .min_input,#comparison_table .max_input,#comparison_table .scope_input").outerWidth(140);
}

function _initEventCallbacks(){
	_initPatternBasedEventCallbacks();
	_initComparisonBasedEventCallbacks();
	//toggle pattern and comparison mode
	$("#sym_type").change(function(){
		if($(this).val()=="pattern"){
			$("#comparison_table").hide();
			$("#symptom_table").show();
		}
		else{
			$("#comparison_table").show();
			$("#symptom_table").hide();
		}
		if($("#filter_board").is(":visible")){
			$("#filter_symptom").click();
		}
	});
	
	$("#export_symptom_definitions").button().click(function(){
		var date = new Date();
		monitorExportProgress("symptoms/exportsymptoms/Symptom Summary ["+date.getFullYear()+"-"+pad((date.getMonth()+1),2)+"-"+pad(date.getDate(),2)+"].xls",
								null, null);
		});
	//enable pager function
	$("#def_show_all").change(function() {
		if ($(this).is(":checked")) {
			if($("#sym_type").val()=="pattern"){
				var size = $("#symptom_table>tbody>tr:gt(1)").length;
				XUtil.pageXTable("#def_pagination", "#symptom_table>tbody>tr", size);
			}
			else{
				var size = $("#comparison_table>tbody>tr:gt(1)").length;
				XUtil.pageXTable("#def_pagination", "#comparison_table>tbody>tr", size);
			}
		} else {
			if($("#sym_type").val()=="pattern"){
				XUtil.pageXTable("#def_pagination", "#symptom_table>tbody>tr");
			}
			else{
				XUtil.pageXTable("#def_pagination", "#comparison_table>tbody>tr");
			}
		}
	});
	$("#pager_setting input[type='text']").bind("keydown blur",function(evt){
		if(evt.type=="blur" || evt.which==13){
			var validator=XUI.createXValidator("#pager_setting input[type='text']", "\\d+");
			if(validator.validate()){
				var num_per_page=parseInt($(this).val());
				config.page_config.items_per_page=num_per_page;
				if($("#sym_type").val()=="pattern"){
					XUtil.pageXTable("#def_pagination", "#symptom_table>tbody>tr");
				}
				else{
					XUtil.pageXTable("#def_pagination", "#comparison_table>tbody>tr");
				}
				$("#def_show_all").prop("checked",false);
			}
		}
	});
	//enable search function of symptoms
	$("#filter_symptom").click(function(){
		if(!$("#filter_board").is(":visible")){
			if($("#sym_type").val()=="pattern"){
				$("#filter_board table tr:eq(1)").show();
				$("#search_comparison,#filter_board table .slide_up:eq(0)").hide();
			}
			else{
				$("#filter_board table tr:eq(1)").hide();
				$("#search_comparison,#filter_board table .slide_up:eq(0)").show();
			}
			if(!isIE){
				$("#filter_board").slideDown();						
			}
			else{
				$("#filter_board").show();		
			}
			$(this).attr("title",config.page_config.slideup_tooltip);
		}
		else{
			if(!isIE){
				$("#filter_board").slideUp();
			}
			else{
				$("#filter_board").hide();	
			}
			$(this).attr("title",config.page_config.search_symptom_tooltip);
		}
	});
	$(".slide_up").click(function(){
		if($("#filter_board").is(":visible")){
			if(!isIE){
				$("#filter_board").slideUp();							
			}
			else{
				$("#filter_board").hide();
			}
		}
	});
	//add symptom ui....
	$("#add_symptom").click(function(){
		if($("#sym_type").val()=="pattern"){
			$("#symptom_table tr:eq(1)").clone(true).appendTo("#symptom_table").show().trigger("click");
			//$("#symptom_table>tbody>tr:last .substitute").click();
		}
		else{
			$("#comparison_table tr:eq(1)").clone(true).appendTo("#comparison_table").show().trigger("click");
		}
		$(document).scrollTop(9999);
	});
	//when select 'none' scope, scope field set empty.
	$("body").on("change",".scope_select",function(){
		if($(this).val()=="null"){
			$(this).siblings(".scope_input").val("");
		}
	});
	//when select symptom id, enable backreference autocomplete
//	$("body").on("autocompleteselect",".ref_sym_id, .sub_sym_id",function(evt,ui){
//		var ajaxURL="symptoms/allbackrefs/"+ui.item.value;
//		var key=ajaxURL;
//		var source=cache.getItem(key);
//		if(source==null){
//			var context=$(this);
//			$.get(ajaxURL, function(data){
//				var source=[];
//				for(var i=0;i<data.length;i++){
//					source.push({label:data[i],value:data[i]});
//				}
//				context.siblings(".ref_regex, .sub_back_ref").autocomplete({source:source,minLength:0});
//			});
//		}
//		else{
//			$(this).siblings(".ref_regex, .sub_back_ref").autocomplete({source:source,minLength:0});
//		}
//	});
//	$("body").on("click",".ref_regex, .sub_back_ref",function(){
//		$(this).autocomplete("search",$(this).val());
//	});
	//synchronize description fields
	$("body").on("change",".description_input",function(){
		$(this).siblings(".description_input").val($(this).val()).attr("title",$(this).val());
	});
}

function _initPatternBasedEventCallbacks(){
	//enable save button
	$("#symptom_table").on('change','select',function(){
		var jTr = $(this).parents('td').parent('tr');
		jTr.find('.save').attr("src","images/save2.png").addClass('enable');
	});
	$("#symptom_table").on('propertychange input paste autocompleteselect change','input,textarea',function(){
		var jTr = $(this).parents('td').parent('tr');
		jTr.find('.save').attr("src","images/save2.png").addClass('enable');
	});
	//shitty IE hack, to support propertychange event in table
	if(isIE){
		$("#symptom_table").on('mouseover','.save,.delete,.test,.export,.bug',function(){
			$(this).parents('td').parent('tr').find("input,textarea").blur();
		});
	}
	//change of the pattern causes to empty id field
	$("#symptom_table").on("change",".pattern_input",function(){
		$(this).parent("div").parent("div").parent("td").parent("tr").find(".patternId").val("-1");
	});
	//when resize cell, resize description textarea as well
	var resizeTextarea=function(ele){
		ele.outerHeight(ele.parent("td").parent("tr").find("td:eq(0)").outerHeight()-10);
	};
	//click one row and expand
	$("body").on("click","#symptom_table>tbody>tr:gt(1)",function(evt){
		var unexpandableNodes=["INPUT","SELECT","OPTION","TEXTAREA","IMG","A","SPAN","TH"];
		var nodeName=evt.target.nodeName.toUpperCase();
		if(unexpandableNodes.indexOf(nodeName)==-1 || (nodeName=="SPAN" && $(evt.target).is(".sym_id"))){
			//expand this row
			if($(this).find("input.description_input").is(":visible")){
				$(this).find(".second_inner_row,textarea.description_input,.substitute, .third_inner_row").show();
				$(this).find(".substitute").css("display","inline-block");
				$(this).find("input.description_input").hide();	
				if($(this).find(".third_inner_row tr:eq(2) .placeholder").val()==""){
					$(this).find(".third_inner_row").hide();
				}
				resizeTextarea($(this).find("textarea.description_input"));
			}
			//but, when collapse this row, test if this row is last focused, if not, do not collapse
			else if($(this).hasClass("selected")){
				$(this).find(".second_inner_row,textarea.description_input,.substitute, .third_inner_row").hide();
				$(this).find("input.description_input").show();	
			}
		}
		$("#symptom_table tr").removeClass("selected");
		$(this).addClass("selected");
	});
	//enable substitute function
	$("#symptom_table").on("click",".substitute",function(){
		$(this).parent("td").parent("tr").find(".third_inner_row").toggle();
		resizeTextarea($(this).parent("td").parent("tr").find("textarea.description_input"));
	});
	//add substitution
	$("#symptom_table").on("click",".add_substitute",function(){
		var table=$(this).parent("td").parent("tr").parent("tbody").parent("table");
		table.find("tr:eq(1)").clone(true).insertAfter($(this).parent("td").parent("tr")).show();
		resizeTextarea(table.parent("div").parent("td").parent("tr").find("textarea.description_input"));
	});
	//delete substitution
	$("#symptom_table").on("click",".delete_substitute",function(){
		if(!confirm(config.page_config.delete_substitution_confirm)){
			return false;
		}
		if($(this).parent("td").parent("tr").index()==2 && $(this).parent("td").parent("tr").next("tr").length==0){
			$(this).parent("td").find(".add_substitute").click();
			$(this).parent("td").parent("tr").parent("tbody").parent("table").parent("div").hide();
		}
		var row=$(this).parent("td").parent("tr").parent("tbody").parent("table").parent("div").parent("td").parent("tr");
		var textarea=row.find("textarea.description_input");
		$(this).parent("td").parent("tr").remove();
		resizeTextarea(textarea);
		row.find('.save').attr("src","images/save2.png").addClass('enable');
	});
	//keyup name select and enable pattern input
	$("#symptom_table").on("propertychange input paste autocompleteselect change",".name_select",function(){
		//if name select field is complete, then enable pattern input
		if($(this).val()!="" || $(this).val()!=config.page_config.select_symptom_data){
			$(this).parent("div").parent("div").find(".pattern_input").removeAttr("disabled");
		}
		else{
			$(this).parent("div").parent("div").find(".pattern_input").attr("disabled","disabled");
		}
	});
	//change substitution type dropdown list
	$("#symptom_table").on("change",".sub_type",function(){
		var td=$(this).parent("td");
		if($(this).val()=="reference"){
			td.find(".sub_var_name").hide();
			td.find(".sub_sym_id").show();
			td.find(".sub_back_ref").show();
		}
		else{
			td.find(".sub_sym_id").hide();
			td.find(".sub_back_ref").hide();
			td.find(".sub_var_name").show();
		}
	});
	//delete this symptom
	$("#symptom_table").on("click",".delete",function(){
		if(!confirm(config.page_config.delete_symptom_confirm)){
			return false;
		}
		var id=$(this).parent("td").parent("tr").find(".sym_id").text();
		if(id!=""){
			var symIds=$(".sub_sym_id");
			for(var i=0;i<symIds.length;i++){
				//check if it is referenced by other symptoms
				if($(symIds[i]).val()==id){
					XUI.createXDialog("Error", config.page_config.delete_referenced_symptom,[], []);
					return false;
				}	
			}
			var	loading_div = XUI.createXLoadingDiv();
			//delete this symptom by AJAX
			var context=$(this);
			$.ajax({
				type:"delete",
				url:"symptoms/pattern?symptomId="+id,
				success:function(){
					loading_div.hideLoadingDiv();
					XUI.createXDialog("Success", config.page_config.delete_symptom_success,[], []);
					context.parent("td").parent("tr").remove();
				},
				error:function(xhr){
					loading_div.hideLoadingDiv();
					XUI.createXDialog("Failed", xhr.getResponseHeader("msg"),[], []);
				}
			});
		}
		else{
			$(this).parent("td").parent("tr").remove();
		}
	});
	//get current symptom function
	var getCurrentSymptomByRow=function(row){
		var symptom={};
		symptom.id=row.find(".sym_id").text();
		symptom.description=row.find(".description_input").val();
		symptom.updateVersion=row.find(".updateVersion").val();
		symptom.readVersion=row.find(".readVersion").val();
		//get pattern
		var pattern={};
		if(row.find(".patternId").val()!="-1"){
			pattern.id=row.find(".patternId").val();			
		}
		pattern.regex=row.find(".pattern_input").val().trim();
		row.find(".pattern_input").val(pattern.regex);
		pattern.occurMin=row.find(".min_input").val();
		pattern.occurMax=(row.find(".max_input").val()=="" ? 0 : row.find(".max_input").val());
		if(row.find(".scope_input").val()!=""){
			pattern.scope=row.find(".scope_input").val()+" "+row.find(".scope_select").val();			
		}
		//get sourceTable of pattern
		var id_arr=row.find(".name_select").attr("select-ids").split(config.page_config.id_splitter);
		var sourceTable={};
		sourceTable.id=id_arr[id_arr.length-1];
		pattern.sourceTable=sourceTable;
		//get placeholderList of pattern
		var placeholderList=[];
		var placeholderRows=row.find(".third_inner_row table tr:gt(1)");
		//iterate placeholder rows
		for(var i=0;i<placeholderRows.length;i++){
			var placeholderRow=$(placeholderRows[i]);
			var placeholder={};
			placeholder.id=placeholderRow.find(".placeholderId").val();
			placeholder.index=placeholderRow.find(".placeholder").val().substring(1);
			//if empty, do not insert into array
			if(placeholder.index==""){
				break;
			}
			placeholder.type=placeholderRow.find(".sub_type").val().toUpperCase();
			placeholder.backReference=placeholderRow.find(".sub_back_ref").val();
			placeholder.name=placeholderRow.find(".sub_var_name").val();
			placeholder.refSymptomId=placeholderRow.find(".sub_sym_id").val();
			placeholder.pattern={};
			placeholder.pattern.id=pattern.id;
			var mapping_arr=placeholderRow.find(".mapping").attr("select-ids").split(config.page_config.id_splitter);
			var mapping={};
			mapping.id=mapping_arr[mapping_arr.length-1];
			placeholder.mapping=mapping;
			placeholderList.push(placeholder);
		}
		pattern.placeholderList=placeholderList;
		symptom.pattern=pattern;
		return symptom;
	};
	//validate symptom
	var validateSymptomByRow=function(row){
		var isCorrect=true;
		row.find("input,select").removeClass("x_error_field");
		//check symptom data
		var nameSelect=row.find(".name_select");
		if(nameSelect.val()==""){
			nameSelect.addClass("x_error_field");
			isCorrect=false;
		}
		//check regex
		var patternInput=row.find(".pattern_input");
		if(patternInput.val()==""){
			patternInput.addClass("x_error_field");
			isCorrect=false;
		}
		//check number field
		var regex=new RegExp("^\\d+$");
		row.find(".min_input,.max_input,.scope_input,.sub_sym_id,.sub_back_ref").each(function(){
			if($(this).val()!="" && !regex.test($(this).val())){
				$(this).addClass("x_error_field");
				isCorrect=false;
			}
		});
		//min field must not be smaller than 1
		row.find(".min_input").each(function(){
			if(!regex.test($(this).val()) || $(this).val()==0){
				$(this).addClass("x_error_field");
				isCorrect=false;
			}
		});
		//max field must be greater than or equal to min field
		row.find(".max_input").each(function(){
			if($(this).val() != ""){
				if($(this).val()<row.find(".min_input").val()){
					$(this).addClass("x_error_field");
					isCorrect=false;
				}
			}
		});
		//check scope input
		row.find(".scope_input").each(function(){
			if($(this).val() != ""){
				if($(this).val()<=0){
					$(this).addClass("x_error_field");
					isCorrect=false;
				}
			}
			if($(this).val()==""){
				if($(this).siblings(".scope_select").val()!="null"){
					$(this).addClass("x_error_field");
					$(this).siblings(".scope_select").addClass("x_error_field");
					isCorrect=false;
				}
			}
			else{
				if($(this).siblings(".scope_select").val()=="null"){
					$(this).addClass("x_error_field");
					$(this).siblings(".scope_select").addClass("x_error_field");
					isCorrect=false;
				}
			}
		});
		//check placeholder
		regex=new RegExp("^\\$\\d+$");
		row.find(".placeholder").each(function(){
			if($(this).val()!="" && !regex.test($(this).val())){
				$(this).addClass("x_error_field");
				isCorrect=false;
			}
			else if(row.find(".pattern_input").val().indexOf($(this).val())==-1){
				$(this).addClass("x_error_field");
				isCorrect=false;
			}
		});
		//check mapping
		row.find(".mapping").each(function(){
			if($(this).val()!=config.page_config.select_mapping && $(this).attr("all-level")!=$(this).attr("current-level")){
				$(this).addClass("x_error_field");
				isCorrect=false;
			}
		});
		if(isCorrect==false){
			if(!row.find(".second_inner_row").is(":visible")){
				row.click();
			}
		}
		return isCorrect;
	}
	//save this symptom
	$("#symptom_table").on("click",".save",function(){
		if($(this).hasClass("enable")){
			var row=$(this).parent("td").parent("tr");	
			if(validateSymptomByRow(row)){
				var symptom=getCurrentSymptomByRow(row);
				var	loading_div = XUI.createXLoadingDiv();
				$.ajax({
					type:"put",
					url:"symptoms/pattern",
					contentType : "application/json",
					data : JSON.stringify(symptom),
					success:function(data){
						loading_div.hideLoadingDiv();
						XUI.createXDialog("Success", config.page_config.save_symptom_success,[], []);
						row.find(".updateVersion").val(data.updateVersion);
						row.find(".readVersion").val(data.readVersion);
						row.find(".sym_id").text(data.id);
						row.find(".patternId").val(data.patternId);
						row.find(".save").attr("src","images/save1.png").removeClass('enable');
					},
					error:function(xhr){
						loading_div.hideLoadingDiv();
						var msg = xhr.getResponseHeader("msg");
						if(msg==null || msg==undefined)
							msg=config.page_config.save_symptom_error;
						XUI.createXDialog("Failed", msg,[], []);
					}
				});	
			}
			else{
				XUI.createXDialog("Error", config.page_config.fill_fields_error,[], []);
			}
		}
	});
	//test this symptom
	$("#symptom_table").on("click",".test",function(){
		var row=$(this).parent("td").parent("tr");	
		if(validateSymptomByRow(row)){
			//save before test
			if(row.find(".save").hasClass("enable")){
				XUI.createXDialog("Failed", config.page_config.save_before_test_symptom,[], []);
			}
			else{
				totalcount=0;
				totalper=0.0;
				_continuously_scan(row);
			}	
		}
		else{
			XUI.createXDialog("Error", config.page_config.fill_fields_error,[], []);
		}
		
	});
	

	
	//export this symptom
	$("#symptom_table").on("click",".export",function(){
		var uid=new Date().getTime();
		var row=$(this).parent("td").parent("tr");	
		var symId=row.find(".sym_id").text();
		var myDate=new Date();
		var formattedDate = "["+myDate.getFullYear()+"-"+pad((myDate.getMonth()+1),2)+"-"+pad(myDate.getDate(),2)+"]";
		
		monitorExportProgress("symptoms/exporttestresult/symptomid/"+symId+"/uid/"+uid+"/Symptom "+symId+" Penetration "+formattedDate+".xls", 
							  "symptoms/exporttestresult/progress/"+uid,
							  "symptoms/exporttestresult/stop/"+uid);
	});
	
	
	//view bugs related
	$("#symptom_table .bug").bind("click",function(){
		var row=$(this).parent("td").parent("tr");
		var bugzillaId=row.find(".bugs_id").val();
//		alert(bugzillaId);
//		console.write(bugzillaId);
		if(bugzillaId!=undefined||bugzillaId!=null||bugzillaId.val()!="null"){
			window.open("manage_bug_def.html?bugzilla_id="+bugzillaId);
		} else{
			return;
		}
	});
	
	
	//sort function
	$("#symptom_table .sortable").click(function(){
		var field="";
		var order="";
		if ($(this).hasClass("desc")) {
			$("#symptom_table .sortable").removeClass("desc");
			$(this).addClass("asc");
			field = $(this).data("field");
			order="asc";
		} else if ($(this).hasClass("asc")) {
			$("#symptom_table .sortable").removeClass("asc");
			$(this).addClass("desc");
			field = $(this).data("field");
			order="desc";
		} else {
			$("#symptom_table .sortable").removeClass("desc").removeClass("asc");
			$(this).addClass("asc");
			field = $(this).data("field");
			order="asc";
		}
		//order by some field
		_getAllPatternSymptoms(field,order);
	});
	//search function
	$("#search_symptom").click(function(){
		_getAllPatternSymptoms("id","desc");
	});
	//autocomplete symptom ids
	$("#symptom_table").on("click",".sub_sym_id",function(){
		$(this).autocomplete({source:symIds,minLength:0}).autocomplete("search",$(this).val());
	});
}

//init name dropdown list
function _initNameSelection(){
	//autocomplete support
	_initSelectionAutocomplete("nameselection",".name_select","symptoms/symptomdata");
	_initSelectionAutocomplete("nameselection",".var_select","symptoms/symptomdata");
}

//init mapping selection
function _initMapSelection(){
	//autocomplete support
	_initSelectionAutocomplete("mapselection",".mapping","symptoms/mapping");
	_initSelectionAutocomplete("mapselection",".var_calculation","symptoms/mapping");
}

//enable multi-level selector with autocomplete function
function _initSelectionAutocomplete(type, selector, ajaxURL){
	var id_splitter=config.page_config.id_splitter;
	var path_splitter=config.page_config.path_splitter;
	//tooltip
	var tooltip=function(level,ele){
		var hintInfo="[INFO] ";
		switch(level){
		case 0:
			if(type=="mapselection"){
				hintInfo+=config.page_config.select_table_set;				
			}
			else{
				hintInfo+=config.page_config.select_symptom_data;
			}
			break;
		case 1:
			if(type=="mapselection"){
				hintInfo+=config.page_config.select_table_subset;
			}
			else{
				hintInfo+=config.page_config.select_table;
			}
			break;
		case 2:
			if(type=="mapselection"){
				hintInfo+=config.page_config.select_domain;
			}
			else{
				hintInfo+=config.page_config.select_section;
			}
			break;
		default:
			if(type=="mapselection"){
				hintInfo+=config.page_config.select_codomian;				
			}
			else{
				hintInfo+=config.page_config.select_variable;				
			}
			break;
		}
		$("#tooltip").text(hintInfo).stop().show().css({top:(ele.offset().top-30)+"px",left:ele.offset().left+"px"});
	};
	//Backspace key event
	$("body").on("keyup",selector,function(evt){
		if(evt.keyCode==8){
			$(this).attr({"select-path":"","current-level":"0","select-ids":""}).val("").trigger("click");
		}
	});
	//click event
	$("body").on("click",selector,function(){
		//level: 1--datasource/table, 2--table/key, 3--section/from, 4--variable/to
		//selected-path: splitted by '#'
		//url format: lastLevel=1&search=501
		var currentLevel=parseInt($(this).attr("current-level"));
		var allLevel=parseInt($(this).attr("all-level"));
		var text=$(this).val();
		//if input field is default, empty it.
		if(text==config.page_config.select_symptom_data || text==config.page_config.select_variable || text==config.page_config.select_mapping){
			$(this).val("");
			text="";
		}
		else if(text!=""){
			return;
		}
		tooltip(currentLevel,$(this));
		var id=0;
		//if level is larger than 0 and not equal to all level, then autocomplete next level after searching by the last one.(AJAX)
		if(currentLevel>0 && currentLevel<allLevel){
			id=$(this).attr("select-ids").split(id_splitter)[currentLevel-1];
		}
		//if level is 0, then search all symptom_data.(AJAX)
		else if(currentLevel!=0){
			return false;
		}
		$("#selection_loading").css({top:($(this).offset().top+4)+"px",left:($(this).offset().left+5)+"px"}).show();
		//cache function
		var url=ajaxURL+"/"+id;
		var source=cache.getItem(url);
		if(source==null){
			var context=$(this);
			$.ajax({
				type:"get",
				url:url,
				cache:true,
				success:function(data){
					source=[];
					//if there is no symptom data under this level
					if(data.length==0){
						var path=context.attr("select-path");
						context.val(path.split(path_splitter)[currentLevel-1]);
						var reg=new RegExp(path_splitter,"g");
						context.attr("title",path.replace(reg,config.page_config.tip_splitter));	
						$("#tooltip").hide();
					}
					else{
						if(currentLevel!=0){
							source=[{label:config.page_config.back_level_item,value:config.page_config.back_level_item}];					
						}
						for(var i=0;i<data.length;i++){
							var item={};
							item.value=data[i].id;
							item.label=data[i].name;
							source.push(item);
						}
						context.autocomplete({source:source,minLength:0}).autocomplete("search",text);
					}
					//cache
					cache.setItem(url,source);
					$("#selection_loading").hide();
				}
			});
		}
		else{
			//if there is no symptom data under this level
			if(source.length==0){
				var path=$(this).attr("select-path");
				$(this).val(path.split(path_splitter)[currentLevel-1]);
				var reg=new RegExp(path_splitter,"g");
				$(this).attr("title",path.replace(reg,config.page_config.tip_splitter));
				$("#tooltip").hide();
			}
			else{
				$(this).autocomplete({source:source,minLength:0}).autocomplete("search",text);
			}
			$("#selection_loading").hide();
		}
		console.log(source);
	});
	//autocomplete selection event
	$("body").on("autocompleteselect",selector,function(evt,ui){
		var currentLevel=parseInt($(this).attr("current-level"));
		var allLevel=parseInt($(this).attr("all-level"));
		var path=$(this).attr("select-path");
		var ids=$(this).attr("select-ids");
		//if want to go to last level, then modify path ,ids and level data.
		if(ui.item.value==config.page_config.back_level_item){
			$(this).attr("current-level",currentLevel-1);
			var path_arr=path.split(path_splitter);
			path_arr.pop();
			var id_arr=ids.split(id_splitter);
			id_arr.pop();
			$(this).attr("select-path",path_arr.join(path_splitter));
			$(this).attr("select-ids",id_arr.join(id_splitter));
			$(this).val("");
			//trigger last level autocomplete
			var context=$(this);
			setTimeout(function(){context.click();},0);
		}
		//if current level is less than all level, then increase level and trigger next level autocomplete event. 
		else if(currentLevel!=allLevel){
			currentLevel++;
			$(this).attr("current-level",currentLevel);
			//record path attribute
			if(path!=""){
				path+=path_splitter;
			}
			path+=ui.item.label;
			$(this).attr("select-path",path);
			//record ids attribute
			if(ids!=""){
				ids+=id_splitter;
			}
			ids+=ui.item.value;
			$(this).attr("select-ids",ids);
			//if the new current level is still not equal to all level, then trigger next level autocomplete event. 
			if(currentLevel!=allLevel){
				$(this).val("");
				//trigger next level autocomplete
				var context=$(this);
				setTimeout(function(){context.click();},0);
			}
			//if the new current level is equal to all level, do not trigger autocomplete event and display the value.
			else{
				$(this).val(ui.item.label);				
			}
		}
		//if current level is equal to all level, then record path, and display the value.
		else{
			//update path attribute
			var path_arr=path.split(path_splitter);
			path_arr[path_arr.length-1]=ui.item.label;
			path=path_arr.join(path_splitter);
			$(this).attr("select-path",path);
			//update ids attribute
			var id_arr=ids.split(id_splitter);
			id_arr[id_arr.length-1]=ui.item.value;
			ids=id_arr.join(id_splitter);
			$(this).attr("select-ids",ids);
			//display value
			$(this).val(ui.item.label);
		}
		//show path as tooltip
		if(currentLevel==allLevel){
			var reg=new RegExp(path_splitter,"g");
			$(this).attr("title",path.replace(reg,config.page_config.tip_splitter));
		}
		//prevent default event action
		evt.preventDefault();
	});
	//blur event
	$("body").on("blur",selector,function(){
		//when empty the field, the path, the level should go back to the initial one.
		if($(this).val()==""){
			var allLevel=parseInt($(this).attr("all-level"));
			$(this).attr({"select-path":"","current-level":"0","select-ids":""});
			if(type=="nameselection"){
				if(selector==".name_select"){
					$(this).val(config.page_config.select_symptom_data);
				}
				else{
					$(this).val(config.page_config.select_variable);
				}
			}
			else{
				$(this).val(config.page_config.select_mapping);
			}
		}
		$("#tooltip").hide();
	});
}

//get all pattern-based symptoms in order
function _getAllPatternSymptoms(field,order){
	var searchForm={};
	searchForm.id=$("#filter_id").multiselect("getChecked").map(function(){return this.value}).get().join(",");
	searchForm.regex=$("#filter_pattern").val();
	searchForm.description=$("#filter_description").val();
	searchForm.source_table=($("#filter_data").val()==config.page_config.select_symptom_data?"":$("#filter_data").val());
	var url="symptoms/pattern/field/"+field+"/order/"+order;
	var	loading_div = XUI.createXLoadingDiv();
	$.get(url,searchForm,function(data){
		$("#symptom_table>tbody>tr:gt(1)").remove();
		var id_splitter=config.page_config.id_splitter;
		var path_splitter=config.page_config.path_splitter;
		var tip_splitter=config.page_config.tip_splitter;
		var reg=new RegExp(path_splitter,"g");
		symIds=[];
		var symId=0;
		for(var i=0;i<data.length;i++){
			var symptom=data[i];
			// not a good way to dedupe
			if(symId!=symptom.id){
				symId=symptom.id;
			}
			else{
				continue;
			}
			$("#symptom_table tr:eq(1)").clone(true).appendTo("#symptom_table").show();
			var new_row=$("#symptom_table>tbody>tr:last");
			new_row.find(".sym_id").text(symptom.id).attr("title","symptom "+symptom.id);
			symIds.push({label:symptom.id,value:symptom.id});
			new_row.find(".readVersion").val(symptom.readVersion);
			new_row.find(".updateVersion").val(symptom.updateVersion);
			new_row.find(".description_input").val(symptom.description).attr("title",symptom.description);
			var pattern=symptom.pattern;
			new_row.find(".patternId").val(pattern.id);
		

			//test tooltips for sympton_bug
			var bugNum=symptom.bugNum;
			var bugs=symptom.bugs;
			
			
			if(bugNum==0){
				new_row.find(".bug").attr({"title":config.page_config.symptom_zero_referenced,"src":"images/bug_icon2.png"});
				new_row.find(".bug").unbind("click").removeAttr("onclick");
				new_row.find(".bug").removeClass('enable');
//				new_row.find(".bug").attr("disabled","disabled");
				new_row.find(".bugs_id").val("null");
				new_row.find(".flag").css("visibility","hidden");
			}

//			var text="There is {bugNum} bugs related, they are {bugs}, click for detail"; //write in config.js later
			else{
				
				new_row.find(".bug").attr({"title":config.page_config.symptom_referenced.format({bugNum:bugNum,bugs:bugs}), "src":"images/bug_icon1.png"});
				new_row.find(".bug").addClass('enable');
				new_row.find(".bugs_id").val(bugs);
				new_row.find(".flag").html(bugNum).css("visibility","visible");
			}
			
			//end
			
			var source_arr=pattern.symptomDataPath;
			if(source_arr!=null && source_arr.length>0){
				var path="",ids="";
				for(var k=0;k<source_arr.length;k++){
					path+=source_arr[k].name;
					ids+=source_arr[k].id;
					if(k!=source_arr.length-1){
						path+=path_splitter;
						ids+=id_splitter;
					}
				}
				new_row.find(".name_select").attr({"current-level":source_arr.length,"select-path":path,"select-ids":ids}).val(source_arr[source_arr.length-1].name).attr("title",path.replace(reg,tip_splitter));
			}
			//cache
			new_row.find(".pattern_input").removeAttr("disabled").val(pattern.regex);
			new_row.find(".min_input").val(pattern.occurMin);
			if(pattern.occurMax!=0){
				new_row.find(".max_input").val(pattern.occurMax);				
			}
			else{
				new_row.find(".max_input").val("");	
			}
			if(pattern.scope!=null){
				var scope_arr=pattern.scope.split(" ");
				new_row.find(".scope_input").val(scope_arr[0]);
				new_row.find(".scope_select").val(scope_arr[1]);
			}
			var placeHolderList=pattern.placeholderList;
			if(placeHolderList!=undefined && placeHolderList.length>0){
				for(var j=0;j<placeHolderList.length;j++){
					var placeholder=placeHolderList[j];
					var row=new_row.find(".third_inner_row table tr:eq(2)");
					if(j!=0){
						new_row.find(".third_inner_row table tr:eq(1)").clone(true).show().appendTo(new_row.find(".third_inner_row table"));
						row=new_row.find(".third_inner_row table tr:last");
					}
					row.find(".placeholderId").val(placeholder.id);
					if(placeholder.index!=null){
						row.find(".placeholder").val("$"+placeholder.index);						
					}
					row.find(".sub_type").val(placeholder.type.toLowerCase()).change();
					if(placeholder.backReference!=null){
						row.find(".sub_back_ref").val(placeholder.backReference);						
					}
					row.find(".sub_sym_id").val(placeholder.refSymptomId);
					row.find(".sub_var_name").val(placeholder.name);
					var mappings=placeholder.mappingPath;
					if(mappings!=null && mappings.length==4){
						var path=mappings[0].name+path_splitter+mappings[1].name+path_splitter+mappings[2].name+path_splitter+mappings[3].name;
						var ids=mappings[0].id+id_splitter+mappings[1].id+id_splitter+mappings[2].id+id_splitter+mappings[3].id;
						row.find(".mapping").attr({"current-level":"4","select-path":path,"select-ids":ids}).val(mappings[3].name).attr("title",path.replace(reg,tip_splitter));	
					}
				}	
			}
			else{
				new_row.find(".third_inner_row").hide();
			}
			new_row.find(".save").attr("src","images/save1.png").removeClass('enable');
		}
		loading_div.hideLoadingDiv();
		//pagination
		$("#def_show_all").change();	
	});
}


//continue to scan more data when testing a symptom
function _continuously_scan(row){
	var taskId;
	var symId=row.find(".sym_id").text();
	var monitor;
	$.ajax({
		type:"post",
		url:"symptoms/test/"+symId+"/gettaskid",
		success:function(data){
				console.log(data);
				taskId = data;
				monitor = monitorTestProgress("symptoms/test/progress/"+taskId, "symptoms/test/cancel/" + taskId);
				$.ajax({
					type:"post",
					url:"symptoms/pattern/test/"+symId,
					success:function(data){
						monitor.waitAndClose();
						var count=data.sum;
						var percentage=data.percentage;
						totalcount=count;
						totalper=percentage;
						if(totalper>1.0)
							totalper=1.0;
						if(totalcount>0){
						if(count>0){
							row.find(".sym_id").text(data.symptomId);
							row.find(".patternId").val(data.patternId);
							$("#test_count").text(totalcount);
							$("#pre_percentage").text(totalper*100);
							taskId = data.taskId;
							var asupid=data.asupid;
							var timestamp=data.timestamp;
							var detail=data.detail;
							
							$("#matched_title").html("SN: " + data.sn + ", ASUP ID: " + data.asupid[0]);
							
							var html="<tr><th>Timestamp</th><th>Details</th></tr>";
							for(var i=0;i<asupid.length;i++){
								html+="<tr><td>"+timestamp[i]+"</td><td>"+detail[i]+"</td></tr>";
							}
							$("#test_result table").html(html);
							
							var progress_html="<span id='exported_lines' style='margin-left:20px'></span><span id='export_message' style='margin-left:20px'></span><a id='stop_export' style='display:none;margin-left:20px'>Stop</a><span id='stop_hint' style='margin-left:20px'></span>";
							XUI.createXDialog("Test Result for Symptom ID " + symId, $("#test_result").html()+progress_html,["Export","Search More Data"], [
							function(){
								var myDate=new Date();
								var formattedDate = " ["+myDate.getFullYear()+"-"+pad((myDate.getMonth()+1),2)+"-"+pad(myDate.getDate(),2)+"] ";
								
								monitorExportProgress("symptoms/exporttestresult/symptomid/0/uid/"+taskId+"/Symptom "+symId+" Penetration "+formattedDate+".xls",
													"symptoms/exporttestresult/progress/"+taskId, 
													"symptoms/exporttestresult/stop/"+taskId);
							}, function(){
								if(totalper!=1.0)
								_continuously_scan(row);
								else{
									XUI.createXDialog("Information","All Data has been scanned",[],[]);
								}
								}
							]);							
						}
						else{
							XUI.createXDialog("Information",totalper*100+"% data has been scanned."+totalcount+" matches are found!",["Search More"],[function(){
								if(totalper!=1.0)
									_continuously_scan(row);
									else{
										XUI.createXDialog("Information","All Data has been scanned",[],[]);
									}
							}]);
						}
						}
						else{
							XUI.createXDialog("Information",totalper*100+"% data has been scanned. No matches are found!",["Search More"],[function(){
								if(totalper!=1.0)
									_continuously_scan(row);
									else{
										XUI.createXDialog("Information","All Data has been scanned",[],[]);
									}
							}]);
						}
//						loading_div.hideLoadingDiv();
					}
				});	
			},
			error:function(xhr){
				monitor.waitAndClose();
				XUI.createXDialog("Error", config.page_config.test_bug_error,[], []);
			}
		});
}

function _initComparisonBasedEventCallbacks(){
	//enable save button
	$("#comparison_table").on('change','select',function(){
		var jTr = $(this).parents('td').parent('tr');
		jTr.find('.save').attr("src","images/save2.png").addClass('enable');
	});
	$("#comparison_table").on('propertychange input paste autocompleteselect change','input,textarea',function(){
		var jTr = $(this).parents('td').parent('tr');
		jTr.find('.save').attr("src","images/save2.png").addClass('enable');
	});
	//shitty IE hack, to support propertychange event in table
	if(isIE){
		$("#comparison_table").on('mouseover','.save,.delete,.test,.export',function(){
			$(this).parents('td').parent('tr').find("input,textarea").blur();
		});
	}
	//when resize cell, resize description textarea as well
	var resizeTextarea=function(ele){
		ele.outerHeight(ele.parent("td").parent("tr").find("td:eq(0)").outerHeight()-10);
	};
	//click one row and expand
	$("body").on("click","#comparison_table>tbody>tr:gt(1)",function(evt){
		var unexpandableNodes=["INPUT","SELECT","OPTION","TEXTAREA","IMG","A","TH"];
		var nodeName=evt.target.nodeName.toUpperCase();
		if(unexpandableNodes.indexOf(nodeName)==-1){
			//expand this row
			if($(this).find("input.description_input").is(":visible")){
				$(this).find(".second_inner_row,textarea.description_input").show();
				$(this).find("input.description_input").hide();	
				resizeTextarea($(this).find("textarea.description_input"));
			}
			//but, when collapse this row, test if this row is last focused, if not, do not collapse
			else if($(this).hasClass("selected")){
				$(this).find(".second_inner_row,textarea.description_input").hide();
				$(this).find("input.description_input").show();	
			}
		}
		$("#comparison_table tr").removeClass("selected");
		$(this).addClass("selected");
	});
	//operand type changes...
	$("#comparison_table").on("change",".op_type",function(){
		if($(this).val()=="variable"){
			$(this).siblings(".value_operand,.ref_sym_id,.ref_regex").hide();
			$(this).siblings(".var_select,.var_calculation").show();
		}
		else if($(this).val()=="value"){
			$(this).siblings(".value_operand").show();
			$(this).siblings(".var_select,.var_calculation,.ref_sym_id,.ref_regex").hide();
		}
		else{
			$(this).siblings(".ref_sym_id,.ref_regex").show();
			$(this).siblings(".var_select,.var_calculation,.value_operand").hide();
		}
	});
	//autocomplete symptom ids
	$("#comparison_table").on("click",".ref_sym_id",function(){
		$(this).autocomplete({source:symIds,minLength:0}).autocomplete("search",$(this).val());
	});
	//delete this symptom
	$("#comparison_table").on("click",".delete",function(){
		alert("delete");
	});
	//save this symptom
	$("#comparison_table").on("click",".save",function(){
		alert("save");
	});
	//test this symptom
	$("#comparison_table").on("click",".test",function(){
		alert("test");
	});
	//export this symptom
	$("#comparison_table").on("click",".export_symptom",function(){
		alert("export");
	});
	//sort function
	$("#comparison_table .sortable").click(function(){
		alert("sort");
	});
	//search function
	$("#search_comparison").click(function(){
		_getAllComparisonSymptoms("id","desc");
	});
}

//get all comparison-based symptoms in order
function _getAllComparisonSymptoms(field, order) {
	$("#comparison_table>tbody>tr:gt(1)").remove();
	$("#search_loading").css("visibility", "visible");
	
	var data = [
	            {
	            	"symid":500,
	            	"bugNum":1,
	            	"bugs":73222,
	            	"pattern":
	            		{"symptomDataPath":
	            			[{"id":1,"name":"autosupport"},
	            			 {"id":2,"name":"kernel info log"}]
	            		},
	            	"pattern2":
						{"symptomDataPath": 
							[{"id":1,"name":"Controller performance"},
							 {"id":4,"name":"Controller ID"},
							 {"id":7,"name":"Average processor util"}]
						},
					"pattern3":
	            		{"symptomDataPath":
	            			[{"id":1,"name":"autosupport"},
	            			 {"id":9,"name":"enclosure model"}]
	            		},
	            	"pattern4":
						{"symptomDataPath": 
							[{"id":2,"name":"Disk performance"}]
						},
	            	"description":"test",
	            	"operandAType":"value",
	            	"operandBType":"value",
	            	"valueOperandA":"test",
	            	"operator":"<=",
	            	"refSymIdA":"461",
	            	"refRegexA":"test",
	            	"valueOperandB":"test",
	            	"refSymIdB":"test",
	            	"refRegexB":"test",
	            	"minInput":"1",
	            	"maxInput":"2",
	            	"scopeInput":"1",
	            	"scopeSelect":"minutes"
	            }
	];
	/*$.ajax({
		type : "get",
		url : "symptoms/comparison",
		contentType : "application/json",

		success : function(data, status) {*/

			$("#selection_loading").css("visibility", "hidden");
			
			$("#comparisonBasedTemplate").tmpl(data).appendTo("#comparison_table");
		/*},
		error : function(xhr) {
			$("#selection_loading").hide();
			if (!XUtil.checkSession(xhr)) {
				XUI.createXDialog("Error", xhr.getResponseHeader("msg"),[], []);
			}
		}
	});*/
}

function getSymptomDataPath(data) {
	var arr = [];
	if (data != null && data.length > 0) {
		for (var k = 0; k < data.length; k++) {
			arr.push(data[k].name);
		}
	}
	return arr.join(",");
}

function getSymptomDataIds(data){
	var arr = [];
	if (data != null && data.length > 0) {
		for (var k = 0; k < data.length; k++) {
			arr.push(data[k].id);
		}
	}
	return arr.join("#");
}