package com.xwarner.eml.library.global;

import java.io.IOException;
import java.util.ArrayList;

import com.xwarner.eml.interpreter.bundle.Bundle;
import com.xwarner.eml.interpreter.context.functions.Function;
import com.xwarner.eml.interpreter.context.objects.EObject;
import com.xwarner.eml.nodes.Node;
import com.xwarner.eml.parser.Parser;
import com.xwarner.eml.parser.Tree;
import com.xwarner.eml.util.IOManager;

public class ImportFunction extends Function {

	public Object run(ArrayList<Object> args, Bundle bundle) {
		if (args == null)
			throw new Error("missing which library to import");
		Object o = args.get(0);
		if (!(o instanceof String))
			throw new Error("incorrect agument in import function");

		String str = (String) o;
		System.out.println("importing: " + str);
		String src = "";
		if (str.startsWith("./")) {
			String file = str.substring(2);
			try {
				src = IOManager.readFile(file);
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			String file = str + ".eml";
			try {
				src = IOManager.readLibrary(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		EObject obj = new EObject(null);
		Tree tree = new Parser(src).parse();

		bundle.context.enter(obj);
		for (Node node : tree.getChildren())
			node.pre_invoke(bundle);
		for (Node node : tree.getChildren())
			node.invoke(bundle);
		bundle.context.exit();

		return obj;

	}

}
