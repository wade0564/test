$(function(){
	var id=$.cookie("user.id");
	$("#photo").attr("src","image/"+id);
	$("#info_list table td:eq(0)").text(id);
	var iscomplete=false;
	
	CALLBACK ={
			callback :function (){
				iscompleted = true;
				$("#info_list table td:eq(1)").text($.cookie("user.name"));
				var email=$.cookie("user.email");
				$("#info_list table td:eq(2)").text(email);
//				$.get("email/exists",{data:email},function(data){
				var user = new User(JSON.parse($.cookie("user")));
				var ischecked=false;
				var finalValue;
				if(user.getOptions()!=null || user.getOptions()!=undefined){
					for(var i=0;i<user.getOptions().length;i++){
						if(user.getOptions()[i].getOption().getId()=="1"){
							if(user.getOptions()[i].getValue()=="true")
								ischecked=true;
							break;
						}
					}
				}	
				
				if(ischecked){
					$("#receive_yes").attr("checked","checked");
						}
				else{
					$("#receive_no").attr("checked","checked");
					}
			}
	};
	
	var timer= setTimeout(GLOBAL.checkUserCookieReady,200);
//	clearInterval(timer);
//	while(iscomplete){
//	
//	}
	
	

	$("#save").button().click(function(){
		if($("#receive_no").is(":checked")&& ischecked){
//			$.post("email/receiver/remove",{email:email},function(data){
//				XUI.createXDialog("Information","Save completed!",[],[]);
//			});
			
			for(var i=0;i<user.getOptions().length;i++){
				if(user.getOptions()[i].getOption().getId()=="1"){
					finalValue="false";
					user.getOptions()[i].setValue("false");
					break;
				}
			}
			
		}
		else if ($("#receive_yes").is(":checked")&& !ischecked){
//			$.post("email/receiver/add",{email:email},function(data){
//				XUI.createXDialog("Information","Save completed!",[],[]);
//			}); 
			var isExist=false;
			for(var i=0;i<user.getOptions().length;i++){
				if(user.getOptions()[i].getOption().getId()=="1"){
					isExist=true;
					user.getOptions()[i].setValue("true");
					finalValue="true";
					break;
				}
			}
			if(!isExist){
				finalValue="true";
			}
			
		}
		else{
			XUI.createXDialog("Information", "Save completed!",[],[]);
			return;
		}
		$.removeCookie('user');
		$.ajax({
			 
			type:"put",
			url:"users/"+ id+"/options/"+1,
			data:finalValue,
			success:function(data){
				XUI.createXDialog("Information", "Save completed!",[],[]);
				ischecked= !ischecked;
				$.cookie("user",JSON.stringify(data));
			},
			error:function(data,status, xhr){
				xhr.responseText="";
				XUI.createXDialog("Error", xhr.getResponseHeader("msg"), [], []);
			}
		});
	});
	
	

});