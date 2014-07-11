var isIE=/*@cc_on!@*/false; //shitty IE
var bugList = [];

$(function() {
	initOptionUI();
	initPageUI();
	initShowBugDef();
	initEditBugDef();
	initEventCallbacks();

	// update page
	$.ajax({
		type : "post",
		url : "user/history/page",
		data : "manage_bug_def.html"
	});
});

function initPageUI() {
	// enable ajax cache
	// $.ajaxSetup({cache:true});
	bugId = XUtil.getURLParameter("bugID");
	if (bugId == null) {
		$("#bug_table").hide();
		$("div.xui_content_body:eq(1)").append("<p id='page_tip'>" + config.page_config.bug_page_tooltip + "</p>");
	}

	loading_div = XUI.createXLoadingDiv();

//	$("#export_bug_definitions").button();
	$("#export_bug_matches").button();
	$("#manage_bug_btn").button();
	$("#software_tree_ok_btn").button();
	$("#hardware_tree_ok_btn").button();

	$(".software_tree_input,.hardware_tree_input").attr("title", "Click to open dropdown list.");

	$("#add_bug").attr("title", config.page_config.add_bug_def);
	$(".delete_bug").attr("title", config.page_config.remove_bug_def);
	$(".edit_bug").attr("title", config.page_config.edit_bug_def);
	$(".export_bug").attr("title", config.page_config.export_bug_def);
	$(".bug_remediation").attr("title", config.page_config.show_bug_def);

	$("#bug_table th:not(:last)").attr("title", config.page_config.sortable_column_hint);

	// XUtil.disableUIElement($(".symptom_op"), $(".scope_input"),
	// $(".symptom_unit"), $(".delete_symptom"),$(".symptom_cmp"));
	$(".symptom_op,.symptom_op+span,.scope_input,.symptom_unit,.delete_symptom,.symptom_cmp").css("visibility", "hidden");

	$("#pager_setting input[type='text']").val(config.page_config.items_per_page);

	//parse URL parameters
	var bzids=XUtil.getURLParameter("bugzilla_id");
	if(bzids!=null){
		$("#customize_layout").show();
		$("#bug_table").show();
		$("#page_tip").hide();
		$("#bug_table th").removeClass("desc").removeClass("asc");
		$("#bug_table th[data-field='bugzillaId']").addClass("asc");
		$.get("signature/search/bugzillaId/asc", {
			bugzilla_id : bzids
		}, function(data,status,xhr) {
			sigVersion=xhr.getResponseHeader("sig_version");
			bugList = [];
			data.each(function(v,k){
				bugList.push(v);
			});
			_showBugs(data);
		});
	}
	_initTreeWidgets();
	_initProductSelect();
	_initBugIdSelect();
	_initDomainSelect("#domain_select,.domain");
	_initStateSelect("#lifecyle_select,.lifecycle");
	_initImpactSelect("#impact_select,.impact");
//	_initColumnOption();
	
}

function _initProductSelect() {
	$.get("signature/product", function(data) {
		for (var i = 0; i < data.length; i++) {
			$("#product_select").append("<option value='" + data[i].id + "'>" + data[i].name + "</option>");
		}
	});
}


function _initBugIdSelect() {
	$.get("signature/ids", function(data) {
		$("#id_select option:gt(0)").remove();
		for (var i = 0; i < data.length; i++) {
			$("#id_select").append("<option value='" + data[i].id + "'>" + data[i].bugzillaId + "</option>");
		}
		loading_div.hideLoadingDiv();
		if (bugId != null) {
			$("#id_select").val(bugId);
			$("#manage_bug_btn").click();
		}
	});
}

