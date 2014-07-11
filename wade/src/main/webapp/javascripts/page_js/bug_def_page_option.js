var isIE=/*@cc_on!@*/false; //shitty IE

function initOptionUI(){
	$("#reset_btn").button();
	$("#customize_layout").button().hide();
	$("#reset_option").button();
	$("#save_option").button();
	
	$("#save_option").attr("title", config.page_config.save_option_info);
	$(".slide_up").attr("title",config.page_config.slideup_tooltip);
	
	initEventCallBacks();
	_initColumnOption();
}

function _initColumnOption(){
	$("#option_board input").prop("checked", false);
//	$.removeCookie("user");
	setTimeout(GLOBAL.checkUserCookieReady,200);
	
	
}

CALLBACK={
	callback:function(){
		var user = new User(JSON.parse($.cookie("user")));
		var temp = user.getOptions();
		var isExists = false;
		if(temp.length>0){		
			for(var i=0;i<temp.length;i++){
				if(temp[i].getOptionId()=="2"){
					var options = temp[i].getValue().replace(/_/g," ").trim().split(" ");
					if(options.length==0){
						_resetOptionsToDefault();
					}
					else{
						for(var j=0;j<options.length;j++){
							$("#" + options[j]).prop("checked", true);
						}
						_showSelectedColumns();
						isExists = true;
					}
					break;
				}
			}
		}
		if(!isExists) _resetOptionsToDefault();	
	}	

};

function _resetOptionsToDefault(){
	$("#option_board input[disabled!=disabled]").prop("checked", false);
	$("#option_board input[disabled=disabled]").prop("checked", true);
	$(".optional_domain,.optional_component,.optional_lifecycle,.optional_impact").prop("checked", true);
	_showSelectedColumns();
}

function initEventCallBacks(){
	$("#customize_layout").click(function(){
		if($("#option_board").is(":visible")){
			if(!isIE){
				$("#option_board").slideUp();							
			}
			else{
				$("#option_board").hide();
			}
		}
		else{
			if(!isIE){
				$("#option_board").slideDown();							
			}
			else{
				$("#option_board").show();
			}
		}
		
	});
	
	$("#option_board input").click(function(){
		//validate num(up to 8, total 10) 
		if($("#option_board input:checked").size()>8){
			XUI.createXDialog("Error", config.page_config.select_option_error, [], []);
			$(this).prop("checked", false);
			return;
		}
		var index = $(this).attr("id").match(/\d+/g);
		if($(this).attr("checked")!=undefined){
			$("#bug_table th:eq(" + (index-1) + ")").show();
			$("#bug_table tr").find("td:eq(" + (index-1) + ")").show();
		}
		else{
			$("#bug_table th:eq(" + (index-1) + ")").hide();
			$("#bug_table tr").find("td:eq(" + (index-1) + ")").hide();
		}
	});
	
	$("#reset_option").click(function(){
		_resetOptionsToDefault();
	});
	
	$("#save_option").click(function(){
		var loading_div = XUI.createXLoadingDiv();
		var value = "";
		$("#option_board input:checked").each(function(){
			value = value.concat($(this).attr("id")).concat("_");
		});
		var userId = $.cookie("user.id");
		var optionId = "2";
		$.removeCookie("user");
		$.ajax({
			type : "put",
			url : "users/" + userId + "/options/" + optionId,
			data : value,
			success : function(data, status, xhr) {
				loading_div.hideLoadingDiv();
				if (data) {
					XUI.createXDialog("Success", config.page_config.save_option_success, [], []);
					$.cookie("user",JSON.stringify(data));
				} else {
					XUI.createXDialog("Error", config.page_config.save_option_error, [], []);
				}
			},
			error : function(xhr) {
				loading_div.hideLoadingDiv();
				XUI.createXDialog("Error", xhr.getResponseHeader("msg"), [], []);
			}
		});

	});
	
	$(".slide_up").click(function(){
		if($("#option_board").is(":visible")){
			if(!isIE){
				$("#option_board").slideUp();							
			}
			else{
				$("#option_board").hide();
			}
		}
	});
	
}

function _showSelectedColumns() {
	$("#option_board input:checked").each(function(){
		var index = $(this).attr("id").match(/\d+/g);
		if(index!=null){
			$("#bug_table th:eq(" + (index-1) + ")").show();
			$("#bug_table tr").find("td:eq(" + (index-1) + ")").show();
		}
	});
	$("#option_board input:not(:checked)").each(function(){
		var index = $(this).attr("id").match(/\d+/g);
		if(index!=null){
			$("#bug_table th:eq(" + (index-1) + ")").hide();
			$("#bug_table tr").find("td:eq(" + (index-1) + ")").hide();
		}
	});
}