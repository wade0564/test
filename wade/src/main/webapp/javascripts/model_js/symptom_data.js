function SymptomData(json){	
	this.id=null;
	this.name=null;
	this.type=null;
	this.parent=null;
	this.dataType=null;
	this.table=null;
	this.columnTargeted=null;
	this.columnRequired=null;
	this.comment=null;
	this.deduplication=null;
	this.symptomType=null;
	this.children=[];
	if(json!=undefined){
		this.parseJSON(json);
	}
}

SymptomData.prototype={
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
		setType:function(type){
			this.type=type;
		},
		getType:function(){
			return this.type;
		},
		setParent:function(parent){
			this.parent=parent;
		},
		getParent:function(){
			return this.parent;
		},
		setDataType:function(dataType){
			this.dataType=dataType;
		},
		getDataType:function(){
			return this.dataType;
		},
		setTable:function(table){
			this.table=table;
		},
		getTable:function(){
			return this.table;
		},
		setColumnRequired:function(columnRequired){
			this.columnRequired=columnRequired;
		},
		getColumnRequired:function(){
			return this.columnRequired;
		},
		setColumnTargeted:function(columnTargeted){
			this.columnTargeted=columnTargeted;
		},
		getColumnTargeted:function(){
			return this.columnTargeted;
		},
		setComment:function(comment){
			this.comment=comment;
		},
		getComment:function(){
			return this.comment;
		},
		setDeduplication:function(deduplication){
			this.deduplication=deduplication;
		},
		getDeduplication:function(){
			return this.deduplication;
		},
		setSymptomType:function(symptomType){
			this.symptomType=symptomType;
		},
		getSymptomType:function(){
			return this.symptomType;
		},
		addChild:function(child){
			this.children.push(child);
		},
		getChildren:function(){
			return this.children;
		},
		
		parseJSON:function(json){
			this.setId(json.id);
			this.setName(json.name);
			this.setType(json.type);
			this.setParent(json.parent);
			this.setDataType(json.dataType);
			this.setTable(json.table);
			this.setColumnTargeted(json.columnTargeted);
			this.setColumnRequired(json.columnRequired);
			this.setComment(json.comment);
			this.setDeduplication(json.deduplication);
			this.setSymptomType(json.symptomType);
			for(var i=0;i<json.children.length;i++){
				var child=json.children[i];
				this.addChild(child);
			}
		}
};


function Node(sd){
	this.id=sd.getId();
	this.text=sd.getName();
	this.children=[];
	this.title="Click for more detail.";
};
