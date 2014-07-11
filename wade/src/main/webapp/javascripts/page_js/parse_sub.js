$(function(){

	initPageUI();
	initEventCallbacks();
	
});

function initPageUI(){
	$("#parse_sub_btn").button();
	XUI.createXHintableInput("#location","e.g /auto/support/ftpusers/EMC_1csftp/63604784/3FA0729061.tar.gz");
};

function initEventCallbacks(){
	$("#parse_sub_btn").click($.proxy(this._parse_sub,this));
};


function _get_subInfo(){

			var location=$("#location").val();
			var sub =new Sub();
			sub.setLocation(location);
			sub.setSource("customer");
			return sub;
}

function _parse_sub(){
		
		if(this._validateSubStatusFields()==true){
			$("#parse_sub_btn").button({disabled:true}).val("");
			var sub=_get_subInfo();
			var loading_div = XUI.createXLoadingDiv();
			$.ajax({
				type : "post",
				url : "sub/parse",
				contentType : "application/json",
				data: JSON.stringify(sub),
				success : function(data,status) {
					loading_div.hideLoadingDiv();
					var dialog = XUI.createXSimpleDialog("Message",data,["OK"],[function(){dialog.closeDialog();$("#parse_sub_btn").button({disabled:false});}]);
				},
				error: function(xhr){
					loading_div.hideLoadingDiv();
					var dialog = XUI.createXSimpleDialog("Error","Server Error",["Close"],[function(){dialog.closeDialog();$("#parse_sub_btn").button({disabled:false});}]);
					}
			});
		}else{
			XUI.createXDialog("Warning","The SUB file must be under the directory of /auto/support/ or /auto/cores/");
		}
};


function _validateSubStatusFields(){
	var validatorManager = XUI.createXValidatorManager();
	validatorManager.addValidator(XUI.createXValidator("#location", "^/auto/support/.*|^/auto/cores/.*"));
	return validatorManager.validate();
};

