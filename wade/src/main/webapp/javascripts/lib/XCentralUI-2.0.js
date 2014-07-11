var XUI={
	loadingDiv: null,
	configParams:new XConfigParams(),
	initUI:function(configs){
		for(var name in this.configParams){
			this.configParams[name] = (configs !== undefined && configs[name] !== undefined) ? configs[name] : this.configParams[name];
		}
	},
	createXPage:function(){
		this.currentPage=new XPage(this.configParams);
		return this.currentPage;
	},
	createXMenuBar:function(){
		this.currentMenuBar=new XMenuBar(this.configParams);
		return this.currentMenuBar;
	},
	creatXMenuItem:function(item_name,id){
		return new XMenuItem(item_name,id,this.configParams);
	},
	createXHintableInput:function(selector,default_text,hint_color){
		return new XHintableInput(selector,default_text,hint_color);
	},
	createXDialog:function(title,html,button_text_arr,button_callback_arr,isNew){
		return new XDialog(this.configParams,title,html,button_text_arr,button_callback_arr,isNew);
	},
	createXSimpleDialog:function(title,html,button_text_arr,button_callback_arr,isNew){
		return new XSimpleDialog(this.configParams,title,html,button_text_arr,button_callback_arr,isNew);
	},
	createXLoadingDiv:function(){
		XUI.loadingDiv = new XLoadingDiv(this.configParams);
		return XUI.loadingDiv;
	},
	createXValidatorManager:function(){
		return new XValidatorManager();
	},
	createXValidator:function(selector,regex){
		return new XValidator(selector,regex);
	},
	createXTreeNode:function(data,value){
		return new XTreeNode(data,value);
	},
	createXTreeMenu:function(selectors){
		return new XTree(selectors);
	},
	createXLocalStorage:function(){
		return new XLocalStorage();
	},
	createXSessionStorage:function(){
		return new XSessionStorage();
	},
	createXClipBoard:function(){
		return new XClipBoard();
	}
};

