if (typeof emc !== 'object') { emc = {}; }
if (typeof emc.prometheus !== 'object') { emc.prometheus = {}; }
if (typeof emc.prometheus.symptonScan !== 'object') { emc.prometheus.symptonScan = {}; }

emc.prometheus.symptonScan.copyLogic = function(initData){
	
	var sortAllBug = function(){
		initData.sort(function(a,b){
			return a.bugzillaId - b.bugzillaId;
		});
	};
	var renderCaseNumber = function(){
		var val = $('#case_number_input').val();
		if(val !== null && val != ''){
			return '<strong><div>Case Number: ' + val + '</div></strong>';
		}
		return '';
	};
	
	var rendBugSummary = function(){
		if(initData == null || initData.length == 0)return '';
		var dLength = initData.length;
		var totalBug = 0;
		for(var i=0;i<dLength;i++){
			var oneBug = initData[i];
			totalBug = totalBug + parseInt(oneBug.matchedBugs,10);
		}
		return '<strong><div>SCAN RESULT: ' + singleOrMutilpe({len:totalBug,single:"bug",mutiple:"bugs"}) + ' of ' + singleOrMutilpe({len:dLength,single:"kind",mutiple:"kinds",showLinkVerb:true}) + ' found</div></strong>';
	};
	
	var checkGenInfoDuplicate = function(index,genInfo){
		var version = genInfo[index].VERSION;
		var isDuplicate = false;
		for(var i = 0;i<index;i++){
			if(version == genInfo[i].VERSION){
				isDuplicate = true;
				break;
			}
		}
		return isDuplicate;
	};
	
	var genInfoStr = function(genInfoObj,titleStr){
		return '========== GENERAL INFO ========== ' + titleStr + '<br>'
				+ 'GENERATED_ON=' + genInfoObj.generated_ON + '<br>'
				+ 'GENERATED_EPOCH_TIME=' + genInfoObj.generated_EPOCH_TIME + '<br>'
				+ 'TIME_ZONE=' + genInfoObj.time_ZONE + '<br>'
				+ 'VERSION=' + genInfoObj.version + '<br>'
				+ 'SYSTEM_ID=' + genInfoObj.system_ID + '<br>'
				+ 'SERVICE_TAG=' + genInfoObj.service_TAG + '<br>'
				+ 'MODEL_NO=' + genInfoObj.model_NO + '<br>'
				+ 'HOSTNAME=' + genInfoObj.hostname + '<br>';
	};
	

	var renderRemediation = function(remediation,bugId, gaList, nonGaList){
		var remediationStr = 'BUG ' + bugId + ' SIGNATURE:<br>'
							+'Fixed In (GA) :<br>' + gaList + '<br>'
							+'Fixed In (non-GA) :<br>' + nonGaList + '<br>'
							+ 'Knowledge Base Article ' + '<a href="' + remediation.url + '" target="_blank">'+remediation.kbaId + '</a>';
		return '<p class="remediation">' + remediationStr + '</p>';
	};
	
	var renderBugLogs = function(bugLogsList){
		if(bugLogsList == null)return '';
		var bugLogsStr = '';
		for(var i = 0;i<bugLogsList.length;i++){
			bugLogsStr = bugLogsStr + bugLogsList[i] + '<br>';
		}
		return '<p class="bugLogs">' + bugLogsStr.replace(/\r\n/g, "<BR>") + '</p>';
	};
	
	var renderBugLogsList = function(matchedTimeStamp,timeFlag){
		var str = '';
		var lineBreakSymbol = '<div>--------------------------------</div>';
		for(var i=0;i<matchedTimeStamp.length;i++){
			var matched = matchedTimeStamp[i];
			if(timeFlag == null){
				str = str + lineBreakSymbol + renderBugLogs(matched.bugLogs);
			}else{
				if(isSameDate(matched.start,timeFlag)){
					str = str + lineBreakSymbol+ renderBugLogs(matched.bugLogs);
				}
			}
		}
		return str;
	};
	//{len:123,single:bug,mutiple:bugs,showLinkVerb:true}
	var singleOrMutilpe = function(obj){
		if(obj.linkVerb == null)obj.linkVerb = '';
		if(obj.len > 1){
			if(obj.showLinkVerb)return obj.len + ' ' + obj.mutiple + ' are';
			return obj.len + ' ' + obj.mutiple;
		}else{
			if(obj.showLinkVerb)return obj.len + ' ' + obj.single + ' is';
			return obj.len + ' ' + obj.single;
		}
	};
	
	var copySortedByBugId = function(bugObj){
		
		if(initData == null || initData.length == 0)return;
		var renderGroup = function(bug){
			var detail = function(bug){
				if(bug.matchedTimeStamp == null || bug.matchedTimeStamp.lenght == 0)return '';
				var timeListStr = '';
				$.each(bug.matchedTimeStamp,function(i,time){
					timeListStr = timeListStr + formateTimeToStr(time.start) + '<br>';
				});
				return '<ul class="detail">' + timeListStr + '</ul>';
			};
			
			var group = '<strong class="group">\
				<div>BUG ' + bug.bugzillaId + ': ' + singleOrMutilpe({len:bug.matchedBugs,single:"match",mutiple:"matches"}) + ' (The latest ' + singleOrMutilpe({len:bug.matchedTimeStamp.length,single:"bug",mutiple:"bugs",showLinkVerb:true}) + ' shown)<br>\
				DESCRIPTION : '+ bug.description+'</div>'
				+ detail(bug) + 
			'</strong>';
			
			return group;
		};
		
		var renderGenInfo = function(genInfoObj,bugId){
			var titleStr = '(for Bug ' + bugId + ')';
			return '<p class="genInfo">' + genInfoStr(genInfoObj,titleStr) + '</p>';
		};
		
		var renderGenInfoList = function(genInfoList,bugId){
			var genInfoListStr = '';
			$.each(genInfoList,function(i,genInfo){
				if(!checkGenInfoDuplicate(i,genInfoList)){
					genInfoListStr = genInfoListStr + renderGenInfo(genInfo,bugId);
				}
			});
			return genInfoListStr;
		};
		
		var renderBug = function(bugObj){
			var gaStr="";
			for(var i=0;i<bugObj.fixedGA.length;i++){
				gaStr+=bugObj.fixedGA[i].version+"<br>";
			}
			var nonGaStr="";
			for(var i=0;i<bugObj.fixedNonGA.length;i++){
				nonGaStr+=bugObj.fixedNonGA[i].version+"<br>";
			}
			return '<div>' + renderGroup(bugObj) + renderGenInfoList(bugObj.genInfo,bugObj.bugzillaId) + renderRemediation(bugObj.remediation,bugObj.bugzillaId,gaStr,nonGaStr) + renderBugLogsList(bugObj.matchedTimeStamp) +'</div>';
		};
		return renderBug(bugObj);
	};
	
	var copySortedByDate = function(){
		//renderGroup
		var renderGroup = function(dateBugIdArray){
			var oneDetail = function(oneDate){
				var oneBugListStr = '';
				$.each(oneDate.ids,function(i,bugId){
					oneBugListStr = oneBugListStr + 'BUG ' + bugId + '<br>';
				});
				return '<div>' + formateTimeToStr(oneDate.date) + '</div><ul class="detail">' + oneBugListStr + '</ul>';
			};
			var bugListStr = '';
			$.each(dateBugIdArray,function(i,oneDate){
				bugListStr = bugListStr + oneDetail(oneDate);
			});
			var group = '<strong class="group">\
							<div>DATE MATCHES: ' + singleOrMutilpe({len:dateBugIdArray.length,single:"day",mutiple:"days"}) + '</div>'
							+ bugListStr + 
						'</strong>';
			return group;
		};
		
		var renderGenInfo = function(genInfoOne,date){
			var titleStr = '(Bugs on ' + formateTimeToStr(date) + ')';
			return '<p class="genInfo">' + genInfoStr(genInfoOne,titleStr) + '</p>';
		};
		
		var renderGenInfoByBugId = function(oneDate){
			var firstId = oneDate.ids[0];
			for(var i=0;i<initData.length;i++){
				var oneBug = initData[i];
				if(firstId == oneBug.bugzillaId){
					return renderGenInfo(oneBug.genInfo[0],oneDate.date);
				}
			}
			return '';
		};
		var renderRemediationByBugId = function(oneDate){
			var remediationStr = '';
			var ids = oneDate.ids;
			for(var i=0;i<ids.length;i++){
				var idInDateArray = ids[i];
				for(var j= 0;j<initData.length;j++){
					if(idInDateArray == initData[j].bugzillaId){
						var gaStr="";
						for(var m=0;m<initData[j].fixedGA.length;m++){
							gaStr+=initData[j].fixedGA[m].version+"<br>";
						}
						var nonGaStr="";
						for(var n=0;n<initData[j].fixedNonGA.length;n++){
							nonGaStr+=initData[j].fixedNonGA[n].version+"<br>";
						}
						remediationStr = remediationStr + renderRemediation(initData[j].remediation, initData[j].bugzillaId,gaStr,nonGaStr) + renderBugLogsList(initData[j].matchedTimeStamp,oneDate.date);
						break;
					}
				}
			}
			return remediationStr;
		};
		
		var renderOneDayBug = function(oneDate){
			return renderGenInfoByBugId(oneDate) + renderRemediationByBugId(oneDate);
		};
		var renderBugList = function(){
			var genInfoAndRemediaitonStr  = '';
			$.each(dateBugIdArray,function(i,oneDate){
				genInfoAndRemediaitonStr = genInfoAndRemediaitonStr + renderOneDayBug(oneDate);
			});
			return genInfoAndRemediaitonStr;
		};
		
		var dateBugIdArray = createNewArraySortedByDate(initData);
		return renderGroup(dateBugIdArray) + renderBugList();
		
	};
	
	var monthSimple = [ "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
	var isSameDate = function(d1,d2){
		var date1 = new Date(d1);
		var date2 = new Date(d2);
		if(date1.getFullYear() == date2.getFullYear() && date1.getMonth() == date2.getMonth() && date1.getDate() == date2.getDate()){
			return true;
		}else{
			return false;
		}
	};
	var formateTimeToStr = function(date){
		var d = new Date(date);
		return d.getDate() + ' ' + monthSimple[d.getMonth()] + ' ' + d.getFullYear();
	};
	
	//[{date:'2013--05-16',ids:[11,12,13,14,15]},{date:'2013--05-17',ids:[15]}]
	//matchedTimeStamp
	var checkDateExit = function(array,oneBug){
		var matched = oneBug.matchedTimeStamp;
		var bugId = oneBug.bugzillaId;
		for(var startIndex = 0; startIndex < matched.length; startIndex ++){
			var startTime = matched[startIndex].start;
			var inArray = false;
			for(var i = 0;i<array.length;i++){
				var oneDate = array[i]; //{date:'2013--05-16',ids:[11,12,13,14,15]}
				if(isSameDate(oneDate.date,startTime)){
					var ids = oneDate.ids;
					var inIds = false;
					for(var k = 0; k<ids.length;k++){
						if(bugId == ids[k]){
							inIds = true;
							break;
						}
					}
					if(!inIds){
						ids.push(bugId);
					}
					inArray = true;
					break;//break for check the sameDate loop;
				}
			}
			if(!inArray){
				var oneDate = {};
				oneDate.date = startTime;
				oneDate.ids = [bugId];
				array.push(oneDate);
			}
		}
	};
	
	var createNewArraySortedByDate = function(){
		var dateBugIdArray = [];
		$.each(initData,function(i,oneBug){
			checkDateExit(dateBugIdArray,oneBug);
		});
		return dateBugIdArray;
	};
	
	
	this.renderOneBug = function(bugObj){
		return renderCaseNumber() + copySortedByBugId(bugObj);
	};
	
	this.renderAllById = function(){
		sortAllBug();
		var bugStr = '';
		$.each(initData,function(i,bug){
			bugStr = bugStr +  copySortedByBugId(bug);
		});
		return renderCaseNumber() + rendBugSummary() + bugStr;
	};
	
	this.renderBugsByDate = function(){
		sortAllBug();
		return renderCaseNumber() + rendBugSummary() + copySortedByDate();
	};
	
};

//$(function(){
//	$.ajax({url:'./testData/scan1.json',success:function(data){
//		var copy = new emc.prometheus.symptonScan.copyLogic(data);
//		//var copyStr = copy.renderAllById();
//		var copyStr = copy.renderBugsByDate();
//		$('.copy').html(copyStr);
//	}});
////	$.ajax({url:'./testData/scan2.json',success:function(data){
////		var copy = new emc.prometheus.symptonScan.copyLogic(data);
////		var copyStr = copy.renderOneBug(data);
////		$('.copy').html(copyStr);
////	}});
//})
