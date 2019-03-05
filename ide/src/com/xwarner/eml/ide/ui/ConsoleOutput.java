package com.xwarner.eml.ide.ui;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class ConsoleOutput extends VBox {

	public TextArea text;
	public PrintStream listener;

	public ConsoleOutput() {
		text = new TextArea();
		text.setEditable(false);
		getChildren().add(text);

		listener = new PrintStream(new OutputStream() {
			public void write(int b) throws IOException {
				text.appendText(String.valueOf((char) b));
			}
		});

	}

}
