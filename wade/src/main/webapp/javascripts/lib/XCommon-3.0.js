/**
 * This jQuery plugin make you develop a web page more convenience.
 * Depends on jQuery lib and jQuery UI.
 * Optionally, depends on "jquery.jstree.js", "jquery.cookie.js", "pagination.js" and their relative resources.
 *
 * @author EMC BRS team (Shanghai, China)
 * @version 3.0
 * 
 */

/*************some extensions of jQuery plugin**************************/
(function($,exports){
	//namespace
	$.namespace=function(str){
		var name_arr=str.split("."),		
			parent=exports,
			i, len, name;
		for(i=0,len=name_arr.length;i<len;i++){
			name=name_arr[i];
			if(typeof parent[name]=="undefined"){
				parent[name]={};
			}
			parent=parent[name];
		}
		return parent;
	};
	//Array
	Array.prototype.indexOf=function(object){
		var i,len=this.length;
		for(i=0;i<len;i++){
			if(this[i]==object){
				return i;
			}
		}
		return -1;
	};
	Array.prototype.each=function(callback){
		var i,len=this.length;
		for(var i=0;i<len;i++){
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
	//String
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
	                    var reg = new RegExp("({" + i + "})", "g");
	                    result = result.replace(reg, arguments[i]);
	                }
	            }
	        }
	    }
	    return result;
	};
	//prototype inheritance
	Object.create=function(o){
		var child,
			args=Array.prototype.slice.call(arguments,1),
			i, len=args.length,
			prop;
		function F(){};
		F.prototype=o;
		child=new F();
		for(i=0;i>len;i++){
			for(prop in args[i]){
				if(args[i].hasOwnProperty(prop)){
					child[prop]=args[i][prop];
				}
			}
		}
		return child;
	};
	//function
	Function.prototype.bind=function(thisAry){
		var fn=this,
			slice=Array.prototype.slice,
			args=slice.call(arguments,1);
		return function(){
			fn.apply(thisArg,args.concat(slice.call(arguments)));
		};
	};
	Function.prototype.curry=function(){
		var slice=Array.prototype.slice, 
			stored_args=slice.call(arguments,0),
			fn=this;
		return function(){
			var new_args=slice.call(arguments),
				args=stored_args.concat(new_args);
			return fn.apply(null,args);
		};
	};
})(jQuery,window);




/*********************************some util functions********************************/
$.namespace("COM.EMC.UTIL");

COM.EMC.UTIL=(function($){
	var isIE=/*@cc_on!@*/false;
	//require resource async
	var requireJS=function(url,callback){
		var async=!(typeof callback == "undefined");
		$.ajax({
			type:"get",
			async:async,
			url:url,
			success: function(data){
				var scriptElem=document.createElement("script");
				document.getElementsByTagName("head")[0].appendChild(scriptElem);
				scriptElem.text=data;
				scriptElem.setAttribute("type","text/javascript");
				if(typeof callback == "undefined"){
					return;
				}
				if(isIE){// IE
					scriptElem.onreadystatechange=function(){
						if(scriptElem.readyState==="loaded" || scriptElem.readyState==="complete"){
							scriptElem.onreadystatechange=null;
							callback();							
						}
					}
				}
				else{
					//TODO
					setTimeout(callback,0);
				}
			}
		});
	};
	var requireCSS=function(url,callback){
		var async=!(typeof callback == "undefined");
		$.ajax({
			type:"get",
			async:async,
			url:url,
			success: function(data){
				var styleElem=document.createElement("style");
				styleElem.setAttribute("type","text/css");
				document.getElementsByTagName("head")[0].appendChild(styleElem);
				styleElem.innerHTML=data;
				if(typeof callback == "undefined"){
					return;
				}
				if(isIE){// IE
					styleElem.onreadystatechange=function(){
						if(styleElem.readyState==="loaded" || styleElem.readyState==="complete"){
							styleElem.onreadystatechange=null;
							callback();							
						}
					}
				}
				else{
					//TODO
					setTimeout(callback,0);
				}
			}
		});
	};
	//get parameter value in url
	var getURLParameter=function(key){
		var reg = new RegExp("(^|&)" + key + "=([^&]*)(&|$)", "i");
	    var r = window.location.search.substr(1).match(reg);
	    if (r != null){
	    	return unescape(r[2]); 
	    }
	    return null;
	};
	//preload resources
	var preload=function(){
		var args=Array.prototype.slice.call(arguments,0);
		for(var i=0;i<args.length;i++){
			if(isIE){
				new Image().src=args[i];
			}
			else{
				var obj=document.createElement("object"),
					body=document.body;
				obj.width=0;
				obj.height=0;
				obj.data=args[i];
				body.appendChild(obj);
			}		
		}
	};
	//generate guid
	var guid=function(){
		return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g,function(c){
			var r=Math.random()*16|0,v=c=='x'?r:(r&0x3|0x8);
			return r.toString(16);
		}).toUpperCase();
	};
	return {
		requireJS:requireJS,
		requireCSS:requireCSS,
		getURLParameter:getURLParameter,
		preload:preload,
		guid:guid
	};
})(jQuery);




