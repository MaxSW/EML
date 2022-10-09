package com.xwarner.eml.ide;

import java.io.File;
import java.util.ArrayList;

import com.xwarner.eml.ide.io.EMLFile;
import com.xwarner.eml.ide.ui.tabs.EditTab;
import com.xwarner.eml.interpreter.Interpreter;
import com.xwarner.eml.parser.InputStream;
import com.xwarner.eml.parser.Parser;
import com.xwarner.eml.parser.TokenStream1;
import com.xwarner.eml.parser.TokenStream2;
import com.xwarner.eml.parser.TokenStream3;
import com.xwarner.eml.parser.TokenStream4;
import com.xwarner.eml.parser.TokenStream5;
import com.xwarner.eml.parser.Tree;

import javafx.collections.ListChangeListener;
import javafx.scene.control.Tab;

/**
 * Manages the backend of the app without worrying (too much) about UI
 * 
 * @author max
 *
 */

public class Controller {

	public App app;

	public ArrayList<EditTab> activeFiles;

	public Controller(App app) {
		this.app = app;
		activeFiles = new ArrayList<EditTab>();
		app.tabPane.getTabs().addListener(new ListChangeListener<Tab>() {
			public void onChanged(Change<? extends Tab> c) {
				c.next();
				if (c.wasRemoved()) {
					Tab t = c.getRemoved().get(0);
					activeFiles.remove((EditTab) t);
				}
			}
		});
	}

	public void openFile(File file) {
		EMLFile efile = new EMLFile(file);
		EditTab tab = new EditTab(efile);
		activeFiles.add(tab);
		app.tabPane.getTabs().add(tab);
	}

	public void run() {
		EditTab currentFile = getCurrentFile();

		TokenStream1 ts = new TokenStream1(new InputStream(currentFile.getSource()));
		Parser parser = new Parser(new TokenStream5(new TokenStream4(new TokenStream3(new TokenStream2(ts)))));
		Tree tree = parser.parse();
		Interpreter interpreter = new Interpreter(tree);
		interpreter.bundle.output = app.console.listener;
		interpreter.run();
	}

	public EditTab getCurrentFile() {
		return ((EditTab) app.tabPane.getSelectionModel().getSelectedItem());
	}

}
