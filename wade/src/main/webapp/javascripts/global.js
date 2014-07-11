$(function(){
	testAxm();
	
	var START_TIME = (new Date()).getTime();
	
	//init params
	XUI.initUI(config.ui_config);
	//create widgets
	var page=XUI.createXPage();
	var menubar=XUI.createXMenuBar();
	var item_customer=XUI.creatXMenuItem("Customer Support","customer");
	var item_subject=XUI.creatXMenuItem("Subject Matter Expert","subject");
	var item_admin=XUI.creatXMenuItem("Administrator","admin");
	var item_customer_scan=XUI.creatXMenuItem("Scan Symptom Data","customer_scan");
	var item_customer_query_sub=XUI.creatXMenuItem("Query Symptom Data Status","customer_querySub");
	var item_subject_bug=XUI.creatXMenuItem("Manage Bug Definitions","subject_bug");
	var item_subject_symptom=XUI.creatXMenuItem("Manage Symptom Definitions","subject_symptom");
	var item_subject_symptomdata=XUI.creatXMenuItem("Manage Symptom Data","subject_symptomdata");
	var item_admin_manage=XUI.creatXMenuItem("Manage Users and Roles","admin_manage");
	var item_admin_profile=XUI.creatXMenuItem("Manage Profile","admin_profile");
	var item_admin_prescan=XUI.creatXMenuItem("Manage System Configuration","admin_config");
	var item_admin_parse_sub=XUI.creatXMenuItem("Parse SUB File","admin_parse_sub");
	//menu
	menubar.addMenuItem(item_customer);
	menubar.addMenuItem(item_subject);
	menubar.addMenuItem(item_admin);
	page.addMenuBar(menubar);
	//submenus
	item_customer.setSubMenuItems([item_customer_scan,item_customer_query_sub]);
	item_subject.setSubMenuItems([item_subject_bug,item_subject_symptom,item_subject_symptomdata]);
	item_admin.setSubMenuItems([item_admin_manage,item_admin_profile,item_admin_prescan,item_admin_parse_sub]);
	//menu click handler
	item_customer_scan.addClickListener("scan_symptom_data.html");
	item_customer_query_sub.addClickListener("query_sub.html");
	item_admin_manage.addClickListener("manage_users_and_roles.html");
	item_subject_symptom.addClickListener("manage_symptom_def.html");
	item_subject_symptomdata.addClickListener("manage_symptom_data.html");
	item_subject_bug.addClickListener("manage_bug_def.html");
	item_admin_profile.addClickListener("admin_profile.html");
	item_admin_prescan.addClickListener("admin_config.html");
	item_admin_parse_sub.addClickListener("parse_sub.html");
	//disable ajax cache
	$.ajaxSetup({cache:false});
	updateEnviironment();
	//welcome info
	updateWelcomeInfo(menubar);
	$(document).ajaxError(function(evt,xhr,opts){
		if (XUI.loadingDiv != null) {
			XUI.loadingDiv.hideLoadingDiv();
		}
		
        if(xhr.status==403){
        	document.location.href="error.html";
        } 
        else if(xhr.status==0 && xhr.statusText=="error" && xhr.responseText==""){
        	if ((new Date()).getTime() - START_TIME > 600000) {
        		// do ignore timeout message if less then 10 minutes since the page is opened.
	            XUI.createXDialog("ERROR", 
	            		"RSA Access Manager Session Timeout!" +
	            		"<br />Please press F5 to relogin!",[],[]);
        	}
        }
        else if(String(xhr.status).charAt(0)=='5'){
        	//comment: if backend response 500,we can't show all server error message to user
            XUI.createXDialog("ERROR","Server side error.",[],[],true);
        }
    });

});

var GLOBAL={
		timestampToString : function(timestamp) {
			var date = new Date(timestamp);
			var year = date.getFullYear();
			var month = date.getMonth() <= 8 ? "0" + (date.getMonth() + 1) : ""
					+ (date.getMonth() + 1);
			var day = date.getDate() <= 9 ? "0" + date.getDate() : ""
					+ date.getDate();
			return  year + '-' + month + '-' + day;
		},
		validateFields:function(regex_arr){
			for(var selectors in regex_arr){
				var regex=new RegExp(regex_arr[selectors]);
				var fields=$(selectors);
					
				for(var i=0;i<fields.length;i++){
					if(regex.test($(fields[i]).val())){
						$(fields[i]).removeClass("error_field");
					}
					else{
						$(fields[i]).addClass("error_field");
						return false;
					}
				};
			}
			return true;
		},
		addComma:function(num){
			if(isNaN(num)) return num;
			
			re = /(\d{1,3})(?=(\d{3})+(?:$|\.))/g;
			return num.toString().replace(re,"$1,");
		},
		 checkUserCookieReady:function(){
			if($.cookie("user") !=undefined){
				CALLBACK.callback();
				return;
			}
			setTimeout(GLOBAL.checkUserCookieReady, 200);
		}
	};

function updateWelcomeInfo(menubar){		
	$.ajax({
		type : "get",
		url : "session/user",
		dataType:"json",
		success : function(data) {
			if (data != null) {
				var user =JSON.stringify(data);
				$.cookie("user.id", data.id);
				$.cookie("user.name", data.name);
				$.cookie("user.ntaccount", data.ntAccount);
				$.cookie("user.email", data.email);
				$.cookie("user.lastPage", data.lastPage);
				$.cookie("user",user);
			} else {
				$.removeCookie('user.id');
				$.removeCookie('user.name');
				$.removeCookie('user.ntaccount');
				$.removeCookie('user.email');
				$.removeCookie('user.lastPage');
				$.removeCookie('user');
			}
			menubar.setWelcomeInfo($.cookie("user.name"));
		},
		error:function(xhr){
//			XUtil.checkSession(xhr);
		}
	});

	//sample
//	EMC.prometheus.reqMap.session.user({
//		type : "get",
//		url : "session/user",
//		success : function(data) {
//			if (data != null) {
//				$.cookie("user.id", data.id);
//				$.cookie("user.name", data.name);
//				$.cookie("user.ntaccount", data.ntAccount);
//				$.cookie("user.email", data.email);
//				$.cookie("user.lastPage", data.lastPage);
//			} else {
//				$.removeCookie('user.id');
//				$.removeCookie('user.name');
//				$.removeCookie('user.ntaccount');
//				$.removeCookie('user.email');
//				$.removeCookie('user.lastPage');
//			}
//			menubar.setWelcomeInfo($.cookie("user.name"));
//			console.log('load session user success');
//		}
//	});
//	EMC.prometheus.reqMap.session.getUserById({
//		url: '/session/2',
//		success:function(data){
//			console.log('load session getUserbyId success');
//		}
//	});
//	EMC.prometheus.reqMap.session.complexUrlTest({
//		success:function(data){
//			console.log('load session complexUrlTest success');
//		}
//	});
};

function updateEnviironment() {
	$.ajax({
		type : "get",
		url : "system/environment",
		success : function(data) {
			// update page name
			XUI.currentPage.setTitle(data.name);
			
			// update title
			XUI.currentPage.setBannerText("Support Central Version " + data.version);
		}
	});	
};

function testAxm() {
	$.ajax({
		type:'get',
		url: 'login/axm/status', 
		error: function(evt,xhr,opts) {
			// reload page if it is timeout.
			if(evt.status==0 && evt.statusText=="error" && evt.responseText==""){
		    	window.location.reload(true);
		    }
		}
	});    
};



