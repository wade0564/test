package com.emc.prometheus.parser.parse.match;

public enum MatchState {

	NOT_STARTED,

	IN_PROGRESS_START,

	IN_PROGRESS_CHILDREN,

	IN_PROGRESS_END,

	MIS,

	FAIL,

	SUCCESS, //individual match success

	START_SUCCESS, //parent start match success

	BODY_SUCCESS, //parent start match success
	
	SUCCESS_FINISHED, //parent match success
	
	SUCCESS_NO_CONSUME_FINISHED, //parent match success
	
	FAIL_FINISHED;

	
	public boolean isSuccessful() {

		boolean isSuccessful = false;
		switch (this) {
		case SUCCESS:
//		case START_SUCCESS:
//		case BODY_SUCCESS:
		case SUCCESS_FINISHED:
			isSuccessful = true;
			break;
		default:
			break;
		}
		return isSuccessful;

	}
	
	public boolean isFinished() {

		boolean isFinished = false;
		switch (this) {
		case SUCCESS_FINISHED:
		case FAIL_FINISHED:
			isFinished = true;
			break;
		default:
			break;
		}
		return isFinished;

	};

}