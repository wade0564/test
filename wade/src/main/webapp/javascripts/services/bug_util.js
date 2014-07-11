var symptom_ids;
var softwareTree;
var hardwareTree;
var bugs;
var loading_div;
var lastPage=0;
var ops=["ANDNOT","ORNOT","XORNOT","THENNOT","AND","OR","XOR","THEN"];
var isEditable=true;
var bugId;
var sigVersion;

function _addSwTreeNode(verObj, nodes) {
	var version_arr=verObj.version.split(".");
	var current_parent="";
	for(var j=0;j<(version_arr.length-1);j++){
		if(j==0){
			current_parent=version_arr[j];		
		}
		else{
			current_parent+="."+version_arr[j];
		}
		if(nodes[current_parent]==undefined){
			nodes[current_parent]=XUI.createXTreeNode(current_parent,null);
			if(j==0){
				softwareTree.addRoot(nodes[current_parent]);
			}
		}
		var current_child=current_parent+"."+version_arr[j+1];
		if(nodes[current_child]==undefined){
			if(current_child==verObj.version){
				nodes[current_child]=XUI.createXTreeNode(current_child,verObj.id);
			}
			else{
				nodes[current_child]=XUI.createXTreeNode(current_child,null);							
			}
			nodes[current_parent].addChild(nodes[current_child]);
		}
	}	
}

function _initTreeWidgets() {
	var storage=XUI.createXLocalStorage();
	XUI.createXHintableInput(".software_tree_input:visible:last",
			config.page_config.software_input_default_text);
	XUI.createXHintableInput(".hardware_tree_input:visible:last",
			config.page_config.hardware_input_default_text);
	if (softwareTree == undefined || hardwareTree == undefined) {
		$.get("signature/swddosVersion", function(data) {
			softwareTree = XUI.createXTreeMenu({
				input : ".software_tree_input",
				div : "#software_tree_div",
				tree : "#software_tree",
				selectAll : "#software_tree_select_all",
				clearAll : "#software_tree_clear_all",
				ok : "#software_tree_ok_btn"
			});
			//optimized, using map data structure
			var nodes = {};
			for ( var i = 0; i < data.length; i++) {
				_addSwTreeNode(data[i], nodes);
			}
			var soft_tree_cache=storage.getItem("software_tree");
			var data_cached=storage.getItem("software_tree_version");
			if(JSON.stringify(data)==JSON.stringify(data_cached)){
				$("#software_tree").html(soft_tree_cache);
				$("#software_tree").jstree({
					"plugins" : [ "themes", "html_data", "ui","checkbox","sort" ]
				});
			}
			else{
				softwareTree.initTreeUI();
				setTimeout(function(){storage.setItem("software_tree",$("#software_tree").html());});	
				setTimeout(function(){storage.setItem("software_tree_version",data);});
			}
			softwareTree.enableEventCallBacks();	
		});
		$.get("signature/hwmodelnumber", function(data) {
			hardwareTree = XUI.createXTreeMenu({
				input : ".hardware_tree_input",
				div : "#hardware_tree_div",
				tree : "#hardware_tree",
				selectAll : "#hardware_tree_select_all",
				clearAll : "#hardware_tree_clear_all",
				ok : "#hardware_tree_ok_btn"
			});
			for ( var i = 0; i < data.length; i++) {
				hardwareTree.addRoot(XUI.createXTreeNode(data[i].number,
						data[i].id));
			}
			var hard_tree_cache=storage.getItem("hardware_tree");
			var data_cached=storage.getItem("hardware_tree_version");
			if(JSON.stringify(data)==JSON.stringify(data_cached)){
				$("#hardware_tree").html(hard_tree_cache);
				$("#hardware_tree").jstree({
					"plugins" : [ "themes", "html_data", "ui","checkbox","sort" ]
				});
			}
			else{
				hardwareTree.initTreeUI();
				setTimeout(function(){storage.setItem("hardware_tree",$("#hardware_tree").html());});
				setTimeout(function(){storage.setItem("hardware_tree_version",data);});
			}
			hardwareTree.enableEventCallBacks();			
		});
	} else {
//		softwareTree.initTreeUI();
		softwareTree.enableEventCallBacks();
//		hardwareTree.initTreeUI();
		hardwareTree.enableEventCallBacks();
	}
}

