package com.xwarner.eml;

import java.io.File;
import java.io.IOException;

import com.xwarner.eml.interpreter.Interpreter;
import com.xwarner.eml.parser.Parser;
import com.xwarner.eml.parser.Tree;
import com.xwarner.eml.util.IOManager;

public class Bootstrap {

	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("missing file");
			return;
		}
		String path = args[0];
		if (args.length == 2)
			IOManager.libRoot = args[1];

		String[] split = path.split(File.separator);

		String file = split[split.length - 1];
		String root = path.substring(0, path.length() - split[split.length - 1].length());

		String src = "";

		IOManager.root = root;

		try {
			src = IOManager.readFile(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Parser parser = new Parser(src);
		Tree tree = parser.parse();
		Interpreter interpreter = new Interpreter(tree);
		interpreter.run();
	}

}
