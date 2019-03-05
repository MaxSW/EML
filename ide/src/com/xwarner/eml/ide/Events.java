package com.xwarner.eml.ide;

/**
 * Handles GUI events. The connection between JavaFX and Controller/App
 * 
 * @author max
 *
 */

public class Events {

	public App app;

	public Events(App app) {
		this.app = app;
	}

	public void runButton() {
		App.controller.run();
	}

}
