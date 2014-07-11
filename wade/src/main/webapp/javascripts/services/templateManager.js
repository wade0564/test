
if (typeof emc !== 'object') { emc = {}; }
if (typeof emc.prometheus !== 'object') { emc.prometheus = {}; }

emc.prometheus.renderTemplate = new function(){
	
	this.renderByRepalce = function(jsonData,targetStr){
		for(var key in jsonData){
			targetStr = targetStr.replace(new RegExp('\{'+key+'\}', 'g'),jsonData[key]);
		}
		return targetStr;
	};
};