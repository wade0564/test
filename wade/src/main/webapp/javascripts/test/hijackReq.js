
if (typeof emc !== 'object') { emc = {}; }
if (typeof emc.prometheus !== 'object') { emc.prometheus = {}; }


emc.prometheus.hijackReq = new (function() {
	
	this.reqType = 'get';

	/* this function hijack default url then replace with a new one which ends with .json
	 /user --> ./testData/user.json
	 /user/1 --> ./testData/user/index.json
	 /user/12 --> ./testData/user/index.json
	 user/12 --> ./testData/user/index.json
	/user/2/3/33/3/3/
	 /user/1/role --> ./testData/user/index/role.json
	 /user/12/role --> ./testData/user/index/role.json

	 /user/1/role/1 --> ./testData/user/index/role/index.json
	 /user/12/role/12 --> ./testData/user/index/role/index.json
	 /user/12/34/role/12 --> ./testData/user/index/index/role/index.json
	 */
	this.replaceUrl = function(url){
		if(url == null)return '';
		var newUrl = url;
		if(/\/\d+\/?$/.test(newUrl)){
			newUrl = newUrl.replace(/\/\d+\/?$/,'/index.json');
		}else{
			newUrl = newUrl.replace(/\/$/,'') + '.json';
		}
		newUrl = newUrl.replace(/\/?\d+\//g,'/index/');
		newUrl = newUrl.replace(/\/\//g,'/');
		if(/^\//.test(newUrl)){
			newUrl = './test/testData' + newUrl;
		}else{
			newUrl = './test/testData/' + newUrl;
		}
		return newUrl;
	};
	
});


