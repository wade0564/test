function Bug(json){
	this.id=null;
	this.description=null;
	this.modelNumber=null;
	this.ddosVersion=null;
	this.lifeCycleState=null;
	this.category=null;
	this.domain=null;
	this.component=null;
	this.impact=null;
	this.penetration=null;
	this.remediation=null;
	this.logic=null;
	this.link=null;
	this.product=null;
	this.bugzillaId=null;
	this.dupes=[];
	if(json!=undefined){
		this.parseJSON(json);		
	}
}

Bug.prototype={
	setId:function(id){
		this.id=id;
	},
	getId:function(){
		return this.id;
	},
	setDescription:function(description){
		this.description=description;
	},
	getDescription:function(){
		return this.description;
	},
	setModelNumber:function(modelNumber){
		this.modelNumber=modelNumber;
	},
	getModelNumber:function(){
		return this.modelNumber;
	},
	setDdosVersion:function(ddosVersion){
		this.ddosVersion=ddosVersion;
	},
	getDdosVersion:function(){
		return this.ddosVersion;
	},
	setLifeCycleState:function(lifeCycleState){
		this.lifeCycleState=lifeCycleState;
	},
	getLifeCycleState:function(){
		return this.lifeCycleState;
	},
	setCategory:function(category){
		this.category=category;
	},
	getCategory:function(){
		return this.category;
	},
	setDomain:function(domain){
		this.domain=domain;
	},
	getDomain:function(){
		return this.domain;
	},
	setComponent:function(component){
		this.component=component;
	},
	getComponent:function(){
		return this.component;
	},
	setImpact:function(impact){
		this.impact=impact;
	},
	getImpact:function(){
		return this.impact;
	},
	setPenetration:function(penetration){
		this.penetration=penetration;
	},
	getPenetration:function(){
		return this.penetration;
	},
	setRemediation:function(remediation){
		this.remediation=remediation;
	},
	getRemediation:function(){
		return this.remediation;
	},
	setLogic:function(logic){
		this.logic=logic;
	},
	getLogic:function(){
		return this.logic;
	},
	setLink:function(link){
		this.link=link;
	},
	getLink:function(){
		return this.link;
	},
	setProduct:function(product){
		this.product=product;
	},
	getProduct:function(){
		return this.product;
	},
	setBugzillaId:function(bugzillaId){
		this.bugzillaId=bugzillaId;
	},
	getBugzillaId:function(){
		return this.bugzillaId;
	},
	addDupe:function(dupe){
		this.dupes.push(dupe);
	},
	getDupes:function(){
		return this.dupes;
	},
	parseJSON:function(json){
		this.setId(json.id);
		this.setDescription(json.description);
		this.setModelNumber(json.modelNumber.substr(1));
		this.setDdosVersion(json.ddosVersion.substr(1));
		this.setLifeCycleState(new LifeCycleState(json.lifeCycleState));
		this.setCategory(new Category(json.category));
		this.setDomain(new Domain(json.domain));
		this.setComponent(new Component(json.component));
		this.setImpact(new Impact(json.impact));
		this.setPenetration(json.penetration);
		this.setRemediation(new Remediation(json.remediation));
		this.setProduct(new Product(json.product));
		this.setLogic(json.logic);
		this.setLink(json.link);
		this.setBugzillaId(json.bugzillaId);
		for(var i=0;i<json.dupes.length;i++){
			var dupe=json.dupes[i];
			this.addDupe(dupe);
		}
	}
};

function LifeCycleState(json){
	this.id=null;
	this.state=null;
	if(json!=undefined){
		this.parseJSON(json);		
	}
}

LifeCycleState.prototype={
	setId:function(id){
		this.id=id;
	},
	getId:function(){
		return this.id;
	},
	setState:function(state){
		this.state=state;
	},
	getState:function(){
		return this.state;
	},
	parseJSON:function(json){
		this.setId(json.id);
		this.setState(json.state);
	}
};

function Category(json){
	this.id=null;
	this.name=null;
	if(json!=undefined){
		this.parseJSON(json);		
	}
}

Category.prototype={
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

function Domain(json){
	this.id=null;
	this.name=null;
	if(json!=undefined){
		this.parseJSON(json);		
	}
}

Domain.prototype={
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

function Component(json){
	this.id=null;
	this.name=null;
	if(json!=undefined){
		this.parseJSON(json);		
	}
}

Component.prototype={
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

function Impact(json){
	this.id=null;
	this.description=null;
	this.details=null;
	this.examples=null;
	
	if(json!=undefined){
		this.parseJSON(json);		
	}
}

Impact.prototype={
	setId:function(id){
		this.id=id;
	},
	getId:function(){
		return this.id;
	},
	setDescription:function(description){
		this.description=description;
	},
	getDescription:function(){
		return this.description;
	},
	setDetails:function(details){
		this.details=details;
	},
	getDetails:function(){
		return this.details;
	},
	setExamples:function(examples){
		this.examples=examples;
	},
	getExamples:function(){
		return this.examples;
	},
	
	parseJSON:function(json){
		this.setId(json.id);
		this.setDescription(json.description);
		this.setDetails(json.details);
		this.setExamples(json.examples);
	}
};

function Remediation(json){
	this.id=null;
	this.url=null;
	this.major=null;
	this.feature=null;
	this.maintenance=null;
	this.patchLevel=null;
	if(json!=undefined){
		this.parseJSON(json);		
	}
}

Remediation.prototype={
	setId:function(id){
		this.id=id;
	},
	getId:function(){
		return this.id;
	},
	setUrl:function(url){
		this.url=url;
	},
	getUrl:function(){
		return this.url;
	},
	setMajor:function(major){
		this.major=major;
	},
	getMajor:function(){
		return this.major;
	},
	setFeature:function(feature){
		this.feature=feature;
	},
	getFeature:function(){
		return this.feature;
	},
	setMaintenance:function(maintenance){
		this.maintenance=maintenance;
	},
	getMaintenance:function(){
		return this.maintenance;
	},
	setPatchLevel:function(patchLevel){
		this.patchLevel=patchLevel;
	},
	getPatchLevel:function(){
		return this.patchLevel;
	},
	setKbaId:function(kbaId){
		this.kbaId=kbaId;
	},
	getKbaId:function(){
		return this.kbaId;
	},
	parseJSON:function(json){
		this.setId(json.id);
		this.setFeature(json.feature);
		this.setMaintenance(json.maintenance);
		this.setMajor(json.major);
		this.setPatchLevel(json.patchLevel);
		this.setUrl(json.url);
		this.setKbaId(json.kbaId);
	}
};

function Product(json){
	this.id=null;
	this.name=null;
	if(json!=undefined){
		this.parseJSON(json);		
	}
}

Product.prototype={
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