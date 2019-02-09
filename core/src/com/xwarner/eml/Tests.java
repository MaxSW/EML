package com.xwarner.eml;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import com.xwarner.eml.compiler.PartialCompiler;
import com.xwarner.eml.interpreter.Interpreter;
import com.xwarner.eml.nodes.Node;
import com.xwarner.eml.parser.InputStream;
import com.xwarner.eml.parser.Parser;
import com.xwarner.eml.parser.TokenStream1;
import com.xwarner.eml.parser.TokenStream2;
import com.xwarner.eml.parser.TokenStream3;
import com.xwarner.eml.parser.TokenStream4;
import com.xwarner.eml.parser.Tree;
import com.xwarner.eml.util.IOManager;

public class Tests {

	public static String out = "";

	public static void main(String[] args) {
		try {

			IOManager.root = "/home/max/Dropbox/Workspace/Programming/Projects/eml/core/tests/";

			String s = "";
			s = IOManager.readFile("test.eml");

			System.out.println("==== EML TESTS ====");

			System.out.println("Test 1: Parsing, Saving and Loading");

			TokenStream1 ts = new TokenStream1(new InputStream(s));
			Parser parser = new Parser(new TokenStream4(new TokenStream3(new TokenStream2(ts))));
			long a = System.currentTimeMillis();
			Tree tree = parser.parse();
			long b = System.currentTimeMillis();
			for (Node n : tree.getChildren()) {
				stringNode(n, 0);
			}

			PartialCompiler comp = new PartialCompiler();
			long c = System.currentTimeMillis();
			comp.save("test.emlc", tree);
			long d = System.currentTimeMillis();
			Tree t = comp.load("test.emlc");
			long e = System.currentTimeMillis();

			String copy = out;

			out = "";

			for (Node n : t.getChildren()) {
				stringNode(n, 0);
			}

			// compare string output for both times
			System.out.println("passing: " + copy.equals(out));

			String[] split1 = copy.split("\n");
			String[] split2 = out.split("\n");

			if (split2.length > split1.length || split1.length > split2.length) {
				System.err.println("different number of lines");
			}

			for (int i = 0; i < split1.length; i++) {
				if (!split1[i].equals(split2[i])) {
					System.err.println("difference on line " + i + ":");
					System.err.println("\"" + split1[i].trim() + "\" vs. \"" + split2[i].trim() + "\"");
				}
			}
			System.out.println("");
			System.out.println("Test 2: Interpreting");

			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(baos, true, "UTF-8");
			Interpreter interpreter = new Interpreter(tree);
			interpreter.bundle.output = ps;
			long f = System.currentTimeMillis();
			interpreter.run();
			long g = System.currentTimeMillis();
			String data = new String(baos.toByteArray(), StandardCharsets.UTF_8);

			System.out.println("passing: " + data.equals(ref));

			split1 = ref.split("\n");
			split2 = data.split("\n");

			if (split2.length > split1.length || split1.length > split2.length) {
				System.err.println("different number of lines");
			}

			for (int i = 0; i < split1.length; i++) {
				if (!split1[i].equals(split2[i])) {
					System.err.println("difference on line " + i + ":");
					System.err.println("\"" + split1[i].trim() + "\" vs. \"" + split2[i].trim() + "\"");
				}
			}
			System.out.println("");
			System.out.println("Time Performance");
			System.out.println("parsing: " + (b - a) + "ms");
			System.out.println("saving: " + (d - c) + "ms");
			System.out.println("loading: " + (e - d) + "ms");
			System.out.println("interpreting: " + (g - f) + "ms");
			System.out.println("total time to run: " + ((b - a) + (g - f)) + "ms");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void stringNode(Node node, int offset) {
		String str = "";
		for (int i = 0; i < offset * 3; i++) {
			str += " ";
		}
		str += "* ";

		str += node.toString();
		out += str;
		out += "\n";
		for (Node n : node.getChildren()) {
			stringNode(n, offset + 1);
		}
	}

	static String ref = "== 1 ==\n" + "5.0\n" + "97.50\n" + "98.50\n" + "97.50\n" + "102.50\n" + "92.50\n" + "185.000\n"
			+ "1\n" + "== 2 ==\n" + "ten\n" + "ten 5 ten\n" + "eleven\n" + "true\n" + "false\n" + "false\n" + "true\n"
			+ "true\n" + "false\n" + "false\n" + "false\n" + "true\n" + "false\n" + "true\n" + "false\n" + "false\n"
			+ "true\n" + "false\n" + "true\n" + "true\n" + "==3==\n" + "a!\n" + "9.0\n" + "yes no\n" + "false\n"
			+ "hello\n" + "5.0\n" + "15.0\n" + "yes\n" + "7.0\n" + "==4==\n" + "5.0\n" + "3.0\n" + "==5==\n" + "1\n"
			+ "5\n" + "9\n" + "0.0\n" + "1.0\n" + "2.0\n" + "3.0\n" + "4.0\n" + "5.0\n" + "6.0\n" + "7.0\n" + "8.0\n"
			+ "9.0\n" + "10.0\n" + "hello\n" + "==6==\n" + "[5.0, 10.00, 3.0]\n" + "[7.0, 13.00, 7.0]\n"
			+ "[25.00, 50.000, 15.00]\n" + "[4.0, 9.00, 3.0]\n" + "[5.0, 2.0 | 3.0, 1.0]\n" + "[6.0, 3.0 | 3.0, 3.0]\n"
			+ "[25.00, 10.00 | 15.00, 5.00]\n" + "[5.00, 2.00 | 3.00, 1.00]\n" + "[19.00, -13.00 | 11.00, -7.00]\n"
			+ "==7==\n" + "15.0\n" + "10.0\n" + "true\n" + "false\n" + "hello world\n" + "goodbye world\n"
			+ "[100.0, 5.0, 3.0]\n" + "[10.0, 5.0, 3.0]\n" + "[10.0, 2.0 | -10.0, -9.0]\n" + "[5.0, 2.0 | -5.0, -4.0]" + "\n";

}
