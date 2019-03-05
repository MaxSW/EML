package com.xwarner.eml.ide.ui;

import com.xwarner.eml.ide.App;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class TopRow extends HBox {

	public Label status;

	public TopRow() {
		setPadding(new Insets(5));
		setAlignment(Pos.CENTER_LEFT);

		// Label modelName = new Label("Solow Growth Model");
		Button runButton = new Button("Run");
		runButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				App.events.runButton();
			}
		});

		status = new Label("");

		setMargin(runButton, new Insets(0, 2, 0, 2));
		setMargin(status, new Insets(0, 2, 0, 5));

		getChildren().addAll(runButton, status);
	}

}