var XUtil={
	/*
	 * convert timestamp to a yyyy-mm-dd format
	 * */
	timestampToString : function(timestamp,concat_date,concat_time) {
		var date = new Date(timestamp);
		var year = date.getFullYear();
		var month = date.getMonth() <= 8 ? "0" + (date.getMonth() + 1) : ""
				+ (date.getMonth() + 1);
		var day = date.getDate() <= 9 ? "0" + date.getDate() : ""
				+ date.getDate();
		var concat="-";
		if(concat_date!=undefined){
			concat=concat_date;
		}
		var result=year + concat + month + concat + day;
		if(concat_time!=undefined && concat_time!=""){
			var hour=date.getHours()<=9?"0"+date.getHours():""+date.getHours();
			var min=date.getMinutes()<=9?"0"+date.getMinutes():""+date.getMinutes();
			result+=" "+hour+concat_time+min;
		}
		return result;
	},
	/*
	 * make <table> pageable, pagination.js is needed to import.
	 * */
	pageXTable:function(pagination_selector,table_tr_selector,items_per_page,current_page){
		$(table_tr_selector+":not(.bug_info_row):eq(1)").hide();
		var size=$(table_tr_selector+":not(.bug_info_row):gt(1)").length;
		var item_num=items_per_page;
		if(items_per_page==undefined){
			item_num=config.page_config.items_per_page;
		}
		var current=0;
		if(current_page!=undefined){
			current=current_page;
		}
		$(pagination_selector).pagination(size, {
			items_per_page:item_num,
			num_display_entries:config.page_config.num_display_entries,
			current_page:current,
			callback:function(page,jq){
				$(table_tr_selector+":not(.bug_info_row):gt(1)").hide();
				var max_elem = Math.min((page+1) * item_num, size);
				for(var i=page*item_num;i<max_elem;i++){
					$(table_tr_selector+":not(.bug_info_row):eq("+(i+2)+")").show();
				}
				if(typeof resizeAllTextArea!=="undefined"){
					resizeAllTextArea();					
				}
			}
		});
	},
	/*
	 * get url parameter
	 * */
	getURLParameter:function(key){
		var reg = new RegExp("(^|&)" + key + "=([^&]*)(&|$)", "i");
	    var r = window.location.search.substr(1).match(reg);
	    if (r != null) return unescape(r[2]); return null;
	},
	/*
	 * disable the UI element
	 */
	disableUIElement:function(){
		for(var i=0;i<arguments.length;i++){
			var node=arguments[i];
			switch(node.get(0).tagName){
			case "IMG":node.css("visibility","hidden");break;
			case "INPUT":node.attr("readonly","readonly");break;
			case "TEXTAREA":node.attr("readonly","readonly");break;
			default:node.attr("disabled","disabled");break;
			}	
		}
	},
	/*
	 * enable the UI element
	 */
	enableUIElement:function(){
		for(var i=0;i<arguments.length;i++){
			var node=arguments[i];
			switch(node.get(0).tagName){
			case "IMG":node.css("visibility","visible");break;
			case "INPUT":node.removeAttr("readonly");break;
			case "TEXTAREA":node.removeAttr("readonly");break;
			default:node.removeAttr("disabled");break;
			}
		}
	},
	/*
	 * generate uid
	 */
	guid:function(){
		return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g,function(c){
			var r=Math.random()*16|0,v=c=='x'?r:(r&0x3|0x8);
			return r.toString(16);
		}).toUpperCase();
	},
	/*
	 * validate user session
	 */
	checkSession:function(xhr){
		if(xhr.status == 0 && xhr.statusText == "error" && xhr.responseText == ""){
			XUI.createXDialog("Error","Your session is out of date, please refresh this page to login again.");
			return true;
		}
		return false;
	},
	/*
	 * get cursor position of textarea or input element
	 */
	getCursorPosition:function(jqueryEle){
		var ele=jqueryEle.get(0);
		var pos=0;
		if(document.selection){ //IE
			var sel=document.selection.createRange();
			sel.moveStart('character',-ele.value.length);
			pos=sel.text.length;
		}
		else if(ele.selectionStart || ele.selectionStart=='0'){
			pos=ele.selectionStart;
		}
		return pos;
	},
	/*
	 * set cursor position of textarea or input element
	 */
	setCursorPosition:function(jqueryEle,pos){
		var ele=jqueryEle.get(0);
		if(ele.setSelectionRange){
			ele.setSelectionRange(pos,pos);
		}
		else if(ele.createTextRange){
			var range=ele.createTextRange();
			range.collapse(true);
			range.moveEnd('character',pos);
			range.moveStart('character',pos);
			range.select();
		}
	},
	/*
	 * Preload images
	 */
	preloadImages:function(){
		var args=Array.prototype.slice.call(arguments,0);
		for(var i=0;i<args.length;i++){
			$("<img>").attr("src",args[i]);
		}
	},
	/*
	 * Require javascript files asynchronizedly
	 */
	requireJS:function(url,callback){
		$.get(url,function(data){
			var scriptElem=document.createElement("script");
			document.getElementsByTagName("head")[0].appendChild(scriptElem);
			scriptElem.text=data;
			scriptElem.onload=callback;
		});
	}
};

/*
 * extend Array prototype methods
 */
Array.prototype.indexOf=function(object){
	for(var i=0;i<this.length;i++){
		if(this[i]==object){
			return i;
		}
	}
	return -1;
};
Array.prototype.each=function(callback){
	for(var i=0;i<this.length;i++){
		callback(this[i],i);
	}
};
Array.prototype.clone=function(){
	var o=[];
	this.each(function(v,k){
		o[k]=v;
	});
	return o;
};
Array.prototype.mapClone=function(callback){
	var o=[];
	this.each(function(v,k){
		o[k]=callback(v,k);
	});
	return o;
};

/*
 * extend String prototype methods
 */
String.prototype.trim=function(){
	return this.replace(/^\s\s*/,'').replace(/\s\s*$/,'');
};

