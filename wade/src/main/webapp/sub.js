function Sub(json){
	
	this.subid=null;
	this.location=null;
	this.username=null;
	this.status=null;
	this.sn=null;
	this.channel=null;
	this.source=null;
	if(json!=undefined){
		this.parseJSON(json);
	}
}

Sub.prototype={
	setSubid:function(subid){
		this.subid=subid;
	},
	getSubId:function(){
		return this.subid;
	},
	setLocation:function(location){
		this.location=location;
	},
	getLocation:function(){
		return this.location;
	},
	setUsername:function(username){
		this.username=username;
	},
	getUsername:function(){
		return this.username;
	},
	setStatus:function(status){
		this.status=status;
	},
	getStatus:function(){
		return this.status;
	},
	setSn:function(sn){
		this.sn=sn;
	},
	getSn:function(){
		return this.sn;
	},
	setChannel:function(){
		return this.channel;
	},
	getChannel:function(channel){
		this.channel=channel;
	},
	setSource:function(){
		return this.source;
	},
	getSource:function(source){
		this.source=source;
	},
	parseJSON:function(json){
		
		this.setSubid(json.subid);
		this.setLocation(json.location);
		this.setUsername(json.username);
		this.setStatus(json.status);
		this.setSource(json.source);
		this.setSn(json.sn);
		this.setChannel(json.channel);
	}	
};

