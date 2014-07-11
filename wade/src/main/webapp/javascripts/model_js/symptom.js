function Symptom(json){
	this.id=null;
//	this.source=null;
	this.name=null;
	this.occurMin=null;
	this.occurMax=null;
	this.duration=null;
	this.description=null;
	this.bugNum=null;
	this.bugs=null;
	if(json!=undefined){
		this.parseJSON(json);		
	}
	if(json!=undefined){
		this.parseJSON(json);		
	}
}

Symptom.prototype={
	setId:function(id){
		this.id=id;
	},
	getId:function(){
		return this.id;
	},
//	setSource:function(source){
//		this.source=source;
//	},
//	getSource:function(){
//		return this.source;
//	},
	setName:function(name){
		this.name=name;
	},
	getName:function(){
		return this.name;
	},
	setOccurMin:function(occurMin){
		this.occurMin=occurMin;
	},
	getOccurMin:function(){
		return this.occurMin;
	},
	setOccurMax:function(occurMax){
		this.occurMax=occurMax;
	},
	getOccurMax:function(){
		return this.occurMax;
	},
	setDuration:function(duration){
		this.duration=duration;
	},
	getDuration:function(){
		return this.duration;
	},
	setDescription:function(description){
		this.description=description;
	},
	getDescription:function(){
		return this.description;
	},
	getBugNum:function(){
		return this.bugNum;
	},
	setBugNum:function(bugNum){
		this.bugNum=bugNum;
	},
	getBugs:function(){
		return this.bugs;
	},
	setBugs:function(bugs){
		this.bugs=bugs;
	},
	parseJSON:function(json){
		this.setId(json.id);
//		this.setSource(new SymptomSource(json.source));
		this.setName(json.name);
		this.setOccurMin(json.occurMin);
		this.setOccurMax(json.occurMax);
		this.setDuration(json.duration);
		this.setDescription(json.description);
		this.setBugNum(json.bugNum);
		this.setBugs(json.bugs);
	}
};

//function SymptomSource(json){
//	this.id=null;
//	this.name=null;
//	if(json!=undefined){
//		this.parseJSON(json);
//	}
//}
//
//SymptomSource.prototype={
//	setId:function(id){
//		this.id=id;
//	},
//	getId:function(){
//		return this.id;
//	},
//	setName:function(name){
//		this.name=name;
//	},
//	getName:function(){
//		return this.name;
//	},
//	parseJSON:function(json){
//		this.setId(json.id);
//		this.setName(json.name);
//	}
//};

function SysNameToPattern(json){
	this.id=null;
	this.name=null;
	this.pattern=null;
	this.schedule=null;
	if(json!=undefined){
		this.parseJSON(json);		
	}
}

SysNameToPattern.prototype={
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
	setPattern:function(pattern){
		this.pattern=pattern;
	},
	getPattern:function(){
		return this.pattern;
	},
	setSchedule:function(schedule){
		this.schedule=schedule;
	},
	getSchedule:function(){
		return this.schedule;
	},
	parseJSON:function(json){
		this.setId(json.id);
		this.setName(json.name);
		this.setPattern(json.pattern);
		this.setSchedule(json.schedule);
	}
};