String.prototype.format = function(args) {
    var result = this;
    if (arguments.length > 0) {    
        if (arguments.length == 1 && typeof (args) == "object") {
            for (var key in args) {
                if(args[key]!=undefined){
                    var reg = new RegExp("({" + key + "})", "g");
                    result = result.replace(reg, args[key]);
                }
            }
        }
        else {
            for (var i = 0; i < arguments.length; i++) {
                if (arguments[i] != undefined) {
                    var reg = new RegExp("({[" + i + "]})", "g");
                    result = result.replace(reg, arguments[i]);
                }
            }
        }
    }
    return result;
};

String.prototype.lastIndexOf=function(substring,endPos){
	var arr=new Array(substring.length);
	for(var i=0;i<substring.length;i++){
		if(i>0){
			if(substring[i]==substring[arr[i-1]]){
				arr[i]=arr[i-1]+1;
			}
			else{
				arr[i]=0;
			}
		}
		else{
			arr[i]=0;
		}
	}
	var i=0,j=0;
	var find=-1;
	var srcLen=(endPos==undefined?this.length:endPos+1);
	while(i<srcLen && j<substring.length){
		if(this[i]==substring[j]){
			if(j==substring.length-1){
				find=i-substring.length+1;
				if(j>=1){
					j=arr[j-1];					
				}
				else{
					i++;
				}
			}
			else{
				i++;
				j++;	
			}
		}
		else{
			if(j>=1){
				j=arr[j-1];
			}
			else{
				i++;
			}
		}
	}
	return find;
};

function XConfigParams(){
	this.title="Blank Page"; // title of page
	this.font_family="Arial, Helvetica, sans-serif"; // font of page
	this.body_width="1000px"; // width of body tag
	this.body_margin="0px auto";
	this.background_color="white"; // background color of body
	this.background_image="";
	this.background_image_repeat="";
	this.background_image_position="";
	this.head_image=""; // image head of page
	this.head_height="82px";
	this.head_text_system="System Name";
	this.head_text_application="Application Name";
	this.head_text_system_color="WHITE";
	this.head_text_system_size="16pt";
	this.head_text_application_color="WHITE";
	this.head_text_application_size="20pt";
	this.foot_info=""; // foot information
	this.rights_info=""; // rights information
	this.head_div_id="head"; // head image div id
	this.content_div_id="content"; // page loader id
	this.foot_div_id="foot"; // foot div id
	this.menu_div_id="menu"; //menu div id
	this.dialog_div_id="dialog"; //dialog div id
	this.content_js_id="content_js"; // content html id
	this.content_css_id="content_css"; // content css id
	this.menu_font_color="#FFFFFF"; //menu font color
	this.menu_bg_color="#333333"; //menu background color
	this.menu_hover_font_color="#FFFFFF"; //menu hover font color
	this.menu_hover_bg_color="#538ECB"; //menu hover background colors
	this.menu_height="30px"; //menu height
	this.menu_font_size="10pt"; //menu font size
	this.menu_item_padding="15px"; //menu item padding
	this.dialog_font_size="11pt";//dialog font size,
	this.loading_image=""; //loading image
	this.loading_div_id="loadingDiv"; //loading div id
	this.welcome_info="Welcome";//welcome info
}

/**
 * `XPage` class 
 * 
 **/
function XPage(configParams){
	this.configParams=configParams;
	this._setHeadStyle();
	this._setBodyStyle();
	this._setFootStyle();
}