function _initSymptomAutocomplete() {
	if (symptom_ids != undefined) {
		var symptom_id_input = XUI.createXHintableInput(
				".symptom_id:not(.ui-autocomplete-input)", "Input ID.");
		symptom_id_input.autocomplete(symptom_ids);
//		symptom_id_input.setAutocompleteEvent("autocompleteselect", function(
//				evt, ui) {
//			$(this).val(ui.item.label);
//		});
	} else {
		var symptom_id_input = XUI.createXHintableInput(
				".symptom_id:not(.ui-autocomplete-input)", "Input ID.");
		loading_div = XUI.createXLoadingDiv();
		var filter={};
		filter.id="";
		filter.description="";
		filter.source_table="";
		filter.regex="";
		$.getJSON("symptoms/pattern/field/id/order/asc", filter,function(data) {
			symptom_ids = [];
			for ( var i = 0; i < data.length; i++) {
				var symptom_id = {};
				symptom_id.label = data[i].id + " : " + data[i].description;
				symptom_id.value = data[i].id+ " : " + data[i].description;
				symptom_ids.push(symptom_id);
			}
			symptom_id_input.autocomplete(symptom_ids);
//			symptom_id_input.setAutocompleteEvent("autocompleteselect",
//					function(evt, ui) {
//						$(this).val(ui.item.label);
//					});
			loading_div.hideLoadingDiv();
		});
	}
}

function _initSymptomAndBugAutocomplete() {
	if (symptom_ids != undefined) {
		var symptom_id_input = XUI.createXHintableInput(
				".symptom_id:not(.ui-autocomplete-input)", "Input ID.");
		symptom_id_input.autocomplete(symptom_ids);
	} else {
		var symptom_id_input = XUI.createXHintableInput(
				".symptom_id:not(.ui-autocomplete-input)", "Input ID.");
		loading_div = XUI.createXLoadingDiv();
		$.getJSON("signature/definition_refs",function(data) {
			symptom_ids = [];
			for ( var i = 0; i < data.length; i++) {
				var symptom_id = {};
				var id=data[i].id.substr(1);
				symptom_id.label = id + " : " + data[i].description;
				symptom_id.sid = data[i].id;
//				symptom_id_input.attr("reftype",data[i].type);
				symptom_id.value = id + " : " + data[i].description;
				symptom_ids.push(symptom_id);
			}
			symptom_id_input.autocomplete(symptom_ids);
			loading_div.hideLoadingDiv();
		});
	}
}

function _initCategoryAutocomplete(){
	var category_input = XUI.createXHintableInput(".category_input","");
	loading_div = XUI.createXLoadingDiv();
	$.getJSON("signature/category", function(data) {
		var category_ids = [];
		for ( var i = 0; i < data.length; i++) {
			var category_id = {};
			category_id.label = data[i].name;
			category_id.value = data[i].name;
			category_ids.push(category_id);
		}
		category_input.autocomplete(category_ids);
		category_input.setAutocompleteEvent("autocompleteselect", function(evt, ui) {
			$(this).val(ui.item.value);
		});
		loading_div.hideLoadingDiv();
	});
}

function _initComponentSelect(selector,reqDomain,component) {
	 var jComponent = $(selector)
	$.get("signature/component/"+reqDomain, function(data) {
		for ( var i = 0; i < data.length; i++) {
			jComponent.append(
					"<option value='" + data[i].id + "'>" + data[i].name
							+ "</option>");
		}
	}).complete(function(){
		jComponent.val(component)
	});
}

function _initDomainSelect(selector) {
	$.get("signature/domain", function(data) {
		for ( var i = 0; i < data.length; i++) {
			$(selector).append(
					"<option value='" + data[i].id + "'>" + data[i].name
							+ "</option>");
		}
	});
	$(document).on("change",selector,function(){
		var componentSelect=$(selector).parent("td").parent("tr").parent("tbody").parent("table").find("#component_select,.component");
		if($(this).val() == -1){
			componentSelect.find("option[value!='-1']").remove();
			return;
		}
		$.get("signature/component/"+$(this).val(),function(data){
			componentSelect.find("option[value!='-1']").remove();
			data.each(function(v,k){
				componentSelect.append("<option value='"+v.id+"'>"+v.name+"</option>");
			});
		});
	});
}