/*********date namespace**************/
$.namespace("COM.EMC.DATE");

COM.EMC.DATE=(function($){
	var timezoneToOffset={
		GMT:0,
		EST:-4,
		CST:-5,
		MST:-6,
		PST:-7,
		//summer time
		EDT:-5,
		CDT:-6,
		MDT:-7,
		PDT:-8
	};
	var dateToStr=function(date,options){
		var defaults={
			date_concatenator:"-",
			time_concatenator:":",
			containTime:true
		};
		var options=$.extend(defaults,options);
		var year = date.getFullYear();
		var month = date.getMonth() <= 8 ? "0" + (date.getMonth() + 1) : ""
				+ (date.getMonth() + 1);
		var day = date.getDate() <= 9 ? "0" + date.getDate() : ""
				+ date.getDate();
		var result=year + options.date_concatenator + month + options.date_concatenator + day;
		if(containTime){
			var hour=date.getHours()<=9?"0"+date.getHours():""+date.getHours();
			var min=date.getMinutes()<=9?"0"+date.getMinutes():""+date.getMinutes();
			result+=" "+hour+options.time_concatenator+min;
		}
		return result;
	};
	var calculateDate=function(date,offset){
		var ts=date.getTime();
		ts=offset*60*60*1000+ts;
		return new Date(ts);
	};
	return {
		dateToStr:dateToStr,
		getCurrentUTCDate:function(){
			return this.localeDateToUTCDate(new Date());
		},
		localeDateToUTCDate:function(date){
			var offset=-new Date().getTimezoneOffset()/60;
			return calculateDate(date,offset);
		},
		utcDateToLocaleDate:function(utcDate){
			var offset=new Date().getTimezoneOffset()/60;
			return calculateDate(utcDate,offset);
		},
		utcDateToLocaleDate:function(timezone,utcDate){
			var offset=timezoneToOffset[timezone.toUpperCase()];
			var utcDate=utcDate;
			if(typeof utcDate=="undefined"){
				utcDate=this.getCurrentUTCDate();
			}
			if(typeof offset!="undefined"){
				return calculateDate(utcDate,offset);
			}
			return null;
		}
	};
})(jQuery);



/***************some test functions**********************************/
$.namespace("COM.EMC.TEST");

COM.EMC.TEST={
	assert:function(value,msg){
		if(!value){
			throw(msg || (value+" does not equal true!"));
		}
	},
	assertEqual:function(val1,val2,msg){
		if(val1!=val2){
			throw(msg || (val1+" does not equal "+val2+"!"));
		}
	}
};



/*******common UI widgets, need to import XCommonUI stylesheet, pagination.js, jstree.js and their resources***********/
$.namespace("COM.EMC.UI");