XPage.prototype={			
	addMenuBar:function(menu_bar){
		menu_bar._paintComponent();
	},
	
	setTitle:function(newTitle) {
		$("title")[0].text=newTitle;
	},
	
	setBannerText:function(newBannerText) {
		$("#"+this.configParams["head_div_id"]).attr("title",newBannerText);
	},
	
	loadContent:function(html,css,js){
		if(css!=undefined && css!=null){
			if($("#"+this.configParams["content_css_id"]).length>0){
				$("#"+this.configParams["content_css_id"]).remove();
			}
			var css_node=$("<link href='"+css+"' rel='stylesheet' type='text/css' id='"+this.configParams["content_css_id"]+"'/>");
			$("head").append(css_node);
		}
		$("#"+this.configParams["content_div_id"]).load(html,function(){
			if(js!=undefined && js!=null){
				if($("#"+this.configParams["content_js_id"]).length>0){
					$("#"+this.configParams["content_js_id"]).remove();
				}
				var js_node=$("<script type='text/javascript' src='"+js+"' id='"+this.configParams["content_js_id"]+"'></script>");
				$("head").append(js_node);
			}
		});
	},
	
	_setHeadStyle:function(){
		document.title=this.configParams["title"];
		if(this.configParams["head_image"]!=""){
			$("#"+this.configParams["head_div_id"]).html(
					"<img src='"+this.configParams["head_image"]+"' width='"+this.configParams["body_width"]+"' height='"+this.configParams["head_height"]+"'/>" +
					"<div class='xui_header_text'>" +
						"<span class='xui_header_text_system'>"+this.configParams['head_text_system']+"</span>" +
						"<span class='xui_header_text_application'>|</span>" +
						"<span class='xui_header_text_application'>"+this.configParams['head_text_application']+"</span>" +
					"</div>").css("height",this.configParams["head_height"]);
			$(".xui_header_text").css({
				"position":"relative",
				"top":"-65px",
				"left":"10px",
				"text-shadow":"0.1em 0.1em 0.2em #111111"
			});
			$(".xui_header_text_system").css({
				"margin-left":"10px",
				"font-weight":"bold",
				"color":this.configParams["head_text_system_color"],
				"font-size":this.configParams["head_text_system_size"]
			});
			$(".xui_header_text_application").css({
				"position":"relative",
				"margin-left":"10px",
				"top":"2px",
				"font-weight":"bold",
				"color":this.configParams["head_text_application_color"],
				"font-size":this.configParams["head_text_application_size"]
			});
		}
	},

	_setBodyStyle:function(){
		$("body").css({
			"font-family":this.configParams["font_family"],
			"width":this.configParams["body_width"],
			"background":this.configParams["background_color"]+" url("+this.configParams["background_image"]+") "+this.configParams["background_image_repeat"]+" "+this.configParams["background_image_position"],
			"margin":this.configParams["body_margin"]
		});
		$("#"+this.configParams["dialog_div_id"]).hide();
	},
	
	_setFootStyle:function(){
		$("#"+this.configParams["foot_div_id"]).html("<div id='foot'>"+this.configParams["foot_info"]+"</div><div id='rights'>"+this.configParams["rights_info"]+"</div>");
		$("#foot").css({
			"margin-top":"50px",
			"width":"100%",
			"line-height":"32px",
			"white-space":"nowrap",
			"text-align":"center",
			"color":"#444444",
			"font-family":"Verdana",
			"font-size":"8pt",
			"font-weight":"bold"
		});
		$("#rights").css({
			"border-style":"solid",
			"border-width":"1px 0 0 0",
			"border-color":"#c0c0c0",
			"width":"100%",
			"line-height":"32px",
			"white-space":"nowrap",
			"text-align":"center",
			"color":"#444444",
			"font-family":"Verdana",
			"font-size":"8pt"
		});
	}
};

/**
 * `XMenuBar` class, `id` is required, `styles` is optional 
 * 
 **/
function XMenuBar(configParams){
	this.configParams=configParams;
	this.menu_items=[];
}