function _initStateSelect(selector) {
	$.get("signature/state", function(data) {
		for ( var i = 0; i < data.length; i++) {
			$(selector).append(
					"<option value='" + data[i].id + "'>" + data[i].state
							+ "</option>");
		}
	});
}

function _initImpactSelect(selector) {
	$.get("signature/impact", function(data) {
		for ( var i = 0; i < data.length; i++) {
			$(selector).append(
				"<option value='" + data[i].id + "' title='" + data[i].details + "'>" + data[i].description + "</option>");
		}
	});
}

function _showBugDialog(title, buttons, callbacks, bug, bool) {
	isEditable=bool;
	$("#bug_edit_div .symptom_id").removeClass("ui-autocomplete-input")
			.removeAttr("autocomplete");
	XUI.createXDialog(title, $("#bug_edit_div").html(), buttons, callbacks);
	_initTreeWidgets();
//	_initSymptomAutocomplete();
	_initSymptomAndBugAutocomplete();
	_initCategoryAutocomplete();
	$("#dialog .bugzilla_input").bind('propertychange keyup input paste',function(){
		$("#dialog .identification_link").val(config.page_config.bugzilla_url_prefix+$(this).val());
		resizeOneTextArea($("#dialog .identification_link"));
	});
	if (bug != null && bug != undefined) {
		$("#dialog .description_input").val(bug.getDescription());
		$("#dialog .category_input").val(bug.getCategory().getName());
		$("#dialog .bugzilla_input").val(bug.getBugzillaId());
		$("#dialog .kba_input").val(bug.getRemediation().getKbaId());
		$("#dialog .domain").val(bug.getDomain().getId());
		//$("#dialog .domain").change();
		//$("#dialog .component").val(bug.getComponent().getId());
		_initComponentSelect('#dialog .component',bug.getDomain().getId(),bug.getComponent().getId());
		$("#dialog .lifecycle").val(bug.getLifeCycleState().getId());
		$("#dialog .impact").val(bug.getImpact().getId()).attr('title', bug.getImpact().description);
//		$("#dialog .penetration_percentage").val(bug.getPenetration());
		$("#dialog .identification_link").val(bug.getLink());
		$("#dialog .remediation_link").val(bug.getRemediation().getUrl());
		$("#dialog .hardware_tree_input").val(bug.getModelNumber());
		$("#dialog .software_tree_input").val(bug.getDdosVersion());
		
		//all dupes link		
		$.each(bug.getDupes(),function(index,value){
			var dupe=value;
			var dupehref="https:\/\/bugzilla.datadomain.com\/bugzilla\/show_bug.cgi?id="+dupe;
			var target=" target=\"_blank\"";
			var title=" title=\"Link to bugzilla\"";
			var $link=$("<a href="+dupehref+ target+title+">"+dupe+"</a>");
			var $span=$("<span> </span>");
		$("#dialog .dupes_input").append($link).append($span);
		});
		
		
		if(bug.getLogic()!=null){
			var logic_arr=bug.getLogic().split("#");
			//set cope information
			for(var i=0;i<logic_arr.length;i++){			
				if(i>=1){
					$("#dialog img.add_symptom:eq("+(i-1)+")").click();
					var row=$("#dialog table tr:eq("+(10+i)+")");
					var scope_arr=logic_arr[i].split(" ");
					row.find("select.symptom_cmp").val(scope_arr[0]);
					row.find("input.scope_input").val(scope_arr[1]);
					row.find("select.symptom_unit").val(scope_arr[2]);
				}
			}
			//set operation expression information
			var op_arr=logic_arr[0].split(" ");
			var line_num=11;
			var regex=new RegExp("^\\w?\\d+$");
			for(var i=0;i<op_arr.length;i++){
				var row=$("#dialog table tr:eq("+line_num+")");
				if(op_arr[i]=="("){
//					var left_num=row.find(".left_parent_num");
//					left_num.val(parseInt(left_num.val())+1).blur();
					row.find(".left_parenthesis").trigger({
					    type: 'mousedown',
					    which: 1
					});
				}
				else if(regex.test(op_arr[i])){
					var symIdInput=row.find("input.symptom_id");
					symIdInput.val(op_arr[i].substr(1));
					symIdInput.attr("sid",op_arr[i]);
					(function(input,id){
						var controller="";
						if(id.charAt(0)=='B')
							controller="signature";
						else
							controller="symptoms";
						id=id.substr(1);
						$.get(controller+"/hint/"+id,function(data){
							input.attr("title",data);
							if(typeof symptom_ids=="undefined"){
								setTimeout(arguments.callee,10);
							}
							else{
								for(var i=0;i<symptom_ids.length;i++){
									var symptom=symptom_ids[i];
									if(symptom.value.split(":")[0].trim()==id){
										input.val(symptom.label);
										row.find("input.symptom_id").attr("sid",op_arr[i]);
									}
								}
							}
						});
					})(symIdInput,op_arr[i]);
				}
				else if(ops.indexOf(op_arr[i])!=-1){
					line_num++;
					if(op_arr[i]=="OR"){
						row.find(".symptom_cmp,.scope_input,.symptom_unit,span").css("visibility","hidden");
					}
					row.find("select.symptom_op").val(op_arr[i]);
				}
				else if(op_arr[i]==")"){
//					var right_num=row.find(".right_parent_num");
//					right_num.val(parseInt(right_num.val())+1).blur();
					row.find(".right_parenthesis").trigger({
					    type: 'mousedown',
					    which: 1
					});
				}
			}
		}
		$("#dialog input,#dialog select").css("color","#222");
		$("#dialog").attr("title", "");
		
	}
	XUtil.disableUIElement($("#dialog .identification_link,#dialog .penetration_percentage"));
	if(!isEditable){
		$("#dialog .left_parenthesis").attr("title", "");
		$("#dialog .right_parenthesis").attr("title", "");
		XUtil.disableUIElement($("#dialog input[type='text']:not(.hardware_tree_input):not(.software_tree_input)"),
				$("#dialog select"), $("#dialog img.add_symptom"),$("#dialog img.delete_symptom"));
		$("#dialog .right_parenthesis,#dialog .left_parenthesis").mousedown(function(){
			return false;
		});
	}
	resizeAllTextArea();
}

