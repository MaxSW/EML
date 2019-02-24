package com.xwarner.eml.ide.ui.tabs;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import com.xwarner.eml.ide.io.EMLFile;

import javafx.scene.control.Tab;

public class EditTab extends Tab {

	public EMLFile file;
	public CodeArea codeArea;

	public EditTab(EMLFile file) {
		this.file = file;
		setText(file.name);
		codeArea = new CodeArea();
		codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
		codeArea.replaceText(0, 0, file.getText());
		setContent(codeArea);
	}

}