XMenuBar.prototype={
	addMenuItem:function(menu_item){
		this.menu_items.push(menu_item);
	},
	
	setWelcomeInfo: function(username) {
		var welcomeInfo=this.configParams["welcome_info"]+"!";
		if (username != null && username!=""){
			welcomeInfo = this.configParams["welcome_info"]+", " + username;	
		}
		$("#xui_welcome_info").html(welcomeInfo);
	},
	
	_paintComponent:function(){
		var html="<ul>";
		for(var i=0;i<this.menu_items.length;i++){
			html+="<li id='item_"+this.menu_items[i].id+"'>"+this.menu_items[i].item_name+"</li>";
		}
		html+="</ul>";
		
		html+=("<span id='xui_welcome_info' style='line-height:"+this.configParams['menu_height']+"'></span>");
		
		$("#"+this.configParams["menu_div_id"]).html(html).css({
			"color":this.configParams["menu_font_color"],
			"background":this.configParams["menu_bg_color"],
			"width":this.configParams["body_width"],
			"font-size":this.configParams["menu_font_size"],
			"height":this.configParams["menu_height"],
			"font-weight":"bold"
		});
		$("#xui_welcome_info").css({
			 "float":"right",
			 "margin-right":"20px"
		});
		$("#"+this.configParams["menu_div_id"]+" ul").css({
			"float":"left",
			"z-index":"100"
		});
		$("#"+this.configParams["menu_div_id"]+" ul li").css({
			"line-height":this.configParams["menu_height"],
			"padding-left":this.configParams["menu_item_padding"],
			"padding-right":this.configParams["menu_item_padding"],
			"float":"left",
			"cursor":"pointer"
		});
		(function(context){
			$("#"+context["configParams"]["menu_div_id"]+" ul li").hover(function(){
				$(this).css({"color":context["configParams"]["menu_hover_font_color"],"background":context["configParams"]["menu_hover_bg_color"]});
			},function(){
				$(this).css({"color":context["configParams"]["menu_font_color"],"background":context["configParams"]["menu_bg_color"]});					
			});
		})(this);
	}
};

/**
 * `XMenuItem` class, `item_name` is required, `id` is optional(default equal to `item_name`),`styles` is optional
 * 
 **/
function XMenuItem(item_name,id,configParams){
	this.item_name=item_name;
	this.id=id;
	this.configParams=configParams;
	this.subMenuItems=[];
}

XMenuItem.prototype={	
	addClickListener:function(callback){
		$("#"+this.configParams["menu_div_id"]).on("click","li#item_"+this.id,function(){
			if(callback!=undefined && callback!=null){
				if(typeof callback=="string"){
					document.location.href=callback;
				}
				else if(typeof callback=="function"){
					callback.call(this);
				}
			}
		});
	},
	
	setSubMenuItems:function(subMenuItems){
		this.subMenuItems=subMenuItems;
		var ul="<ul id='pop_"+this.id+"'>";
		for(var i=0;i<subMenuItems.length;i++){
			ul+="<li id='item_"+subMenuItems[i].id+"'>"+subMenuItems[i].item_name+"</li>";			
		}
		ul+="</ul>";
		$("#"+this.configParams["menu_div_id"]).append($(ul));
		$("ul#pop_"+this.id).css({
			"position":"absolute",
			"display":"none",
			"line-height":this.configParams["menu_height"],
			"z-index":"100"
		});
		$("ul#pop_"+this.id+" li").css({
			"padding-left":this.configParams["menu_item_padding"],
			"padding-right":this.configParams["menu_item_padding"],
			"cursor":"pointer",
			"background":this.configParams["menu_bg_color"]
		});
		(function(context){
			$("#item_"+context["id"]).hover(function(){
				$("ul[id^='pop_']").hide();
				var top=$(this).offset().top+$(this).height();
				var left=$(this).offset().left;
				$("ul#pop_"+context["id"]).css({
					"top":top+"px",
					"left":left+"px"
				}).show();
			},function(evt){
				var y1=$(this).offset().top;
				var x1=$(this).offset().left;
				var y2=y1+$(this).height()+$("ul#pop_"+context["id"]).height();
				var x2=x1+Math.max($(this).width(),$("ul#pop_"+context["id"]).width());
				if((evt.pageX>=x1 && evt.pageX<=x2) && (evt.pageY>=y1 && evt.pageY<=y2)==false){
					$("ul#pop_"+context["id"]).hide();
				}
			});
			$("ul#pop_"+context["id"]+" li").hover(function(){
				$(this).css({"color":context["configParams"]["menu_hover_font_color"],"background":context["configParams"]["menu_hover_bg_color"]});
			},function(){
				$(this).css({"color":context["configParams"]["menu_font_color"],"background":context["configParams"]["menu_bg_color"]});				
			});
			$("ul#pop_"+context["id"]).hover(function(){},function(){
				$(this).hide();
			});
		})(this);
	}
};

