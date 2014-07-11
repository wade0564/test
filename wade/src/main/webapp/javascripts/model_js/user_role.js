function User(json){
	this.id=null;
	this.name=null;
	this.email=null;
	this.ntAccount=null;
	this.ntDomain=null;
	this.updateTime=null;
	this.lastPage=null;
	this.roles=[];
	this.options=[];
	if(json!=undefined){
		this.parseJSON(json);		
	}
}

User.prototype={
	setId:function(id){
		this.id=id;
	},
	getId:function(){
		return this.id;
	},
	setName:function(name){
		this.name=name;
	},
	getName:function(){
		return this.name;
	},
	setEmail:function(email){
		this.email=email;
	},
	getEmail:function(){
		return this.email;
	},
	setNtAccount:function(ntAccount){
		this.ntAccount=ntAccount;
	},
	getNtAccount:function(){
		return this.ntAccount;
	},
	setNtDomain:function(ntDomain){
		this.ntDomain=ntDomain;
	},
	getNtDomain:function(){
		return this.ntDomain;
	},
	setUpdateTime:function(updateTime){
		this.updateTime=updateTime;
	},
	getUpdateTime:function(){
		return this.updateTime;
	},
	setLastPage:function(lastPage){
		this.lastPage=lastPage;
	},
	getLastPage:function(){
		return this.lastPage;
	},
	addRole:function(role){
		this.roles.push(role);
	},
	getRoles:function(){
		return this.roles;
	},
	addOption:function(option){
		this.options.push(option);
	},
	getOptions:function(){
		return this.options;
	},
//	setOptionToUsersList:function(otul){
//		this.optionToUsersList = otul;
//	},
//	getOptionToUsersList:function(){
//		return this.optionToUsersList;
//	},
	
	parseJSON:function(json){
		this.setId(json.id);
		this.setEmail(json.email);
		this.setLastPage(json.lastPage);
		this.setName(json.name);
		this.setNtAccount(json.ntAccount);
		this.setNtDomain(json.ntDomain);
		this.setUpdateTime(json.updateTime);
		for(var i=0;i<json.roleIds.length;i++){
			var role=new Role(json.roleIds[i]);
			this.addRole(role);
		}
		this.optionToUsersList=new OptionToUsersList();
		if(json.optionToUsersList.options!=null &&json.optionToUsersList.options!=undefined ){
		for(var i=0;i<json.optionToUsersList.options.length;i++){
			var option = new OptionToUsers(json.optionToUsersList.options[i]);
			this.addOption(option);
		}
	}
}
};

function Role(json){
	this.id=null;
	this.name=null;
	if(json!=undefined){
		this.parseJSON(json);		
	}
}

Role.prototype={
	setId:function(id){
		this.id=id;
	},
	getId:function(){
		return this.id;
	},
	setName:function(name){
		this.name=name;
	},
	getName:function(){
		return this.name;
	},
	parseJSON:function(json){
		this.setId(json.id);
		this.setName(json.name);
	}
};

function OptionToUsers(json){
	this.id=null;
	this.value=null;
	this.option=null;
	this.appId=null;
	this.userId=null;
	this.optionId=null;
	if(json!=undefined){
		this.parseJSON(json);
	}
}

OptionToUsers.prototype={
		setId:function(id){
			this.id=id;
		},
		getId:function(){
			return this.id;
		},
		setValue:function(value){
			this.value=value;
		},
		getValue:function(){
			return this.value;
		},
		
		getAppId:function(){
			return this.app;
		},
		setAppId:function(app){
			this.app=app;
		},
		setOption:function(option){
			this.option=option;
		},
		getOption:function(){
			return this.option;
		},
		getUserId:function(){
			return this.userId;
		},
		setUserId:function(userId){
			this.userId=userId;
		},
		setOptionId:function(optionId){
			this.optionId=optionId;
		},
		getOptionId:function(){
			return this.optionId;
		},
		parseJSON:function(json){
			this.setId(json.id);
			this.setValue(json.value);
			this.setAppId(json.appId);
			this.setUserId(json.userId);
			this.setOptionId(json.optionId);
			this.setOption(new Option(json.optionId,json.appId));
		}	
};

function Option(id,appId){
	this.id=null;
	this.name=null;
	this.application=null;
	if(id!=undefined){
		this.parseJSON(id,appId);
	}
}
Option.prototype={
		setId:function(id){
			this.id=id;
		},
		getId:function(){
			return this.id;
		},
		setName:function(name){
			this.name=name;
		},
		getName:function(){
			return this.name;
		},
		setApplication:function(application){
			this.application=application;
		},
		getApplication:function(){
			return this.application;
		},
		parseJSON:function(id,appId){
			this.setId(id);
			this.setApplication(new Application(appId));
		}	
};

function Application(appId){
	this.id=null;
	this.name=null;
	if(appId!=undefined){
		this.parseJSON(appId);
	}
}
Application.prototype={
		setId:function(id){
			this.id=id;
		},
		getId:function(){
			return this.id;
		},
		setName:function(name){
			this.name=name;
		},
		getName:function(){
			return this.name;
		},
		parseJSON:function(appId){
			this.setId(appId);
		}	
		
};
function OptionToUsersList(){
	this.options=[];
}
OptionToUsersList.prototype={
};
	
