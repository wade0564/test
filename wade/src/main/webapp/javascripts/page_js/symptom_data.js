var sdTree;
var symptom_datas=[];
var symptom_data;
var node;
var root;
var currentData;
var jsOnclick;
var currentIndex;  //dangerous way for storing index


$(function(){

	initPageUI();
	initEventCallbacks();
	
	//update page
	$.ajax({
		type: "post",
		url : "user/history/page",
		data : "manage_symptom_data.html"
	});
});


function initPageUI(){
	
	loading_div = XUI.createXLoadingDiv();
	
	$("#sd_table").css("visibility","hidden");
	$("#sd_path_info").css("visibility","hidden");

	$("#add").button();
	$("#save").button();
	$("#delete").button();
	this._initDomainSelect();
	
//	$("#sd_table_name").on("change",function(evt){
//		if($("#sd_table_name").val()==""||$("#sd_table_name").val()==null){
//			$("#sd_column_name,#sd_column_required").val(null);
//			$("#sd_column_name,#sd_column_required").attr("disabled","true");
//		}
//		else{
//			$("#sd_column_name,#sd_column_required").removeAttr("disabled");
//		}
//	});
//	
//	$("#sd_table_name").change();
//	
//	$("#sd_column_name,#sd_column_required").on("change",function(evt){
//		var typeSelect=$("#sd_data_type");
//		if(($("#sd_column_name").val()==""||$("#sd_column_name").val()==null)
//				&&($("#sd_column_required").val()==""||$("#sd_column_required").val()==null)){
//			
//			typeSelect.append("<option value='-1'> </option>");
//			typeSelect.val("-1");
//			typeSelect.attr("disabled","true");
//		}
//		
//		else{
//			typeSelect.find("option[value='-1']").remove();
//			typeSelect.removeAttr("disabled");
//		}
//	});
//	$("#sd_column_name,#sd_column_required").change();
	var context=this;

	
	$.get("symptomdata/getsymptomdatas",function(data){
		context._showSymptomData(data);
		loading_div.hideLoadingDiv();
	});
	
};

function initEventCallbacks(){
	$("#add").click($.proxy(this._add_symptom_data,this));
	$("#save").click($.proxy(this._save_symptom_data,this));
	$("#delete").click($.proxy(this._delete_symptom_data,this));
	
	_initTypeSelector("#sd_type");
//	$("#sd_type").change(function(){
//		var selected=$("select option:selected");
//		if(selected.text()!="variable"){
//			$("#sd_type").val("");
//			$("#sd_type").attr("disabled","true");
//		}
//		else{
//			$("#sd_type").removeAttr("disabled");
//		}
//	});
	
};


function _initDomainSelect(){
	
	$("#sd_data_type").append("<option value='-1'>N/A</option>");
	$("#sd_data_type").append("<option value='integer'>integer </option>");
	$("#sd_data_type").append("<option value='string'>string </option>");
	
	
	$("#sd_type").append("<option value='symptom data'>symptom data </option>");
	
	$("#sd_type").append("<option value='section'>section </option>");
	$("#sd_type").append("<option value='variable'>variable </option>");
};

function _initTypeSelector(selector){
	$(document).on("change",selector,function(){
		
		if($(this).val()=="symptom data"){
			_disableAll();
		}
		
		else{
			_enableAll();
		}
//		var typeSelect=$("#sd_data_type");
//		if($(this).val()!="variable"){
//			typeSelect.append("<option value='-1'> </option>");
//			typeSelect.val("-1");
//			typeSelect.attr("disabled","true");
//		}
//		else{
//			typeSelect.find("option[value='-1']").remove();
//			typeSelect.removeAttr("disabled");
//		}
	});
	
	$(selector).change();
};


function _showSymptomData(data){
	
	for(var c=0;c<data.length;c++){
		var sd=new SymptomData(data[c]); 
		symptom_datas.push(sd);
		if(data[c].id=="0"){
			root=new SymptomData(data[c]);
		}
	}
	if(root==undefined){
		console.log("Can't find root!");
	}
	
	
	_initJstree(true);
};

jsOnclick=function jsTreeOnclick(e,data){
	var nodeId=data.instance.get_node(data.selected).id;
	//tricks to handle root node
	if(isNaN(nodeId)){
		nodeId=0;
	}
	for(var i=0;i<symptom_datas.length;i++){
		if(nodeId==symptom_datas[i].getId()){
			
			currentData=symptom_datas[i];
			currentIndex=i;
			_displaySymptomData(symptom_datas[i]);
			break;
		}
	}		
};