/**
 * `XHintableInput` class, `id` is required, `default_text` is required, `hint_color` is optional 
 * 
 **/
function XHintableInput(selector,default_text,hint_color){
	this.selector=selector;
	this.hint_color=(hint_color==undefined?"#888":hint_color);
	this.default_text=default_text;
	(function(context){
		$(context["selector"]).val(default_text).css("color",context["hint_color"]).focus(function(){
			if($(this).val()==default_text){
				$(this).val("").css("color","black");			
			}
		}).blur(function(){
			if($(this).val()==""){
				$(this).val(default_text).css("color",context["hint_color"]);
			}
		});
	})(this);
}

XHintableInput.prototype={
	autocomplete:function(source){
		$(this["selector"]).autocomplete({source:source,minLength:0}).focus(function(){
			if($(this).attr('readonly') == 'readonly')return;
			$(this).autocomplete("search","");
		});
	},
	setAutocompleteEvent:function(event,callback){
		$(this["selector"]).on(event,function(evt,ui){
			callback.call(this,evt,ui);
		});
	},
	protectCurrentValue:function(){
		(function(context){
			$(context["selector"]).focus(function(){
				$(this).attr("current",$(this).val());
			}).blur(function(){
				if($(this).val()==context["default_text"] || $(this).val()==""){
					if($(this).attr("current")!="" && $(this).attr("current")!=undefined){
						$(this).val($(this).attr("current")).css("color","black");											
					}
				}
			});
		})(this);
	}
};

/**
 * `XDialog` class, `title` is required, `html` is required, `button_text_arr` is optional,`button_callback_arr` is optional 
 * 
 **/
function XDialog(configParams,title,html,button_text_arr,button_callback_arr,isNew){
	this.configParams=configParams;
	this.dialogID=(isNew==true ? "dialog_"+XUtil.guid():this.configParams["dialog_div_id"]);
	if($("#"+this.dialogID).length>0){
		$("#"+this.dialogID).attr("title",title).html(html);
	}
	else{
		$("<div id='"+this.dialogID+"' title='"+title+"' style='display:none;'>"+html+"</div>").appendTo($("html"));
	}
	var buttons=[];
	if(button_text_arr!=undefined){
		for(var i=0;i<button_text_arr.length;i++){
			buttons.push({text:button_text_arr[i],click:button_callback_arr[i]});		
		}	
	}
	buttons.push({text:"Close",click:function(){
		$(this).dialog("destroy").remove();
	}});
	$("#"+this.dialogID).dialog({buttons:buttons,modal:true,resizable:false});
	$("div.ui-dialog").css("font-size",this.configParams["dialog_font_size"]);
}

XDialog.prototype.closeDialog=function(){
	$("#"+this.dialogID).dialog("destroy").remove();
}

/**
 * `XSimpleDialog` class, `title` is required, `html` is required, `button_text_arr` is optional,`button_callback_arr` is optional 
 * 
 **/
function XSimpleDialog(configParams,title,html,button_text_arr,button_callback_arr,isNew){
	this.configParams=configParams;
	this.dialogID=(isNew==true ? "dialog_"+XUtil.guid():this.configParams["dialog_div_id"]);
	if($("#"+this.dialogID).length>0){
		$("#"+this.dialogID).attr("title",title).html(html);
	}
	else{
		$("<div id='"+this.dialogID+"' title='"+title+"' style='display:none;'>"+html+"</div>").appendTo($("html"));
	}
	var buttons=[];
	if(button_text_arr!=undefined){
		for(var i=0;i<button_text_arr.length;i++){
			buttons.push({text:button_text_arr[i],click:button_callback_arr[i]});		
		}	
	}
	$("#"+this.dialogID).dialog({buttons:buttons,dialogClass:"simple-dialog", modal:true,resizable:false});
	$("div.ui-dialog").css("font-size",this.configParams["dialog_font_size"]);
}

