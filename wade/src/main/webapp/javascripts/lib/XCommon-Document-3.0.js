//COM.EMC.UTIL.requireCSS("stylesheets/lib/redmond/jquery-ui-1.10.1.custom.css");

$(function(){
	$("#download").button().click(function(){
		document.location.href="";
	});
	$(".xui_content_body>div").hide();
	var ui=COM.EMC.UI;
	ui.configUI({
		menu_hover_font_color:"#EEE",
		title:"XCommon Doc",
		head_image : "images/head_image.png",
		head_text_system : "Documents and Examples",
		loading_image:"images/loadingPage.gif",
		head_text_application : "XCommon",
		foot_info : "This website is best viewed using Chrome, Firefox and IE 9+ web browsers.<br/>",
		rights_info : "Copyright 2013 EMC.<br/>"
	});
	var menubar=ui.createMenuBar();
	ui.createPage().addMenuBar(menubar);
	var m1=ui.createMenuItem("UI framework");
	var m2=ui.createMenuItem("Util functions");
	var m3=ui.createMenuItem("Date framework");
	var m4=ui.createMenuItem("Test framework");
	var m5=ui.createMenuItem("Validation framework");
	var m6=ui.createMenuItem("Extensions of jQuery");
	menubar.addMenuItems(m1,m2,m3,m4,m5,m6);
	menubar.setWelcomeInfo();
	m1.addClickListener(function(){
		$(".xui_content_body>div").hide();
		$("#uiDoc").show();
		$(".xui_title_bar").text("UI framework");
		var arr=$(".xui_url_dir>span").text().split(">");
		arr[1]="UI framework";
		$(".xui_url_dir>span").text(arr[0]+">"+arr[1]);
	});
	m2.addClickListener(function(){
		$(".xui_content_body>div").hide();
		$("#utilDoc").show();
		$(".xui_title_bar").text("Util functions");
		var arr=$(".xui_url_dir>span").text().split(">");
		arr[1]="Util functions";
		$(".xui_url_dir>span").text(arr[0]+">"+arr[1]);
	});
	m3.addClickListener(function(){
		$(".xui_content_body>div").hide();
		$("#dateDoc").show();
		$(".xui_title_bar").text("Date framework");
		var arr=$(".xui_url_dir>span").text().split(">");
		arr[1]="Date framework";
		$(".xui_url_dir>span").text(arr[0]+">"+arr[1]);
	});
	m4.addClickListener(function(){
		$(".xui_content_body>div").hide();
		$("#testDoc").show();
		$(".xui_title_bar").text("Test framework");
		var arr=$(".xui_url_dir>span").text().split(">");
		arr[1]="Test framework";
		$(".xui_url_dir>span").text(arr[0]+">"+arr[1]);
	});
	m5.addClickListener(function(){
		$(".xui_content_body>div").hide();
		$("#validationDoc").show();
		$(".xui_title_bar").text("Validation framework");
		var arr=$(".xui_url_dir>span").text().split(">");
		arr[1]="Validation framework";
		$(".xui_url_dir>span").text(arr[0]+">"+arr[1]);
	});
	m6.addClickListener(function(){
		$(".xui_content_body>div").hide();
		$("#extensionsDoc").show();
		$(".xui_title_bar").text("Extensions of jQuery");
		var arr=$(".xui_url_dir>span").text().split(">");
		arr[1]="Extensions of jQuery";
		$(".xui_url_dir>span").text(arr[0]+">"+arr[1]);
	});
	$("#indexDoc").show();
	//asynchronized loading jstree plugin
	COM.EMC.UTIL.requireJS("javascripts/lib/jquery.jstree.js",function(){
		var root1=ui.createTreeNode("1","1"); //create tree node
		var root2=ui.createTreeNode("2","2");
		var node1=ui.createTreeNode("1.1","1.1");
		var node2=ui.createTreeNode("1.2","1.2");
		root1.addChildren(node1, node2); 
		//bind HTML selector with TreeList
		var tree = ui.createTreeList({
			input : "#tree_input",
			div : "#tree_div",
			tree : "#tree",
			selectAll : "#select_all",
			clearAll : "#clear_all",
			ok : "#ok_btn",
			themePath:"javascripts/lib/themes/"
		});
		//put tree datastructure into tree list ui
		tree.addRoot(root1);
		tree.addRoot(root2);
		//init UI
		tree.initTreeUI();
		tree.enableEventCallBacks(); 
	});
});