function initEventCallbacks() {
	$("#reset_btn").click(function() {
		_resetFields();
	});
	$("#manage_bug_btn").click(function() {
		$("#customize_layout").show();
		_searchBugs("bugzillaId", "asc");
	});
	$("#show_all").change(function() {
		if ($(this).is(":checked")) {
			var size = $("#bug_table tr:gt(1)").length;
			_cacheLastPage();
			XUtil.pageXTable("#pagination", "#bug_table tr", size);
		} else {
			// var size = $("#bug_table tr:gt(1)").length;
			XUtil.pageXTable("#pagination", "#bug_table tr", config.page_config.items_per_page, lastPage);
			lastPage = 0;
		}
	});
	$("#add_bug").click(
			function() {
				_showBugDialog("Create Bug Definition", [ "Create", "Test" ], [
						function() {
							_saveOrUpdateBug(_getBugByDialog());
						},
						function() {
							_testBugByLogic($("#dialog .bugzilla_input").val(), _getLogicByDialog(), $("#dialog .hardware_tree_input")
									.val(), $("#dialog .software_tree_input").val());
						} ], null, true);
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
	$(document).on("click", "img.delete_symptom", function() {
		var old_row = $(this).parent("td").parent("tr");
		old_row.remove();
		// XUtil.disableUIElement($(".symptom_op:last"),
		// $(".scope_input:last"),
		// $(".symptom_unit:last"),$(".symptom_cmp:last"));
		$(".symptom_op:last,.symptom_op:last+span,.scope_input:last,.symptom_unit:last,.symptom_cmp:last").css("visibility", "hidden");
		if ($(".delete_symptom:visible").length == 1) {
			XUtil.disableUIElement($(".delete_symptom:visible"));
		}
	});

	$(document).on("mousedown", "img.left_parenthesis",function(event){
		switch (event.which) {
			//left click
			case 1:
				var num = parseInt($(this).attr("num")) + 1;
				if (num >= 4) {
					$(this).attr("class","left_parenthesis left_parenthesis_4");
//					$(this).next().html(num).css("visibility","visible");
					$(this).next().html(num).css("visibility","visible");
				}else{
					$(this).attr("class","left_parenthesis left_parenthesis_"+num);
				}
				$(this).attr("num", num);
				break;
			//right click
			case 3:
				var num = parseInt($(this).attr("num"));
				if (num == 0) {
					break;
				}
				num--;
				if (num < 4) {
					$(this).next().html(num).css("visibility","hidden");
//					$(this).next().html(num).hide();
					$(this).attr("class","left_parenthesis left_parenthesis_"+num);
				}else{
					$(this).next().html(num);
				}
				$(this).attr("num", num);
				break;
		}
		
		
	});
	$(document).on("mousedown", "img.right_parenthesis",function(event){
		switch (event.which) {
			//left click
			case 1:
				var num = parseInt($(this).attr("num")) + 1;
				if (num >= 4) {
					$(this).attr("class","right_parenthesis right_parenthesis_4");
//					$(this).next().html(num).css("visibility","visible");
					$(this).next().html(num).css("visibility","visible");
				}else{
					$(this).attr("class","right_parenthesis right_parenthesis_"+num);
				}
				$(this).attr("num", num);
				break;
			//right click
			case 3:
				var num = parseInt($(this).attr("num"));
				if (num == 0) {
					break;
				}
				num--;
				if (num < 4) {
//					$(this).next().html(num).hide();
					$(this).next().html(num).css("visibility","hidden");
					$(this).attr("class","right_parenthesis right_parenthesis_"+num);
				}else{
					$(this).next().html(num);
				}
				$(this).attr("num", num);
				break;
		}
		
		
	});
	// cancle default right click menucontext event
	$(document).on("contextmenu", "img.left_parenthesis,img.right_parenthesis", function(event) {
		return false;
	});
	// $(document).on("blur","input.left_parent_num,input.right_parent_num",function(){
	// var img=$(this).prev("img");
	// if($(this).val()=="0"){
	// if(img.attr("data-status")!="false"){
	// img.click();
	// }
	// }
	// else{
	// if(img.attr("data-status")!="true"){
	// img.click();
	// }
	// }
	// });

	$(document).on("click",".bug_description",function(evt){
		var bug_id = $(this).parent("td").parent("tr").find(".bug_id").val();
		var bug = _getBugById(bug_id);
		_showBugDialog("Show Bug Definition", [], [], bug, false);
		return false;
	});
	
	$("#bug_table").on("click", ".delete_bug", function(evt) {
		var id = $(this).parent("td").parent("tr").find(".bug_id").val();
		var bugzillaId = $(this).parent("td").parent("tr").find(".bugzilla_id").text();

		if (confirm("Are you sure to delete bug \"" + bugzillaId + "\"?")) {
			_deleteBugById(id);
		}
//		evt.stopPropagation();
//		$("#bug_table tr").removeClass("selected");
//		$(this).parent("td").parent("tr").addClass("selected");
	});
	$("#bug_table th:not(:last)").click(function() {
		_cacheLastPage();
		var field = $(this).data("field");
		var order = "";
		if ($(this).hasClass("desc")) {
			$("#bug_table th").removeClass("desc");
			$(this).addClass("asc");
			order = "asc";
		} else if ($(this).hasClass("asc")) {
			$("#bug_table th").removeClass("asc");
			$(this).addClass("desc");
			order = "desc";
		} else {
			$("#bug_table th").removeClass("desc").removeClass("asc");
			$(this).addClass("asc");
			order = "asc";		
		}
		if(XUtil.getURLParameter("bugzilla_Id")!==null){
			_sortBugList(field, order);
		}
		else{
			_searchBugs(field, order);
		}
	});
	$("#product_select").bind("change", function() {
		_searchBugs("bugzillaId", "desc");
	});
	$(document).on("change", "select.symptom_op:visible", function() {
		if ($(this).val() == "OR") {
			$(this).siblings(".symptom_cmp,.scope_input,.symptom_unit,span").css("visibility", "hidden");
			$(this).siblings(".scope_input").val("0");
		} else {
			$(this).siblings(".symptom_cmp,.scope_input,.symptom_unit,span").css("visibility", "visible");
		}
	});
	$("#pager_setting input[type='text']").bind("keydown blur", function(evt) {
		if (evt.type == "blur" || evt.which == 13) {
			var validator = XUI.createXValidator("#pager_setting input[type='text']", "\\d+");
			if (validator.validate()) {
				var num_per_page = parseInt($(this).val());
				config.page_config.items_per_page = num_per_page;
				XUtil.pageXTable("#pagination", "#bug_table tr");
				$("#show_all").prop("checked", false);
			} else {
				$(this).val("");
			}
		}
	});
	$(document).on("focus", "#dialog .hardware_tree_input:visible,#dialog .software_tree_input:visible", function() {
		softwareTree.setIsEditable(isEditable);
		hardwareTree.setIsEditable(isEditable);
	});
	$(".xui_content_body:eq(0) .hardware_tree_input,.xui_content_body:eq(0) .software_tree_input").focus(function() {
		softwareTree.setIsEditable(true);
		hardwareTree.setIsEditable(true);
		isEditable = true;
	});
	$("#bug_table").on("click", ".test_bug", function(evt) {
		var bugzilla_id = $(this).attr("data-bugzillaId");
		var logic = $(this).attr("data-field");
		var model = $(this).attr('data-model');
		var ddos = $(this).attr('data-ddos');
		_testBugByLogic(bugzilla_id, logic, model, ddos);
//		evt.stopPropagation();
//		$("#bug_table tr").removeClass("selected");
//		$(this).parent("td").parent("tr").addClass("selected");
	});
	$("#bug_table").on("click", ".export_bug", function(evt) {
		var bug_id = $(this).parent("td").parent("tr").find(".bug_id").val();
		var bugzillaId = $(this).parent("td").parent("tr").find(".bugzilla_id").text();
		var uid = new Date().getTime();
		_exportBugInfo(bug_id, bugzillaId, uid);
//		evt.stopPropagation();
//		$("#bug_table tr").removeClass("selected");
//		$(this).parent("td").parent("tr").addClass("selected");
	});

	$(document).on("keydown", ".hardware_tree_input:visible,.software_tree_input:visible", function() {
		return false;
	});
	// $(document).on("autocompleteopen",".symptom_id:visible",function(evt,ui){
	// var items=$("ul.ui-autocomplete:visible li.ui-menu-item a");
	// for(var i=0;i<items.length;i++){
	// var id=$(items[i]).text().split(":")[0].trim();
	// var context=$(items[i]);
	// (function(id,context){
	// var controller="";
	// if(id.charAt(0)=='B')
	// controller="signature";
	// else
	// controller="symptoms";
	// $.get(controller+"/hint/"+id,function(data){
	// context.attr("title",data);
	// });
	// })(id,context);
	// }
	// });
	$(document).on("autocompleteselect", ".symptom_id:visible", function(evt, ui) {
		var context = $(this);
		var id = ui.item.sid;
		// var type=ui.item.type.split(":")[1].trim();
		var controller = "";
		if (id.charAt(0) == 'B')
			controller = "signature";
		else
			controller = "symptoms";
		$.get(controller + "/hint/" + id.substr(1), function(data) {
			context.attr("title", data);
			context.attr("sid", id);
		});
	});

//	$("#export_bug_definitions").button().click(
//			function() {
//				var date = new Date();
//				var taskId = date.getTime();
//				
//				monitorExportProgress("signature/exportbugs/"+taskId+"/Bug Summary [" + date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + "].xls",
//										"signature/exportbugs/progress/"+taskId, 
//										"signature/exportbugs/stop/"+taskId);
//				
//	});
	
	$("#export_bug_matches").button().click(
			function() {
				var loading_div = XUI.createXLoadingDiv();
				$.get("signature/description_for_all",function(data){
					var dialog_id = "bug_match_export";
					var html="<div id='"+dialog_id+"' style='height:250px;width:800px;overflow-y:scroll;border:1px #000000 solid;'>";
					for(var i=0;i<data.length;i++){
						var bugzilla_id = data[i].split(":")[0];
						var description = data[i].split(":")[1];
						html+="<input type='checkbox' name='"+bugzilla_id+"'>"+bugzilla_id+" - "+description+"<br>";
					}
				html+="</div>";
				html+="<div style='margin-top:5px;float:left'><input type='radio' name='match_radio' id='chkAll' onclick='$(\"#bug_match_export>input[type=checkbox]\").attr(\"checked\",true)'>select all<input type='radio' name='match_radio' id='clrAll' style='margin-left:10px' onclick='$(\"#bug_match_export>input[type=checkbox]\").attr(\"checked\",false)'>clear all";
//				html+="&nbsp;&nbsp;&nbsp;From:<input type=\"text\" id=\"date_from_input\" data-role=\"date\"/ onload='$(\"#date_from_input\").val(XUtil.timestampToString(new Date().getTime()-1000*60*60*24*182))'>To:<input type=\"text\" id=\"date_to_input\" data-role=\"date\" onload='$(\"#date_to_input\").val(XUtil.timestampToString(new Date().getTime()))'/>";
//				html+="<span id='exported_lines' style='margin-left:20px'></span><span id='export_message' style='margin-left:20px'></span>";
				html+="</div>";
				
				html+="<div id='bug_tabs' style='margin-top:5px;float:right;margin-right:10px'>";
				html+="<input type='checkbox' name='bug_definition'> Bug Definition </input>";
				html+="<input type='checkbox' name='bug_penetration'> Bug Penetration </input>";
				html+="<input type='checkbox' name='infected_systems'> Infected Systems </input>";
				html+="</div>"
				var dialog = XUI.createXDialog("Select Bug ID to export bug matches", html, 
						["Export"], 
						[function(){
							$("#exported_lines").text("");
							$("#export_message").text("");
							var bugids=[];
							var tabs=[];
							$("#"+dialog_id+" input[type=checkbox]").each(function(){
								if($(this).attr("checked")=="checked")
									bugids.push($(this).attr("name"));
							});
							$("#bug_tabs input[type=checkbox]").each(function(){
								if($(this).attr("checked")=="checked")
									tabs.push($(this).attr("name"));
							});
							if(bugids.length==0) {
								alert("Please select a bug");
								return false;
							}
							if(tabs.length==0){
								alert("Please select a tab");
								return false;
							}
							dialog.closeDialog();
							var date = new Date();
							var task_id = date.getTime();
							monitorExportProgress("signature/export_bug_matches/"+task_id+"/Bug Summary [" + date.getFullYear() + "-" + pad((date.getMonth() + 1),2) + "-" + pad(date.getDate(),2) + "].xls?bugzilla_ids="+bugids + "&tabs="+tabs,
													"signature/export_bug_matches/progress/"+task_id, 
													"signature/export_bug_matches/stop/"+task_id);
							
						}],true);
					loading_div.hideLoadingDiv();
				});
				
	});
}

function initShowBugDef() {
//	$("#bug_table tr:gt(0)").attr("title", config.page_config.show_bug_def);
	$("#bug_table").on("click", "tr:gt(1)", function(evt) {
		$("#bug_table tr").removeClass("selected");
		$(this).addClass("selected");
//		var bugzilla_id = $(this).find(".bugzilla_id").text();
//		window.open("bug_def_detail.html?bugzilla_id=" + bugzilla_id);
//		evt.stopPropagation();
	});
}

function initEditBugDef() {
	$("#bug_table").on(
			"click",
			".edit_bug",
			function(evt) {
				var bug_id = $(this).parent("td").parent("tr").find(".bug_id").val();
				var bug = _getBugById(bug_id);
				_showBugDialog("Edit Bug Definition", [ "Manage Symptom Definitions", "Save Changes", "Test Bug" ], [
						function() {
							_manageSymptoms();
						},
						function() {
							var saved_bug = _getBugByDialog();
							saved_bug.setId(bug_id);
							_saveOrUpdateBug(saved_bug);
						},
						function() {
							_testBugByLogic($("#dialog .bugzilla_input").val(), _getLogicByDialog(), $("#dialog .hardware_tree_input")
									.val(), $("#dialog .software_tree_input").val());
						} ], bug, true);
//				evt.stopPropagation();
//				$("#bug_table tr").removeClass("selected");
//				$(this).parent("td").parent("tr").addClass("selected");
			});
}

function _resetFields() {
	$("div.xui_content_body:eq(0) input[type='text']").val("").blur();
	$("div.xui_content_body:eq(0) select").val("-1");
}

function _showBugs(data) {
	bugs = [];
	$("#bug_table tr:gt(1)").remove();
	data.each(function(v, k) {
		var bug = new Bug(v);
		$("#bug_table tr:eq(1)").clone().appendTo($("#bug_table"));
		$("#bug_table tr:last .bugzilla_id").text(bug.getBugzillaId()).attr({
			"href" : bug.getLink(),
			"title" : "Link to bugzilla page."
		}).click(function(evt) {
//			evt.stopPropagation();
//			$("#bug_table tr").removeClass("selected");
//			$(this).parent("td").parent("tr").addClass("selected");
		});
		$("#bug_table tr:last .bug_id").val(bug.getId());
		var description = bug.getDescription();
		$("#bug_table tr:last .bug_description").attr({"title":description,"href":"manage_bug_def.html?bugID="+bug.getId(),'data-bugId':bug.getId()}).text(description.substr(0, 22) + "...");
		var category = bug.getCategory().getName();
		if(category==null) category="";
		$("#bug_table tr:last .bug_category").attr("title", category).text(category.substr(0, 15));
		var domain = bug.getDomain().getName();
		$("#bug_table tr:last .bug_domain").attr("title", domain).text(domain.substr(0, 15));
		var component = bug.getComponent().getName();
		$("#bug_table tr:last .bug_component").attr("title", component).text(component.substr(0, 15));
		var lifecycle = bug.getLifeCycleState().getState();
		$("#bug_table tr:last .bug_lifecycle").attr("title", lifecycle).text(lifecycle.substr(0, 15));
		var impact = bug.getImpact().getDescription();
		$("#bug_table tr:last .bug_impact").attr("title", impact).text(impact.substr(0, 15));
		if (bug.getRemediation().getKbaId()) {
			$("#bug_table tr:last .bug_remediation").text(bug.getRemediation().getKbaId()).attr("href", bug.getRemediation().getUrl())
					.click(function(evt) {
						if($.cookie("kba_never_remind")==undefined && $.cookie("kba_never_remind")!="true"){
							var url = $(this).attr("href");
							var html="<div id='kba_dialog' style='width:330px; margin-left:15px;margin-top:10px'>";
							html+="You are required to login to <a href='https://support.emc.com/' target=_blank>support.emc.com</a> first.";
							html+="</br></br>";
							html+="( I have already logined. Go to <a href='" + url +"' target=_blank>KBA page</a> directly.)";
							html+="</div>";
							html+="<div style='margin-top:25px;margin-left:15px;float:left'><input type='checkbox' id='never_remind'/>";
							html+="<label for='never_remind' style='margin-left:5px'>Never remind me</label></div>";
							
							var dialog = XUI.createXDialog("Link to KBAs", html, 
									["OK"], 
									[function(){
										if($("#never_remind").attr("checked")!=undefined){
											$.cookie("kba_never_remind","true");
										}
										dialog.closeDialog();
									}],true);
							return false;
						}
					});
		}
		var penetration =  bug.getPenetration();
		if(penetration==null) penetration="";
		var systemNum = penetration.substring(penetration.indexOf(":")+1 , penetration.lastIndexOf("/"));
		var zero = "0";
		var penetrationNum = penetration.substring(penetration.lastIndexOf("/")+1, penetration.lastIndexOf("%")+1);
		if((penetrationNum.charAt(0)=='.')) 
			penetrationNum = zero.concat(penetrationNum);
		$("#bug_table tr:last .bug_penetration").text(penetrationNum).attr("title", systemNum);
		$("#bug_table tr:last .test_bug").attr({
			"data-bugzillaId" : bug.getBugzillaId(),
			"data-field" : bug.getLogic(),
			"title" : "Test this bug.",
			'data-model' : bug.getModelNumber(),
			'data-ddos' : bug.getDdosVersion()
		});
		bugs.push(bug);
	});
	// XUtil.pageXTable("#pagination", "#bug_table
	// tr",config.page_config.items_per_page,lastPage);
	$("#show_all").change();

	loading_div = XUI.createXLoadingDiv();
	$.get("signature/quantity", function(data) {
		$("#selected_bug_num").text(bugs.length);
		$("#all_bug_num").text(data);
		$("#selected_bug_percent").text(parseInt(bugs.length * 100 / parseInt(data)) + "%");
		loading_div.hideLoadingDiv();
	});
	lastPage = 0;
}

function _getBugById(bug_id) {
	var bug = null;
	bugs.each(function(v, k) {
		if (v.getId() == bug_id) {
			bug = v;
		}
	});
	return bug;
}

function _deleteBugById(id) {
	$.ajax({
		type : "post",
		url : "signature/delete/" + id + "/" + sigVersion,
		success : function(data, status, xhr) {
			if (data) {
				XUI.createXDialog("Success", config.page_config.delete_bug_success, [], []);
				_searchBugs("bugzillaId", "asc");
			} else {
				XUI.createXDialog("Error", config.page_config.delete_bug_error, [], []);
			}
		},
		error : function(xhr) {
			XUI.createXDialog("Error", xhr.getResponseHeader("msg"), [], []);
		}
	});
}

function _manageSymptoms() {
	var symptom_arr = [];
	var symptom_inputs = $("#dialog .symptom_id:visible");
	for (var i = 0; i < symptom_inputs.length; i++) {
		if ($(symptom_inputs[i]).val() != "Input ID.") {
			symptom_arr.push($(symptom_inputs[i]).val().split(":")[0].trim());
		}
	}
	if (symptom_arr.length > 0) {
		window.open("manage_symptom_def.html?symptoms=" + symptom_arr.join(","));
	} else {
		window.open("manage_symptom_def.html");
	}
}

function _getBugByDialog() {
	var bug = new Bug();
	bug.setDescription($("#dialog .description_input").val());
	bug.setModelNumber($("#dialog .hardware_tree_input").val());
	bug.setDdosVersion($("#dialog .software_tree_input").val());
	var product = new Product();
	product.setId($("#product_select").val());
	bug.setProduct(product);
	var state = new LifeCycleState();
	state.setId($("#dialog .lifecycle").val());
	bug.setLifeCycleState(state);
	var category = new Category();
	category.setName($("#dialog .category_input").val());
	bug.setCategory(category);
	bug.setBugzillaId($("#dialog .bugzilla_input").val());
	var domain = new Domain();
	domain.setId($("#dialog .domain").val());
	bug.setDomain(domain);
	var component = new Component();
	component.setId($("#dialog .component").val());
	bug.setComponent(component);
	var impact = new Impact();
	impact.setId($("#dialog .impact").val());
	bug.setImpact(impact);
	bug.setPenetration($("#dialog .penetration_percentage").val());
	var remediation = new Remediation();
	remediation.setUrl($("#dialog .remediation_link").val());
	remediation.setKbaId($("#dialog .kba_input").val());
	bug.setRemediation(remediation);
	bug.setLink($("#dialog .identification_link").val());
	var logic = _getLogicByDialog();
	bug.setLogic(logic);
	return bug;
}

function _saveOrUpdateBug(bug) {
	var validatorManager = XUI.createXValidatorManager();
	validatorManager.addValidator(XUI.createXValidator(
			"textarea.description_input:visible,#dialog:visible .hardware_tree_input,#dialog:visible .software_tree_input", "\\w+"));
	// validatorManager.addValidator(XUI.createXValidator("textarea.category_input:visible","^(?!"+config.page_config.category_hint_text+")"));
	validatorManager.addValidator(XUI.createXValidator(
			"#dialog:visible .hardware_tree_input,#dialog:visible .software_tree_input,input.bugzilla_input:visible", "\\d+"));
	validatorManager.addValidator(XUI.createXValidator("#dialog:visible input.scope_input:visible", "\\d*"));
	validatorManager.addValidator(XUI.createXValidator("#dialog:visible .domain,#dialog:visible .component", "^(?!\-1)"));
	if (!validatorManager.validate()) {
		alert(config.page_config.fill_fields_error);
		return;
	}
	if (!_validateSymptomLogic())
		return;
	var symptoms = $("#dialog:visible input.symptom_id");
	for (var i = 0; i < symptoms.length; i++) {
		for (var j = 0; j < symptom_ids.length; j++) {
			if (symptom_ids[j].value.split(":")[0].trim() == $.trim($(symptoms[i]).val().split(":")[0])) {
				$(symptoms[i]).removeClass("x_error_field");
				break;
			} else {
				if (j == (symptom_ids.length - 1)) {
					alert(config.page_config.symptom_expression_error + "\nYou have input some unexisted symptom(s)");
					$(symptoms[i]).addClass("x_error_field");
					return;
				} else {
					continue;
				}
			}
		}
	}
	loading_div = XUI.createXLoadingDiv();
	if (sigVersion == undefined) {
		sigVersion = 0;
	}

	$.ajax({
		type : "post",
		url : "signature/saveOrUpdate/" + sigVersion,
		contentType : "application/json",
		data : JSON.stringify(bug),
		success : function(data, status, xhr) {
			if (data) {
				_searchBugs("bugzillaId", "asc");
				_initBugIdSelect();
				XUI.createXDialog("Success", config.page_config.save_bug_success, [], []);
				loading_div.hideLoadingDiv();
			} else {
				alert("[ERROR]: " + config.page_config.save_bug_error);
				// XUI.createXDialog("Error",
				// config.page_config.save_bug_error,[], []);
				// XUI.createXDialog("Error", xhr.getResponseHeader("msg"),[],
				// []);
				loading_div.hideLoadingDiv();
			}
		},
		error : function(xhr) {
			// XUI.createXDialog("Error", xhr.getResponseHeader("msg"), [],[]);
			alert("[ERROR]: " + xhr.getResponseHeader("msg"));
			loading_div.hideLoadingDiv();
		}
	});
}

function _cacheLastPage() {
	lastPage = parseInt($("#pagination span:not(.prev):eq(0)").text()) - 1;
}

function _validateSymptomExpr(expr) {
	var leftNum = 0;
	var rightNum = 0;
	for (var i = 0; i < expr.length; i++) {
		if (expr.charAt(i) == '(') {
			leftNum++;
		} else if (expr.charAt(i) == ')') {
			rightNum++;
		}
	}
	return leftNum == rightNum;
}

function _validateSymptomLogic() {
	var trJ = $('#dialog tr');
	var _validateParentheseMatch = function() {
		var total = 0;
		var leftPArr = $(".left_parenthesis:visible");
		var rightPArr = $(".right_parenthesis:visible");
		for (var i = 0; i < leftPArr.length; i++) {
			var leftNum = parseInt($(leftPArr[i]).attr("num"));
			var rightNum = parseInt($(rightPArr[i]).attr("num"));
			total = total + leftNum - rightNum;
			if (total < 0) {
				alert(config.page_config.symptom_expression_error);
				return false;
			}
		}
		// for(var i=0;i<rightPArr.length;i++){
		// var rightNum=parseInt($(rightPArr[i]).attr("num"));
		// rightP=rightP+rightNum;
		// }
		if (total > 0) {
			alert(config.page_config.symptom_expression_error);
			return false;
		}
		var validator = XUI.createXValidator("#dialog:visible .scope_input:visible", "\\d*");
		return true && validator.validate();
	};
	var _validateSymptomIdExit = function(inputJ) {
		var trimStr = $.trim(inputJ.val().split(":")[0]);
		if (trimStr == null || trimStr == '')
			return false;
		if (symptom_ids == undefined)
			return /^\d*$/.test(trimStr);
		for (var i = 0; i < symptom_ids.length; i++) {
			if (trimStr == symptom_ids[i].value.split(":")[0].trim())
				return true;
		}
		return false;
	};
	var _validateAllBugId = function() {
		var symptom_idsDom = trJ.find('.symptom_id');
		for (var i = 0; i < symptom_idsDom.length; i++) {
			var inputJ = $(symptom_idsDom[i]);
			if (!_validateSymptomIdExit(inputJ)) {
				if (!inputJ.hasClass('x_error_field')) {
					inputJ.addClass('x_error_field');
				}
				return false;
			} else {
				inputJ.removeClass('x_error_field');
			}
		}
		return true;
	};
	var _validateAllScope = function() {
		var scope_inputs = trJ.find('.scope_input');
		for (var i = 0; i < scope_inputs.length; i++) {
			var scopeJ = $(scope_inputs[i]);
			if (scopeJ.css('visibility') != 'hidden' && scopeJ.attr("readonly") != "readonly") {
				if (!/^(\d+)(\.\d*)?$/.test(scopeJ.val()) && scopeJ.val() != "") {
					if (!scopeJ.hasClass('x_error_field')) {
						scopeJ.addClass('x_error_field');
					}
					return false;
				} else {
					scopeJ.removeClass('x_error_field');
				}
			}
		}
		return true;
	};
	return _validateParentheseMatch() && _validateAllBugId() && _validateAllScope();
}

function _getLogicByDialog() {
	var symptom_rows = $("#dialog table tr:gt(10)");
	var logic = "";
	var scope = "";
	for (var i = 0; i < symptom_rows.length; i++) {
		var row = $(symptom_rows[i]);
		if (row.find("input.symptom_id").attr("sid") == undefined)
			return "";
		var num = parseInt(row.find("img.left_parenthesis").attr("num"));
		if (num != 0) {
			for (var j = 0; j < num; j++) {
				logic += "( ";
			}
		}
		// logic+=row.find("input.symptom_id").val().split(":")[0].trim()+" ";
		logic += row.find("input.symptom_id").attr("sid").charAt(0) == "B" ? "B" + row.find("input.symptom_id").val().split(":")[0].trim()
				+ " " : "S" + row.find("input.symptom_id").val().split(":")[0].trim() + " ";

		num = parseInt(row.find("img.right_parenthesis").attr("num"));
		if (num != 0) {
			// var num=parseInt(row.find("input.right_parent_num").val());
			for (var j = 0; j < num; j++) {
				logic += ") ";
			}
		}
		if (i != (symptom_rows.length - 1) && symptom_rows.length != 1) {
			logic += row.find("select.symptom_op").val() + " ";
			scope += "#" + row.find("select.symptom_cmp").val() + " " + row.find("input.scope_input").val() + " "
					+ row.find("select.symptom_unit").val();
		}
	}
	return logic + scope;
}

function _testBugByLogic(bugzillaId, logic, modelStr, ddosVer) {
	if ($("#dialog").is(":visible") && _validateSymptomLogic() == false) {
		alert(config.page_config.symptom_expression_error);
		return;
	}
	if ($("#dialog").is(":visible")
			&& (modelStr == config.page_config.hardware_input_default_text || ddosVer == config.page_config.software_input_default_text)) {
		alert("Please fill the hardware and software fields.");
		return;
	}
	var inputSnDialog = XUI
			.createXDialog(
					"Input Serial Number",
					config.page_config.test_bug_sn_prompt + "<br/><br/><input type='text' style='width:200px;'>",
					[ "OK" ],
					[ function() {
						var sn = $("#" + inputSnDialog.dialogID + " input").val();
						if (sn == "") {
							sn = null;
						}
						var uid = new Date().getTime();
						var sp = {};
						sp.bugzillaId = bugzillaId;
						sp.logic = logic;
						sp.product = $("#product_select").val();
						sp.serialNumber = sn;
						sp.modelStr = modelStr;
						sp.ddosVer = ddosVer;
						var taskId;
						$.ajax({
							type : "post",
							url : "signature/test/gettaskid",
							data : JSON.stringify(sp),
							contentType : 'application/json',
							success : function(data) {
								taskId = data;
								inputSnDialog.closeDialog();
								var monitor = monitorTestProgress("signature/test/progress/"+taskId, "signature/test/cancel/" + taskId);
								$.ajax({
									type : "post",
									url : "signature/test",
									data : JSON.stringify(sp),
									contentType : 'application/json',
									dataType : 'json',
									success : function(data) {
										var test_error = false;
										if (data == null || data == "") {
											clearTimeout(timeout);
											XUI.createXDialog("Error", "Some error happened on the server-side, please try again.", [], []);
											return false;
										}
										monitor.waitAndClose();
										var bug_num = data.countOfBug;
										var machine_num = data.countOfMachine;
										var html = "";
										if (bug_num > 1) {
											html = "There are " + bug_num + " bugs in " + machine_num + " system(s).";
										} else if (bug_num == 1) {
											html = "There is 1 bug in " + machine_num + " system(s).";
										} else if (bug_num == undefined) {
											return;
										} else {
											html = config.page_config.test_bug_error;
											test_error = true;
										}
										if (bug_num >= 1) {
											html += "<br/>Here is an example:<br/><textarea readonly cols='90' rows='13' wrap='off' style='overflow-y:auto;resize:none;width:800px;height:80%;'>"
													+ data.example + "</textarea>";
										}
										XUI.createXDialog("Test Result for Bug ID " + bugzillaId, html, [ "Export" ],
												[ function() {
													var date = new Date();
													monitorExportProgress("signature/exportBugTestResult/bug_id/0/uid/" + taskId + "/Bug "+bugzillaId+" Penetration ["+date.getFullYear()+"-"+pad((date.getMonth()+1),2)+"-"+pad(date.getDate(),2)+"].xls",
															"signature/exportBugTestResult/progress/"+taskId,
															"signature/exportbugs/stop/"+taskId);
													
												} ], true);
										if(test_error||data.bugId==undefined){
											$('button:contains("Export")').attr('disabled', 'disabled').css('color','#abcdef');
											if(data.bugId==undefined){
												$('button:contains("Export")').attr("title", "Please save the bug defintion before you can export the test result");
											}
										}
									},
									error : function(xhr) {
										XUI.createXDialog("Error", xhr.getResponseHeader("msg"), [], []);
										monitor.waitAndClose();
									}
								});
							},
							error : function(xhr){
								inputSnDialog.closeDialog();
								XUI.createXDialog("Error", xhr.getResponseHeader("msg"), [], []);
							}
						});
						
					} ], true);
	
	$("#" + inputSnDialog.dialogID + " input").autocomplete({"source": function(request, response) {
		var currentValue=request.term;
		if(currentValue==""){
			currentValue="-1";
		}
		
		$.get("scan/validSn/"+currentValue,function(data){
			response(data);
		});
	}});	

}

function _exportBugInfo(bug_id, bugzillaId, taskId) {
	var date = new Date();
	monitorExportProgress("signature/exportBugTestResult/bug_id/"+bug_id+"/uid/" + taskId + "/Bug "+bugzillaId+"Penetration ["+date.getFullYear()+"-"+pad((date.getMonth()+1),2)+"-"+pad(date.getDate(),2)+"].xls",
			"signature/exportBugTestResult/progress/"+taskId,
			"signature/exportbugs/stop/"+taskId);
	
}

function _testTimeout(taskId) {
	var html = config.page_config.test_bug_timeout_confirm.format({
		"time" : config.page_config.ajax_timeout_threshold / 1000
	});
	var dialog = XUI.createXDialog("Confirm Dialog", html, [ "Yes" ], [ function() {
		$.post("signature/test/cancel/" + taskId);
		dialog.closeDialog();
	} ], true);
	$("#" + dialog.dialogID).parent("div").find("button:eq(2) span").text("No");
	$("#" + dialog.dialogID).parent("div").css("z-index", "1000");
}
function _sortBugList(field, order){
	switch(field){
	case 'bugzillaId':
		bugList.sort(function(bug1, bug2){
			var a = bug1.bugzillaId;
			var b = bug2.bugzillaId;
			if(a==""||a==null) return -1;
			if(b==""||b==null) return 1;
			return a - b;
		});
		break;
	case 'description':
		bugList.sort(function(bug1, bug2){
			return bug1.description.toLowerCase().localeCompare(bug2.description.toLowerCase());
		});
		break;
	case 'category.name':
		bugList.sort(function(bug1, bug2){
			return bug1.category.name.toLowerCase().localeCompare(bug2.category.name.toLowerCase());
		});
		break;
	case 'domain.name':
		bugList.sort(function(bug1, bug2){
			return bug1.domain.name.toLowerCase().localeCompare(bug2.domain.name.toLowerCase());
		});
		break;
	case 'component.name':
		bugList.sort(function(bug1, bug2){
			return bug1.component.name.toLowerCase().localeCompare(bug2.component.name.toLowerCase());
		});
		break;
	case 'lifeCycleState.state':
		bugList.sort(function(bug1, bug2){
			return bug1.lifeCycleState.state.toLowerCase().localeCompare(bug2.lifeCycleState.state.toLowerCase());
		});
		break;
	case 'impact.descprition':
		bugList.sort(function(bug1, bug2){
			return bug1.impact.descprition.toLowerCase().localeCompare(bug2.impact.descprition.toLowerCase());
		});
		break;
	case 'remediation.kbaId':
		bugList.sort(function(bug1, bug2){
			var a = bug1.remediation.kbaId;
			var b = bug2.remediation.kbaId;
			if(a==""||a==null) return -1;
			if(b==""||b==null) return 1;
			return a - b;
		});
		break;
	case 'penetration':
		bugList.sort(function(bug1, bug2){
			var a = bug1.penetration;
			var b = bug2.penetration;
			if(a==""||a==null) return -1;
			if(b==""||b==null) return 1;
			a = _handlePenetration(a);
			b = _handlePenetration(b);
			return a - b;
		});
		break;
	}
	if(order=="desc") bugList.reverse();
	_showBugs(bugList);
}

function _handlePenetration(penetration){
	var zero = "0";
	penetration = penetration.substring(
			penetration.lastIndexOf("/") + 1, penetration.lastIndexOf("%"))
			.replace(/</, "");
	if((penetration.charAt(0)=='.')) 
		penetration = zero.concat(penetration);
	return penetration;
}