COM.EMC.UI=(function($){
	var config={
		title:"Blank Page", // title of page
		font_family:"Arial, Helvetica, sans-serif", // font of page
		body_width:"1000px", // width of body tag
		body_margin:"0px auto",
		background_color:"white", // background color of body
		background_image:"", //background image of body
		background_image_repeat:"",
		background_image_position:"",
		head_image:"", // image head of page
		head_height:"82px",
		head_text_system:"System Name",
		head_text_application:"Application Name",
		head_text_system_color:"WHITE",
		head_text_system_size:"16pt",
		head_text_application_color:"WHITE",
		head_text_application_size:"20pt",
		foot_info:"", // foot information
		rights_info:"", // rights information
		head_div_selector:"#head", // head image div
		content_div_selector:"#content", // content div
		foot_div_selector:"#foot", // foot div id
		menu_div_selector:"#menu", //menu div id
		dialog_div_selector:"#dialog", //dialog div id
		menu_font_color:"#FFFFFF", //menu font color
		menu_bg_color:"#333333", //menu background color
		menu_hover_font_color:"#FFFFFF", //menu hover font color
		menu_hover_bg_color:"#538ECB", //menu hover background colors
		menu_height:"30px", //menu height
		menu_font_size:"10pt", //menu font size
		menu_item_padding:"15px", //menu item padding
		dialog_font_size:"11pt", //dialog font size,
		loading_image:"", //loading image
		loading_div_selector:"#loadingDiv", //loading div selector
		welcome_info:"Welcome"//welcome info
	};
	//config parameters
	var configUI=function(newConfig){
		for(var name in config){
			config[name] = (newConfig !== undefined && newConfig[name] !== undefined) ? newConfig[name] : config[name];
		}
		return this;
	};
	//page widget
	function Page(){
		//init UI
		//set head part style
		document.title=config["title"];
		if(config["head_image"]!=""){
			$(config["head_div_selector"]).html(
					"<img src='"+config["head_image"]+"' width='"+config["body_width"]+"' height='"+config["head_height"]+"'/>" +
					"<div class='xui_header_text'>" +
					"<span class='xui_header_text_system'>"+config['head_text_system']+"</span>" +
					"<span class='xui_header_text_application'>|</span>" +
					"<span class='xui_header_text_application'>"+config['head_text_application']+"</span>" +
			        "</div>").css("height",config["head_height"]);
			$(".xui_header_text").css({
				"position":"relative",
				"top":"-65px",
				"left":"10px",
				"text-shadow":"0.1em 0.1em 0.2em #111111"
			});
			$(".xui_header_text_system").css({
				"margin-left":"10px",
				"font-weight":"bold",
				"color":config["head_text_system_color"],
				"font-size":config["head_text_system_size"]
			});
			$(".xui_header_text_application").css({
				"position":"relative",
				"margin-left":"10px",
				"top":"2px",
				"font-weight":"bold",
				"color":config["head_text_application_color"],
				"font-size":config["head_text_application_size"]
			});
		}
		//set body part style
		$("body").css({
			"font-family":config["font_family"],
			"width":config["body_width"],
			"background":config["background_color"]+" url("+config["background_image"]+") "+config["background_image_repeat"]+" "+config["background_image_position"],
			"margin":config["body_margin"]
		});
		$(config["dialog_div_selector"]).hide();
		//set foot part style
		$(config["foot_div_selector"]).html("<div>"+config["foot_info"]+"</div><div>"+config["rights_info"]+"</div>");
		$(config["foot_div_selector"]+" div:eq(0)").css({
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
		$(config["foot_div_selector"]+" div:eq(1)").css({
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
	};
	Page.prototype.addMenuBar=function(menu_bar){
		menu_bar.paint();
	};
	//menubar widget
	function MenuBar(){
		this.menu_items=[];
	}
	MenuBar.prototype={
		addMenuItem:function(menu_item){
			this.menu_items.push(menu_item);
			this.paint();
		},
		addMenuItems:function(){
			this.menu_items=this.menu_items.concat(Array.prototype.slice.call(arguments));
			this.paint();
		},
		setWelcomeInfo: function(username) {
			var welcomeInfo=config["welcome_info"]+"!";
			if (username != undefined && username!=""){
				welcomeInfo = config["welcome_info"]+", " + username;	
			}
			$("#xui_welcome_info").text(welcomeInfo);
			console.log(welcomeInfo);
		},
		paint:function(){
			var html="<ul>";
			for(var i=0;i<this.menu_items.length;i++){
				html+="<li id='item_"+this.menu_items[i].id+"'>"+this.menu_items[i].item_name+"</li>";
			}
			html+="</ul>";
			html+="<span id='xui_welcome_info' style='line-height:"+config['menu_height']+"'></span>";
			$(config["menu_div_selector"]).html(html).css({
				"color":config["menu_font_color"],
				"background":config["menu_bg_color"],
				"width":config["body_width"],
				"font-size":config["menu_font_size"],
				"height":config["menu_height"],
				"font-weight":"bold"
			});
			$("#xui_welcome_info").css({
				 "float":"right",
				 "margin-right":"20px"
			});
			$(config["menu_div_selector"]+" ul").css({
				"float":"left",
				"z-index":"100"
			});
			$(config["menu_div_selector"]+" ul li").css({
				"line-height":config["menu_height"],
				"padding-left":config["menu_item_padding"],
				"padding-right":config["menu_item_padding"],
				"float":"left",
				"cursor":"pointer"
			});
			$(config["menu_div_selector"]+" ul li").hover(function(){
				$(this).css({"color":config["menu_hover_font_color"],"background":config["menu_hover_bg_color"]});
			},function(){
				$(this).css({"color":config["menu_font_color"],"background":config["menu_bg_color"]});					
			});
		}
	};
	//menu item widget
	function MenuItem(item_name,id){
		this.item_name=item_name;
		this.id=(typeof id=="undefined" ? COM.EMC.UTIL.guid() : id);
	};
	MenuItem.prototype={	
		addClickListener:function(callback,context){
			$(config["menu_div_selector"]).on("click","li#item_"+this.id,function(){
				if(callback!=undefined && callback!=null){
					if(typeof callback=="string"){
						document.location.href=callback;
					}
					else if(typeof callback=="function"){
						callback.call(typeof context=="undefined" ? this : context);
					}
				}
			});
		},
		setSubMenuItems:function(){
			var subMenuItem_arr=[].concat(Array.prototype.slice.call(arguments));
			var ul="<ul id='pop_"+this.id+"'>";
			for(var i=0;i<subMenuItem_arr.length;i++){
				ul+="<li id='item_"+subMenuItem_arr[i].id+"'>"+subMenuItem_arr[i].item_name+"</li>";			
			}
			ul+="</ul>";
			$(config["menu_div_selector"]).append($(ul));
			$("ul#pop_"+this.id).css({
				"position":"absolute",
				"display":"none",
				"line-height":config["menu_height"],
				"z-index":"100"
			});
			$("ul#pop_"+this.id+" li").css({
				"padding-left":config["menu_item_padding"],
				"padding-right":config["menu_item_padding"],
				"cursor":"pointer",
				"background":config["menu_bg_color"]
			});
			(function(id){
				$("#item_"+id).hover(function(){
					$("ul[id^='pop_']").hide();
					var top=$(this).offset().top+$(this).height();
					var left=$(this).offset().left;
					$("ul#pop_"+id).css({
						"top":top+"px",
						"left":left+"px"
					}).show();
				},function(evt){
					var y1=$(this).offset().top;
					var x1=$(this).offset().left;
					var y2=y1+$(this).height()+$("ul#pop_"+id).height();
					var x2=x1+Math.max($(this).width(),$("ul#pop_"+id).width());
					if((evt.pageX>=x1 && evt.pageX<=x2) && (evt.pageY>=y1 && evt.pageY<=y2)==false){
						$("ul#pop_"+id).hide();
					}
				});
				$("ul#pop_"+id+" li").hover(function(){
					$(this).css({"color":config["menu_hover_font_color"],"background":config["menu_hover_bg_color"]});
				},function(){
					$(this).css({"color":config["menu_font_color"],"background":config["menu_bg_color"]});				
				});
				$("ul#pop_"+id).hover(function(){},function(){
					$(this).hide();
				});
			})(this.id);
		}
	};
	//dialog widget
	function Dialog(inputParams){
		var params={
			isNew:true,
			title:"Dialog",
			html:"",
			buttons:[],
			modal:true,
			resizable:false
		};
		for(var name in params){
			params[name] = (inputParams !== undefined && inputParams[name] !== undefined) ? inputParams[name] : params[name];
		}
		this.selector=(params.isNew==true ? "#dialog_"+COM.EMC.UTIL.guid() : config["dialog_div_selector"]);
		if($(this.selector).length>0){
			$(this.selector).attr("title",params.title).html(params.html);
		}
		else{
			$("<div id='"+this.selector.substring(1)+"' title='"+params.title+"' style='display:none;'>"+params.html+"</div>").appendTo($("html"));
		}
		var buttons=params.buttons;
		if(buttons.length==0){
			buttons.push({text:"Close",click:function(){
				$(this).dialog("destroy");
			}});
		}
		$(this.selector).dialog({buttons:buttons,modal:params.modal,resizable:params.resizable});
		$("div.ui-dialog").css("font-size",config["dialog_font_size"]);
	};
	Dialog.prototype.close=function(){
		$(this.selector).dialog("destroy");
	};
	//loading gif widget
	function LoadingGif(){
		var loadingDiv=$(config["loading_div_selector"]);
		if(loadingDiv.length>0){
			if(!loadingDiv.is(":visible")){
				loadingDiv.css({
					"top":$(window).scrollTop()+"px",
					"width":$(window).width()+"px",
					"height":$(window).height()+"px"
				}).show();
				$("body").css("overflow","hidden");	
			}
		}
		else{
			var loading_div="<div id='"+config["loading_div_selector"].substring(1)+"'>"+
			"<img src='"+config["loading_image"]+"'/>"+
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
			$(config["loading_div_selector"]+" img").css({
				"position":"absolute",
				"top":"50%",
				"margin-top":"-10px",
				"left":"50%",
				"margin-left":"-10px"
			});
			$("body").css("overflow","hidden");
		}
		$(window).resize(function(){
			$(config["loading_div_selector"]).css({
				"top":$(window).scrollTop()+"px",
				"width":$(window).width()+"px",
				"line-height":$(window).height()+"px"
			});
		});
	};
	LoadingGif.prototype.close=function(){
		if($(config["loading_div_selector"]).length>0){
			$(config["loading_div_selector"]).hide();
		}
		$("body").css("overflow-y","auto");
		$(window).unbind("resize");
	};
	//tree-list widget, jquery.jstree.js is needed
	function TreeNode(data,value){
		this.data=data;
		this.attr={nodeValue:data};
		this.value=value;
		this.children=[];
	};
	TreeNode.prototype={
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
		},
		addChildren:function(){
			this.children=this.children.concat(Array.prototype.slice.call(arguments));
		}
	};
	function TreeList(inputParams){
		this.input=inputParams.input;
		this.div=inputParams.div;
		this.tree=inputParams.tree;
		this.select_all=inputParams.selectAll;
		this.clear_all=inputParams.clearAll;
		this.ok=inputParams.ok;
		$.jstree._themes=inputParams.themePath;
		this.roots=[];
	};
	TreeList.prototype={
		addRoot:function(root){
			this.roots.push(root);
		},
		getRoots:function(){
			return this.roots;
		},
		initTreeUI:function(){
			$(this.ok).button();
			$(this.div).addClass("xui_tree_div");
			$(this.tree).addClass("xui_tree_list");
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
				context.setIsEditable(true);
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
	};
	//pagination function
	var pageTable=function(inputParams){
		var params={
			items_per_page:5, //number of rows of each page
			current_page:0,
			num_display_entries:8,  //number of visible page icons
			from:1, //head row number
			row_selector:undefined,  //selector of rows to be paged
			pager_selector:undefined  //selector of pagination bar
		};
		for(var name in params){
			params[name] = (inputParams !== undefined && inputParams[name] !== undefined) ? inputParams[name] : params[name];
		}
		var row_selector=params.row_selector;
		if(row_selector==undefined){
			return;
		}
		var from=params.from;
		var size=$(row_selector+":gt("+(from-1)+")").length;
		var items_per_page=params.items_per_page;
		$(params.pager_selector).addClass("pagination").pagination(size, {
			items_per_page:items_per_page,
			num_display_entries:params.num_display_entries,
			current_page:params.current_page,
			callback:function(page,jq){
				$(row_selector+":gt("+(from-1)+")").hide();
				var max_elem = Math.min((page+1) * items_per_page, size);
				for(var i=page*items_per_page;i<max_elem;i++){
					$(row_selector+":eq("+(i+from)+")").show();
				}
			}
		});
	}
	return {
		configUI:configUI,
		pageTable:pageTable,
		createPage:function(){
			return new Page();
		},
		createMenuBar:function(){
			return new MenuBar();
		},
		createMenuItem:function(item_name,id){
			return new MenuItem(item_name,id);
		},
		createDialog:function(params){
			return new Dialog(params);
		},
		createLoadingGif:function(){
			return new LoadingGif();
		},
		createTreeNode:function(data,value){
			return new TreeNode(data,value);
		},
		createTreeList:function(params){
			return new TreeList(params);
		}
	};
})(jQuery);



/**************validation framework, need to import XCommonUI stylesheet******************/
$.namespace("COM.EMC.VALIDATE");

COM.EMC.VALIDATE=(function($){
	function Validator(selector,regex){
		this.selector=selector;
		this.regex=regex;
	};
	Validator.prototype.validate=function(){
		var regex=new RegExp(this.regex);
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
	};
	return {
		validate:function(selector,regex){
			var validator=new Validator(selector,regex);
			return validator.validate();
		},
		validate:function(validator_arr){
			for(var i=0;i<this.validator_arr.length;i++){
				if(this.validator_arr[i].validate()==false){
					return false;
				}
			}
			return true;
		},
		createValidator:function(selector,regex){
			return new Validator(selector,regex);
		}
	};
})(jQuery);