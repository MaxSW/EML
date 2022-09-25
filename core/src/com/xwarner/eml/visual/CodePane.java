package com.xwarner.eml.visual;

import javafx.scene.control.TextArea;

public class CodePane extends TextArea {

	public CodePane() {
		setEditable(false);
	}

	public void update(String code) {
		setText(code);
	}

}
