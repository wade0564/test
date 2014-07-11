
if (typeof emc !== 'object') { emc = {}; }
if (typeof emc.prometheus !== 'object') { emc.prometheus = {}; }
if (typeof emc.prometheus.ajax !== 'object') { emc.prometheus.ajax = {}; }
if (typeof emc.prometheus.reqMap !== 'object') { emc.prometheus.reqMap = {}; }



emc.prometheus.ajax.callService =  function(defArgs,args){
	
	var newArgs = $.extend({},defArgs,args);
	if(config.test_config && config.test_config.isHijack){
		var reqType = emc.prometheus.hijackReq.reqType;
		var newUrl = emc.prometheus.hijackReq.replaceUrl(newArgs.url);
		$.extend(newArgs,{type:reqType,url:newUrl});
	}
	$.ajax(newArgs);
};

emc.prometheus.reqMap.session = new function(){
	
	this.user = function(args){
		emc.prometheus.ajax.callService(args);
	};
	this.getUserById = function(args){
		var defArgs = {
			url: 'session/1',
			type: 'get'
		};
		emc.prometheus.ajax.callService(defArgs,args);
	};
	this.complexUrlTest = function(args){
		var defArgs = {
			url: 'session/1/23/',
			type: 'get'
		};
		emc.prometheus.ajax.callService(defArgs,args);
	};
	
};
emc.prometheus.scanSymptonData = new function(){
	this.getProduct = function(args){
		var defArgs = {
				url: 'signature/product',
				type: 'get'
		};
		emc.prometheus.ajax.callService(defArgs,args);
	};
	this.getComponent = function(args){
		var defArgs = {
				url: 'signature/component',
				type: 'get'
		};
		emc.prometheus.ajax.callService(defArgs,args);
	};
	this.getDomain = function(args){
		var defArgs = {
				url: 'signature/domain',
				type: 'get'
		};
		emc.prometheus.ajax.callService(defArgs,args);
	};
	this.getIds = function(args){
		var defArgs = {
				url: 'signature/ids',
				type: 'get'
		};
		emc.prometheus.ajax.callService(defArgs,args);
	};
	this.getScanData = function(args){
		var defArgs = {
				url: 'scan',
				type: 'post'
		};
		emc.prometheus.ajax.callService(defArgs,args);
	};
};














