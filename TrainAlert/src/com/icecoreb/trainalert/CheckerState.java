package com.icecoreb.trainalert;

public enum CheckerState {
	started, stopped;

	public CheckerState switchState() {
		if (this.equals(started)) {
			return stopped;
		}
		return started;
	}

	public int getStringMessageId() {
		int msgId;
		if (this.equals(started)) {
			msgId = R.string.CHECK;
		} else {
			msgId = R.string.STOP;
		}
		return msgId;
	}
}