function _buildJstree(symptom_data,node){
	for(var i=0;i<symptom_data.getChildren().length;i++){
		for(var j=0;j<symptom_datas.length;j++){
			if(symptom_datas[j].getId()==symptom_data.getChildren()[i]){
				var childNode=new Node(symptom_datas[j]);				
				childNode=_buildJstree(symptom_datas[j],childNode);
				node.children.push(childNode);
				break;
				
			}
		}
	}
	
	return node;
};

function _initJstree(flag){
	
	$('#tree').jstree("destroy");
	symptom_data=root;
	node=new Node(symptom_data);
	node.text="Symptom Data";
	node.id=0;
	
	var rootNode=_buildJstree(symptom_data,node);
	
	
	
	$('#tree')
	.bind("loaded.jstree", function (e, data) { 
                    data.instance.open_all(0); // -1 opens all nodes in the container 
                    })
//    .bind("hover_node.jstree",function(e,data){
//    	var node=$(".jstree-hovered")[0];
//    	node.title="Click for detail";
////    	data.node.a_attr.title="Click for detail";
////    	data;
//    	
////    	alert(data.node.title);
//    })
	.jstree({
	    'core' : {
	        'data' : rootNode,
	        'multiple':false
	    	},
	    'ui':{
	    	'initially_select' : 1
	    },
	    'plugins':["sort"]
	    })
	.on('changed.jstree', jsOnclick);
	
                    		
//	$("#tree").jstree('select_node',0);
	
};



function _displaySymptomData(symptom_data){
	_showAllComponent();
	if(symptom_data.id==0){
		$("#save").css("visibility", "hidden");
		$("#add").css("visibility" , "visible");
		var sd=new SymptomData();
		sd.setId(0);
		sd.setName("Symptom Data Root Node");
		_fillAttr(sd);
		$("#sd_name,#sd_type,#sd_table_name,#sd_column_name,#sd_column_required,#sd_comment,#sd_data_type").attr("disabled" ,"true");
		
	}
	else{
		
		$("#save").css("visibility", "visible");
		$("#add").css("visibility" , "visible");
		$("#sd_name,#sd_type,#sd_table_name,#sd_column_name,#sd_column_required,#sd_comment,#sd_data_type").removeAttr("disabled");
		if(symptom_data.getType()=="variable"){
			$("#add").css("visibility" , "hidden");
		}
		_fillAttr(symptom_data);

	}
	if(symptom_data.getChildren().length>0){
		$("#delete").css("visibility", "hidden");
	}
	else{
		$("#delete").css("visibility", "visible");
	}
	
	$("#sd_column_name,#sd_column_required").change();
	$("#sd_table_name").change();
};

function _add_symptom_data(){
	$("#add").css("visibility","hidden");
	$("#delete").css("visibility","hidden");
	//handle root node
	$("#sd_name,#sd_type,#sd_table_name,#sd_column_name,#sd_column_required,#sd_comment,#sd_data_type").removeAttr("disabled");
	$("#save").css("visibility","visible");
	var newSD=new SymptomData();
	_fillAttr(newSD);
	$("#sd_parent").val(currentData.getId());
};

function _save_symptom_data(){
	
	var sd=_getSymptomDataForSave();
	if(sd!=undefined){
		if(_validateDup(sd)){
			XUI.createXDialog("Error","Duplicated names in the same parent node",[],[]);
			return;
		}
		if(!_validateSD()){
			XUI.createXDialog("Error","Required fields should be filled",[],[]);
			return;
		}
		loading_div=XUI.createXLoadingDiv();
		$.ajax({
			type:"post",
			url: "symptomdata/save",
			contentType : "application/json",
			data : JSON.stringify(sd),
			success : function(data, status, xhr){
				var update=false;
				var index;
				for(var i=0;i<symptom_datas.length;i++){
					if(data.id==symptom_datas[i].getId()){
						update=true;
						index=i;
						break;
					}
				}
				if(update){
					symptom_datas[index]=new SymptomData(data);
					
				}
				else{
					var newsd=new SymptomData(data);
					symptom_datas.push(newsd);
					symptom_datas[currentIndex].addChild(newsd.getId());
					if(currentIndex==0||symptom_datas[currentIndex]==root){
						root.addChild(newsd.getId())
					}
				}
				
				_initJstree();
				$("#sd_table").css("visibility","hidden");
				$("#sd_path_info").css("visibility","hidden");
				$("#add,#save,#delete").css("visibility","hidden");
				loading_div.hideLoadingDiv();
				XUI.createXDialog("Success",config.page_config.save_symptom_data_success,[],[]);
				
			},
			error :function(xhr){
				loading_div.hideLoadingDiv();
				XUI.createXDialog("Error", xhr.getResponseHeader("msg"), [], []);
			}
		});
		
	}
	
};

