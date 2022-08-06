package com.xwarner.eml.ide;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.xwarner.eml.ide.ui.ConsoleOutput;
import com.xwarner.eml.ide.ui.MainMenu;
import com.xwarner.eml.ide.ui.ProjectTree;
import com.xwarner.eml.ide.ui.TopRow;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The GUI manager and entry point
 */

public class App extends Application {

	public TopRow topRow;
	public MainMenu menu;
	public ProjectTree tree;
	public TabPane tabPane;
	public ConsoleOutput console;

	public static ExecutorService threadPool;

	public static Events events;
	public static Controller controller;

	public void start(Stage stage) {
		threadPool = Executors.newFixedThreadPool(3);

		topRow = new TopRow();
		menu = new MainMenu();
		tabPane = new TabPane();
		tree = new ProjectTree();
		console = new ConsoleOutput();

		VBox root = new VBox();
		root.getChildren().add(menu);
		root.getChildren().add(topRow);
		HBox root2 = new HBox();
		root2.getChildren().addAll(tree, tabPane);
		HBox.setHgrow(tabPane, Priority.ALWAYS);
		root.getChildren().add(root2);
		VBox.setVgrow(root2, Priority.ALWAYS);
		root.getChildren().add(console);

		Scene scene = new Scene(root, 1024, 600);

		stage.setTitle("EML IDE");
		stage.setScene(scene);
		stage.show();

		events = new Events(this);
		controller = new Controller(this);

		startup();
	}

	public void startup() {
		controller.openFile(new File("D:\\Dropbox\\Personal Workspace\\Projects\\eml\\ide\\dev\\dev.eml"));
	}

	public static void main(String[] args) {
		launch(args);
	}

}
