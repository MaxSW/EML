package com.xwarner.eml.ide.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;

public class TopRow extends HBox {

	private Button tickButton;

	public ProgressIndicator progress;

	public Label status;

	public TopRow() {
		setPadding(new Insets(5));
		setAlignment(Pos.CENTER_LEFT);

		// Label modelName = new Label("Solow Growth Model");
		Button runButton = new Button("Run");
		runButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				// Events.runButton();
			}
		});
		Button resetButton = new Button("Reset");
		resetButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				// Events.resetButton();
			}
		});

		tickButton = new Button("Tick");
		tickButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				// Events.tickButton();
			}
		});

		progress = new ProgressIndicator(ProgressIndicator.INDETERMINATE_PROGRESS);
		progress.setVisible(false);

		status = new Label("");

		setMargin(runButton, new Insets(0, 2, 0, 2));
		setMargin(resetButton, new Insets(0, 2, 0, 2));
		setMargin(tickButton, new Insets(0, 2, 0, 2));
		setMargin(status, new Insets(0, 2, 0, 5));

		getChildren().addAll(runButton, resetButton, tickButton, status, progress);
	}

}