XSimpleDialog.prototype.closeDialog=function(){
	$("#"+this.dialogID).dialog("destroy").remove();
}

function XLoadingDiv(configParams){
	this.configParams=configParams;
	if($("#"+this.configParams["loading_div_id"]).length>0){
		if(!$("#"+this.configParams["loading_div_id"]).is(":visible")){
			$("#"+this.configParams["loading_div_id"]).css({
				"top":$(window).scrollTop()+"px",
				"width":$(window).width()+"px",
				"height":$(window).height()+"px"
			}).show();
			$("body").css("overflow","hidden");	
		}
	}
	else{
		var loading_div="<div id='"+this.configParams["loading_div_id"]+"'>"+
		"<img src='"+this.configParams["loading_image"]+"'/>"+
		"</div>";
		$(loading_div).css({
			"position":"absolute",
			"z-index":"999",
			"left":"0px",
			"top":$(window).scrollTop()+"px",
			"width":$(window).width()+"px",
			"height":$(window).height()+"px",
			"background":"#CCC",
			"opacity":"0.8"
		}).appendTo($("body"));
		$("#"+this.configParams["loading_div_id"]+" img").css({
			"position":"absolute",
			"top":"50%",
			"margin-top":"-10px",
			"left":"50%",
			"margin-left":"-10px"
		});
		$("body").css("overflow","hidden");
	}
	(function(context){
		$(window).resize(function(){
			$("#"+context.configParams["loading_div_id"]).css({
				"top":$(window).scrollTop()+"px",
				"width":$(window).width()+"px",
				"line-height":$(window).height()+"px"
			});
		});
	})(this);
}

XLoadingDiv.prototype.hideLoadingDiv=function(){
	if($("#"+this.configParams["loading_div_id"]).length>0){
		$("#"+this.configParams["loading_div_id"]).hide();
	}
	$("body").css("overflow-y","auto");
	$(window).unbind("resize");
}

/**
 * `XValidatorManager` class
 * 
 **/
function XValidatorManager(){
	this.validators=[];
}

XValidatorManager.prototype={
	addValidator:function(validator){
		this.validators.push(validator);
	},
	validate:function(){
		for(var i=0;i<this.validators.length;i++){
			if(this.validators[i].validate()==false){
				return false;
			}
		}
		return true;
	}
}


/**
 * `XValidator` class
 * 
 **/
function XValidator(selector,validate_regex){
	this.selector=selector;
	this.validate_regex=validate_regex;
}

XValidator.prototype.validate=function(){
	var regex=new RegExp(this.validate_regex);
	var fields=$(this.selector);
	for(var i=0;i<fields.length;i++){
		if(regex.test($(fields[i]).val())){
			$(fields[i]).removeClass("x_error_field");
		}
		else{
			$(fields[i]).addClass("x_error_field");
			return false;
		}
	}
	return true;
}

/**
 * `XTreeNode` class
 */
function XTreeNode(data,value){
	this.data=data;
	this.attr={nodeValue:data};
	this.value=value;
	this.children=[];
}

XTreeNode.prototype={
	getData:function(){
		return this.data;
	},
	setData:function(data){
		this.data=data;
		this.attr={nodeValue:data};
	},
	getValue:function(){
		return this.value;
	},
	setValue:function(value){
		this.value=value;
	},
	getChildren:function(){
		return this.children;
	},
	addChild:function(node){
		this.children.push(node);
	}
}

/**
 * `XTree` class, jquery.jstree.js is needed
 */
