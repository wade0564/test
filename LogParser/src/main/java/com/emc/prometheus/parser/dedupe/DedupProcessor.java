package com.emc.prometheus.parser.dedupe;

import java.security.MessageDigest;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class DedupProcessor {
	
	// core function dedup
	public static void dedup(List<Range> existedLogs, List<TsAndMsg> newLogFrags){
		
		if (existedLogs == null || existedLogs.size() == 0) {
			return;
		}
		
		Long existedStartDate = existedLogs.get(0).start.ts;
		Long existedEndDate = existedLogs.get(existedLogs.size() - 1).end.ts;
		Long newStartDate = newLogFrags.get(0).ts;
		Long newEndDate = newLogFrags.get(newLogFrags.size() - 1).ts;
		
		// head
		if (existedStartDate >= newStartDate) {
			if (existedStartDate.compareTo(newEndDate) > 0) {
				write(newLogFrags, 0, newLogFrags.size());
				//update range
				RangeNode start = getNewMsgRangeNodeStart(newLogFrags);
				RangeNode end = getNewMsgRangeNodeEnd(newLogFrags);
				existedLogs.add(new Range(start, end));
				Collections.sort(existedLogs, comparatorRange);
				return;
			} else {
				//write logs in one sec
				writeNewMsgInOneSecAroundBigRange(newLogFrags, existedLogs.get(0).start);
				if(newEndDate.compareTo(existedStartDate) == 0 && newStartDate.compareTo(existedStartDate) == 0){
					return;
				}
				//write before
				if(newStartDate.compareTo(existedStartDate) < 0){
					int lastOneSecBeforeExistedStartDate = -1;
					for(int i = 0; i < newLogFrags.size(); i++){
						if(newLogFrags.get(i).ts.compareTo(existedStartDate) < 0){
							lastOneSecBeforeExistedStartDate = i;
						}
					}
					write(newLogFrags, 0, lastOneSecBeforeExistedStartDate + 1);
					existedLogs.get(0).start = getNewMsgRangeNodeStart(newLogFrags);
				}
			}
		}
				
		// tail
		if(newEndDate.compareTo(existedEndDate) >= 0){
			if (newStartDate.compareTo(existedEndDate) > 0){
				write(newLogFrags, 0, newLogFrags.size());
				//update range
				RangeNode start = getNewMsgRangeNodeStart(newLogFrags);
				RangeNode end = getNewMsgRangeNodeEnd(newLogFrags);
				existedLogs.add(new Range(start, end));
				Collections.sort(existedLogs, comparatorRange);
				return;
			}else{
				//write logs in one sec
				writeNewMsgInOneSecAroundSmallRange(newLogFrags, existedLogs.get(existedLogs.size() - 1).end);
				if(newEndDate.compareTo(existedEndDate) == 0 && newStartDate.compareTo(existedEndDate) == 0){
					return;
				}
				//write after
				if(newEndDate.compareTo(existedEndDate) > 0){
					int firstOneSecAfterExistedEndDate = -1;
					for(int i = 0; i < newLogFrags.size(); i++){
						if(newLogFrags.get(i).ts.compareTo(existedEndDate) > 0){
							firstOneSecAfterExistedEndDate = i;
							break;
						}
					}
					write(newLogFrags, firstOneSecAfterExistedEndDate, newLogFrags.size());
					existedLogs.get(existedLogs.size() - 1).end = getNewMsgRangeNodeEnd(newLogFrags);
				}
			}
		}
		Collections.sort(existedLogs, comparatorRange);
		
		//middle
		for (int i = 0; i < existedLogs.size() - 1; i++) {
			int originExistedSize = existedLogs.size();
			Range beforeRange = existedLogs.get(i);
 			Range afterRange = existedLogs.get(i + 1);
			removeDupeBetweenTwoRange(newLogFrags, existedLogs, beforeRange, afterRange);
			Collections.sort(existedLogs, comparatorRange);
			if(existedLogs.size() > originExistedSize){
				break;
			}else if(existedLogs.size() < originExistedSize){
				i--;
			}
		}
		
		return;
	}
	
	
	static void removeDupeBetweenTwoRange(List<TsAndMsg> newLogFrags, List<Range> existedLogs, Range beforeRange, Range afterRange){
		
		RangeNode smallRange = beforeRange.end;
		RangeNode bigRange = afterRange.start;
		Long newStartDate = newLogFrags.get(0).ts;
		Long newEndDate = newLogFrags.get(newLogFrags.size() - 1).ts;
		Long smallRangeTs = smallRange.ts;
		Long bigRangeTs = bigRange.ts;
		
		if(newStartDate.compareTo(smallRangeTs) > 0 && newEndDate.compareTo(bigRangeTs) < 0 && newEndDate.compareTo(smallRangeTs) >= 0){//first situation
			write(newLogFrags, 0, newLogFrags.size());
			//update range
			RangeNode start = getNewMsgRangeNodeStart(newLogFrags);
			RangeNode end = getNewMsgRangeNodeEnd(newLogFrags);
			existedLogs.add(new Range(start, end));
			return;
		}else if(newStartDate.compareTo(smallRangeTs) <= 0 && newEndDate.compareTo(bigRangeTs) < 0 && newEndDate.compareTo(smallRangeTs) >= 0){//second situation
			// write logs in the sec of smallRange
			writeNewMsgInOneSecAroundSmallRange(newLogFrags, smallRange);
			// write logs between smallRange and bigRange
			if(newEndDate.compareTo(smallRangeTs) > 0){
				int startIndex = -1;
				int endIndex = -1;
				for (int i = 0; i < newLogFrags.size(); i++) {
					if (newLogFrags.get(i).ts.compareTo(smallRangeTs) > 0) {
						startIndex = i;
						break;
					}
				}
				for (int i = newLogFrags.size() - 1; i >= 0; i--) {
					if (newLogFrags.get(i).ts.compareTo(bigRangeTs) < 0) {
						endIndex = i;
						break;
					}
				}
				if (newEndDate.compareTo(bigRangeTs) == 0) {
					write(newLogFrags, startIndex, endIndex + 1);
					// write logs in the sec of bigRange
					writeNewMsgInOneSecAroundBigRange(newLogFrags, bigRange);
				} else {
					write(newLogFrags, startIndex, newLogFrags.size());
					// update range
					RangeNode newBigRange = getNewMsgRangeNodeEnd(newLogFrags);
					RangeNode newSmallRange = beforeRange.start;
					existedLogs.add(new Range(newSmallRange, newBigRange));
					existedLogs.remove(beforeRange);
				}
			}
		}else if(newStartDate.compareTo(smallRangeTs) <= 0 && newEndDate.compareTo(bigRangeTs) >= 0){//third situation
			// write logs in the sec of smallRange
			writeNewMsgInOneSecAroundSmallRange(newLogFrags, smallRange);
			// write logs in the sec of bigRange
			writeNewMsgInOneSecAroundBigRange(newLogFrags, bigRange);
			// write logs between smallRange and bigRange
			int startIndex = -1;
			int endIndex = -1;
			for (int i = 0; i < newLogFrags.size(); i++) {
				if (newLogFrags.get(i).ts.compareTo(smallRangeTs) > 0) {
					startIndex = i;
					break;
				}
			}
			for (int i = newLogFrags.size() - 1; i >= 0; i--) {
				if (newLogFrags.get(i).ts.compareTo(bigRangeTs) < 0) {
					endIndex = i;
					break;
				}
			}
			write(newLogFrags, startIndex, endIndex + 1);
			//update range
			RangeNode newBigRange = afterRange.end;
			RangeNode newSmallRange = beforeRange.start;
			existedLogs.add(new Range(newSmallRange, newBigRange));
			existedLogs.remove(beforeRange);
			existedLogs.remove(afterRange);
		}else if(newStartDate.compareTo(smallRangeTs) > 0 && newEndDate.compareTo(bigRangeTs) >= 0 && newEndDate.compareTo(afterRange.end.ts) < 0){//fourth situation
			//write before
			int lastOneSecBeforeBigRangeTs = -1;
			for(int i = 0; i < newLogFrags.size(); i++){
				if(newLogFrags.get(i).ts.compareTo(bigRangeTs) < 0){
					lastOneSecBeforeBigRangeTs = i;
				}
			}
			write(newLogFrags, 0, lastOneSecBeforeBigRangeTs + 1);
			//write logs in one sec
			writeNewMsgInOneSecAroundBigRange(newLogFrags, bigRange);
			//update range
			if(newStartDate.compareTo(bigRangeTs) < 0){
				RangeNode newSmallRange = getNewMsgRangeNodeStart(newLogFrags);
				RangeNode newBigRange = afterRange.end;
				existedLogs.remove(afterRange);
				existedLogs.add(new Range(newSmallRange, newBigRange));
			}
		}
		return;
	}
	
	
	static void writeNewMsgInOneSecAroundSmallRange(List<TsAndMsg> newLogFrags, RangeNode oldRangeNode){
		//calculate last msg index of the List<TsAndMsg> in one sec
		int lastMsgIndexInOneSec = -1;
		for(int i = 0; i < newLogFrags.size(); i++){
			if(newLogFrags.get(i).ts.compareTo(oldRangeNode.ts) == 0){
				lastMsgIndexInOneSec = i;
			}
		}
		
		Long firstMsgTs = newLogFrags.get(0).ts;
		Long lastMsgTs = newLogFrags.get(newLogFrags.size() - 1).ts;
		
		for(int i = 0; i < newLogFrags.size(); i++){
			TsAndMsg tsAndMsg = newLogFrags.get(i);
			if(tsAndMsg.ts.compareTo(oldRangeNode.ts) == 0){
				if(tsAndMsg.ts.compareTo(firstMsgTs) > 0){
					if(oldRangeNode.index < newLogFrags.size() - i){//situation 1
						write(newLogFrags, i + oldRangeNode.index, lastMsgIndexInOneSec + 1);
					}
					//update range
					oldRangeNode.hash = MD5(newLogFrags.get(lastMsgIndexInOneSec).msg);
					oldRangeNode.index += lastMsgIndexInOneSec - i;
				}else if(tsAndMsg.ts.compareTo(firstMsgTs) == 0){//situation 2&3
					boolean isSame = false;
					int sameMsgIndex = 0;
					int j = lastMsgIndexInOneSec;
					for(; j >= 0; j--){
						if(MD5(newLogFrags.get(j).msg).equals(oldRangeNode.hash)){
							sameMsgIndex = j;
							isSame = true;
						}
					}
					if(isSame){
						write(newLogFrags, sameMsgIndex + 1, lastMsgIndexInOneSec + 1);
						//update range
						if(tsAndMsg.ts.compareTo(lastMsgTs) == 0){//situation 1-2,1-3,2-2,3-1
							oldRangeNode.hash = MD5(newLogFrags.get(newLogFrags.size() - 1).msg);
							oldRangeNode.index += lastMsgIndexInOneSec - i;
						}
					}else{
						write(newLogFrags, 0, lastMsgIndexInOneSec + 1);
						oldRangeNode.hash = MD5(newLogFrags.get(lastMsgIndexInOneSec).msg);
						oldRangeNode.index += lastMsgIndexInOneSec + 1;
					}
				}
				break;
			}
		}
	}
	
	
	static void writeNewMsgInOneSecAroundBigRange(List<TsAndMsg> newLogFrags, RangeNode oldRangeNode){
		//calculate first msg index of List<TsAndMsg> in this sec
		int firstMsgIndexInOneSec = -1;
		for(int i = 0; i < newLogFrags.size(); i++){
			if(newLogFrags.get(i).ts.compareTo(oldRangeNode.ts) == 0){
				firstMsgIndexInOneSec = i;
				break;
			}
		}
		int lastMsgIndexInOneSec = -1;
		for(int i = 0; i < newLogFrags.size(); i++){
			if(newLogFrags.get(i).ts.compareTo(oldRangeNode.ts) == 0){
				lastMsgIndexInOneSec = i;
			}
		}
		
		Long lastMsgTs = newLogFrags.get(newLogFrags.size() - 1).ts;
		
		for(int i = 0; i < newLogFrags.size(); i++){
			TsAndMsg tsAndMsg = newLogFrags.get(i);
			if(tsAndMsg.ts.compareTo(oldRangeNode.ts) == 0){
				if(tsAndMsg.ts.compareTo(lastMsgTs) < 0){
					write(newLogFrags, firstMsgIndexInOneSec, lastMsgIndexInOneSec + oldRangeNode.index + 1);
					//update range
					oldRangeNode.index -= lastMsgIndexInOneSec - firstMsgIndexInOneSec + 1 + oldRangeNode.index;
					oldRangeNode.hash = MD5(newLogFrags.get(firstMsgIndexInOneSec).msg);
				}else if(tsAndMsg.ts.compareTo(lastMsgTs) == 0){
					boolean isSame = false;
					for(int j = lastMsgIndexInOneSec; j >= 0; j--){
						if(MD5(newLogFrags.get(j).msg).equals(oldRangeNode.hash)){
							isSame = true;
							write(newLogFrags, firstMsgIndexInOneSec, j);
							//update range
							oldRangeNode.index -= j - firstMsgIndexInOneSec;
							oldRangeNode.hash = MD5(newLogFrags.get(firstMsgIndexInOneSec).msg);
							break;
						}
					}
					if(!isSame){
						write(newLogFrags, firstMsgIndexInOneSec, lastMsgIndexInOneSec + 1);
						//update range
						oldRangeNode.index -= lastMsgIndexInOneSec - firstMsgIndexInOneSec + 1;
						oldRangeNode.hash = MD5(newLogFrags.get(firstMsgIndexInOneSec).msg);
					}
				}
				break;
			}
		}
	}
	
	
	static RangeNode getNewMsgRangeNodeEnd(List<TsAndMsg> newLogFrags){
		int p = newLogFrags.size() - 1;
		int endIndex = 1;
		while(p >= 1 && newLogFrags.get(p).ts.compareTo(newLogFrags.get(p - 1).ts) == 0){
			p--;
			endIndex++;
		}
		return new RangeNode(newLogFrags.get(newLogFrags.size() - 1).ts, MD5(newLogFrags.get(newLogFrags.size() - 1).msg), endIndex);
	}
	
	
	static RangeNode getNewMsgRangeNodeStart(List<TsAndMsg> newLogFrags){
		Long beginTs = newLogFrags.get(0).ts;
		for(int i = 0; i < newLogFrags.size(); i++){
			if(newLogFrags.get(i).ts.compareTo(beginTs) != 0){
				return new RangeNode(beginTs, MD5(newLogFrags.get(0).msg), -i);
			}
		}
		return new RangeNode(beginTs, MD5(newLogFrags.get(0).msg), -newLogFrags.size());
	}
	
	
	static void write(List<TsAndMsg> newLogFrags, int begin, int end){
		for(int i = begin; i < end; i++){
			newLogFrags.get(i).setToBeWritten(true);
		}
	}
	
	
	public static String MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];

		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();

		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}

		return hexValue.toString();
	}
	
	
	static private Comparator<Range> comparatorRange = new Comparator<Range>() {
		public int compare(Range c1, Range c2) {
			return c1.start.ts.compareTo(c2.start.ts);
		}
	};
}
