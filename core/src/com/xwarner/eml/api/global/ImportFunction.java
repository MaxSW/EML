package com.xwarner.eml.api.global;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.xwarner.eml.api.Library;
import com.xwarner.eml.api.libraries.display.GraphLibrary;
import com.xwarner.eml.api.libraries.maths.MathsLibrary;
import com.xwarner.eml.api.libraries.maths.RandomLibrary;
import com.xwarner.eml.api.libraries.ui.AlertLibrary;
import com.xwarner.eml.api.libraries.ui.ConsoleLibrary;
import com.xwarner.eml.api.libraries.ui.InputLibrary;
import com.xwarner.eml.interpreter.Bundle;
import com.xwarner.eml.interpreter.context.SubContext;
import com.xwarner.eml.interpreter.context.functions.Function;
import com.xwarner.eml.interpreter.context.objects.EObject;
import com.xwarner.eml.nodes.Node;
import com.xwarner.eml.parser.InputStream;
import com.xwarner.eml.parser.Parser;
import com.xwarner.eml.parser.TokenStream1;
import com.xwarner.eml.parser.TokenStream2;
import com.xwarner.eml.parser.TokenStream3;
import com.xwarner.eml.parser.TokenStream4;
import com.xwarner.eml.parser.Tree;
import com.xwarner.eml.tools.IOManager;

public class ImportFunction extends Function {

	private static HashMap<String, Library> libraries;

	public static void loadLibraries() {
		libraries = new HashMap<String, Library>();
		libraries.put("maths", new MathsLibrary());
		libraries.put("math", libraries.get("maths")); // take that
		libraries.put("input", new InputLibrary());
		libraries.put("alert", new AlertLibrary());
		libraries.put("random", new RandomLibrary());
		libraries.put("graph", new GraphLibrary());
		libraries.put("console", new ConsoleLibrary());
	}

	public Object run(ArrayList<Object> args, Bundle bundle) {
		if (args == null)
			throw new Error("missing which library to import");
		Object o = args.get(0);
		if (!(o instanceof String))
			throw new Error("incorrect agument in import function");

		String str = (String) o;
		System.out.println("importing: " + str);

		if (str.startsWith("./")) {
			String file = str.substring(2);
			String src = "";
			try {
				src = IOManager.readFile(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			EObject obj = new EObject(null);
			Tree tree = new Parser(
					new TokenStream4(new TokenStream3(new TokenStream2(new TokenStream1(new InputStream(src))))))
							.parse();

			bundle.context.enter(obj);
			for (Node node : tree.getChildren())
				node.invoke1(bundle);
			for (Node node : tree.getChildren())
				node.invoke2(bundle);
			bundle.context.exit();

			return obj;

		} else {
			if (!libraries.containsKey(str))
				throw new Error("unknown library");

			SubContext ctx = libraries.get(str).export();
			EObject obj = new EObject(null);
			bundle.context.importLibrary(obj, ctx);
			return obj;
		}

	}

}
