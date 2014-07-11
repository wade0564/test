var progress_html = "<table style='width:300px;text-align:center'>"
		+ "<tr ><td colspan=2><div class='clock'><span id='info'>elapsed time : </span><span id='hours'>00</span><span id='point'>:</span>"
		+ "<span id='min'>00</span><span id='point'>:</span><span id='sec'>00</span></div></td><tr>"
		+ "<tr><td width='60%'><div id ='progressbar' style='width:128px;height:128px;margin:auto;border:none'><div></td>"
		+ "<td><a id='stop' style='display:none;'>Stop</a></td></tr>"
		+ "<tr><td><span id='message'/></td>"
		+ "<td><span id='lines'/></td></tr></table>";

var listenInterval = 1000;

function monitorExportProgress(exportFileDownloadUrl, exportProgressUrl, exportStopUrl) {

	if(exportProgressUrl == null) return;
	
	var dialog = XUI.createXSimpleDialog("Export Progress", progress_html, [], []);
	var timer = timerClock();
	$("#lines").text("lines : 0");
	$("#message").text("");
	$("#stop").show();
	if (exportStopUrl != null) {
		$("#stop").button().click(function() {
			$.post(exportStopUrl, function() {
				console.log("stop export");
				clearInterval(progressTimer);
				clearInterval(timer);
				dialog.closeDialog();
			});
			dialog.closeDialog();

		});
	} else {
		$("#stop").hide();
	}
	var $progressbar = initProgressBar();
	$.fileDownload(exportFileDownloadUrl);
	var progressTimer = setInterval(_queryProgress, listenInterval);
	var prevValue=0;
	function _queryProgress() {
		$.get(exportProgressUrl, function(data) {
			console.log(data.exportedLines + "  "	+ data.exportProgressPercentage);
			$("#lines").text("lines : "+data.exportedLines);
			$("#message").text(data.message);
			var currentValue=data.exportProgressPercentage;
			var animateFunc = function() {
				prevValue += 5;	    
				if (prevValue > currentValue) {
					prevValue = currentValue;  
				}
				if(prevValue==100){
					clearInterval(progressTimer);
					clearInterval(timer);
					dialog.closeDialog();
				}
				$progressbar.setProgress(prevValue / 100);
				$progressbar.setValue(data.exportProgressFraction);
				if (prevValue < currentValue) {
					setTimeout(animateFunc, 100);
				} 
		    };
			setTimeout(animateFunc, 25);
		});
	}
	

	this.waitAndClose = function() {

		dialog.closeDialog();
	};
	
	return this;
	
}


function monitorTestProgress(testProgressUrl, testStopUrl) {
	if(testProgressUrl == null) return;
	
	var dialog = XUI.createXSimpleDialog("Test Progress", progress_html, [], []);
	
	var timer = timerClock();
	
	$("#message").text("");
	$("#stop").show();
	if (testStopUrl != null) {
		$("#stop").button().click(function() {
			$.post(testStopUrl, function() {
				console.log("stop test");
				clearInterval(progressTimer);
				clearInterval(timer);
				dialog.closeDialog();
			});
			dialog.closeDialog();

		});
	} else {
		$("#stop").hide();
	}
	var closed=false;
	var $progressbar = initProgressBar();
	var progressTimer = setInterval(_queryProgress, listenInterval);
	var prevValue = 0;
	function _queryProgress() {
		$.get(testProgressUrl, function(data) {
			console.log(data.progress + "  " + data.message);
			$("#message").text(data.message);
			var currentValue = data.progress;
			var animateFunc = function() {
				prevValue += 5;	    
				if (prevValue > currentValue) {
					prevValue = currentValue;  
				}
				if(prevValue==100){
					clearInterval(progressTimer);
					clearInterval(timer);
					setTimeout(function(){closed=true;},1000);

				}
				$progressbar.setProgress(prevValue / 100);
				if (prevValue < currentValue) {
					setTimeout(animateFunc, 100);
				} 
		    };
				setTimeout(animateFunc, 25);	
		});
	}
	

	this.close = function(){
		dialog.closeDialog();
	};
	
	this.waitAndClose = function(callback,data) {
		
		var execute =function(){
			if(closed==true){
				  dialog.closeDialog();
				  callback(data);
			}else{
	            setTimeout(execute, 1000);
			}
		};
		setTimeout(execute, 1000);
//		var waiter = setInterval(_swift2Max, 500);
//		function _swift2Max(){
//			prevValue++;
//			$progressbar.setProgress(prevValue++ / 100);
//		}
//		
//		clearInterval(waiter);
//		while(prevValue<100){}
	};
	
	return this;
}

function timerClock(){
	var secondCounter = 0;
	var seconds = 0;
	var minutes = 0;
	var hours = 0;
	var timer = setInterval(function() {
					secondCounter++;
					seconds = parseInt(secondCounter % 60);
					$("#sec").html((seconds < 10 ? "0" : "") + seconds);
					minutes = parseInt(secondCounter / 60);
					if (minutes >= 60) {
						minutes = 0;
					}
					$("#min").html((minutes < 10 ? "0" : "") + minutes);
					hours = parseInt(secondCounter / 3600);
					$("#hours").html((hours < 10 ? "0" : "") + hours);
				}, 1000);
	return timer;
}

function initProgressBar(){
	var progressBar = $("#progressbar").progressbar({ value : 0 });
	progressBar.percentageLoader({
				width : 128,
				height : 128,
				controllable : false,
				progress : 0.0,
				onProgressUpdate : function(val) {
					$progressbar.setValue(Math.round(val * 100.0));
				}
			});
	
	progressBar.setValue("");
	return progressBar;
}