function _delete_symptom_data(){
	if(currentData==undefined||currentData==null){
		alert("Symptom Data Not Found");
	}
	var sdId=currentData.getId();
	var spath=_getPath();
	if (confirm("Are you sure to delete symptom data \"" + spath + "\"?")) {
		loading_div=XUI.createXLoadingDiv();
		$.ajax({
			type :"post",
			url: "symptomdata/delete/"+ sdId,
			success :function (data,status, xhr){
				if(data){
					for(var i=0;i<symptom_datas.length;i++){
						if(currentData.getId()==symptom_datas[i].getId()){
							symptom_datas.splice(i,1);
							break;
						}
					}
					
					for(var i=0;i<symptom_datas.length;i++){
						if(currentData.getParent()==symptom_datas[i].getId()){
							var children=symptom_datas[i].getChildren();
							for(var j=0;j<children.length;j++){
								if(children[j]==currentData.getId()){
									symptom_datas[i].getChildren().splice(j,1);
									break;
								}
							}
							break;
						}
					}
					
					_initJstree();
					$("#sd_table").css("visibility","hidden");
					$("#sd_path_info").css("visibility","hidden");
					$("#add,#save,#delete").css("visibility","hidden");
					loading_div.hideLoadingDiv();
					XUI.createXDialog("Success",config.page_config.delete_symptom_data_success,[],[]);
				}
//				else{
//					loading_div.hideLoadingDiv();
//					XUI.createXDialog("Error", xhr.getResponseHeader("msg"), [], []);
//				}
			},
			error :function(xhr){
				xhr.responseText="";
				loading_div.hideLoadingDiv();
				XUI.createXDialog("Error", xhr.getResponseHeader("msg"), [], []);
			}
		});
		
	}
	
};
function _fillAttr(symptom_data){
	$("#sd_name").val(symptom_data.getName());
	$("#sd_type").val(symptom_data.getType());
	$("#sd_table_name").val(symptom_data.getTable());
	$("#sd_column_name").val(symptom_data.getColumnTargeted());
	$("#sd_column_required").val(symptom_data.getColumnRequired());
	$("#sd_comment").val(symptom_data.getComment());
	$("#sd_data_type").val(symptom_data.getDataType());
	$("#sd_parent").val(symptom_data.getParent());
	$("#sd_id").val(symptom_data.getId());
	$("#sd_type").change();
	
	if(symptom_data.getDeduplication()==true){
		$("#dedup_yes").attr("checked","checked");
	}
	else if(symptom_data.getDeduplication()==false){
		$("#dedup_no").attr("checked","checked");
	}	
	if(symptom_data.getSymptomType()=="NON_LOG_ENTRY"){
		$("#log_no").attr("checked","checked");
		
	}
	else if(symptom_data.getSymptomType()=="LOG_ENTRY"){
		$("#log_yes").attr("checked","checked");
	}
	var totalPath=_getPath()+":";
	
	$("#sd_path").html(totalPath);
};

