package com.xwarner.eml.visual;

import java.io.IOException;

import com.xwarner.eml.interpreter.Interpreter;
import com.xwarner.eml.parser.InputStream;
import com.xwarner.eml.parser.Parser;
import com.xwarner.eml.parser.TokenStream1;
import com.xwarner.eml.parser.TokenStream2;
import com.xwarner.eml.parser.TokenStream3;
import com.xwarner.eml.parser.TokenStream4;
import com.xwarner.eml.parser.TokenStream5;
import com.xwarner.eml.parser.Tree;
import com.xwarner.eml.util.IOManager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Visualiser extends Application {

	public static String path;
	public CodePane codePane;
	public TreePane treePane;

	public void start(Stage stage) throws Exception {
		codePane = new CodePane();
		treePane = new TreePane();

		HBox root = new HBox();
		root.getChildren().addAll(codePane, treePane);
		stage.setTitle("EML Visualiser");
		stage.setScene(new Scene(root, 400, 300));
		stage.setMaximized(true);
		stage.show();

		run(path);
	}

	public void run(String path) {
		// Run the code
		String[] split = path.split("/");
		String file = split[split.length - 1];
		String root = path.substring(0, path.length() - split[split.length - 1].length());
		String src = "";
		IOManager.root = root;
		try {
			src = IOManager.readFile(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		TokenStream1 ts = new TokenStream1(new InputStream(src));
		Parser parser = new Parser(new TokenStream5(new TokenStream4(new TokenStream3(new TokenStream2(ts)))));
		Tree tree = parser.parse();
		Interpreter interpreter = new Interpreter(tree);
		interpreter.run();

		codePane.update(src);
		treePane.update(tree);
	}

	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("missing file");
			return;
		}
		path = args[0];
		launch(args);
	}

}