function XTree(selectors){
	this.input=selectors.input;
	this.div=selectors.div;
	this.tree=selectors.tree;
	this.select_all=selectors.selectAll;
	this.clear_all=selectors.clearAll;
	this.ok=selectors.ok;
	this.roots=[];
}

XTree.prototype={
	addRoot:function(root){
		this.roots.push(root);
	},
	getRoots:function(){
		return this.roots;
	},
	initTreeUI:function(){
		$(this.tree).jstree({
			"json_data":{"data" : this.roots},
			"plugins" : [ "themes", "json_data", "ui","checkbox","sort" ]
		});
	},
	enableEventCallBacks:function(){
		(function(context){
			$(context.select_all).click(function(){
				$(context.tree).jstree("check_all");
			});
			$(context.clear_all).click(function(){
				$(context.tree).jstree("uncheck_all");
			});
			$(context.ok).unbind("click").click(function(){
				var checked_items=$(context.tree+" li.jstree-checked.jstree-leaf");
				var value="";
				for(var i=0;i<checked_items.length;i++){
					value+=$(checked_items[i]).attr("nodeValue");
					if(i!=(checked_items.length-1)){
						value+=",";
					}
				}
				$(context.input+":visible:last").val(value).css("color","black");
				$(context.tree).jstree("uncheck_all");
				$(context.div).hide();
			});
			$(context.input).click(function(){
				if($(this).attr('readonly') == 'readonly')return;
				$(context.div).css({"left":$(this).offset().left+"px","top":$(this).offset().top+30+"px"}).show();
				$(context.tree).jstree("uncheck_all");
				var values=$(this).val().split(",");
				for(var i=0;i<values.length;i++){
					$(context.tree).jstree("check_node",$(context.tree+" li[nodeValue='"+values[i]+"']"));
				}
			});
			$(document).click(function(evt){
				if(!$(evt.target).is(context.input) && ($(evt.target).parents(context.div).length==0 && !$(evt.target).is(context.div))){
					$(context.div).hide();
				}			
			});
		})(this);
	},
	setIsEditable:function(isEditable){
		if(isEditable){
			$(this.select_all+","+this.clear_all).removeAttr("disabled");
			$(this.ok).button({disabled:false});
			$("#software_tree li a>ins,#hardware_tree li a>ins").removeAttr("disabled");
		}
		else{
			$(this.select_all+","+this.clear_all).attr("disabled","disabled");
			$(this.ok).button({disabled:true});	
			$("#software_tree li a>ins,#hardware_tree li a>ins").attr("disabled","disabled");
		}
	}
}

/**
 * `XLocalStorage` class, need browser support HTML5 LocalStorage API.
 * json2.js is needed.
 */
function XLocalStorage(){
	this.cache=window.localStorage;
}

XLocalStorage.prototype={
	setItem:function(name,obj){
		this.cache.setItem(name,JSON.stringify(obj));
	},
	getItem:function(name){
		return JSON.parse(this.cache.getItem(name));
	},
	clear:function(){
		this.cache.clear();
	}
}

/**
 * `XSessionStorage` class, need browser support HTML5 SessionStorage API.
 * json2.js is needed.
 */
function XSessionStorage(){
	this.cache=window.sessionStorage;
}

XSessionStorage.prototype={
	setItem:function(name,obj){
		this.cache.setItem(name,JSON.stringify(obj));
	},
	getItem:function(name){
		return JSON.parse(this.cache.getItem(name));
	},
	clear:function(){
		this.cache.clear();
	}
}

function XClipBoard(){
	ZeroClipboard.setMoviePath("javascripts/lib/ZeroClipboard.swf");
	this.clip=new ZeroClipboard.Client();
	this.clip.setHandCursor(true);
	this.clip.addEventListener("complete",function(){
		XUI.createXDialog("Information", "Copy complete!",[],[]);
	});		
}

XClipBoard.prototype={
	copyTextByDom:function(text,trigger_id){
		this.clip.setText(text);	
		this.clip.glue(trigger_id);
	}
}