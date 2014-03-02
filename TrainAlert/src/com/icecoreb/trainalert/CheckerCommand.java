package com.icecoreb.trainalert;

import com.icecoreb.trainalert.service.TrainCheckerService;

public enum CheckerCommand {
	UNDEFINED(0) {
		public void executeCommand(TrainCheckerService service) {
			service.stopService();
		}
	},
	START(1) {
		public void executeCommand(TrainCheckerService service) {
			service.startService();
			service.ckeckTrainSchedule();
		}
	},
	STOP(2) {
		public void executeCommand(TrainCheckerService service) {
			service.stopService();
		}
	},
	CHECK_STATE(3) {
		public void executeCommand(TrainCheckerService service) {
			service.notifyStatus();
		}
	},
	CHECK_TRAIN_SCHEDULE(4) {
		public void executeCommand(TrainCheckerService service) {
			service.ckeckTrainSchedule();
		}
	};

	private int value;

	private CheckerCommand(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}

	public static CheckerCommand fromValue(int value) {
		for (CheckerCommand command : CheckerCommand.values()) {
			if (command.value == value) {
				return command;
			}
		}
		return UNDEFINED;
	}

	public abstract void executeCommand(TrainCheckerService service);
}
