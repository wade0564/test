package com.emc.prometheus.parser.parse.match;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
* @author wade 
* @version Nov 11, 2014 8:04:28 PM 
*/

public class IndivMatchSimple implements Match {
	
	private Pattern reg;
	
	private List<String> matchResutls;
	
	private MatchState state = MatchState.NOT_STARTED;
	
	private boolean consumeTarget;
	
	String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public IndivMatchSimple(String name,Pattern reg) {
		this.name = name;
		this.reg = reg;
	}

	public IndivMatchSimple(String name,Pattern reg,List<String> matchResutls) {
		this.reg = reg;
		this.name =name;
		this.matchResutls = matchResutls;
	}
	

	public List<String> getMatchResutls() {
		return matchResutls;
	}
	
	public Pattern getReg() {
		return reg;
	}


	@Override
	public void match(String target) {
		
		assert target!=null;
		Matcher matcher = reg.matcher(target);
		
		
		//TODO maybe there more group 
		if(matcher.find()){
			//section end need not store
			if(matchResutls!=null){
				matchResutls.add(matcher.group(1));
			}
			
			this.setMatchState(MatchState.SUCCESS);
			
		}else {
			//if the individual match not matched ,finished 
			this.setMatchState(MatchState.FAIL);
		}
		
		
	}

	public void setMatchResutls(List<String> matchResutls) {
		this.matchResutls = matchResutls;
	}


	public void setReg(Pattern reg) {
		this.reg = reg;
	}


	@Override
	public MatchState getMatchState() {
		return this.state;
	}

	@Override
	public void setMatchState(MatchState state) {
		this.state  = state;
	}

	@Override
	public boolean isConsumeTarget() {
		return this.consumeTarget;
	}

	@Override
	public void setConsumeTarget(boolean b) {
		this.consumeTarget= b;
	}
	
} 
