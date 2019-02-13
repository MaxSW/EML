package com.xwarner.eml;

import java.io.File;
import java.io.IOException;

import com.xwarner.eml.interpreter.Interpreter;
import com.xwarner.eml.nodes.Node;
import com.xwarner.eml.parser.InputStream;
import com.xwarner.eml.parser.Parser;
import com.xwarner.eml.parser.TokenStream1;
import com.xwarner.eml.parser.TokenStream2;
import com.xwarner.eml.parser.TokenStream3;
import com.xwarner.eml.parser.TokenStream4;
import com.xwarner.eml.parser.Tree;
import com.xwarner.eml.performance.Optimiser;
import com.xwarner.eml.util.IOManager;

public class Dev {
	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("missing file");
			return;
		}
		String path = args[0];

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

		TokenStream1 ts = new TokenStream1(new InputStream(src));
		Parser parser = new Parser(new TokenStream4(new TokenStream3(new TokenStream2(ts))));
		Tree tree = parser.parse();

		for (Node n : tree.getChildren()) {
			printNode(n, 0);
		}

		System.out.println("");
		System.out.println("==============================");
		System.out.println("");

		Optimiser optimiser = new Optimiser();
		Tree tree2 = optimiser.optimise(tree);

		Interpreter interpreter = new Interpreter(tree2);

		/*
		 * long sum = 0; long sum2 = 0; for (int i = 0; i < 100; i++) { long f =
		 * System.currentTimeMillis(); interpreter.run(); long g =
		 * System.currentTimeMillis(); sum += g - f; if (i < 5) sum2 += g - f; }
		 * System.out.println("first five speed:" + (sum2 / 5) + "ms");
		 * System.out.println("average speed: " + sum / 100 + "ms");
		 */

		interpreter.run();
	}

	public static void printNode(Node node, int offset) {
		String str = "";
		for (int i = 0; i < offset * 3; i++) {
			str += " ";
		}
		str += "* ";

		str += node.toString();
		System.out.println(str);
		for (Node n : node.getChildren()) {
			printNode(n, offset + 1);
		}
	}

}
