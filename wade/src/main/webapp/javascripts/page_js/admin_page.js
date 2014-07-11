var	loading_div;

$(function(){
	AdminPage.initPageUI();
	AdminPage.initEventCallBacks();
	AdminPage.initPhotoLists();
	
	// update page
	$.ajax({
		type : "post",
		url : "user/history/page",
		data: "manage_users_and_roles.html"
	});
});

var AdminPage={
	initPageUI:function(){
		loading_div=XUI.createXLoadingDiv();
		XUI.createXHintableInput("#emp_search",config.page_config.search_emp_default_text);
		$("#search_emp").button();
		$("#save_emp").button();
		$("img.delete").attr({"align":"top","title":config.page_config.delete_photo_tooltip});
		this._resetFields();
	},
	initEventCallBacks:function(){
		$("#search_emp").click(function(){
			var emp=null;
			if($("#emp_search").val()!=config.page_config.search_emp_default_text && $("#emp_search").val()!="" 
				&& AdminPage._validateEmployeeFormat($("#emp_search").val())){
				emp=$("#emp_search").val();
				$(".tips").text("");
				AdminPage._searchEmployee(emp);
			}
			else{
				$(".tips").text(config.page_config.search_emp_tooltip);
			}
		});
		$("#save_emp").click(function(){
			AdminPage._saveChanges();
		});
		$("img.delete").live("click",function(){
			if(!confirm(config.page_config.confirm_delete_photo)){
				return false;
			}
			$(this).parent("li").animate({"opacity":"0"},300,function(){
				$(this).hide(300);
			});
			var index=$("ul.photo_list").index($(this).parent("li").parent("ul.photo_list"));
			var roleID=config.page_config.role2_id;
			if(index==0){
				roleID=config.page_config.role1_id;
			}
			var userID=$(this).siblings(".photo_id").val();
			AdminPage._removeRoleFromUser(roleID,userID);
		});
		$("img.photo").attr("title",config.page_config.photo_hint).live("click",function(){
			var userID=$(this).siblings(".photo_id").val(); 
			AdminPage._searchEmployee(userID);
			$("body").animate({scrollTop:0},"slow");
			document.documentElement.scrollTop=0;
		});
	},
	initPhotoLists:function(){
		this._getRole1();
		this._getRole2();
	},
	_searchEmployee:function(emp){
		this._resetFields();
		$("#emp_search").val(emp);
		$("img.loadingGif").show();
		if (!isNaN(emp) && emp.length < 10) {
			$.ajax({
				type : "get",
				url : "users/" + emp,
				dataType : "json",
				success : function(data) {
					$("img.loadingGif").hide();
					if(data==null){
						$(".tips").text(config.page_config.search_emp_null);
					}
					else{
						$("#name_result").val(data.name);
						$("#email_result").val(data.email);
						var roles_arr = data.roleIds;
						$("#role"+ config.page_config.role1_id).attr("checked",false);
						$("#role"+ config.page_config.role2_id).attr("checked",false);
						
						for ( var i = 0; i < roles_arr.length; i++) {
							$("#role" + roles_arr[i]).attr("checked", "checked");
						}
						$("#id_hidden").val(data.id);
						$("#ntAccount_hidden").val(data.ntAccount);
					}
				}, 
				error : function(data) {
					$("img.loadingGif").hide();
					$(".tips").text(config.page_config.search_emp_null);
				}
			});
		}
		else{
			$.ajax({
				type : "get",
				url : "users/find/" + emp,
				dataType : "json",
				success : function(data) {
					$("img.loadingGif").hide();
					if(data.users.length==0){
						$(".tips").text(config.page_config.search_emp_null);
					}
					else{
						var html="<select id='name_select' size='5'>";
						for(var i=0;i<data.users.length;i++){
							if(i==0){
								html+="<option value='"+data.users[i].id+"' selected>"+data.users[i].name+" ( "+data.users[i].email+" )</option>";																
							}
							else{
								html+="<option value='"+data.users[i].id+"'>"+data.users[i].name+" ( "+data.users[i].email+" )</option>";								
							}
						}
						html+="</select>";
						XUI.createXDialog(
							"Select an employee ... ",
							html,
							["OK"],
							[function(){
							 	var emp=$(".ui-dialog-content select#name_select").val();
							 	AdminPage._searchEmployee(emp);
							 	$("#emp_search").val(emp);
							 	$(this).dialog("destroy");
							 }]	
						);
					}
				}
			});
		}
	},
	_saveChanges:function(){
		if($("#id_hidden").val()==""){
			$(".tips").text(config.page_config.save_emp_null);
			return;
		}
		var user=new User();
		user.id=$("#id_hidden").val();
		user.ntAccount=$("#ntAccount_hidden").val();
		user.name=$("#name_result").val();
		user.email=$("#email_result").val();
		user.roles=[];
		if($("#role"+ config.page_config.role1_id).attr("checked")!=undefined){
			var role={};
			role.id=config.page_config.role1_id;
			user.roles.push(role);
		}
		if($("#role"+ config.page_config.role2_id).attr("checked")!=undefined){
			var role={};
			role.id=config.page_config.role2_id;
			user.roles.push(role);
		}
		loading_div=XUI.createXLoadingDiv();
		$.ajax({
			type:"put",
			url:"users/"+user.getId(),
			contentType:"application/json",
			data:JSON.stringify(user),
			success:function(data){
				if($("#id_hidden").val()==$.cookie("user.id")){
					$.get(document.location.protocol+"//"+document.location.host+"/plgs/j_spring_security_logout",function(){
						XUI.createXDialog("Information","Your role has been changed and the page will reload in seconds.");
						document.location.reload();
					});
				}
				else{
					AdminPage._getRole1();
					AdminPage._getRole2();
					AdminPage._resetFields();
				}
				loading_div.hideLoadingDiv();
			}
		});
	},
	_resetFields:function(){
		$("#name_result").val("");
		$("#email_result").val("");
		$(".tips").text("");
		$("#emp_search").val(config.page_config.search_emp_default_text);
		$("#id_hidden").val("");
		$("#ntAccount_hidden").val("");
		$("#role"+ config.page_config.role1_id).attr("checked",false);
		$("#role"+ config.page_config.role2_id).attr("checked",false);
	},
	_getRole1:function(){
		$("ul.photo_list:eq(0) li:gt(0)").remove();
		$("img.loading_role1").show();
		$.ajax({
			type:"get",
			url:"roles/"+config.page_config.role1_id,
			dataType:"json",
			success : function(data) {
				var user_arr = data.users;
				if (user_arr.length != 0) {
					for ( var i = 0; i < user_arr.length; i++) {					
						var name = user_arr[i].name;
						var pic="image/"+user_arr[i].id;
						$("ul.photo_list:eq(0) li:eq(0)").clone().appendTo(
								$("ul.photo_list:eq(0)"));
						$("ul.photo_list:eq(0) li:eq(" + (i+1) + ") .photo").attr("src",pic);
						$("ul.photo_list:eq(0) li:eq(" + (i+1) + ") .photo_name").text(
								name);
						$("ul.photo_list:eq(0) li:eq(" + (i+1) + ") .photo_id").val(
								user_arr[i].id);
						var update_time="N/A";
						if(user_arr[i].updateTime!=null){
							update_time=GLOBAL.timestampToString(user_arr[i].updateTime);
						}
						$("ul.photo_list:eq(0) li:eq(" + (i+1) + ") .photo_visit_date").text(
								update_time);
						var last_page=user_arr[i].lastPage;
						if(last_page==null){
							last_page="N/A";
						}
						$("ul.photo_list:eq(0) li:eq(" + (i+1) + ") .photo_visit_url").text(
								last_page);
					}
					$("img.loading_role1").hide();
					$("ul.photo_list:eq(0) li:gt(0)").show();
				}
			}
		});
	},
	_getRole2:function(){
		$("ul.photo_list:eq(1) li:gt(0)").remove();
		$("img.loading_role2").show();
		loading_div=XUI.createXLoadingDiv();
		$.ajax({
			type:"get",
			url:"roles/"+config.page_config.role2_id,
			dataType:"json",
			success:function(data){
				var user_arr=data.users;
				if (user_arr.length != 0) {
					for ( var i = 0; i < user_arr.length; i++) {
						var name = user_arr[i].name;
						var pic="image/"+user_arr[i].id;
						$("ul.photo_list:eq(1) li:eq(0)").clone().appendTo(
								$("ul.photo_list:eq(1)"));
						$("ul.photo_list:eq(1) li:eq(" + (i+1) + ") .photo").attr("src",pic);
						$("ul.photo_list:eq(1) li:eq(" + (i+1) + ") .photo_name").text(
								name);
						$("ul.photo_list:eq(1) li:eq(" + (i+1) + ") .photo_id").val(
								user_arr[i].id);
						var update_time="N/A";
						if(user_arr[i].updateTime!=null){
							update_time=GLOBAL.timestampToString(user_arr[i].updateTime);
						}
						$("ul.photo_list:eq(1) li:eq(" + (i+1) + ") .photo_visit_date").text(
								update_time);
						var last_page=user_arr[i].lastPage;
						if(last_page==null){
							last_page="N/A";
						}
						$("ul.photo_list:eq(1) li:eq(" + (i+1) + ") .photo_visit_url").text(
								last_page);
					}
				}
				$("img.loading_role2").hide();
				$("ul.photo_list:eq(1) li:gt(0)").show();
				loading_div.hideLoadingDiv();
			}
		});
	},
	_removeRoleFromUser:function(roleID,userID){
		$.ajax({
			type : "delete",
			url : "roles/"+roleID+"/user/"+userID,
			success:function(){
				if(userID==$.cookie("user.id")){
					$.get(document.location.protocol+"//"+document.location.host+"/plgs/j_spring_security_logout",function(){
						XUI.createXDialog("Information","Your role has been changed and the page will reload in seconds.");
						document.location.reload();
					});
				}
			}
		});
	},
	
	 _validateEmployeeFormat:function(tmp){
		voidChar = "@.";
		for(var i=0;i<voidChar.length;i++){
			aChar = voidChar.substring(i, i+1);
			if(tmp.indexOf(aChar)>-1)
				return false;
		}
		return true;
	}
};