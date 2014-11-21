package com.emc.prometheus.parser.parse.match;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.emc.prometheus.parser.util.EscapeUtils;
import com.google.common.base.Joiner;

/** 
 * @author wade 
 * @version Nov 21, 2014 10:10:26 AM 
 */
public class IndivMatchMerge extends IndivMatchSimple {

	
	List<String> mergedBuffer= new ArrayList<>();
	
	public IndivMatchMerge(String name, Pattern reg, List<String> matchResutls) {
		super(name, reg, matchResutls);
	}

	@Override
	public void match(String target) {
		
		assert target!=null;
		Matcher matcher = reg.matcher(target);
		
		//TODO maybe there more group 
		if(matcher.find()){
			//section end need not store
			if(matchResutls!=null){
				mergedBuffer.add(matcher.group(1));
			}
			this.setMatchState(MatchState.SUCCESS);
		}else {
			//if the individual match not matched ,fail finished 
			String mergedLogs = Joiner.on(EscapeUtils.ENTER_ESCAPE).join(mergedBuffer);
			if(!matchResutls.isEmpty()){
				String LastLog = matchResutls.get(matchResutls.size()-1);
				LastLog+=mergedLogs;
				matchResutls.set(matchResutls.size()-1, LastLog);
			}else{
				if(!mergedLogs.trim().isEmpty()){
					matchResutls.add(mergedLogs);
				}
			}
			mergedBuffer.clear();
			this.setMatchState(MatchState.FAIL);
		}
	}

}