function _getSymptomDataForSave(){
	var sd=new SymptomData();
	sd.setName($("#sd_name").val()==""?null:$("#sd_name").val());
	sd.setType($("#sd_type").val());
	
	sd.setParent($("#sd_parent").val());
	//handle vitual parent
	if(sd.getParent()=="0"){
		sd.setParent(null);
	}
	sd.setId($("#sd_id").val());
	if(sd.getType()=="symptom data"){		
		return sd;
	}
	sd.setTable($("#sd_table_name").val()==""?null:$("#sd_table_name").val());
	sd.setColumnTargeted($("#sd_column_name").val()==""?null:$("#sd_column_name").val());
	sd.setColumnRequired($("#sd_column_required").val()==""?null:$("#sd_column_required").val());
	sd.setComment($("#sd_comment").val())==""?null:$("#sd_comment").val();
	sd.setDataType($("#sd_data_type").val()==-1?null:$("#sd_data_type").val());
	
	
	
	if($("#dedup_yes").is(":checked")){
		sd.setDeduplication(true);
	}
	else if ($("#dedup_no").is(":checked")){
		sd.setDeduplication(false);
	}
	else{
		sd.setDeduplication(null);
	}
	if($("#log_yes").is(":checked")){
		sd.setSymptomType("LOG_ENTRY");
	}
	else if($("#log_no").is(":checked")){
		sd.setSymptomType("NON_LOG_ENTRY");
	}
	else{
		sd.setSymptomType(null);
	}
	return sd;
};
function _showAllComponent(){
//	$("#sd_name,#sd_type,#sd_table_name,#sd_column_name,#sd_column_required,#sd_comment,#sd_data_type,#add,#save,#delete").css("visibility","visible");
	$("#sd_table").css("visibility","visible");
	$("#sd_path_info").css("visibility","visible");
	$("#sd_name,#sd_table_name,#sd_column_required,#sd_column").removeClass("x_error_field");
};

function _validateDup(sd){
	for(var i=0;i<currentData.getChildren().length;i++){
		var childId=currentData.getChildren()[i];
		for(var j=0;j<symptom_datas.length;j++){
			if(childId==symptom_datas[j].getId()){
				if(sd.getName()==symptom_datas[j].getName()){
					return true;
				}
				break;
			}
		}
	}
	return false;
};

function _validateSD(sd){
	var validatorManager = XUI.createXValidatorManager();
	validatorManager.addValidator(XUI.createXValidator(
			"#sd_name", "\\w+"));
	
	if($("#sd_type").val()=="variable"){
		validatorManager.addValidator(XUI.createXValidator(
				"#sd_column_required,#sd_column_name,#sd_table_name", "\\w+"));
	}
	
	if($("#sd_type").val()=="variable"){
		validatorManager.addValidator(XUI.createXValidator(
				"#sd_column_required,#sd_column_name,#sd_table_name", "\\w+"));
	}


	else if(!($("#sd_table_name").val()=="" && $("#sd_data_type").val()==-1
			&& $("#sd_column_required").val()=="" && $("#sd_column_name").val()==""
				&& !$("#log_no").is(":checked") && !$("#log_yes").is(":checked")
				&& !$("#dedup_no").is(":checked")&& !$("#dedup_yes").is(":checked"))){
		validatorManager.addValidator(XUI.createXValidator(
				"#sd_column_required,#sd_column_name,#sd_table_name","\\w+"));
	}
	else{
		return true;
	}
	
	return validatorManager.validate()&& _validateNoInput()  ;
};

function _validateNoInput(){
	if(($("#log_no").is(":checked")||$("#log_yes").is(":checked"))
			&& ($("#dedup_no").is(":checked") || $("#dedup_yes").is(":checked"))
			&& ($("#sd_data_type").val()!=-1)){
		return true;
	}
	else return false;
};


function _getPath(){
	var path=[];
	
	data=currentData;
	if(data.getName()==null){
		return "";
	}
	path.push(data.getName());
	for(;data.getParent()!=null;){		
		var pid=data.getParent();
		for(var i=0;i<symptom_datas.length;i++){
			if(pid==symptom_datas[i].getId()){
				path.push(symptom_datas[i].getName());
				data=symptom_datas[i];
				break;
			}
		}
	}
	var spath="";
	for(;path.length>1;){
		spath+=path.pop()+".";
	}
	spath+=path.pop();
	return spath;
};

function _disableAll(){
	$("#sd_table_name,#sd_column_name,#sd_column_required,#sd_comment,#sd_data_type").attr("disabled" ,"true");
	$("#dedup_yes,#dedup_no,#log_yes,#log_no").attr("disabled","true");
	$("#dedup_yes,#dedup_no,#log_yes,#log_no").removeAttr("checked");
};

function _enableAll(){
	$("#sd_table_name,#sd_column_name,#sd_column_required,#sd_comment,#sd_data_type," +
			"#dedup_yes,#dedup_no,#log_yes,#log_no").removeAttr("disabled");
}

