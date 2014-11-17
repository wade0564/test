package com.emc.prometheus.parser.parse.match;

/** * @author zhangw4 * */public interface ParentMatch extends Match {

	public void addMatch(Match m);

	public void removeMatch(String string);

	public void addMatchAfter(String string, Match parent);

}
