package com.emc.prometheus.parser.parse.match;public abstract interface Match {	public void match(String target);	public MatchState getMatchState();		public void setMatchState(MatchState state);		public boolean isConsumeTarget();	public void setConsumeTarget(boolean b);}