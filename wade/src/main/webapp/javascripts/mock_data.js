$(function(){
	

	$.mockJSON.data.STATUS = ["DUPLICATED", "FILE_NOT_FOUND", "EMAIL_IS_CORRUPTED", "PARSE_FAILED", "PARSE_SUCCESS", "NOT_PARSED_TOO_OLD","READY"];
	$.mockJSON.data.TYPE = ["SUB","ASUP"];
	
	var isAjaxMocked = true;
	if (isAjaxMocked) {
	    $.mockjax({
	        url: 'sub/get/3FA1741815',
	        status: 200,
	        responseTime: 750,
	        responseText: $.mockJSON.generateFromTemplate({
	            "info|30-36": [{
	                "item_id|+1": 1,
	                "ts|+36000000": 1404381481735,
	                "type": "@TYPE",
	                "location": "@DATE_YYYY-@DATE_MM-@DATE_DD.@DATE_DD@DATE_DD@DATE_DD@DATE_DD@DATE_DD@DATE_DD@DATE_DD@DATE_DD@DATE_DD@DATE_DD@DATE_DD@DATE_DD@DATE_DD@DATE_DD@DATE_YYYY-@DATE_MM-@DATE_DD.@DATE_DD@DATE_DD@DATE_DD@DATE_DD@DATE_DD@DATE_DD@DATE_DD@DATE_DD@DATE_DD@DATE_DD@DATE_DD@DATE_DD@DATE_DD@DATE_DD",
	                "status": "@STATUS"
	            }]
	        })
	    });
	}
});
