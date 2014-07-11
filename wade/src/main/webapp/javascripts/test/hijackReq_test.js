
if (typeof emc !== 'object') { emc = {}; }
if (typeof emc.prometheus !== 'object') { emc.prometheus = {}; }


emc.prometheus.hijackReq.test = new (function() {

	test('QUnit parameters test',function(){
		equal(1,1);
		equal(1,1,true);
		equal(1,2);
		equal(1,2,true);
	});
	
	test('Replace URL',function(){
		equal(emc.prometheus.hijackReq.replaceUrl('user'),'./test/testData/user.json');
		equal(emc.prometheus.hijackReq.replaceUrl('user/1'),'./test/testData/user/index.json');
		equal(emc.prometheus.hijackReq.replaceUrl('user/1/'),'./test/testData/user/index.json');
		equal(emc.prometheus.hijackReq.replaceUrl('user/1/1'),'./test/testData/user/index/index.json');
		equal(emc.prometheus.hijackReq.replaceUrl('user/1/22/333'),'./test/testData/user/index/index/index.json');
		equal(emc.prometheus.hijackReq.replaceUrl('user/1/22/333/role/1'),'./test/testData/user/index/index/index/role/index.json');
	});
	
});


