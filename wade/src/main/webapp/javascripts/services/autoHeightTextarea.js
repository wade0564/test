function resizeAllTextArea(){
		$('.autoHeightTextarea').each(function(){
			if($('#testHeight').size() == 0){
				$('<div id="testHeight"><div>').appendTo('body');
			}
			var jTarget = $(this);
			var jAppend = $('#testHeight');
			var width = jTarget.width();
			jAppend.width(width);
			if(jTarget.val() != ''){
				jAppend.html(jTarget.val());
			}else{
				jAppend.html(jTarget.html());
			}
			var height = jAppend.height();
			jTarget.height(height);
			
			if(!jTarget.data('has-scroll')){
				jTarget.height(jTarget[0].scrollHeight - jTarget[0].clientHeight + jTarget.height());
			}
			
		});
};
function resizeOneTextArea(one){
	$(one).each(function(){
		if($('#testHeight').size() == 0){
			$('<div id="testHeight"><div>').appendTo('body');
		}
		var jTarget = $(this);
		var jAppend = $('#testHeight');
		var width = jTarget.width();
		jAppend.width(width);
		if(jTarget.val() != ''){
			jAppend.html(jTarget.val());
		}else{
			jAppend.html(jTarget.html());
		}
		var height = jAppend.height();
		jTarget.height(height);
		
		if(!jTarget.data('has-scroll')){
			jTarget.height(jTarget[0].scrollHeight - jTarget[0].clientHeight + jTarget.height());
		}
		
	});
};

$(function(){
	
	$('.autoHeightTextarea').live('propertychange keyup input paste', function() {
		var jTarget = $(this);
		if(!jTarget.data('has-scroll')){
			jTarget.height(jTarget[0].scrollHeight - jTarget[0].clientHeight + jTarget.height());
		}
	});
	if(typeof resizeAllTextArea!=="undefined"){
		resizeAllTextArea();		
	}
});