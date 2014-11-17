package com.emc.prometheus.parser.dedupe;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DedupeProcessorTest {

	List<TsAndMsg> newDdfsFrags = new ArrayList<>();

	public static void main(String args[]) {
		DedupeProcessorTest t = new DedupeProcessorTest();
		t.test31();
	}
	
	
	void test31(){
		List<Range> existedDdfs = prepareExistedList();
		List<TsAndMsg> newDdfsFrags = prepareTsAndMsgList()
				.prepareTsAndMsgList(new Date(30000), "msg3-0", false)
				.prepareTsAndMsgList(new Date(30000), "msg3-0", false)
				.prepareTsAndMsgList(new Date(30000), "msg3", false)
				.prepareTsAndMsgList(new Date(30000), "msg3-0", false)
				.done();
		DedupProcessor.dedup(existedDdfs, newDdfsFrags);
		printLog(newDdfsFrags);
		printRange(existedDdfs);
	}
	
	void test30(){
		List<Range> existedDdfs = prepareExistedList();
		List<TsAndMsg> newDdfsFrags = prepareTsAndMsgList()
				.prepareTsAndMsgList(new Date(30000), "msg3-0", false)
				.prepareTsAndMsgList(new Date(30000), "msg3-0", false)
				.prepareTsAndMsgList(new Date(30000), "msg3", false)
				.prepareTsAndMsgList(new Date(30000), "msg3-0", false)
				.prepareTsAndMsgList(new Date(30000), "msg3-0", false)
				.prepareTsAndMsgList(new Date(31000), "msg3-0", false)
				.done();
		DedupProcessor.dedup(existedDdfs, newDdfsFrags);
		printLog(newDdfsFrags);
		printRange(existedDdfs);
	}
	
	void test29(){
		List<Range> existedDdfs = prepareExistedList();
		List<TsAndMsg> newDdfsFrags = prepareTsAndMsgList()
				.prepareTsAndMsgList(new Date(30000), "msg3-0", false)
				.done();
		DedupProcessor.dedup(existedDdfs, newDdfsFrags);
		printLog(newDdfsFrags);
		printRange(existedDdfs);
	}
	
	void test28(){
		List<Range> existedDdfs = prepareExistedList();
		List<TsAndMsg> newDdfsFrags = prepareTsAndMsgList()
				.prepareTsAndMsgList(new Date(30000), "msg3-0", false)
				.prepareTsAndMsgList(new Date(30000), "msg3", false)
				.done();
		DedupProcessor.dedup(existedDdfs, newDdfsFrags);
		printLog(newDdfsFrags);
		printRange(existedDdfs);
	}
	
	void test27(){
		List<Range> existedDdfs = prepareExistedList();
		List<TsAndMsg> newDdfsFrags = prepareTsAndMsgList()
				.prepareTsAndMsgList(new Date(20000), "msg2-2", false)
				.done();
		DedupProcessor.dedup(existedDdfs, newDdfsFrags);
		printLog(newDdfsFrags);
		printRange(existedDdfs);
	}
	
	void test26(){
		List<Range> existedDdfs = prepareExistedList();
		List<TsAndMsg> newDdfsFrags = prepareTsAndMsgList()
				.prepareTsAndMsgList(new Date(20000), "msg2", false)
				.prepareTsAndMsgList(new Date(20000), "msg2-2", false)
				.done();
		DedupProcessor.dedup(existedDdfs, newDdfsFrags);
		printLog(newDdfsFrags);
		printRange(existedDdfs);
	}
	
	void test25(){
		List<Range> existedDdfs = prepareExistedList();
		List<TsAndMsg> newDdfsFrags = prepareTsAndMsgList()
				.prepareTsAndMsgList(new Date(8000), "msg1", false)
				.prepareTsAndMsgList(new Date(10000), "msg1", false)
				.prepareTsAndMsgList(new Date(20000), "msg2", false)
				.prepareTsAndMsgList(new Date(24000), "msg1", false)
				.prepareTsAndMsgList(new Date(25000), "msg1", false)
				.prepareTsAndMsgList(new Date(30000), "msg3-0", false)
				.prepareTsAndMsgList(new Date(30000), "msg3", false)
				.prepareTsAndMsgList(new Date(33000), "msg3x", false)
				.prepareTsAndMsgList(new Date(40000), "msg4", false)
				.prepareTsAndMsgList(new Date(44000), "msg4", false)
				.prepareTsAndMsgList(new Date(50000), "msg-0", false)
				.prepareTsAndMsgList(new Date(50000), "msg5", false)
				.prepareTsAndMsgList(new Date(55000), "msg5", false)
				.prepareTsAndMsgList(new Date(60000), "msg6", false)
				.prepareTsAndMsgList(new Date(60000), "msg6-1", false)
				.prepareTsAndMsgList(new Date(61000), "msg6", false)
				.done();
		DedupProcessor.dedup(existedDdfs, newDdfsFrags);
		printLog(newDdfsFrags);
		printRange(existedDdfs);
	}
	
	void test24(){
		List<Range> existedDdfs = prepareExistedList();
		List<TsAndMsg> newDdfsFrags = prepareTsAndMsgList()
				.prepareTsAndMsgList(new Date(8000), "msg1", false)
				.prepareTsAndMsgList(new Date(10000), "msg1", false)
				.prepareTsAndMsgList(new Date(20000), "msg2", false)
				.prepareTsAndMsgList(new Date(24000), "msg1", false)
				.prepareTsAndMsgList(new Date(25000), "msg1", false)
				.prepareTsAndMsgList(new Date(30000), "msg3-0", false)
				.prepareTsAndMsgList(new Date(30000), "msg3", false)
				.prepareTsAndMsgList(new Date(33000), "msg3x", false)
				.prepareTsAndMsgList(new Date(40000), "msg4", false)
				.prepareTsAndMsgList(new Date(44000), "msg4", false)
				.prepareTsAndMsgList(new Date(50000), "msg-0", false)
				.prepareTsAndMsgList(new Date(50000), "msg5", false)
				.prepareTsAndMsgList(new Date(55000), "msg5", false)
				.done();
		DedupProcessor.dedup(existedDdfs, newDdfsFrags);
		printLog(newDdfsFrags);
		printRange(existedDdfs);
	}
	
	void test23(){
		List<Range> existedDdfs = prepareExistedList();
		List<TsAndMsg> newDdfsFrags = prepareTsAndMsgList()
				.prepareTsAndMsgList(new Date(8000), "msg1", false)
				.prepareTsAndMsgList(new Date(10000), "msg1", false)
				.prepareTsAndMsgList(new Date(20000), "msg2", false)
				.prepareTsAndMsgList(new Date(24000), "msg1", false)
				.prepareTsAndMsgList(new Date(25000), "msg1", false)
				.prepareTsAndMsgList(new Date(30000), "msg3-0", false)
				.prepareTsAndMsgList(new Date(30000), "msg3", false)
				.prepareTsAndMsgList(new Date(33000), "msg3x", false)
				.prepareTsAndMsgList(new Date(40000), "msg4", false)
				.prepareTsAndMsgList(new Date(44000), "msg4", false)
				.prepareTsAndMsgList(new Date(50000), "msg-0", false)
				.prepareTsAndMsgList(new Date(50000), "msg5", false)
				.done();
		DedupProcessor.dedup(existedDdfs, newDdfsFrags);
		printLog(newDdfsFrags);
		printRange(existedDdfs);
	}
	
	
	void test22(){
		List<Range> existedDdfs = prepareExistedList();
		List<TsAndMsg> newDdfsFrags = prepareTsAndMsgList()
				.prepareTsAndMsgList(new Date(8000), "msg1", false)
				.prepareTsAndMsgList(new Date(10000), "msg1", false)
				.prepareTsAndMsgList(new Date(20000), "msg2", false)
				.prepareTsAndMsgList(new Date(24000), "msg1", false)
				.prepareTsAndMsgList(new Date(25000), "msg1", false)
				.prepareTsAndMsgList(new Date(30000), "msg3-0", false)
				.prepareTsAndMsgList(new Date(30000), "msg3", false)
				.prepareTsAndMsgList(new Date(33000), "msg3x", false)
				.prepareTsAndMsgList(new Date(40000), "msg4", false)
				.prepareTsAndMsgList(new Date(44000), "msg4", false)
				.prepareTsAndMsgList(new Date(50000), "msg5", false)
				.done();
		DedupProcessor.dedup(existedDdfs, newDdfsFrags);
		printLog(newDdfsFrags);
		printRange(existedDdfs);
	}
	
	void test21(){
		List<Range> existedDdfs = prepareExistedList();
		List<TsAndMsg> newDdfsFrags = prepareTsAndMsgList()
				.prepareTsAndMsgList(new Date(8000), "msg1", false)
				.prepareTsAndMsgList(new Date(10000), "msg1", false)
				.prepareTsAndMsgList(new Date(20000), "msg2", false)
				.prepareTsAndMsgList(new Date(24000), "msg1", false)
				.prepareTsAndMsgList(new Date(25000), "msg1", false)
				.prepareTsAndMsgList(new Date(30000), "msg3-0", false)
				.prepareTsAndMsgList(new Date(30000), "msg3", false)
				.prepareTsAndMsgList(new Date(33000), "msg3x", false)
				.prepareTsAndMsgList(new Date(40000), "msg4", false)
				.prepareTsAndMsgList(new Date(44000), "msg4", false)
				.prepareTsAndMsgList(new Date(44000), "msg4-1", false)
				.done();
		DedupProcessor.dedup(existedDdfs, newDdfsFrags);
		printLog(newDdfsFrags);
		printRange(existedDdfs);
	}
	
	void test20(){
		List<Range> existedDdfs = prepareExistedList();
		List<TsAndMsg> newDdfsFrags = prepareTsAndMsgList()
				.prepareTsAndMsgList(new Date(8000), "msg1", false)
				.prepareTsAndMsgList(new Date(10000), "msg1", false)
				.prepareTsAndMsgList(new Date(20000), "msg2", false)
				.prepareTsAndMsgList(new Date(24000), "msg1", false)
				.prepareTsAndMsgList(new Date(25000), "msg1", false)
				.prepareTsAndMsgList(new Date(30000), "msg3-0", false)
				.prepareTsAndMsgList(new Date(30000), "msg3", false)
				.prepareTsAndMsgList(new Date(33000), "msg3x", false)
				.prepareTsAndMsgList(new Date(40000), "msg4-0", false)
				.done();
		DedupProcessor.dedup(existedDdfs, newDdfsFrags);
		printLog(newDdfsFrags);
		printRange(existedDdfs);
	}
	
	void test19(){
		List<Range> existedDdfs = prepareExistedList();
		List<TsAndMsg> newDdfsFrags = prepareTsAndMsgList()
				.prepareTsAndMsgList(new Date(8000), "msg1", false)
				.prepareTsAndMsgList(new Date(10000), "msg1", false)
				.prepareTsAndMsgList(new Date(20000), "msg2", false)
				.prepareTsAndMsgList(new Date(24000), "msg1", false)
				.prepareTsAndMsgList(new Date(25000), "msg1", false)
				.prepareTsAndMsgList(new Date(30000), "msg3-0", false)
				.prepareTsAndMsgList(new Date(30000), "msg3", false)
				.prepareTsAndMsgList(new Date(33000), "msg3x", false)
				.prepareTsAndMsgList(new Date(40000), "msg4", false)
				.prepareTsAndMsgList(new Date(40000), "msg4-0", false)
				.done();
		DedupProcessor.dedup(existedDdfs, newDdfsFrags);
		printLog(newDdfsFrags);
		printRange(existedDdfs);
	}
	
	void test18(){
		List<Range> existedDdfs = prepareExistedList();
		List<TsAndMsg> newDdfsFrags = prepareTsAndMsgList()
				.prepareTsAndMsgList(new Date(8000), "msg1", false)
				.prepareTsAndMsgList(new Date(10000), "msg1", false)
				.prepareTsAndMsgList(new Date(20000), "msg2", false)
				.prepareTsAndMsgList(new Date(24000), "msg1", false)
				.prepareTsAndMsgList(new Date(25000), "msg1", false)
				.prepareTsAndMsgList(new Date(30000), "msg3-0", false)
				.prepareTsAndMsgList(new Date(30000), "msg3", false)
				.prepareTsAndMsgList(new Date(33000), "msg3x", false)
				.done();
		DedupProcessor.dedup(existedDdfs, newDdfsFrags);
		printLog(newDdfsFrags);
		printRange(existedDdfs);
	}	
	
	void test17(){
		List<Range> existedDdfs = prepareExistedList();
		List<TsAndMsg> newDdfsFrags = prepareTsAndMsgList()
				.prepareTsAndMsgList(new Date(22000), "msg1", false)
				.prepareTsAndMsgList(new Date(22000), "msg1", false)
				.prepareTsAndMsgList(new Date(23000), "msg1", false)
				.prepareTsAndMsgList(new Date(24000), "msg1", false)
				.prepareTsAndMsgList(new Date(25000), "msg1", false)
				.prepareTsAndMsgList(new Date(30000), "msg3-0", false)
				.prepareTsAndMsgList(new Date(30000), "msg3", false)
				.prepareTsAndMsgList(new Date(33000), "msg3x", false)
				.done();
		DedupProcessor.dedup(existedDdfs, newDdfsFrags);
		printLog(newDdfsFrags);
		printRange(existedDdfs);
	}
	
	void test16(){
		List<Range> existedDdfs = prepareExistedList();
		List<TsAndMsg> newDdfsFrags = prepareTsAndMsgList()
				.prepareTsAndMsgList(new Date(18000), "msg2", false)
				.prepareTsAndMsgList(new Date(20000), "msg2", false)
				.prepareTsAndMsgList(new Date(22000), "msg1", false)
				.prepareTsAndMsgList(new Date(23000), "msg1", false)
				.prepareTsAndMsgList(new Date(24000), "msg2", false)
				.prepareTsAndMsgList(new Date(30000), "msg3-0", false)
				.prepareTsAndMsgList(new Date(30000), "msg3", false)
				.prepareTsAndMsgList(new Date(33000), "msg3x", false)
				.done();
		DedupProcessor.dedup(existedDdfs, newDdfsFrags);
		printLog(newDdfsFrags);
		printRange(existedDdfs);
	}
	
	void test15(){
		List<Range> existedDdfs = prepareExistedList();
		List<TsAndMsg> newDdfsFrags = prepareTsAndMsgList()
				.prepareTsAndMsgList(new Date(20000), "msg2", false)
				.prepareTsAndMsgList(new Date(22000), "msg1", false)
				.prepareTsAndMsgList(new Date(23000), "msg1", false)
				.prepareTsAndMsgList(new Date(24000), "msg2", false)
				.prepareTsAndMsgList(new Date(30000), "msg3-0", false)
				.prepareTsAndMsgList(new Date(30000), "msg3", false)
				.prepareTsAndMsgList(new Date(33000), "msg3x", false)
				.done();
		DedupProcessor.dedup(existedDdfs, newDdfsFrags);
		printLog(newDdfsFrags);
		printRange(existedDdfs);
	}
	
	
	void test14(){
		List<Range> existedDdfs = prepareExistedList();
		List<TsAndMsg> newDdfsFrags = prepareTsAndMsgList()
				.prepareTsAndMsgList(new Date(20000), "msg2", false)
				.prepareTsAndMsgList(new Date(22000), "msg1", false)
				.prepareTsAndMsgList(new Date(23000), "msg1", false)
				.prepareTsAndMsgList(new Date(24000), "msg2", false)
				.prepareTsAndMsgList(new Date(30000), "msg3", false)
				.done();
		DedupProcessor.dedup(existedDdfs, newDdfsFrags);
		printLog(newDdfsFrags);
		printRange(existedDdfs);
	}
	
	void test13(){
		List<Range> existedDdfs = prepareExistedList();
		List<TsAndMsg> newDdfsFrags = prepareTsAndMsgList()
				.prepareTsAndMsgList(new Date(20000), "msgx", false)
				.prepareTsAndMsgList(new Date(22000), "msg1", false)
				.prepareTsAndMsgList(new Date(23000), "msg1", false)
				.prepareTsAndMsgList(new Date(24000), "msg2", false)
				.prepareTsAndMsgList(new Date(30000), "msg1", false)
				.prepareTsAndMsgList(new Date(30000), "msgxx", false)
				.done();
		DedupProcessor.dedup(existedDdfs, newDdfsFrags);
		printLog(newDdfsFrags);
		printRange(existedDdfs);
	}
	
	void test12(){
		List<Range> existedDdfs = prepareExistedList();
		List<TsAndMsg> newDdfsFrags = prepareTsAndMsgList()
				.prepareTsAndMsgList(new Date(21000), "msgx", false)
				.prepareTsAndMsgList(new Date(21000), "msg2", false)
				.prepareTsAndMsgList(new Date(22000), "msg1", false)
				.prepareTsAndMsgList(new Date(23000), "msg1", false)
				.prepareTsAndMsgList(new Date(24000), "msg2", false)
				.prepareTsAndMsgList(new Date(25000), "msg1", false)
				.prepareTsAndMsgList(new Date(25000), "msgxx", false)
				.done();
		DedupProcessor.dedup(existedDdfs, newDdfsFrags);
		printLog(newDdfsFrags);
		printRange(existedDdfs);
	}
	
	void test11(){
		List<Range> existedDdfs = prepareExistedList();
		List<TsAndMsg> newDdfsFrags = prepareTsAndMsgList()
				.prepareTsAndMsgList(new Date(20000), "msgx", false)
				.prepareTsAndMsgList(new Date(20000), "msg2", false)
				.prepareTsAndMsgList(new Date(22000), "msg1", false)
				.prepareTsAndMsgList(new Date(23000), "msg1", false)
				.prepareTsAndMsgList(new Date(24000), "msg2", false)
				.prepareTsAndMsgList(new Date(25000), "msg1", false)
				.prepareTsAndMsgList(new Date(25000), "msgxx", false)
				.done();
		DedupProcessor.dedup(existedDdfs, newDdfsFrags);
		printLog(newDdfsFrags);
		printRange(existedDdfs);
	}
	
	void test10(){
		List<Range> existedDdfs = prepareExistedList();
		List<TsAndMsg> newDdfsFrags = prepareTsAndMsgList()
				.prepareTsAndMsgList(new Date(20000), "msgx", false)
				.prepareTsAndMsgList(new Date(22000), "msg1", false)
				.prepareTsAndMsgList(new Date(23000), "msg1", false)
				.prepareTsAndMsgList(new Date(24000), "msg2", false)
				.prepareTsAndMsgList(new Date(25000), "msg1", false)
				.prepareTsAndMsgList(new Date(25000), "msgxx", false)
				.done();
		DedupProcessor.dedup(existedDdfs, newDdfsFrags);
		printLog(newDdfsFrags);
		printRange(existedDdfs);
	}
	
	void test9(){
		List<Range> existedDdfs = prepareExistedList();
		List<TsAndMsg> newDdfsFrags = prepareTsAndMsgList()
				.prepareTsAndMsgList(new Date(15000), "msgx", false)
				.prepareTsAndMsgList(new Date(15000), "msg1", false)
				.prepareTsAndMsgList(new Date(16000), "msg1", false)
				.prepareTsAndMsgList(new Date(20000), "msg2", false)
				.prepareTsAndMsgList(new Date(22000), "msg1", false)
				.prepareTsAndMsgList(new Date(22000), "msgxx", false)
				.done();
		DedupProcessor.dedup(existedDdfs, newDdfsFrags);
		printLog(newDdfsFrags);
		printRange(existedDdfs);
	}
	
	
	void test8(){
		List<Range> existedDdfs = prepareExistedList();
		List<TsAndMsg> newDdfsFrags = prepareTsAndMsgList()
				.prepareTsAndMsgList(new Date(10000), "msgx", false)
				.prepareTsAndMsgList(new Date(10000), "msg1", false)
				.prepareTsAndMsgList(new Date(12000), "msg1", false)
				.prepareTsAndMsgList(new Date(20000), "msg2", false)
				.prepareTsAndMsgList(new Date(22000), "msg1", false)
				.prepareTsAndMsgList(new Date(22000), "msgxx", false)
				.done();
		DedupProcessor.dedup(existedDdfs, newDdfsFrags);
		printLog(newDdfsFrags);
		printRange(existedDdfs);
	}
	
	
	void test7(){
		List<Range> existedDdfs = prepareExistedList();
		List<TsAndMsg> newDdfsFrags = prepareTsAndMsgList()
				.prepareTsAndMsgList(new Date(10000), "msgx", false)
				.prepareTsAndMsgList(new Date(12000), "msg1", false)
				.prepareTsAndMsgList(new Date(20000), "msg2", false)
				.prepareTsAndMsgList(new Date(22000), "msg1", false)
				.prepareTsAndMsgList(new Date(22000), "msgxx", false)
				.done();
		DedupProcessor.dedup(existedDdfs, newDdfsFrags);
		printLog(newDdfsFrags);
		printRange(existedDdfs);
	}
	
	
	void test6(){
		List<Range> existedDdfs = prepareExistedList();
		List<TsAndMsg> newDdfsFrags = prepareTsAndMsgList()
				.prepareTsAndMsgList(new Date(8000), "new-msg-0", false)
				.prepareTsAndMsgList(new Date(8000), "new-msg-1", false)
				.prepareTsAndMsgList(new Date(9000), "new-msg-2", false)
				.prepareTsAndMsgList(new Date(10000), "msg1", false)
				.prepareTsAndMsgList(new Date(12000), "msg1", false)
				.prepareTsAndMsgList(new Date(20000), "msg2", false)
				.prepareTsAndMsgList(new Date(22000), "msg1", false)
				.prepareTsAndMsgList(new Date(22000), "msgxx", false)
				.done();
		DedupProcessor.dedup(existedDdfs, newDdfsFrags);
		printLog(newDdfsFrags);
		printRange(existedDdfs);
	}
	
	void test5(){
		List<Range> existedDdfs = prepareExistedList();
		List<TsAndMsg> newDdfsFrags = prepareTsAndMsgList()
				.prepareTsAndMsgList(new Date(8000), "new-msg-1", false)
				.prepareTsAndMsgList(new Date(9000), "new-msg-2", false)
				.prepareTsAndMsgList(new Date(10000), "msg1", false)
				.prepareTsAndMsgList(new Date(12000), "msg1", false)
				.done();
		DedupProcessor.dedup(existedDdfs, newDdfsFrags);
		printLog(newDdfsFrags);
		printRange(existedDdfs);
	}
	
	void test4(){
		List<Range> existedDdfs = prepareExistedList();
		List<TsAndMsg> newDdfsFrags = prepareTsAndMsgList()
				.prepareTsAndMsgList(new Date(10000), "new-msg-1", false)
				.prepareTsAndMsgList(new Date(10000), "new-msg-2", false)
				.prepareTsAndMsgList(new Date(10000), "new-msg-3", false)
				.prepareTsAndMsgList(new Date(10000), "msg1", false)
				.done();
		DedupProcessor.dedup(existedDdfs, newDdfsFrags);
		printLog(newDdfsFrags);
		printRange(existedDdfs);
	}
	
	void test3(){
		List<Range> existedDdfs = prepareExistedList();
		List<TsAndMsg> newDdfsFrags = prepareTsAndMsgList()
				.prepareTsAndMsgList(new Date(5000), "new-msg-1", false)
				.prepareTsAndMsgList(new Date(8000), "new-msg-2", false)
				.prepareTsAndMsgList(new Date(10000), "new-msg-3", false)
				.prepareTsAndMsgList(new Date(10000), "msg0", false)
				.prepareTsAndMsgList(new Date(10000), "msg0xx", false)
				.prepareTsAndMsgList(new Date(10000), "msg1", false)
				.done();
		DedupProcessor.dedup(existedDdfs, newDdfsFrags);
		printLog(newDdfsFrags);
		printRange(existedDdfs);
	}
	
	void test2(){
		List<Range> existedDdfs = prepareExistedList();
		List<TsAndMsg> newDdfsFrags = prepareTsAndMsgList()
				.prepareTsAndMsgList(new Date(5000), "new-msg-0", false)
				.prepareTsAndMsgList(new Date(5000), "new-msg-1", false)
				.prepareTsAndMsgList(new Date(8000), "new-msg-2", false)
				.prepareTsAndMsgList(new Date(10000), "new-msg-3", false)
				.prepareTsAndMsgList(new Date(10000), "msg1", false)
				.prepareTsAndMsgList(new Date(10000), "msg1xx", false)
				.done();
		DedupProcessor.dedup(existedDdfs, newDdfsFrags);
		printLog(newDdfsFrags);
		printRange(existedDdfs);
	}
	
	
	void test1(){
		List<Range> existedDdfs = prepareExistedList();
		List<TsAndMsg> newDdfsFrags = prepareTsAndMsgList()
				.prepareTsAndMsgList(new Date(5000), "new-msg-1", false)
				.prepareTsAndMsgList(new Date(6000), "new-msg-2", false)
				.prepareTsAndMsgList(new Date(7000), "new-msg-3", false)
				.done();
		DedupProcessor.dedup(existedDdfs, newDdfsFrags);
		printLog(newDdfsFrags);
		printRange(existedDdfs);
	}
	

	List<Range> prepareExistedList() {
		List<Range> existedDdfs = new ArrayList<Range>();

		RangeNode rn1 = new RangeNode(new Date(10000).getTime(), DedupProcessor.MD5("msg1"), -1);
		RangeNode rn2 = new RangeNode(new Date(20000).getTime(), DedupProcessor.MD5("msg2"), 1);
		Range r1 = new Range(rn1, rn2);
		RangeNode rn3 = new RangeNode(new Date(30000).getTime(), DedupProcessor.MD5("msg3"), -1);
		RangeNode rn4 = new RangeNode(new Date(40000).getTime(), DedupProcessor.MD5("msg4"), 1);
		Range r2 = new Range(rn3, rn4);
		RangeNode rn5 = new RangeNode(new Date(50000).getTime(), DedupProcessor.MD5("msg5"), -1);
		RangeNode rn6 = new RangeNode(new Date(60000).getTime(), DedupProcessor.MD5("msg6"), 1);
		Range r3 = new Range(rn5, rn6);
		
		existedDdfs.add(r1);
		existedDdfs.add(r2);
		existedDdfs.add(r3);

		return existedDdfs;
	}

	DedupeProcessorTest prepareTsAndMsgList() {
		newDdfsFrags.clear();
		return this;
	}

	DedupeProcessorTest prepareTsAndMsgList(Date ts, String msg, boolean toBeWritten) {
		TsAndMsg n1 = new TsAndMsg(ts.getTime(), msg, toBeWritten);
		newDdfsFrags.add(n1);
		return this;
	}

	List<TsAndMsg> done() {
		return newDdfsFrags;
	}

	void print(String s) {
		System.out.println(s);
	}

	void printLog(List<TsAndMsg> tsAndMsg) {
		print("Log to be inserted into database: ");
		for (int i = 0; i < tsAndMsg.size(); ++i) {
			print(tsAndMsg.get(i).ts + " - " + tsAndMsg.get(i).msg + " - " + tsAndMsg.get(i).toBeWritten);
		}
	}

	void printRange(List<Range> r) {
		print("Updated range: ");
		for (int i = 0; i < r.size(); ++i) {
			print(r.get(i).start.ts + ":" + r.get(i).start.index + " - " + r.get(i).end.ts +  " : " + r.get(i).end.index);
		}
	}

}
