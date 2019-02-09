package com.xwarner.eml.tools;

import java.io.File;
import java.io.IOException;

import com.xwarner.eml.parser.InputStream;
import com.xwarner.eml.parser.TokenStream;
import com.xwarner.eml.parser.TokenStream1;
import com.xwarner.eml.parser.TokenStream2;
import com.xwarner.eml.parser.TokenStream3;
import com.xwarner.eml.parser.TokenStream4;
import com.xwarner.eml.parser.tokens.Token;
import com.xwarner.eml.util.IOManager;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

public class ParseAnalyser extends Application {

	private HTMLEditor editor;
	private static String path;
	private String src;

	public void start(Stage stage) throws Exception {
		String[] split = path.split(File.separator);
		String file = split[split.length - 1];
		String rootPath = path.substring(0, path.length() - split[split.length - 1].length());
		src = "";
		IOManager.root = rootPath;
		try {
			src = IOManager.readFile(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		stage.setTitle("Parse Analyser");
		VBox root = new VBox();
		Scene scene = new Scene(root);

		root.getChildren().add(new Label("Token Stream 1:"));

		// VBox.setMargin(editor, new Insets(5));

		editor = new HTMLEditor();
		editor.setPrefHeight(245);
		Node[] nodes = editor.lookupAll(".tool-bar").toArray(new Node[0]);
		for (Node node : nodes) {
			node.setVisible(false);
			node.setManaged(false);
		}
		editor.setHtmlText(generateText());
		editor.setVisible(true);

		root.getChildren().add(editor);

		stage.setScene(scene);
		stage.show();
	}

	public String generateText() {
		TokenStream ts = new TokenStream4(new TokenStream3 (new TokenStream2(new TokenStream1(new InputStream(src)))));

		StringBuilder sb = new StringBuilder();
		sb.append("<html><body contentEditable=\"false\">");
		sb.append("<style>");
		
		sb.append(".token-7{color:red;}");
		
		
		sb.append("</style>");

		while (!ts.done()) {
			Token t = ts.next();
			if (t.value.equals("\\n"))
				sb.append("<br/>");
			else {
				sb.append("<span class=\"token-" + t.type + "\">");
				sb.append(t.src);
				sb.append("</span> ");
			}
		}

		sb.append("</body></html>");
		System.out.println(sb.toString());
		return sb.toString();
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