function _searchBugs(field, order) {
	$("#bug_table").show();
	$("#page_tip").hide();
	$("#bug_table th").removeClass("desc").removeClass("asc");
	$("#bug_table th[data-field='"+field+"']").addClass(order);
	var hardware = $(".hardware_tree_input:eq(0)").val();
	if (hardware == config.page_config.hardware_input_default_text) {
		hardware = "";
	}
	var software = $(".software_tree_input:eq(0)").val();
	if (software == config.page_config.software_input_default_text) {
		software = "";
	}
	var domain = $("#domain_select").val();
	var component = $("#component_select").val();
	var impact = $("#impact_select").val();
	var state = $("#lifecyle_select").val();
	var product=$("#product_select").val();
	//TODO
	if(product == null || product == '')product = 1;
	var id=$("#id_select").val();
	loading_div = XUI.createXLoadingDiv();
	$.get("signature/search/" + field + "/" + order, {
		hardware : hardware,
		software : software,
		domain : domain,
		component : component,
		impact : impact,
		state : state,
		product:product,
		id:id
	}, function(data,status,xhr) {
		sigVersion=xhr.getResponseHeader("sig_version");
		loading_div.hideLoadingDiv();
		_showBugs(data);
	});
}

function _searchBugByBugId(id){
	loading_div = XUI.createXLoadingDiv();
	$.get("signature/search/id/desc", {
		hardware : "",
		software : "",
		domain : "-1",
		component : "-1",
		impact : "-1",
		state : "-1",
		product:1,
		id:id
	}, function(data,status,xhr) {
		sigVersion=xhr.getResponseHeader("sig_version");
		//loading_div.hideLoadingDiv();
		//_showBugs(data);
		var bug;
		data.each(function(v, k) {
			bug = new Bug(v);
			return;
		});
		_showBugDialog("Show Bug Definition", [], [], bug,false);
	});
}

function _getBugByBugId(id){
	loading_div = XUI.createXLoadingDiv();
	$.get("signature/"+id, function(data,status,xhr) {
		sigVersion=xhr.getResponseHeader("sig_version");
		//loading_div.hideLoadingDiv();
		//_showBugs(data);
		var bug = new Bug(data);
		_showBugDialog("Show Bug Definition", [], [], bug,false);
	});
}

$(document).keyup(function(e) {
	if (e.keyCode == 27) { // Esc
		$("#software_tree_div,#hardware_tree_div").hide();
	}
});
