package com.xwarner.eml.ide.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;;

public class MainMenu extends MenuBar {

	public MainMenu() {
		Menu menuFile = new Menu("File");
		MenuItem fileNewProject = new MenuItem("New Project");
		fileNewProject.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				// Events.newModel();
			}
		});
		MenuItem fileOpenProject = new MenuItem("Open Project");
		fileOpenProject.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				// Events.openModel();
			}
		});
		MenuItem fileNewFile = new MenuItem("New File");
		fileNewFile.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				// Events.newModel();
			}
		});
		MenuItem fileOpenFile = new MenuItem("Open File");
		fileOpenFile.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				// Events.openModel();
			}
		});
		MenuItem fileSaveFile = new MenuItem("Save File");
		fileSaveFile.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				// Events.saveModel();
			}
		});
		menuFile.getItems().addAll(fileNewProject, fileOpenProject, new SeparatorMenuItem(), fileNewFile, fileOpenFile,
				fileSaveFile);

		Menu menuHelp = new Menu("Help");
		MenuItem helpGuide = new MenuItem("User Guide");
		helpGuide.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
			}
		});
		MenuItem helpAbout = new MenuItem("About");
		helpAbout.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent e) {
				// new AboutWindow();
			}
		});
		menuHelp.getItems().addAll(helpGuide, helpAbout);
		getMenus().addAll(menuFile, menuHelp);
	}

}
