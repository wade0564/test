
if (typeof emc !== 'object') { emc = {}; }
if (typeof emc.prometheus !== 'object') { emc.prometheus = {}; }

emc.prometheus.form = function(jFormElement){
	
	this.serializeToJson = function(){
		var o = {};
		var a = jFormElement.serializeArray();
		$.each(a,function(){
			if(o[this.name] !== undefined){
				if(!o[this.name].push){
					o[this.name] = [o[this.name]];
				}
				o[this.name].push(this.value || '');
			}else{
				o[this.name] = this.value || '';
			}
		});
		return o;
	};
};